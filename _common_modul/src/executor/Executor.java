/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package executor;

import exception.InvalidRequestException;
import exception.ProcessNotFoundException;
import exception.ServiceUnavailableException;
import exception.StorageException;

/**
 *
 * @author FILIP KOSTIC
 */
public interface Executor {
    String startProcess(int number, int threads) throws InvalidRequestException, StorageException, ServiceUnavailableException, ProcessNotFoundException;

    String getStatus(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException, ServiceUnavailableException;

    String getResult(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException, ServiceUnavailableException;

    void stopProcess(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException, ServiceUnavailableException;

    String recommenceProcess(String id) throws InvalidRequestException, ProcessNotFoundException, StorageException, ServiceUnavailableException;
}
