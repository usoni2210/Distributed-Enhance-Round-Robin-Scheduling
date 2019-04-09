package com.twister;

import com.twister.ERRScheduling.ProcessControlBlock;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.concurrent.BlockingQueue;

class ShowResult extends JFrame {

    ShowResult(BlockingQueue<ProcessControlBlock> PCBList){
        setTitle("Result View");
        setBounds(100,100,600,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);


        Container container = getContentPane();

        // Process Data Title
        JLabel title = new JLabel("Process Data",JLabel.CENTER);
        title.setFont(Constant.titleFontStyle);
        title.setBounds(50,25,500,50);
        container.add(title);

        // Process Data View
        JPanel showData = new JPanel();
        showData.setLayout(new BorderLayout());
        showData.setBounds(50, 100, 500, 450);
        showData.setBackground(new Color(255,255,255));

        JTable table = getPCBAsJTable(PCBList);
        JScrollPane scrollPaneTable = new JScrollPane(table);
        showData.add(scrollPaneTable, BorderLayout.CENTER);
        container.add(showData);

        // Average Waiting Time Label
        JLabel waitingTime = new JLabel();
        waitingTime.setBounds(50, 600,250,50);
        waitingTime.setHorizontalAlignment(SwingConstants.CENTER);
        waitingTime.setText("Average Waiting Time : " + ProcessControlBlock.getAverageWaitingTime(PCBList));
        container.add(waitingTime);

        // Average Turn Around Time Label
        JLabel turnAroundTime = new JLabel();
        turnAroundTime.setBounds(300, 600,250,50);
        turnAroundTime.setHorizontalAlignment(SwingConstants.CENTER);
        turnAroundTime.setText("Average Turn Around Time : " + ProcessControlBlock.getAverageTurnAroundTime(PCBList));
        container.add(turnAroundTime);

        // Refresh Button
        JButton back = new JButton("Back");
        back.setFont(Constant.defaultFontStyle);
        back.setBounds(125, 700,100,40);
        back.addActionListener(e -> {
            new ProcessesList().setVisible(true);
            dispose();
        });
        container.add(back);

        // Next Button
        JButton next = new JButton("Exit");
        next.setFont(Constant.defaultFontStyle);
        next.setBounds(375,700,100,40);
        next.addActionListener(e -> dispose());
        container.add(next);
    }

    private JTable getPCBAsJTable(BlockingQueue<ProcessControlBlock> PCBList) {
        DefaultTableModel tableModel = new DefaultTableModel(ProcessControlBlock.getResultColumns(),0);
        for(ProcessControlBlock PCB : PCBList){
            String[] temp = {PCB.getName(), Integer.toString(PCB.getBurstTime()), Integer.toString(PCB.getWaitingTime()), Integer.toString(PCB.getTurnAroundTime())};
            tableModel.addRow(temp);
        }
        return new JTable(tableModel);
    }
}
