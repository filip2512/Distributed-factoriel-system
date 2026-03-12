/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import exception.InvalidRequestException;
import exception.ProcessNotFoundException;
import exception.ServiceUnavailableException;
import exception.StorageException;
import executor.Executor;
import service.ProcessManager;

/**
 *
 * @author FILIP KOSTIC
 */
class ComputationExecutor implements Executor{
     private final ProcessManager manager;

    public ComputationExecutor(ProcessManager manager) {
        this.manager = manager;
    }

    @Override
    public String startProcess(int number, int threads) throws InvalidRequestException, StorageException, ServiceUnavailableException {
        return manager.startProcess(number, threads);
    }

    @Override
    public String getStatus(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException, ServiceUnavailableException {
        return manager.getStatus(id).name();
    }

    @Override
    public String getResult(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException, ServiceUnavailableException {
        java.math.BigInteger result = manager.getResult(id);
        return result != null ? result.toString() : "Not ready";
    }

    @Override
    public void stopProcess(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException, ServiceUnavailableException {
        manager.stopProcess(id);
    }

    @Override
    public String recommenceProcess(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException, ServiceUnavailableException {
        return manager.recommenceProcess(id);
    }

}
