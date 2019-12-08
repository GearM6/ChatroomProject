import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public Server(int port) { this.port = port; }

    public void execute() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected.");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void addUser(String userName) { userNames.add(userName); }

    void removeUser(String userName, Thread userThread) {
        System.out.println(userName + " wants to leave the room.");
        System.out.println("Confirming " + userName + " sign off.");
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(userThread);
            System.out.println(userName + " has left the room.");
        }
    }

    Set<String> getUserNames() { return this.userNames; }

    boolean hasUsers() { return !this.userNames.isEmpty(); }

    void broadcast(String message, UserThread excludeUser) {
        for (UserThread user : userThreads) {
            if (user != excludeUser)
                user.sendMessage(message);
        }
    }
    
    void exitConf(String message, UserThread excludeUser){
        for(UserThread user : userThreads){
            if (user == excludeUser)
                   user.sendMessage(message);
            
        }
    }

    public static void main(String[] args) throws IOException {

        Server server = new Server(8000);

        server.execute();
    }

}
