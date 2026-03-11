/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author FILIP KOSTIC
 */
public class Test {
    public static void main(String[] args) throws InterruptedException, Exception {
        
         ProcessManager manager = new ProcessManager();

        String id = manager.startProcess(1000, 4);

        Thread.sleep(1000);

        manager.stopProcess(id);

        System.out.println("STOPPED");

        Thread.sleep(1000);

        manager.recommenceProcess(id);

        System.out.println("RESTARTED");

    }
}
