/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package task;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author FILIP KOSTIC
 */
public class FactorialTaskTest {

    @Test
    public void compute_factorialOf5_returns120() {
        ForkJoinPool pool = new ForkJoinPool(4);
        BigInteger result = pool.invoke(new FactorialTask(1, 5, new AtomicBoolean(false)));
        assertEquals(BigInteger.valueOf(120), result);
    }

    @Test
    public void compute_factorialOf1_returns1() {
        ForkJoinPool pool = new ForkJoinPool(1);
        BigInteger result = pool.invoke(new FactorialTask(1, 1, new AtomicBoolean(false)));
        assertEquals(BigInteger.ONE, result);
    }

    @Test
    public void compute_factorialOf10_returns3628800() {
        ForkJoinPool pool = new ForkJoinPool(4);
        BigInteger result = pool.invoke(new FactorialTask(1, 10, new AtomicBoolean(false)));
        assertEquals(BigInteger.valueOf(3_628_800), result);
    }

    @Test
    public void compute_whenCancelled_returnsOne() {
        AtomicBoolean cancelled = new AtomicBoolean(true);
        ForkJoinPool pool = new ForkJoinPool(4);
        BigInteger result = pool.invoke(new FactorialTask(1, 100, cancelled));
        assertEquals(BigInteger.ONE, result);
    }
}
