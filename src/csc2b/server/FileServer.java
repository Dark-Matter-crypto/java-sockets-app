package csc2b.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    private static ServerSocket ss;
    private static int port;
    private static boolean serverRunning = false;

    //Constructor
    public FileServer(){

    }

    //Method to start running the server
    public static void runServer(){
        System.out.println("Binding to port: " + 2844);
        try {
            ss = new ServerSocket(2020);
            serverRunning = true;
            System.out.println("Waiting for connections. . .\n");
            runServer();
        }
        catch (IOException e) {
            System.err.println("Failed to bind to port: " + 2844);
        }

        while(serverRunning){
            try {
                System.out.println("Hello 1");
                Socket clientConnection = ss.accept();  //Accepts a client connection
                System.out.println("Hello 2");
                FileServerHandler serverHandler = new FileServerHandler(clientConnection);
                System.out.println("Hello 3");
                Thread thread = new Thread(serverHandler);
                thread.start();
                System.out.println("Hello 4");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        runServer();
    }
}
