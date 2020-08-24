package csc2b.server;

import csc2b.gui.FilePane;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileServerHandler implements Runnable{

    private Socket clientConnection;

    private static PrintWriter pw = null;
    private static BufferedReader br = null;
    private static DataOutputStream out = null;
    private static DataInputStream in = null;

    private static final String FILES_LIST = "data/server/PdfList.txt";

    //Constructor
    public FileServerHandler(Socket clientConnection){
        System.out.println("Hello world");
        this.clientConnection = clientConnection;

        try {
            pw = new PrintWriter(clientConnection.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(clientConnection.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(clientConnection.getInputStream()));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server ready to process files.");

        //Pattern for SHOW command
        Pattern showCommand = Pattern.compile("^SHOW$");
        //Pattern for LANDING command
        Pattern landCommand = Pattern.compile("^LANDING\\s<(\\d+)>$");
        //Pattern for TAKEOFF command
        Pattern takeCommand = Pattern.compile("^TAKEOFF\\s<(\\d+)>\\s<(\\w+)>\\s<(\\d+)>\\s<(Pdf)>$");

        try{
            while(true){
                String command = br.readLine();
                System.out.println("Command: " + command);

                Matcher showMatcher = showCommand.matcher(command);
                Matcher landMatcher = landCommand.matcher(command);
                Matcher takeMatcher = takeCommand.matcher(command);

                if (showMatcher.matches()){
                    File filesList = new File(FILES_LIST);

                    if (filesList.exists()){
                        Scanner txtin = null;

                        try{
                            txtin = new Scanner(filesList);
                            String list = "";
                            while (txtin.hasNext()){
                                String entry = txtin.nextLine();
                                list += entry + "@";
                            }
                            System.out.println(list);
                            pw.println(list);
                            pw.flush();

                        }catch(FileNotFoundException ex){
                            ex.printStackTrace();
                        }
                        finally {
                            if (txtin != null){
                                txtin.close();
                            }
                        }
                    }
                }
                else if (landMatcher.matches()){
                    System.out.println("Matched for landing");
                    File filesList = new File(FILES_LIST);
                    String pdfName = null;

                    if (filesList.exists()){
                        System.out.println("File does exists");
                        Scanner txtin = null;

                        try{
                            txtin = new Scanner(filesList);
                            while (txtin.hasNext()){
                                String line = txtin.nextLine();
                                StringTokenizer fileTokens = new StringTokenizer(line);
                                if (fileTokens.nextToken().equals((String)landMatcher.group(1))){
                                    pdfName = fileTokens.nextToken();
                                    System.out.println(pdfName);
                                    break;
                                }
                            }
                        }catch(FileNotFoundException ex){
                            ex.printStackTrace();
                        }
                        finally {
                            if (txtin != null){
                                txtin.close();
                            }
                        }

                        File pdfFile = new File("data/server/" + pdfName);

                        if (pdfFile.exists()){
                            System.out.println("PDF FOUND");
                            //Send the file size
                            pw.println(pdfFile.length());
                            pw.flush();
                            //Send the file name
                            pw.println(pdfName);
                            pw.flush();

                            //Send the binary file
                            FileInputStream fs = new FileInputStream(pdfFile);
                            byte[] buffer = new byte[2048];
                            int n = 0;

                            while((n = fs.read(buffer)) > 0){
                                out.write(buffer, 0, n);
                                out.flush();
                            }

                            fs.close();
                            System.out.println("File sent to client");
                        }
                        else {
                            System.out.println("PDF NOT FOUND!!!!!!!!!");
                        }

                    }
                    else{
                        System.out.println("File does not exist");
                    }

                }
                else if (takeMatcher.matches()){
                    File filesList = new File(FILES_LIST);
                    String pdfName = takeMatcher.group(2);
                    System.out.println("PDF Name: " + pdfName);
                    File pdfFile = new File("data/server/" + pdfName);

                    FileOutputStream fos = new FileOutputStream(pdfFile);
                    int fileSize = Integer.parseInt(String.valueOf(pdfFile.length()));
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
                    System.out.println("File received from client.");

                    if (filesList.exists()){
                        PrintWriter txtout = null;

                        try{
                            txtout = new PrintWriter(filesList);
                            txtout.println(takeMatcher.group(1) + " " + takeMatcher.group(2) + " " + takeMatcher.group(4) + "\n");
                        }
                        catch (FileNotFoundException ex){
                            ex.printStackTrace();
                        }
                        finally {
                            if (txtout != null) txtout.close();
                        }
                    }
                }
                else {
                    System.out.println("Command not recognised.");
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
