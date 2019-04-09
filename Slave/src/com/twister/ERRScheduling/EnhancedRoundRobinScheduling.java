package com.twister.ERRScheduling;

import java.util.concurrent.BlockingQueue;

public class EnhancedRoundRobinScheduling extends Thread{
    private BlockingQueue<ProcessControlBlock> rawQueue;
    private BlockingQueue<ProcessControlBlock> resQueue;

    public EnhancedRoundRobinScheduling(BlockingQueue<ProcessControlBlock> rawQueue, BlockingQueue<ProcessControlBlock> resQueue) {
        this.rawQueue = rawQueue;
        this.resQueue = resQueue;
    }

    @Override
    public void run() {
        System.out.println("Start Processing Processes...");
        int time_quantum = calcTimeQuantum(rawQueue);
        int size = rawQueue.size();
        int time = 0;
        while(true){
            ProcessControlBlock pcb;
            try {
                if(rawQueue.size() > 1 && size != rawQueue.size()+1) {
                    time_quantum = calcTimeQuantum(rawQueue);
                    size = rawQueue.size();
                }
                pcb = rawQueue.take();

                time += pcb.processedTime(time_quantum);
                if(pcb.isCompleted()){
                    pcb.calcWaitingAndTATTime(time);
                    System.out.println("Processed " + pcb.getName());
                    resQueue.add(pcb);
                    if(rawQueue.size() > 0) {
                        time_quantum = calcTimeQuantum(rawQueue);
                    }
                    size--;
                } else {
                    rawQueue.add(pcb);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Processing Completed");
    }

    private static int calcTimeQuantum(BlockingQueue<ProcessControlBlock> list){
        float mean = getMean(list);
        float sum = 0.0f;
        int count = list.size();

        for(ProcessControlBlock pcb : list){
            sum += Math.pow(pcb.getRemainingTime() - mean,2);
        }
        return (int) (Math.ceil(Math.sqrt(sum/count)) + mean);
        //return (int) mean;
    }

    private static float getMean(BlockingQueue<ProcessControlBlock> list){
        int sum = 0;
        int count = list.size();
        for (ProcessControlBlock PCB : list) {
            sum += PCB.getRemainingTime();
        }
        return count != 0 ? (float)sum/count : sum;
    }
}
