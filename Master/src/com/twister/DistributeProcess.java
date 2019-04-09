package com.twister;

import com.twister.ERRScheduling.ProcessControlBlock;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class DistributeProcess extends Thread{
    private JTextArea processBox;
    private List<ObjectOutputStream> outputStreams;
    private List<ProcessControlBlock> PCBList;

    DistributeProcess(List<ObjectOutputStream> outputStreams, List<ProcessControlBlock> PCBList, JTextArea processBox){
        this.PCBList = PCBList;
        this.outputStreams = outputStreams;
        this.processBox = processBox;
    }

    @Override
    public void run() {
        processBox.append("\n\nProcess Distributor Started");
        try {
            processBox.append("\n\nProcess Distributing...\n");
            int i=0;
            for (ProcessControlBlock pcb : PCBList) {
                processBox.append("Process " + pcb.getName() + " Send\n");
                outputStreams.get(i++).writeObject(pcb);
                if(i==outputStreams.size())
                    i=0;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output Connection Close");
        }
    }
}

