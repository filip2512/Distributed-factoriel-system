/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.sql.DataSource;
import exception.InvalidRequestException;
import exception.ProcessNotFoundException;
import exception.StorageException;
import validation.RequestValidator;
import model.CalculationProcess;
import model.ProcessStatus;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import storage.FileStorageService;
import storage.StorageService;
import storage.DatabaseStorageService;
import task.FactorialTask;

/**
 *
 * @author FILIP KOSTIC
 */
public class ProcessManager {
    
    private Map<String, CalculationProcess> processes = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    private StorageService storage; 
     
    public ProcessManager() throws StorageException, IOException {

        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
            }
        }

        String type = props.getProperty("storage.type", "file");

        if ("database".equalsIgnoreCase(type)) {
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String pass = props.getProperty("db.password");

            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(pass);

            this.storage = new DatabaseStorageService(ds);
        } else {
            try {
                this.storage = new FileStorageService();
            } catch (IOException e) {
                throw new StorageException("Failed to initialize file storage", e);
            }
        }

    }
    
    public void addProcess(CalculationProcess process) {
        writeLock.lock();
        try {
            processes.put(process.getId(), process);
        } finally {
            writeLock.unlock();
        }
    }
    
    public CalculationProcess getProcess(String id) {
        readLock.lock();
        try {
            return processes.get(id);
        } finally {
            readLock.unlock();
        }
    }
    
    public String startProcess(int number, int threadCount) throws InvalidRequestException, StorageException {
        RequestValidator.validateStartParams(number, threadCount);

        String id = UUID.randomUUID().toString();

        CalculationProcess process = new CalculationProcess(id, number, threadCount);

        addProcess(process);
        try {
            storage.saveProcess(process);
        } catch (IOException e) {
            throw new StorageException("Failed to save process", e);
        }
        new Thread(() -> {

            try {

                ForkJoinPool pool = new ForkJoinPool(threadCount);

                BigInteger result = pool.invoke(new FactorialTask(1, number,process.getCancelled()));

                process.setResult(result);

                process.setStatus(ProcessStatus.COMPLETED);
                storage.saveProcess(process);
                pool.shutdown();
            } catch (Exception e) {

                process.setStatus(ProcessStatus.STOPPED);

            }
            
        }).start();
        
        return id;

    }
    public ProcessStatus getStatus(String id) throws InvalidRequestException, ProcessNotFoundException {
        RequestValidator.validateProcessId(id);
        readLock.lock();
        try {
            CalculationProcess process = processes.get(id);
            if (process == null) {
                throw new ProcessNotFoundException("Process not found: " + id);
            }
            return process.getStatus();
        } finally {
            readLock.unlock();
        }
    }
    public BigInteger getResult(String id) throws InvalidRequestException, ProcessNotFoundException {
        RequestValidator.validateProcessId(id);
        readLock.lock();
        try {
            CalculationProcess process = processes.get(id);
            if (process == null) {
                throw new ProcessNotFoundException("Process not found: " + id);
            }
            return process.getResult();
        } finally {
            readLock.unlock();
        }
    }
    
    public void stopProcess(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException {
        RequestValidator.validateProcessId(id);
        CalculationProcess process = getProcess(id);

        if (process == null) {
            throw new ProcessNotFoundException("Process not found: " + id);
        }

        process.getCancelled().set(true);
        process.setStatus(ProcessStatus.STOPPED);
        try {
            storage.saveProcess(process);
        } catch (IOException e) {
            throw new StorageException("Failed to save process", e);
        }
    }
    
    public String recommenceProcess(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException {
        RequestValidator.validateProcessId(id);

        CalculationProcess process;
        try {
            process = storage.loadProcess(id);
        } catch (IOException e) {
            throw new StorageException("Failed to load process", e);
        }

        if (process == null) {
            throw new ProcessNotFoundException("Process not found: " + id);
        }

        addProcess(process);

        process.setStatus(ProcessStatus.RUNNING);

        new Thread(() -> {

            try {

                ForkJoinPool pool = new ForkJoinPool(process.getThreadCount());

                BigInteger result =
                        pool.invoke(
                            new FactorialTask(
                                1,
                                process.getNumber(),
                                process.getCancelled()
                            )
                        );

                process.setResult(result);

                process.setStatus(ProcessStatus.COMPLETED);

                storage.saveProcess(process);

            } catch (Exception e) {

                process.setStatus(ProcessStatus.STOPPED);

            }

        }).start();

        return id;
    }
    
}
