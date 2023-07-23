package com.example.primejavafx;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a number to check prime: "));

        TextField text = new TextField();
        text.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(text);

        BorderPane mainPane = new BorderPane();
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);

        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        text.setOnAction(e -> {
            try {
                int num = Integer.parseInt(text.getText().trim());

                toServer.writeInt(num);
                toServer.flush();

                String result = fromServer.readUTF();

                ta.appendText("Number is "+ num + '\n');
                ta.appendText(num + " " + result + '\n');
            }
            catch (IOException ex){
                System.err.println(ex);
            }
        });

        try {
            Socket socket = new Socket("localhost", 8000);

            fromServer = new DataInputStream(socket.getInputStream());

            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            ta.appendText(ex.toString()+ '\n');
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
