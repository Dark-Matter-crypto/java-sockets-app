package csc2b.client;

import java.io.*;
import java.net.Socket;

public class FileClient {

    private static PrintWriter pw = null;
    private static BufferedReader br = null;
    private static DataOutputStream out = null;
    private static DataInputStream in = null;
    private static int port;

    //Constructor
    public FileClient(int port) {
        this.port = port;
        try {
            Socket severConnection = new Socket("localhost", this.port);
            System.out.println("Connected to the file server.");

            pw = new PrintWriter(severConnection.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(severConnection.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(severConnection.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(severConnection.getInputStream()));

            System.out.println(readServerResponse(br));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to read response from the server
    private static String readServerResponse(BufferedReader br){
        String res = null;
        try {
            res = br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    //Method to write to the server
    private static void writeMessage(PrintWriter out, String message){
        out.println(message);
        out.flush();
    }

    public void uploadFile(int fileID, String fileName, int fileSize){

    }

    public void readFileList(){

    }

    public String downloadFile(int fileID){
        pw.println("LANDING <" + fileID + ">");
        pw.flush();

        String severResponse = null;

        FileOutputStream fos = null;
        try{
            String response = br.readLine();

            severResponse = "File size: " + response;
            System.out.println("File downloaded to the client directory.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return severResponse;
    }


}
