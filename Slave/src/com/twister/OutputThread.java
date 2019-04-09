package com.twister;

import com.twister.ERRScheduling.ProcessControlBlock;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

public class OutputThread extends Thread{
    private ObjectOutputStream objectOutputStream;
    private BlockingQueue<ProcessControlBlock> queue;
    OutputThread(OutputStream outputStream, BlockingQueue<ProcessControlBlock> queue) {
        try {
            this.queue = queue;
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Start Sending Processed Processes...");
        while(true) {
            ProcessControlBlock PCB;
            try {
                PCB = queue.take();
                objectOutputStream.writeObject(PCB);
                System.out.print("Process Sent : " + PCB);
                System.out.println();
            } catch (InterruptedException | IOException e) {
                //e.printStackTrace();
                System.out.println("Output Close");
                break;
            }
        }
        System.out.println("Process Sent Complete");
    }
}
