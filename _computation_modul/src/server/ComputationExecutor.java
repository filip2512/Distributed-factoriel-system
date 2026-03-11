/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

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
    public String startProcess(int number, int threads) throws Exception {
        return manager.startProcess(number, threads);
    }

    @Override
    public String getStatus(String id) throws Exception {
        return manager.getStatus(id).name();
    }

    @Override
    public String getResult(String id) throws Exception {
        return manager.getResult(id) != null ? manager.getResult(id).toString() : "Not ready";
    }

    @Override
    public void stopProcess(String id) throws Exception {
        manager.stopProcess(id);
    }

    @Override
    public String recommenceProcess(String id) throws Exception {
        return manager.recommenceProcess(id);
    }

}
