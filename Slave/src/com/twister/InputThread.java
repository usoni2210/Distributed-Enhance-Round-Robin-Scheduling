package com.twister;

import com.twister.ERRScheduling.ProcessControlBlock;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;

public class InputThread extends Thread {
    private ObjectInputStream objectInputStream = null;
    private BlockingQueue<ProcessControlBlock> queue = null;

    InputThread(InputStream inputStream, BlockingQueue<ProcessControlBlock> queue) {
        try {
            this.queue = queue;
            this.objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Waiting for Processes...");
        try {
            ProcessControlBlock PCB;
            while((PCB = (ProcessControlBlock) objectInputStream.readObject()) != null){
                System.out.println("Process Received : " + PCB);
                queue.put(PCB);
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Input Close");
        }
    }
}
