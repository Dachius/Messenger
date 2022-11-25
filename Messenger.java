import java.util.*;

/*
 * Basic proof of concept
 */

public class Messenger { 
    private static Account activeAccount;
    public static void main(String[] args){
        activeAccount = loginLoop();
        menuLoop(activeAccount);
    }

    // Initial login process.
    public static Account loginLoop(){
        Scanner input = new Scanner(System.in);
        while(true){
            System.out.println("            [Login Menu]            ");
            System.out.println("[login] Login with existing account.");
            System.out.println("  [new] Create new account.");
            System.out.println(" [exit] Exit program.");
              System.out.print("Choose: ");
            switch(input.nextLine().toLowerCase()){
                case "login":
                    Account test = Account.login();
                    if(test != null){
                        return test;
                    }
                    
                    System.out.println("Error during login process.");
                    break;
                case "new":
                    return Account.create();
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please enter one of the commands.");
            }
        }
    }

    /*
     * Create new chat, enter into chat, add new contact.
     */
    public static void menuLoop(Account activeAccount){
        Scanner input = new Scanner(System.in);
        outer: while(true){
            System.out.println("                 [Navigation Menu]                 ");
            System.out.println("  [view chats] View chats that you are a member of.");
            System.out.println(" [create chat] Create a new chatroom.");
            System.out.println(" [add contact] Add a new contact.");
            System.out.println("        [exit] Exit program.");
              System.out.print("Choose: ");
            switch(input.nextLine().toLowerCase()){
                case "view chats":
                    // Display chats
                    break outer;
                case "create chat":
                    // Create chats.
                    break outer;
                case "add contact":
                    // Add a new contact.
                    break outer;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please enter one of the commands.");
            }
        }
    }

    /*
     * Send and receive messages, add someone to the chat, etc.
     * WIP
     */
    public static void chatLoop(){
        Scanner input = new Scanner(System.in);
        
        while(true){
            System.out.println("               [Chat]               ");
            System.out.println("???");
            System.out.println("???");
            System.out.println(" [exit] Exit program.");
              System.out.print("Choose: ");
            switch(input.nextLine().toLowerCase()){
                case "":

                    break;
                case "?":
                    
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please enter one of the commands.");
            }
        }
    }
}

/*
 * Account.
 */
class Account {
    private static final int UUID_LENGTH = 8;

    private final String UUID;
    private String name;
    private List<Account> contacts;
    private List<Chat> chats;

    public Account(String name){
        UUID = Account.newUUID();
        this.name = name;
        contacts = new ArrayList<>();
        chats = new ArrayList<>();
    }

    public void addChat(Chat c){
        chats.add(c);
    }

    public String getUUID(){
        return UUID;
    }

    public String getName(){
        return this.name;
    }

    // WIP, need way to get reference to account object using UUID.
    public void addContact(String ID){
        // Contacts.add(Data.getAccount(ID));
    }

    /*
     * Create new UUID.
     * We're not technically checking that this ID is unique, but there will probabilistically
     * be no ID colissions among currently living humans because there are 3 * 10^14 combinations.
     * Feel free to add some checking functionality if you want, but it doesn't matter.
     * 
     * Generates a string of random numbers or letters, capital and non-capital.
     */
    private static String newUUID(){
        String building = "";
        Random rand = new Random();
        for(int i = 0; i < UUID_LENGTH; i++){
            int randNum = rand.nextInt(62);
            if(randNum <= 9){
                building += (char)(randNum + 48);
            } else if(randNum <= 35){
                building += (char)(randNum + 55);
            } else{
                building += (char)(randNum + 61);
            }
        }

        return building;
    }

    /*
     * Login with existing account. Storing command line inteface here
     * because I dunno how to properly structure command line interfaces.
     * 
     * We need some sort of data class which has a map from UUIDs to account objects.
     * I can code this up and use some JSON but eh.
     */
    public static Account login(){
        Scanner input = new Scanner(System.in);

        String id, password;

        // Get UUID.
        while(true){
            System.out.println("---------------------------------");
            System.out.println("Please provide your account UUID.");
              System.out.print("[UUID] ");
            
            id = input.nextLine();

            // Check if UUID is possibly correct, i.e. has length 8 and is alphanumeric.
            // WIP - We also want to see if UUID actually matches one of the existing accounts.
            if(!(id.length() == 8 && id.matches("^[a-zA-Z0-9]*$"))){
                System.out.println("UUID is impossible. Try again.");
                continue;
            } else{
                break;
            }
        }

        /*
         * Get password.
         * Made maximum attempts logic for fun- doesn't actually provide any security.
         * Passwords will presumably be stored in plaintext so all for fun.
         */
        int attempts = 3;
        while(true){
            System.out.println("---------------------------------");
            System.out.println("Please provide your account password.");
              System.out.print("[Password] ");
            
            password = input.nextLine();

            // Check if password matches account with chosen UUID.
            // WIP

            String truePass = "temp";
            if(!password.equals(truePass)){
                attempts--;
                System.out.println("Invalid password. (" + attempts + "/3 attempts remain)");
            } else{
                System.out.println("Login successful.");
                break;
            }

            if(attempts == 0){
                System.out.println("No further attempts, returning to login menu.");
                return null;
            } 
        }

        // WIP - Get some way to fetch account object given UUID.
        // return Data.getAccount(UUID);
        return new Account("temp");
    }

    /*
     * Create new account.
     * WIP.
     */
    public static Account create(){
        return new Account("temp");
    }
}

/*
 * Chat.
 * 
 * Not quite sure how we want to structure the messages.
 */
class Chat {
    private Account owner;
    private List<Account> accounts;

    private String name;
    private List<Message> messages;

    public Chat(Account owner, String name){
        this.owner = owner;
        accounts = new ArrayList<>();

        this.name = name;
        messages = new ArrayList<>();

        this.addAccount(owner);
    }

    public Chat(Account owner){
        this(owner, "Unnamed Chat");
    }

    // Add new message
    public void pushMessage(Message m){
        messages.add(m);
    }

    // Somehow retrieve messages. WIP.
    public void pullMessage(){
        
    }

    public Account getOwner(){
        return this.owner;
    }

    public String getName(){
        return this.name;
    }

    public void addAccount(Account acc){
        accounts.add(acc);
        acc.addChat(this);
    }
}

/*
 * Message.
 * One could theoretically add functionality for attachments more easily if you used this data structure.
 */
class Message {
    private String text;

    public Message(String text){
        this.text = text;
    }

    public String getMessageString(){
        return text;
    }
}