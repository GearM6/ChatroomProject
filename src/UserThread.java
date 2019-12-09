import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected.");
        }
    }

    void sendMessage(String message) {
        writer.println(message);

    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUser(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "" +userName + ": " + clientMessage;
                if(clientMessage.equals(".")){
                    serverMessage = "Confirming sign off";
                    server.exitConf(serverMessage, this);
                }
                else{
                  server.broadcast(serverMessage, this);
                }
            } while (!clientMessage.equals("."));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has left.";
            server.broadcast(serverMessage, this);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
