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

public class UserClient {
    private String host;
    private int portNumber;

    //will replace with account login/creation
    private String userHandle;
    //used for testing purposes
    //flag 1

    public UserClient(String host, int portNumber){
        this.host = host;
        this.portNumber = portNumber;
    }

    public void exe(){
        try{
            Socket sock = new Socket(host, portNumber);
            System.out.println("You are connected to the chat");

            new readingThreads(sock, this).start();
            new writingThreads(sock, this).start();
        }
        catch(UnknownHostException ohnolol){
            System.out.println("Can't find server: " + ohnolol.getMessage());   
        }
        catch(IOException ohnolol){
            System.out.println("Input error: " + ohnolol.getMessage());
        }
    }

    //pseudo function for user's username
    //will work, just need to replace with our own functionality
    void establishHandle(String userHandle){
        this.userHandle = userHandle;
    }

    String getHandle(){
        return this.userHandle;
    }

    public static void main(String[] args){
        if(args.length < 2){
            return;
        }
        String host = args[0];
        int portNumber = Integer.parseInt(args[1]);
        UserClient currentClient = new UserClient(host, portNumber);
        currentClient.exe();
    }

}
/////////////////////////////////////////////////////////////////////////////
class readingThreads extends Thread {
    private BufferedReader readthrough;
    private Socket sock;
    private UserClient currentClient;
 
    public readingThreads(Socket sock, UserClient currentClient) {
        this.sock = sock;
        this.currentClient = currentClient;
 
        try {
            InputStream input = sock.getInputStream();
            readthrough = new BufferedReader(new InputStreamReader(input));
        } 
        catch (IOException ohnolol){
            System.out.println("Error getting input stream: " + ohnolol.getMessage());
            ohnolol.printStackTrace();
        }
    }
 
    public void run() {
        while (true) {
            try {

                String userRes = readthrough.readLine();
                System.out.println("\n" + userRes);
 
                
                if (currentClient.getHandle() != null) {
                    System.out.print("[" + currentClient.getHandle() + "]: ");
                }
            } catch (IOException ohnolol) {
                System.out.println("Error: " + ohnolol.getMessage());
                ohnolol.printStackTrace();
                break;
            }
        }
    }
}

/////////////////////////////////////////////////////////////////////////////
class writingThreads extends Thread {
    private PrintWriter writeMe;
    private Socket sock;
    private UserClient currentClient;
 
    public writingThreads(Socket sock, UserClient currentClient) {
        this.sock = sock;
        this.currentClient = currentClient;
 
        try {
            OutputStream output = sock.getOutputStream();
            writeMe = new PrintWriter(output, true);
        } catch (IOException ohnolol) {
            System.out.println("Error: " + ohnolol.getMessage());
            ohnolol.printStackTrace();
        }
    }
 
    public void run() {
 
        Console console = System.console();
 
        String userHandle = console.readLine("\nEnter your name: ");
        currentClient.establishHandle(userHandle);
        writeMe.println(userHandle);
 
        String txt;
 
        do {
            txt = console.readLine("[" + userHandle + "]: ");
            writeMe.println(txt);
 
        } while (!txt.equals("bye"));
 
        try {
            sock.close();
        } catch (IOException ohnolol) {
 
            System.out.println("Error: " + ohnolol.getMessage());
        }
    }
}
