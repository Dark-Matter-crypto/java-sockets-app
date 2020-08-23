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
    private String toAddress = null;
    private String fromAddress = null;
    private String ccAddress = null;
    private String subject = null;
    private String message = null;

    public FilePane(){
        FileClient clientConnection = new FileClient(2844);

        //Create layout nodes
        TextField fromAddressForm = new TextField("");
        fromAddressForm.setPrefWidth(400);
        TextField toAddressForm = new TextField("");
        toAddressForm.setPrefWidth(400);
        TextField ccAddressForm = new TextField("");
        ccAddressForm.setPrefWidth(400);
        TextField subjectForm = new TextField("");
        subjectForm.setPrefWidth(400);
        TextArea messageForm = new TextArea("");
        Button emailSender = new Button("Send Email");
        Label response = new Label();
        response.setPadding(new Insets(5, 10, 5, 10));
        Label msgLabel = new Label("Message:");
        msgLabel.setPadding(new Insets(5, 10, 5, 10));

        //Read data from the forms and send email
        emailSender.setOnAction((ActionEvent e) -> {
            if (fromAddressForm.getText().isEmpty() || toAddressForm.getText().isEmpty() || messageForm.getText().isEmpty()){
                response.setTextFill(Color.web("#8B0000", 1));
                response.setText("Please fill out all fields in order to send email.");
            }
            else{

            }
        });

        //Add nodes to pane
        HBox fromBox = new HBox();
        fromBox.setPadding(new Insets(25, 10, 5, 30));
        fromBox.getChildren().add(new Label("From:      "));
        fromBox.getChildren().add(fromAddressForm);
        HBox toBox = new HBox();
        toBox.setPadding(new Insets(5, 10, 5, 30));
        toBox.getChildren().add(new Label("To:          "));
        toBox.getChildren().add(toAddressForm);
        HBox ccBox = new HBox();
        ccBox.setPadding(new Insets(5, 10, 5, 30));
        ccBox.getChildren().add(new Label("Cc:          "));
        ccBox.getChildren().add(ccAddressForm);
        HBox subBox = new HBox();
        subBox.setPadding(new Insets(5, 10, 5, 30));
        subBox.getChildren().add(new Label("Subject:  "));
        subBox.getChildren().add(subjectForm);
        BorderPane composer = new BorderPane();
        composer.setPadding(new Insets(15, 10, 15, 10));
        composer.setCenter(messageForm);
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10, 10, 5, 10));
        buttonBox.getChildren().add(emailSender);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(fromBox, toBox, ccBox, subBox, response, msgLabel, composer, buttonBox);

        BorderPane body = new BorderPane();
        body.setCenter(vBox);

        //Set the root node of the Scene
        getChildren().clear();
        getChildren().addAll(body);
    }
}
