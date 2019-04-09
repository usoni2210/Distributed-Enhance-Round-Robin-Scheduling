package com.twister;

import com.twister.ERRScheduling.ProcessControlBlock;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;

class ReceiveProcess extends Thread {
    private ObjectInputStream inputStream;
    private BlockingQueue<ProcessControlBlock> PCBList;
    private JTextArea processBox;
    ReceiveProcess(ObjectInputStream inputStream, BlockingQueue<ProcessControlBlock> PCBList, JTextArea processBox) {
        this.inputStream = inputStream;
        this.PCBList = PCBList;
        this.processBox = processBox;
    }

    @Override
    public void run() {
        processBox.append("\nWaiting for Processes to Receive...\n");
        try {
            ProcessControlBlock PCB;
            while((PCB = (ProcessControlBlock) inputStream.readObject()) != null){
                processBox.append("\nProcess Received : "+PCB);
                PCBList.add(PCB);
            }
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("Input Connection Close");
        }
    }
}
