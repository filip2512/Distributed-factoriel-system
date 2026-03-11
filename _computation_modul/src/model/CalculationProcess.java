/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author FILIP KOSTIC
 */
public class CalculationProcess {
    
    private String id;
    private int number;
    private int threadCount;

    private BigInteger result;

    private ProcessStatus status;

    private AtomicBoolean cancelled = new AtomicBoolean(false);
    
    // Fields for resume support
    private int lastComputed;
    private BigInteger partialResult;
    
    public CalculationProcess(String id, int number, int threadCount) {
        this.id = id;
        this.number = number;
        this.threadCount = threadCount;
        this.status = ProcessStatus.RUNNING;
        this.lastComputed = 0;
        this.partialResult = BigInteger.ONE;
    }

    public String getId() {
        return id;
    }

    public AtomicBoolean getCancelled() {
        return cancelled;
    }

    
    public int getNumber() {
        return number;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public BigInteger getResult() {
        return result;
    }

    public void setResult(BigInteger result) {
        this.result = result;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public int getLastComputed() {
        return lastComputed;
    }

    public void setLastComputed(int lastComputed) {
        this.lastComputed = lastComputed;
    }

    public BigInteger getPartialResult() {
        return partialResult;
    }

    public void setPartialResult(BigInteger partialResult) {
        this.partialResult = partialResult;
    }
}
