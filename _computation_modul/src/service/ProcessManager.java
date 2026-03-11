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
import java.util.concurrent.Semaphore;
import javax.sql.DataSource;
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
    private Semaphore semaphore = new Semaphore(1);
    private StorageService storage; 
     
    public ProcessManager() throws Exception {

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
            this.storage = new FileStorageService();
        }

    }
    
    public void addProcess(CalculationProcess process) throws InterruptedException {

        semaphore.acquire();

        try {
            processes.put(process.getId(), process);
        } finally {
            semaphore.release();
        }
    }
    
    public CalculationProcess getProcess(String id) throws InterruptedException {

        semaphore.acquire();

        try {
            return processes.get(id);
        } finally {
            semaphore.release();
        }
    }
    
    public String startProcess(int number, int threadCount) throws InterruptedException, IOException {

        String id = UUID.randomUUID().toString();

        CalculationProcess process = new CalculationProcess(id, number, threadCount);

        addProcess(process);
        storage.saveProcess(process);
        new Thread(() -> {

            try {

                ForkJoinPool pool = new ForkJoinPool(threadCount);

                BigInteger result = pool.invoke(new FactorialTask(1, number,process.getCancelled()));

                process.setResult(result);

                process.setStatus(ProcessStatus.COMPLETED);
                storage.saveProcess(process);

            } catch (Exception e) {

                process.setStatus(ProcessStatus.STOPPED);

            }

        }).start();

        return id;

    }
    public ProcessStatus getStatus(String id) throws InterruptedException {

        CalculationProcess process = getProcess(id);

        if (process == null) {
            return null;
        }

        return process.getStatus();
    }
    public BigInteger getResult(String id) throws InterruptedException {

        CalculationProcess process = getProcess(id);

        if (process == null) {
            return null;
        }

        return process.getResult();
    }
    
    public void stopProcess(String id) throws InterruptedException, IOException {

        CalculationProcess process = getProcess(id);

        if (process != null) {

            process.getCancelled().set(true);

            process.setStatus(ProcessStatus.STOPPED);
            storage.saveProcess(process);

        }

    }
    
    public String recommenceProcess(String id) throws Exception {

        CalculationProcess process = storage.loadProcess(id);

        if (process == null) {
            return null;
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
