/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package executor;

/**
 *
 * @author FILIP KOSTIC
 */
public interface Executor {
    String startProcess(int number, int threads) throws Exception;

    String getStatus(String id) throws Exception;

    String getResult(String id) throws Exception;

    void stopProcess(String id) throws Exception;

    String recommenceProcess(String id) throws Exception;
}
