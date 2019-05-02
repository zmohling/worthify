package com.serverApp.serverApp.websocket;

import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * websocket server that handles all online users and their threads
 *
 * @author Griffin Stout
 */
public class EchoServer implements Runnable {

    private ServerSocket socket = null;
    private Thread thread = null;
    ArrayList<EchoServerThread> onlineUsers = new ArrayList<>();
    private static int numOnlineUsers = 0;

    public EchoServer(int port, boolean start){
        if (start) {
            try {
                socket = new ServerSocket(port);
                start();
            } catch (Exception e) {
                System.out.println("Cannot bind to port: " + port + " because " + e.getMessage());
            }
        }
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run(){
        System.out.println("EchoServer started.");
        while(thread != null){
            try{
                addThread(socket.accept());
            }catch(IOException e){
                System.out.println("Failure adding thread: " + e.getMessage());
            }
        }
    }

    public void addThread(Socket socket) throws IOException{
        onlineUsers.add(new EchoServerThread(this, socket));
        onlineUsers.get(numOnlineUsers).open();
        onlineUsers.get(numOnlineUsers).start();
        numOnlineUsers++;
        printOnlineUsers();
    }

    public void remove(EchoServerThread threadToRemove){
        onlineUsers.remove(threadToRemove);
        numOnlineUsers--;
        onlineUsers.trimToSize();
        printOnlineUsers();
    }

    //USD THIS
    public synchronized void handle(EchoServerThread handleThread, String input) {

        System.out.println(handleThread.getID() + ": " + input);

        handleThread.send(input);

    }

    public void printOnlineUsers(){
        System.out.println("------------------------------");
        System.out.println("Online users: " + getNumOnlineUsers());
        System.out.println("------------------------------");
    }

    public int getNumOnlineUsers(){
        return numOnlineUsers;
    }

    @GetMapping("/users/numOnline")
    public String getNumOnline(){
        return "{\"num\":\"" + getNumOnlineUsers() +"\"}";
    }
}
