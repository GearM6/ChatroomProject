import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;


import java.net.*;
import java.io.*;
import java.time.*;
import java.text.*;
import java.util.*;

public class ChatClient{
    private String hostname;
    private int port;
    private String username;
    private StringBuilder chatLogBuilder;
    private Controller controller;
    private Socket socket;

    public ChatClient(String hostname, int port, String username, Controller controller) throws IOException {
        this.hostname = hostname;
        this.port = port;
        this.controller = controller;
        this.username = username;
        this.chatLogBuilder = new StringBuilder();
        this.socket = new Socket(hostname,port);
    }

    public void execute(){
        controller.getUserNameTextField().setText(this.username);
        controller.getMessageTextField().requestFocus();
        try{
            System.out.println("Connected to chat server");
            new ReadThread(socket,this, controller).start();
            new WriteThread(socket,this, this.username, controller).start();
        }
        catch(UnknownHostException e){
            System.out.println("Server not found: " + e.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    void setUsername(){
        this.username = controller.getUserName();
    }

    String getUsername(){
        return this.username;
    }
    public void terminate() throws IOException {
        PrintWriter writer = new PrintWriter(this.socket.getOutputStream());
        writer.println(".");
        writer.flush();
        this.socket.close();
    }

}

class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;
    private String response;
    private Parent root;
    private Controller controller;
    DateTimeFormatter formatter;


    public ReadThread(Socket socket, ChatClient client, Controller controller) throws IOException {
        this.socket = socket;
        this.client = client;
        this.root = FXMLLoader.load(getClass().getResource("ChatAppStyle.fxml"));
        this.controller = controller;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                do{
                    response = reader.readLine();
                    System.out.println("\n" + response);

                    // prints the username after displaying the server's message
                    if (client.getUsername() != null) {
                        System.out.print("" + client.getUsername() + ": ");
                        controller.updateChatLog("\n" + response);
                    }
                }while(response.equals("."));    //exits if client enters "."
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }
    }
}

class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
    private String username;
    private Controller controller;
    DateTimeFormatter formatter;

    public WriteThread(Socket socket, ChatClient client, String username, Controller controller) {
        this.socket = socket;
        this.client = client;
        this.username = username;
        this.controller = controller;
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.getDefault());

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        String username = this.username;
        client.setUsername();
        writer.println(username);

        String text = "";
        do {
            synchronized (controller.messageBuffer){
                if(!controller.messageBuffer.isEmpty()){
                    text = controller.getNextMessage();
                    if(!text.equals("")) {
                        String time = this.formatter.format(LocalTime.now());
                        controller.updateChatLog("\n" + "[" + time + "] " + username + ": " + text);
                        writer.println(text);
                    }
                }
            }

        } while (!text.equals("."));
        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }

    }
}