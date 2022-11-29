import java.util.*;

/*
 * Basic proof of concept
 */

@SuppressWarnings({"resource"})

public class Messenger {
    private static Account activeAccount;
    private static ArrayList<Account> existingAccounts = new ArrayList<Account>();

    public static void main(String[] args) {
        loginLoop();
        // menuLoop(activeAccount);
    }

    // Initial login process.
    public static void loginLoop() {
        Scanner input = new Scanner(System.in);
        activeAccount = null;
        while (true) {
            System.out.println("            [Login Menu]            ");
            System.out.println("[login] Login with existing account.");
            System.out.println("  [new] Create new account.");
            System.out.println(" [exit] Exit program.");
            System.out.print("Choose: ");
            switch (input.nextLine().toLowerCase()) {
                case "login":
                    activeAccount = Account.login(existingAccounts);
                    if (activeAccount != null) {
                        // return activeAccount;
                        menuLoop(activeAccount);
                    }
                    // System.out.println("Error during login process.");
                    break;
                case "new":
                    System.out.print("\nEnter Username: ");
                    String name = input.nextLine();

                    System.out.print("Enter password: ");
                    String password = input.nextLine();
                    System.out.println();

                    activeAccount = new Account(name, password);
                    existingAccounts.add(activeAccount);

                    // return account;
                    menuLoop(activeAccount);
                    break;
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
    public static void menuLoop(Account activeAccount) {
        Scanner input = new Scanner(System.in);
        outer: while (true) {
            System.out.println("                  [Navigation Menu]                  ");
            System.out.println("    [view chats] View chats that you are a member of.");
            System.out.println("   [create chat] Create a new chatroom.");
            System.out.println("  [display chat] Enter a chatroom.");
            System.out.println("   [add contact] Add a new contact.");
            System.out.println(" [view contacts] View contact list.");
            System.out.println("          [UUID] Get account UUID.");
            System.out.println("        [logout] Logout of account.");
            System.out.println("          [exit] Exit program.");
            System.out.print("Choose: ");
            switch (input.nextLine().toLowerCase()) {
                case "view chats":
                    for(Chat c : activeAccount.getChats()){
                        System.out.println("[" + c.getName() + "]");
                    }
                    break;
                case "create chat":
                    System.out.print("Chat name: ");
                    activeAccount.addChat(new Chat(activeAccount, input.nextLine()));
                    break;
                case "view chat":
                    System.out.println("Enter chat name: ");
                    chatLoop(activeAccount, activeAccount.getChat(input.nextLine()));
                    break;
                case "add contact":
                    // Add a new contact.
                    System.out.print("Enter UUID of new contact: ");
                    String ID = input.nextLine();
                    activeAccount.addContact(existingAccounts, ID);
                    break;
                case "view contacts":
                    activeAccount.viewContacts();
                    break;
                case "uuid":
                    System.out.println("UUID: " + activeAccount.getUUID() + "\n");
                    break;
                case "logout":
                    loginLoop();
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
    public static void chatLoop(Account activeAccount, Chat chat) {
        Scanner input = new Scanner(System.in);

        outer: while (true) {
            System.out.println("               [Chat]               ");
            System.out.println("[view] View messages.");
            System.out.println("[send] Send new message.");
            System.out.println("[exit] Exit program.");
            System.out.print("Choose: ");
            switch (input.nextLine().toLowerCase()) {
                case "view":
                    List<Message> messages = chat.getMessages();
                    for(Message m : messages){
                        System.out.println("[" + m.getSender().getName() + "] " + m.getMessageString());
                    }
                    break;
                case "send":
                    System.out.println("Message: ");
                    chat.pushMessage(new Message(input.nextLine(), activeAccount));
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
    private String password;
    private List<Account> contacts;
    private List<Chat> chats;

    public Account(String name, String password) {
        UUID = Account.newUUID();
        this.name = name;
        this.password = password;
        contacts = new ArrayList<>();
        chats = new ArrayList<>();
    }

    public void addChat(Chat c) {
        chats.add(c);
    }

    public List<Chat> getChats() {
        return this.chats;
    }

    public Chat getChat(String name) {
        for(Chat c : chats){
            if(c.getName() == name){
                return c;
            }
        }

        System.out.println("Chat not found.");
        return null;
    }

    public String getUUID() {
        return UUID;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    // WIP, need way to get reference to account object using UUID.
    public void addContact(List<Account> existingAccounts, String ID) {
        // Contacts.add(Data.getAccount(ID));
        if (this.UUID.equals(ID)) {
            System.out.println("\n\tThis is your own UUID.\n");
            return;
        }

        for (Account account : existingAccounts) {
            if (account.getUUID().equals(ID)) {
                contacts.add(account);

                System.out.println("\n\tUser " + account.getName() + " added to contacts.\n");
                return;
            }
        }

        System.out.println("\n\tThis user does not exist.\n");
    }

    public void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("\n\tYour contacts list is empty.");
        }
        for (Account account : contacts) {
            System.out.println("\n\tUsername: " + account.getName() + "\n\tUUID: " + account.getUUID());
        }

        System.out.println();
    }

    /*
     * Create new UUID.
     * We're not technically checking that this ID is unique, but there will probabilistically
     * be no ID colissions among currently living humans because there are 3 * 10^14combinations.
     * Feel free to add some checking functionality if you want, but it doesn't matter.
     * 
     * Generates a string of random numbers or letters, capital and non-capital.
     */
    private static String newUUID() {
        String building = "";
        Random rand = new Random();
        for (int i = 0; i < UUID_LENGTH; i++) {
            int randNum = rand.nextInt(62);
            if (randNum <= 9) {
                building += (char) (randNum + 48);
            } else if (randNum <= 35) {
                building += (char) (randNum + 55);
            } else {
                building += (char) (randNum + 61);
            }
        }

        return building;
    }

    /*
     * Login with existing account. Storing command line interface here
     * because I dunno how to properly structure command line interfaces.
     * 
     * We need some sort of data class which has a map from UUIDs to account objects.
     * I can code this up and use some JSON but eh.
     */
    public static Account login(List<Account> existingAccounts) {
        Scanner input = new Scanner(System.in);

        String username, password;

        // Get username and check if exists.
        while (true) {
            System.out.println("---------------------------------");
            System.out.println("Please provide your username.");
            System.out.print("[Username] ");

            username = input.nextLine();

            for (Account accounts : existingAccounts) {
                if (accounts.getName().equals(username)) {
                    /*
                     * Get password.
                     * Made maximum attempts logic for fun- doesn't actually provide any security.
                     * Passwords will presumably be stored in plaintext so all for fun.
                     */
                    int attempts = 3;
                    while (true) {
                        System.out.println("---------------------------------");
                        System.out.println("Please provide your account password.");
                        System.out.print("[Password] ");

                        password = input.nextLine();

                        // Check if password matches account with chosen username.
                        if (!password.equals(accounts.getPassword())) {
                            attempts--;
                            System.out.println("Invalid password. (" + attempts + "/3 attempts remain)");
                        } else {
                            System.out.println("Login successful.");
                            return accounts;
                        }

                        if (attempts == 0) {
                            System.out.println("No further attempts, returning to login menu.");
                            return null;
                        }
                    }
                }
            }

            System.out.println("Account not found.\n");

            return null;
        }
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

    public Chat(Account owner, String name) {
        this.owner = owner;
        accounts = new ArrayList<>();
        this.accounts.add(owner);

        this.name = name;
        messages = new ArrayList<>();
    }

    public Chat(Account owner) {
        this(owner, "Unnamed Chat");
    }

    // Add new message
    public void pushMessage(Message m) {
        messages.add(m);
    }

    // Somehow retrieve messages. WIP.
    public List<Message> getMessages() {
        return this.messages;
    }

    public Account getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public void addAccount(Account acc) {
        accounts.add(acc);
        acc.addChat(this);
    }
}

/*
 * Message.
 * One could theoretically add functionality for attachments more easily if you
 * used this data structure.
 */
class Message {
    private String text;
    private Account sender;

    public Message(String text, Account sender) {
        this.text = text;
        this.sender = sender;
    }

    public String getMessageString() {
        return text;
    }

    public Account getSender() {
        return this.sender;
    }
}