/*
How to run code:
(1) Open 3 terminals
-1 terminal for Server
-2 terminals for separate clients
(2) For the server terminal
run: 
java Server [XXXX]
X's being any integer between 0-9
For the client terminal
run:
java UserClient localhost [XXXX]
X's being the previously listed integers from the server port
*/

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private int portNumber;
    private Set<String> userHandle = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();
 
    public Server(int portNumber) {
        this.portNumber = portNumber;
    }
 /////////////////////////////////////////////////////////////////////////////
    public void exe() {
        try (ServerSocket servSocket = new ServerSocket(portNumber)) {
 
            System.out.println("Server is listening on portNumber " + portNumber);
 
            while (true) {
                Socket sock = servSocket.accept();
                System.out.println("New user connected");
 
                UserThread newUser = new UserThread(sock, this);
                userThreads.add(newUser);
                newUser.start();
 
            }
 
        } 
        catch (IOException ohnolol) {
            System.out.println("Error: " + ohnolol.getMessage());
            ohnolol.printStackTrace();
        }
    }
 /////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use case syntax: java ChatServer <portNumber-number>");
            System.exit(0);
        }
 
        int portNumber = Integer.parseInt(args[0]);
 
        Server server = new Server(portNumber);
        server.exe();
    }
/////////////////////////////////////////////////////////////////////////////
    void pipelineComms(String msg, UserThread exclUser){
        for(UserThread AserThread : userThreads){
            if(AserThread != exclUser){
                AserThread.sendMessage(msg);
            }
        }
    }
 /////////////////////////////////////////////////////////////////////////////
    //Stores username of recent client connected

    void adduserHandle(String userHandles) {
        userHandle.add(userHandles);
    }

 /////////////////////////////////////////////////////////////////////////////
/*removes user threads after disconnectging */
    void removeUser(String userHandles, UserThread AserThreads) {
        boolean removed = userHandle.remove(userHandle);
        if (removed) {
            userThreads.remove(AserThreads);
            System.out.println(userHandle + " quitted");
        }
    }
 /////////////////////////////////////////////////////////////////////////////
    Set<String> getUserHandles() {
        return this.userHandle;
    }
 
/////////////////////////////////////////////////////////////////////////////
    boolean hasUsers() {
        return !this.userHandle.isEmpty();
    }
}
/////////////////////////////////////////////////////////////////////////////
class UserThread extends Thread { //fix here
    private Socket sock;
    private Server server;
    private PrintWriter writeMe;
 
    public UserThread(Socket sock, Server server) {
        this.sock = sock;
        this.server = server;
    }
 
    public void run() {
        try {
            InputStream input = sock.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = sock.getOutputStream();
            writeMe = new PrintWriter(output, true);

            String userHandle = reader.readLine();
            server.adduserHandle(userHandle);
 
            String serverMessage = "New user: " + userHandle;
            server.pipelineComms(serverMessage, this);
           
 
            String messageClient;
 
            do {
                messageClient = reader.readLine();
                serverMessage = "[" + userHandle + "]: " + messageClient;
                server.pipelineComms(serverMessage, this);
              
 
            } while (!messageClient.equals("bye"));
 
            server.removeUser(userHandle, this);
            sock.close();
 
            serverMessage = userHandle + " has quit.";
            server.pipelineComms(serverMessage, this);
 
        } catch (IOException ohnolol) {
            System.out.println("Error in UserThread: " + ohnolol.getMessage());
            ohnolol.printStackTrace();
        }
    }
 
    void sendMessage(String msg) {
        writeMe.println(msg);
    }
}
