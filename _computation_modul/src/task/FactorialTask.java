package task;


import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FILIP KOSTIC
 */
public class FactorialTask extends RecursiveTask<BigInteger>{
    
    private int start;
    private int end;
    private AtomicBoolean cancelled;

    public FactorialTask(int start, int end, AtomicBoolean cancelled) {
        this.start = start;
        this.end = end;
        this.cancelled = cancelled;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public AtomicBoolean getCancelled() {
        return cancelled;
    }
    
    
    @Override
    protected BigInteger compute() {
        
        if (cancelled.get()) {
            return BigInteger.ONE;
        }
        
        if (end - start <= 5) {

            BigInteger result = BigInteger.ONE;

            for (int i = start; i <= end; i++) {
                result = result.multiply(BigInteger.valueOf(i));
            }

            return result;
        }

        int mid = (start + end) / 2;

        FactorialTask leftTask = new FactorialTask(start, mid, cancelled);
        FactorialTask rightTask = new FactorialTask(mid + 1, end, cancelled);

        leftTask.fork();

        BigInteger rightResult = rightTask.compute();
        BigInteger leftResult = leftTask.join();

        return leftResult.multiply(rightResult);
    }
    
}
