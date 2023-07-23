package com.example.primejavafx;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        TextArea text = new TextArea();

        Scene scene = new Scene(new ScrollPane(text), 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread( () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater( () ->
                        text.appendText("Server started at "+ new Date() + '\n'));

                Socket socket = serverSocket.accept();

                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outPutToClient = new DataOutputStream(
                        socket.getOutputStream());

                while (true) {
                    int inputNum = inputFromClient.readInt();

                    String result = isPrime(inputNum);

                    outPutToClient.writeUTF(result);

                    Platform.runLater(() -> {
                        text.appendText("Number received from Client is: "+ inputNum + '\n');
                        //text.appendText("Area is: "+ result + '\n');
                    });
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    public static String isPrime(int num){
        if(num<2) {
            return "is not prime.";
        }
        int i = 2;
        while(i<num) {
            if(num%i==0) {
                return "is not prime.";
            }
            i++;
        }
        return "is prime";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
