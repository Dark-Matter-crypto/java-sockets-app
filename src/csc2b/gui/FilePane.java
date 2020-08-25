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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
        fileName.setEditable(false);
        fileName.setPrefWidth(170);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data/client"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        Button selectFile = new Button("Choose File");
        TextArea messageForm = new TextArea("");
        messageForm.setEditable(false);
        Label id1 = new Label("File ID: ");
        id1.setPadding(new Insets(0, 10, 0, 30));
        Label id2 = new Label("File ID: ");
        id2.setPadding(new Insets(0, 10, 0, 10));
        Label id3 = new Label("  ");
        id3.setPadding(new Insets(0, 10, 0, 0));
        Button showFiles = new Button("Show Files");
        Button sendFile = new Button("Upload File");
        Button getFile = new Button("Download File");

        //Connect client to server
        FileClient clientConnection = new FileClient(2844);

        if (clientConnection.isClientedConnected())
            messageForm.appendText("Connected to the file server.\r\n");

        //Show files list
        showFiles.setOnAction((ActionEvent e) -> {
            String message = clientConnection.readFileList();
            StringTokenizer messageTokens = new StringTokenizer(message, "@");
            messageForm.clear();
            while (messageTokens.hasMoreTokens()){
                messageForm.appendText(messageTokens.nextToken() + "\r\n");
            }
        });


        //Upload File
        sendFile.setOnAction((ActionEvent e) -> {
            File fileToSend = new File("data/client/" + fileName.getText());
            StringTokenizer fileTokens = new StringTokenizer(fileName.getText(), ".");
            String name = fileTokens.nextToken();
            int size = (int) fileToSend.length();
            String message = clientConnection.uploadFile(Integer.parseInt(idField2.getText()), name, size);

            System.out.println(message);
            messageForm.appendText(message);
        });

        //Choose file
        selectFile.setOnAction((ActionEvent e) -> {
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                fileName.setText(selectedFile.getName());
            }
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
        upBox.getChildren().addAll(selectFile, fileName, id1, idField2, id3, sendFile);

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
