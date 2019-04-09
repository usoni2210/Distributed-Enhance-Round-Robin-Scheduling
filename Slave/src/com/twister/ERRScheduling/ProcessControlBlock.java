package com.twister.ERRScheduling;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Queue;

/**
 *
 * @author Umesh
 */
public class ProcessControlBlock implements Serializable, Comparator<ProcessControlBlock> {
    private static final long serialVersionUID = 9782981198L;
    private String name;
    private int burstTime;
    private int arrivalTime;
    private int remainingTime;
    private int waitingTime;
    private int turnAroundTime;

    public ProcessControlBlock(){}

    ProcessControlBlock(String name, int burstTime) {
        this.name = name;
        this.arrivalTime = this.waitingTime = this.turnAroundTime = 0;
        this.burstTime = this.remainingTime = burstTime;
    }

    ProcessControlBlock(String name, int arrivalTime, int burstTime){
        this(name, burstTime);
        this.arrivalTime = arrivalTime;
    }

    public boolean isCompleted(){
        return remainingTime == 0;
    }

    public int processedTime(int quantum){
        if(quantum < remainingTime){
            remainingTime -= quantum;
            return quantum;
        } else{
            int temp = remainingTime;
            remainingTime = 0;
            return temp;
        }
    }

    public static float getAverageWaitingTime(Queue<ProcessControlBlock> list){
        float sum = 0.0f;
        for (ProcessControlBlock PCB : list) {
            sum += PCB.waitingTime;
        }
        return sum/list.size();
    }

    public static float getAverageTurnAroundTime(Queue<ProcessControlBlock> list) {
        float sum = 0.0f;
        for (ProcessControlBlock PCB : list) {
            sum += PCB.turnAroundTime;
        }
        return sum/list.size();
    }

    public void calcWaitingAndTATTime(int time) {
        this.turnAroundTime = time - this.arrivalTime;
        this.waitingTime = this.turnAroundTime - this.burstTime;
    }

    public static void display(Queue<ProcessControlBlock> list){
        System.out.println("\nProcess\t\t BT\t\t WT\t\t TAT");
        for(ProcessControlBlock PCB : list){
            System.out.println(PCB);
        }
        System.out.println();
    }

    public String getName(){
        return this.name;
    }

    public int getRemainingTime(){
        return this.remainingTime;
    }

    public int getBurstTime(){
        return this.burstTime;
    }

    @Override
    public String toString() {
        return name + " \t " + burstTime + " \t" + waitingTime + " \t" + turnAroundTime;
    }

    @Override
    public int compare(ProcessControlBlock o1, ProcessControlBlock o2) {
        return  o1.remainingTime - o2.remainingTime;
    }

    public static String[] getShowColumns() {
        return new String[]{"Process Name", "Burst Time"};
    }

    public static String[] getResultColumns() {
        return new String[]{"Process Name", "Burst Time", "Waiting Time", "Turnaround Time"};
    }
}
