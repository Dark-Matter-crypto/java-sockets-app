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
import javafx.scene.paint.Color;


public class FilePane extends StackPane {
    private String fileName = null;
    private String fileSize = null;
    private String fileID = null;

    public FilePane(){
        FileClient clientConnection = new FileClient(2844);

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
        Label id = new Label("File ID: ");
        Label name = new Label("File Name: ");
        Label size = new Label("File Size: ");
        Button showFiles = new Button("Show Files");
        Button sendFile = new Button("Upload File");
        Button getFile = new Button("Download File");


        //Show files list
        showFiles.setOnAction((ActionEvent e) -> {
            messageForm.appendText(clientConnection.readFileList());
        });

        //Show files list
        sendFile.setOnAction((ActionEvent e) -> {
            messageForm.appendText(clientConnection.uploadFile(Integer.parseInt(idField2.getText()), fileName.getText(), Integer.parseInt(fileSize.getText())));
        });

        //Show files list
        getFile.setOnAction((ActionEvent e) -> {
            messageForm.appendText(clientConnection.downloadFile(Integer.parseInt(idField1.getText())));
        });

        //Add nodes to pane
        HBox upBox = new HBox();
        upBox.setPadding(new Insets(10, 10, 5, 30));
        upBox.getChildren().addAll(id, idField2, name, fileName, size, fileSize, sendFile);

        HBox downBox = new HBox();
        downBox.setPadding(new Insets(5, 10, 5, 30));
        downBox.getChildren().addAll(id, idField1, getFile);

        HBox showBox = new HBox();
        showBox.setPadding(new Insets(25, 10, 5, 30));
        showBox.getChildren().add(showFiles);

        BorderPane composer = new BorderPane();
        composer.setPadding(new Insets(15, 10, 15, 10));
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
