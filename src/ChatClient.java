import java.net.*;
import java.io.*;

public class ChatClient{
	private String hostname;
	private int port;
	private String username;
	
	public ChatClient(String hostname, int port){
		this.hostname = hostname;
		this.port = port;
	}
		
	public void execute(){
		try{
			Socket socket = new Socket(hostname,port);
			System.out.println("Connected to chat server");
			new ReadThread(socket,this).start();
			new WriteThread(socket,this).start();
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
	
	public static void main(String[] args){
		if(args.length<2)
			return;
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		
		ChatClient client = new ChatClient(hostname,port);
		client.execute();
	}
}
class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;
	private String response;
    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
 
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
                }
			}while(response.equals("."));
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

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
 
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
 
        Console console = System.console();
 
        String username = console.readLine("\nEnter your name: ");
        client.setUsername(username);
        writer.println(username);
 
        String text;
 
        do {
            text = console.readLine("[" + username + "]: ");
            writer.println(text);
 
        } while (!text.equals("."));
 
        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
