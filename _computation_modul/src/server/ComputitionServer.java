/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import handler.StatusHandler;
import handler.RecommenceHandler;
import handler.ResultHandler;
import handler.StopHandler;
import handler.StartHandler;
import com.sun.net.httpserver.HttpServer;
import exception.StorageException;
import executor.Executor;
import java.net.InetSocketAddress;
import service.ProcessManager;

/**
 *
 * @author FILIP KOSTIC
 */
public class ComputitionServer {
    
     public static void main(String[] args) throws StorageException, java.io.IOException {

       ProcessManager manager = new ProcessManager();

        Executor executor = new ComputationExecutor(manager);

        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        server.createContext("/start", new StartHandler(executor));
        server.createContext("/status", new StatusHandler(executor));
        server.createContext("/result", new ResultHandler(executor));
        server.createContext("/stop", new StopHandler(executor));
        server.createContext("/recommence", new RecommenceHandler(executor));

        server.setExecutor(null); 
        server.start();
        System.out.println("Computation server running on port 8081");
    }
}
