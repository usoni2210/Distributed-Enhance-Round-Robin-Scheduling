package com.twister;

import javax.swing.*;
import javax.swing.plaf.basic.BasicListUI;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProcessesList extends JFrame {
    private JList<String> fileList;

    ProcessesList(){
        setTitle("Process Data List");
        setBounds(100,100,600,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Container container = getContentPane();

        // Title
        JLabel title = new JLabel("Available Data List", JLabel.CENTER);
        title.setFont(Constant.titleFontStyle);
        title.setBounds(50,25,500,50);
        container.add(title);

        // Files List
        JPanel showFiles = new JPanel();
        showFiles.setLayout(null);
        showFiles.setBounds(50, 100, 500, 550);
        showFiles.setBackground(new Color(255,255,255));

        DefaultListModel<String> files = makeFilesAsDefaultListModel();
        fileList = new JList<>(files);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setUI(new BasicListUI());
        fileList.setSelectedIndex(0);
        fileList.setVisibleRowCount(5);

        JScrollPane fileScrollPane = new JScrollPane(fileList);
        fileScrollPane.setBounds(0,0,500,550);
        showFiles.add(fileScrollPane);
        container.add(showFiles);

        // Refresh Button
        JButton refresh = new JButton("Refresh");
        refresh.setFont(Constant.defaultFontStyle);
        refresh.setBounds(125, 700,100,40);
        refresh.addActionListener(e -> {
            new ProcessesList().setVisible(true);
            dispose();
        });
        container.add(refresh);

        // Next Button
        JButton next = new JButton("Next");
        next.setFont(Constant.defaultFontStyle);
        next.setBounds(375,700,100,40);
        next.addActionListener(e -> {
            new ProcessData(fileList.getSelectedValue()).setVisible(true);
            dispose();
        });
        container.add(next);
    }

    private DefaultListModel<String> makeFilesAsDefaultListModel(){
        DefaultListModel<String> files = new DefaultListModel<>();
        try{
            for (String s : getFilesList(Constant.ProcessDataPath)) {
                files.addElement(s);
            }
        }catch(Exception e){
            System.out.println("Error in Set Files name in List Model\n" + e);
        }
        return files;
    }

    private List<String> getFilesList(String dirPath){
        List<String> results = new ArrayList<>();

        File[] files = new File(dirPath).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    results.add(file.getName());
                }
            }
        }
        return results;
    }

    public static void main(String[] args){
        new ProcessesList().setVisible(true);
    }
}
