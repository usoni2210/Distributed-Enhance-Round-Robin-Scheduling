package com.twister;

import com.twister.ERRScheduling.ProcessControlBlock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Umesh
 */
class viewProcess extends JFrame implements Runnable{
    private List<ProcessControlBlock> PCBList;
    private List<Socket> socketList;
    private JTextArea processBox;
    private ReceiveProcess receiveProcess;
    private BlockingQueue<ProcessControlBlock> resPCB;
    private DistributeProcess distributeProcess;

    viewProcess(List<ProcessControlBlock> PCBList){
        this.PCBList = PCBList;

        setTitle("View Processes");
        setBounds(100,100,600,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //super.windowClosing(e);
                if(socketList != null && socketList.size() > 0) {
                    for (Socket s : socketList) {
                        try {
                            System.out.println("Connection Close with " + s.getInetAddress()+":"+s.getPort());
                            s.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                e.getWindow().dispose();
                System.exit(0);
            }
        });

        // process Box
        processBox = new JTextArea();
        processBox.setBounds(0, 0,595,775);
        processBox.setFont(Constant.defaultFontStyle);
        processBox.setMargin(new Insets(10,10,10,10));
        processBox.setEditable(false);

        JScrollPane scrollPaneBox = new JScrollPane(processBox);
        scrollPaneBox.setBounds(0,0,595,775);
        add(scrollPaneBox);

        new Thread(this).start();
    }


    @Override
    public void run() {
        processBox.append("Available Slaves :-\n");
        List<SlaveAddress> addresses = getSlaveAddressList();
        for(SlaveAddress sa : addresses){
            processBox.append(sa.getIpAddress() + " : " + sa.getPortNo() + "\n");
        }

        processBox.append("\n\nConnected Slaves :-\n");
        socketList = getSocketConnection(addresses);
        if(socketList.size() > 0){
            List<ObjectInputStream> inputStream = new ArrayList<>();
            List<ObjectOutputStream> outputStreams = new ArrayList<>();
            for(Socket s : socketList){
                processBox.append(s.getInetAddress() + " : " + s.getPort() + "\n");
                try {
                    outputStreams.add(new ObjectOutputStream(s.getOutputStream()));
                    inputStream.add(new ObjectInputStream(s.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            resPCB = new ArrayBlockingQueue<>(1024);
            distributeProcess = new DistributeProcess(outputStreams, PCBList, processBox);
            distributeProcess.start();

            for(ObjectInputStream in : inputStream) {
                receiveProcess = new ReceiveProcess(in, resPCB, processBox);
                receiveProcess.start();
            }

            while (resPCB.size() != PCBList.size());
            new ShowResult(resPCB).setVisible(true);
            dispose();
            for(Socket s : socketList){
                try {
                    s.close();
                    System.out.println("Connection Close : " + s.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            processBox.append("No Connected Slave Found");
            try {
                Thread.sleep(2000);
                new ProcessesList().setVisible(true);
                dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Socket> getSocketConnection(List<SlaveAddress> addresses){
        List<Socket> sockets = new ArrayList<>();
        Socket socket;
        for(SlaveAddress sa : addresses){
            try {
                socket = new Socket(sa.getIpAddress(), sa.getPortNo());
                sockets.add(socket);
            } catch (IOException ignored) {}
        }
        return sockets;
    }

    private List<SlaveAddress> getSlaveAddressList(){
        List<SlaveAddress> address = new ArrayList<>();
        SlaveAddress slaveAddress;
        StringTokenizer str;
        BufferedReader file;
        String dataRow;

        try {
            file = new BufferedReader(new FileReader(Constant.ServerList));
            dataRow = file.readLine();

            while (dataRow != null) {
                slaveAddress = new SlaveAddress();
                str = new StringTokenizer(dataRow, "\t");

                slaveAddress.setIpAddress(str.nextElement().toString());
                slaveAddress.setPortNo(Integer.parseInt(str.nextElement().toString()));

                address.add(slaveAddress);
                dataRow = file.readLine();
            }
            file.close();
        } catch (IOException ignored) {}
        return address;
    }
}
