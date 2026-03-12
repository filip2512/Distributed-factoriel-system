/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import com.sun.net.httpserver.HttpServer;
import executor.Executor;
import handler.RecommenceHandler;
import handler.ResultHandler;
import handler.StartHandler;
import handler.StatusHandler;
import handler.StopHandler;
import java.net.InetSocketAddress;

/**
 *
 * @author FILIP KOSTIC
 */
public class GatewayServer {
     public static void main(String[] args) {
        try{
        Executor executor = new GatewayExecutor("http://localhost:8081");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/start", new StartHandler(executor));
        server.createContext("/status", new StatusHandler(executor));
        server.createContext("/result", new ResultHandler(executor));
        server.createContext("/stop", new StopHandler(executor));
        server.createContext("/recommence", new RecommenceHandler(executor));

        server.start();

        System.out.println("Gateway server running on port 8080");
         }catch(Exception e){
            e.getMessage();
            e.printStackTrace();
            System.exit(1);
         }
    }
}
