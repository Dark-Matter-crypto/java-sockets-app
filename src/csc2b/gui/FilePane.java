package csc2b.gui;

import csc2b.client.FileClient;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.StringTokenizer;


public class FilePane extends StackPane {
    private String fileName = null;
    private String fileSize = null;
    private String fileID = null;

    public FilePane(){

        //Create layout nodes
        TextField idField1 = new TextField("");
        idField1.setPrefWidth(100);
        TextField idField2 = new TextField("");
        idField2.setPrefWidth(100);
        TextField fileName = new TextField("");
        fileName.setPrefWidth(100);
        TextField fileSize = new TextField("");
        fileSize.setPrefWidth(100);
        TextArea messageForm = new TextArea("");
        messageForm.setEditable(false);
        Label id1 = new Label("File ID: ");
        id1.setPadding(new Insets(0, 10, 0, 30));
        Label id2 = new Label("File ID: ");
        id2.setPadding(new Insets(0, 10, 0, 30));
        Label name = new Label("File Name: ");
        name.setPadding(new Insets(0, 10, 0, 30));
        Label size = new Label("File Size: ");
        size.setPadding(new Insets(0, 10, 0, 30));
        Button showFiles = new Button("Show Files");
        Button sendFile = new Button("Upload File");
        Button getFile = new Button("Download File");


        FileClient clientConnection = new FileClient(2020);

        //Show files list
        showFiles.setOnAction((ActionEvent e) -> {
            String message = clientConnection.readFileList();

            StringTokenizer messageTokens = new StringTokenizer(message, "@");
            while (messageTokens.hasMoreTokens()){
                messageForm.clear();
                messageForm.appendText(messageTokens.nextToken() + "\r\n");
            }
        });

        //Upload File
        sendFile.setOnAction((ActionEvent e) -> {
            String message = clientConnection.uploadFile(Integer.parseInt(idField2.getText()), fileName.getText(), Integer.parseInt(fileSize.getText()));

            System.out.println(message);
            messageForm.appendText(message);
        });

        //Download file
        getFile.setOnAction((ActionEvent e) -> {
            String message = clientConnection.downloadFile(Integer.parseInt(idField1.getText()));
            System.out.println(message);
            messageForm.appendText(message);
        });

        //Add nodes to pane
        HBox upBox = new HBox();
        upBox.setPadding(new Insets(50, 10, 10, 30));
        upBox.getChildren().addAll(sendFile, id1, idField2, name, fileName, size, fileSize);

        HBox downBox = new HBox();
        downBox.setPadding(new Insets(10, 10, 10, 30));
        downBox.getChildren().addAll(getFile, id2, idField1);

        HBox showBox = new HBox();
        showBox.setPadding(new Insets(10, 10, 5, 30));
        showBox.getChildren().add(showFiles);

        BorderPane composer = new BorderPane();
        composer.setPadding(new Insets(15, 10, 0, 10));
        composer.setCenter(messageForm);


        VBox vBox = new VBox();
        vBox.getChildren().addAll(upBox, downBox, showBox, composer);

        BorderPane body = new BorderPane();
        body.setCenter(vBox);

        //Set the root node of the Scene
        getChildren().clear();
        getChildren().addAll(body);
    }
}
