package com.twister;

import com.twister.ERRScheduling.EnhancedRoundRobinScheduling;
import com.twister.ERRScheduling.ProcessControlBlock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ViewProcess {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket sock;
        BlockingQueue<ProcessControlBlock> rawQueue = new ArrayBlockingQueue<>(1024);
        BlockingQueue<ProcessControlBlock> resQueue = new ArrayBlockingQueue<>(1024);

        try {
            serverSocket = new ServerSocket(1653);
            System.out.println("Slave ready for Processing");
            sock = serverSocket.accept( );
            System.out.println("Master Connected\n");

            InputThread inputThread = new InputThread(sock.getInputStream(), rawQueue);
            inputThread.start();
            EnhancedRoundRobinScheduling ERRSThread = new EnhancedRoundRobinScheduling(rawQueue, resQueue);
            ERRSThread.start();
            OutputThread outputThread = new OutputThread(sock.getOutputStream(), resQueue);
            outputThread.start();

            inputThread.join();
            ERRSThread.interrupt();
            outputThread.interrupt();
            sock.close();
            System.out.println("Connection Close");
            System.exit(0);
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Connection Close");
        }
    }
}
