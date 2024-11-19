
import java.util.*;

public class ClerkMenuState extends WareState {
    // Clerk tasks
    static final int ADD_CLIENT = 1;
    static final int SHOW_PRODUCTS = 2;
    static final int SHOW_CLIENTS = 3;
    static final int SHOW_CLIENTS_WITH_BALANCE = 4;
    static final int RECORD_PAYMENT = 5;
    static final int BECOME_CLIENT = 6;
    static final int LOGOUT = 7;
    private static Warehouse warehouse;
    private static ClerkMenuState instance;

    // Constructor
    private ClerkMenuState(){
        super();
        warehouse = Warehouse.instance();
        // Instantiate WareContext here
    }

    public static ClerkMenuState instance() {
        if (instance == null) {
            instance = new ClerkMenuState();
        }
        return instance;
      }

    private void addClient(){
        do {
        String name = WareContext.instance().getToken("Enter Client name");
        Client result;
        result = warehouse.addClient(name);
        Boolean test = warehouse.ClientExists(result);

        if ((result == null) || (test == false)) {
        System.out.println("Could not add Client");
        }
        System.out.println(result);
        if (!WareContext.instance().yesOrNo("Add more Clients?")) {
            break;
          }
        } while (true);
    }

    public void showProducts(){
        warehouse.DisplayProducts();
    }

    private void showClients(){
        warehouse.DisplayClients();
    }

    private void showClientsWithBalance(){
        warehouse.DisplayClients();
    }

    public void recievePayment() {
        try {
            do {
              String productID = WareContext.instance().getToken("Enter product id you are recieving");
              Product product = warehouse.searchProductByID(productID);
              if (product == null) {
                System.out.println("Could not find product, might need to add new product.");
                return;
              }
              int Quantity = WareContext.instance().getNumber("Enter Product Quantity: ");
              if (Quantity > 0) {
                warehouse.ReceiveShipment(product, Quantity);
              } else {
                System.out.println("Quantity must be greater than 0");
              }
      
              if (!WareContext.instance().yesOrNo("Recive more Products?")) {
                break;
              }
            } while (true);
      
          } catch (Exception e) {
            System.out.println(e + "Error");
          }
      }

    private void becomeClient(){
        try {
            String ClientID = WareContext.instance().getToken("Enter ClientID: ");
            Client client = warehouse.searchClientByID(ClientID);
            if (client != null){
                WareContext.instance().setClientID(ClientID);
                (WareContext.instance()).changeState(2);
            }
            else {
                System.out.println("ClientID Not Found");
            }
          } catch (Exception e) {
            System.out.println("Error,, Try Again");
          }
    }

    private void Logout(){
        // add if statement
        
        if (WareContext.instance().getLogin() == 2) {
            (WareContext.instance()).changeState(3);
        } else {
            (WareContext.instance()).changeState(0);
        }
    }
    
    // Method for displaying menu
    private void display_menu(){
        System.out.println("Choose an operation to execute");
        System.out.println(ADD_CLIENT + " - Add Client");
        System.out.println(SHOW_PRODUCTS + " - Show Products");
        System.out.println(SHOW_CLIENTS + " - Show Clients");
        System.out.println(SHOW_CLIENTS_WITH_BALANCE + " - Show Clients with balance");
        System.out.println(RECORD_PAYMENT + " - Recieve payment");
        System.out.println(BECOME_CLIENT + " - Become a Client");
        System.out.println(LOGOUT + " - Logout");
    }

    // Method for running basic clerk tasks
    public void run(){
        int command;
        boolean running = true;
        while (running) {
            display_menu();
            if ((command = WareContext.instance().getCommand()) == LOGOUT) {
                running = false;
            }
            switch(command){
                case ADD_CLIENT: addClient(); 
                    break; // add new client
                case SHOW_PRODUCTS: showProducts(); 
                    break; // display menu
                case SHOW_CLIENTS: showClients(); 
                    break; // search for client
                case SHOW_CLIENTS_WITH_BALANCE: showClientsWithBalance(); 
                    break; // Adding a new product
                case RECORD_PAYMENT: recievePayment(); 
                    break; // search products
                case BECOME_CLIENT: becomeClient();
                    break; // receive shipment
                case LOGOUT: Logout(); 
                    break; // exit program; edit if you want a specific exit
                default: System.out.println("Input is unclear!"); break; // default behavior
            }
        }
    }
}