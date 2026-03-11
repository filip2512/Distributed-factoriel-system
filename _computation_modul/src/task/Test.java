/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package task;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author FILIP KOSTIC
 */
public class Test {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        BigInteger result = pool.invoke(new FactorialTask(1, 5,new AtomicBoolean(false)));
        System.out.println("Result is: " + result);
    }
}
