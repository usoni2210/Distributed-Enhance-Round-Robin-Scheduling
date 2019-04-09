package com.twister;

import com.twister.ERRScheduling.ProcessControlBlock;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Umesh
 */
class ProcessData extends JFrame {
    private List<ProcessControlBlock> PCBList;

    ProcessData(String fileName) {
        setTitle("Process Data");
        setBounds(100,100,600,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Container container = getContentPane();
        PCBList = getDataInPCB(fileName);

        // Process Data Title
        JLabel title = new JLabel("Process Data",JLabel.CENTER);
        title.setFont(Constant.titleFontStyle);
        title.setBounds(50,25,500,50);
        container.add(title);

        // Process Data View
        JPanel showData = new JPanel();
        showData.setLayout(new BorderLayout());
        showData.setBounds(50, 100, 500, 550);
        showData.setBackground(new Color(255,255,255));

        JTable table = getPCBAsJTable(PCBList);
        JScrollPane scrollPaneTable = new JScrollPane(table);
        showData.add(scrollPaneTable, BorderLayout.CENTER);
        container.add(showData);

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
        JButton next = new JButton("Next");
        next.setFont(Constant.defaultFontStyle);
        next.setBounds(375,700,100,40);
        next.addActionListener(e -> {
            new viewProcess(PCBList).setVisible(true);
            dispose();
        });
        container.add(next);
    }

    private JTable getPCBAsJTable(List<ProcessControlBlock> PCBList) {
        DefaultTableModel tableModel = new DefaultTableModel(ProcessControlBlock.getShowColumns(),0);
        for(ProcessControlBlock PCB : PCBList){
            String[] temp = {PCB.getName(), Integer.toString(PCB.getBurstTime())};
            tableModel.addRow(temp);
        }
        return new JTable(tableModel);
    }

    private List<ProcessControlBlock> getDataInPCB(String fileName) {
        List<ProcessControlBlock> PCBList = new ArrayList<>();
        ProcessControlBlock PCB;
        StringTokenizer str ;
        BufferedReader file;

        try {
            file = new BufferedReader(new FileReader(Constant.ProcessDataPath+fileName));
            String dataRow = file.readLine();

            while (dataRow != null) {
                str = new StringTokenizer(dataRow, "\t");
                PCB = new ProcessControlBlock(str.nextElement().toString(), Integer.parseInt(str.nextElement().toString()));

                PCBList.add(PCB);
                dataRow = file.readLine();
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PCBList;
    }
}
