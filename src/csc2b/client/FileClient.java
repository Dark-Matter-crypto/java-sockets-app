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
            Socket severConnection = new Socket("localhost", port);
            System.out.println("Connected to the file server.");

            pw = new PrintWriter(severConnection.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(severConnection.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(severConnection.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(severConnection.getInputStream()));

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

    public String uploadFile(int fileID, String fileName, int fileSize){
        String clientResponse = null;
        pw.flush();
        pw.println("TAKEOFF <" + fileID + "> <" + fileName + "> <" + fileSize + "> <Pdf>");
        pw.flush();

        File pdfFile = new File("data/client/" + fileName + ".pdf");

        if (pdfFile.exists()){

            try{
                //Send the binary file
                FileInputStream fs = new FileInputStream(pdfFile);
                byte[] buffer = new byte[1024];
                int n = 0;

                while((n = fs.read(buffer)) > 0){
                    out.write(buffer, 0, n);
                    out.flush();
                }

                fs.close();
                System.out.println("File sent to server.");
                clientResponse = "File sent to server.";
                return clientResponse;
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return clientResponse;
    }

    public String readFileList(){
        pw.flush();
        pw.println("SHOW");
        pw.flush();

        String serverResponse = null;
        try {
            serverResponse = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    public String downloadFile(int fileID){
        pw.flush();
        pw.println("LANDING <" + fileID + ">");
        pw.flush();

        String severResponse = null;

        FileOutputStream fos = null;
        try{
            String response = br.readLine();
            String fileName = br.readLine();
            int fileSize = Integer.parseInt(response);

            File downloadFile = new File("data/client/" + fileName);
            fos = new FileOutputStream(downloadFile);
            byte[] buffer = new byte[2048];
            int n = 0;
            int totalBytes = 0;

            while(totalBytes != fileSize)
            {
                n = in.read(buffer,0, buffer.length);
                fos.write(buffer,0,n);
                fos.flush();
                totalBytes += n;
            }

            fos.close();

            severResponse = "File size: " + response + "\nFile, " + fileName + ".pdf, downloaded successfully.\n";
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
