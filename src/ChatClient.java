
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

	public ChatClient(String hostname, int port, String username, Controller controller){
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.controller = controller;
		this.chatLogBuilder = new StringBuilder();
	}
		
	public void execute(){
		try{
			Socket socket = new Socket(hostname,port);
			System.out.println("Connected to chat server");
			new ReadThread(socket,this, controller).start();
			new WriteThread(socket,this, this.username).start();
		}
		catch(UnknownHostException e){
			System.out.println("Server not found: " + e.getMessage());
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}



	void setUsername(String username){
		this.username = username;
	}
	
	String getUsername(){
		return this.username;
	}

}

class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;
	private String response;
	private Parent root;
	private Controller controller;


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
                    System.out.print("[" + client.getUsername() + "]: ");
                    controller.updateChatLog(response);
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

    public WriteThread(Socket socket, ChatClient client, String username) {
        this.socket = socket;
        this.client = client;
        this.username = username;
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
        Scanner scanner = new Scanner(System.in);

        String username = this.username;
        client.setUsername(username);
        writer.println(username);

        String text;

        do {
            text = scanner.nextLine();
            writer.println(text);
        } while (!text.equals("."));

        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
