import java.util.*;

public class ClientMenuState extends WareState {
    // Clerk tasks
    static final int SHOW_CLIENT_DETAILS = 1;
    static final int DISPLAY_PRODUCTS = 2;
    static final int SHOW_CLIENT_TRANSACTIONS = 3;
    static final int ADD_ITEM_TO_CLIENTS_WISHLIST = 4;
    static final int DISPLAY_CLIENTS_WISHLIST = 5;
    static final int PLACE_AN_ORDER = 6;
    static final int LOGOUT = 7;
    private static Warehouse warehouse;
    private static ClientMenuState instance;
    private Scanner input = new Scanner(System.in);

    // Constructor
    private ClientMenuState(){
        super();
        warehouse = Warehouse.instance();
        // Instantiate WareContext here
    }

    public static ClientMenuState instance() {
        if (instance == null) {
            instance = new ClientMenuState();
        }
        return instance;
      }
    // Method for displaying menu
    private void display_menu(){
        System.out.println("Choose an operation to execute");
        System.out.println(SHOW_CLIENT_DETAILS + " - Show Clients Details");
        System.out.println(DISPLAY_PRODUCTS + " - Display Products");
        System.out.println(SHOW_CLIENT_TRANSACTIONS + " - Show Clients Transactions");
        System.out.println(ADD_ITEM_TO_CLIENTS_WISHLIST + " - Add Item To Clients Wishlist");
        System.out.println(DISPLAY_CLIENTS_WISHLIST + " - Display Clients Wishlist");
        System.out.println(PLACE_AN_ORDER + " - Place An Order");
        System.out.println(LOGOUT + " - Logout");
    }
    
    public void showClientDetails(){
        String clientID = WareContext.instance().getClient();
        System.out.println();
    }

    public void showProducts(){
        warehouse.DisplayProducts();
    }

    public void showClientTransactions(){
        try {
            String clientID = WareContext.instance().getClient();
            Client client = warehouse.searchClientByID(clientID);
            warehouse.DisplayInvoices(client);
          } catch (Exception e) {
            System.out.println("Error,, Try Again");
          }
    }

    public void addItemToWishlist(){
        String cid =  WareContext.instance().getClient();
    do {
      try {
        Client client;
        client = warehouse.searchClientByID(cid);
        if (client == null) {
          System.out.println("Client With id not found");
          return;
        }

        String pid = WareContext.instance().getToken("Enter product id you want to add");
        int quantity = WareContext.instance().getNumber("Enter quantity");

        Product product;
        product = warehouse.searchProductByID(pid);

        if (product == null) {
          System.out.println("product With id not found");
          return;
        }

        WishItem item;
        item = warehouse.addProductToWishlist(client, product, quantity);

        if (item == null) {
          System.out.println("There was an error add product to wishlist");
        }
        System.out.println(item);

        if (!WareContext.instance().yesOrNo("Add more Products to wishlist?")) {
          break;
        }
      } catch (Exception err) {
        System.out.println("There was an error add product to wishlist");
      }
    } while (true);
    }

    public void displayClientWishlist(){
        try {
            String clientID = WareContext.instance().getClient();
            Client client = warehouse.searchClientByID(clientID);
            if (clientID == null) {
              System.out.println("Client Not Found.");
            }
            warehouse.DisplayClientWishlist(client);
          } catch (Exception e) {
            System.out.println("Error, Retry...");
          }
    }

    public void placeAnOrder(){
        try {
            String clientID = WareContext.instance().getClient();
            Client client = warehouse.searchClientByID(clientID);
            if (clientID == null) {
              System.out.println("Client Not Found.");
            }
            Iterator<WishItem> iterator = client.getWishItems();
      
            if (!iterator.hasNext()) {
              System.out.println("Client has nothing in the wishlist...");
            }
            System.out.println("Starting Wishlist");
            warehouse.DisplayClientWishlist(client);
      
            while (iterator.hasNext()) {
              WishItem wishItem = iterator.next();
              System.out.println("-------------------------------------");
              System.out.println(wishItem);
              System.out.println("-------------------------------------");
              System.out.println("1 - Remove Item from the wishlist");
              System.out.println("2 - Change Item Quantity");
              System.out.println("3 - Next Item");
              String option = WareContext.instance().getToken("Enter Option: ");
      
              switch (option) {
                case "1":
                  client.RemoveWishItem(wishItem);
                  System.out.println("removing wish Item... ");
                  break;
                case "2":
                  int newQuantity = WareContext.instance().getNumber("Enter new quantity: ");
                  wishItem.setQuantity(newQuantity);
                  break;
                case "3":
                  System.out.println("Going to next Item...");
                  break;
      
                default:
                  System.out.println("Invalid Option, Restart");
                  placeAnOrder();
                  break;
              }
            }

            warehouse.DisplayClientWishlist(client);
            warehouse.CompleteOrder(client);
          } catch (Exception e) {
      
            System.out.println(e + "Error, Retry...");
          }
    }

    public void Logout(){
        if (WareContext.instance().getLogin() < 1) {
            (WareContext.instance()).changeState(0);
        } else {
            (WareContext.instance()).changeState(2);
        }
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
                case SHOW_CLIENT_DETAILS: showClientDetails();
                    break; // add new client
                case DISPLAY_PRODUCTS: showProducts(); 
                    break; // display menu
                case SHOW_CLIENT_TRANSACTIONS: showClientTransactions(); 
                    break; // search for client
                case ADD_ITEM_TO_CLIENTS_WISHLIST: addItemToWishlist(); 
                    break; // Adding a new product
                case DISPLAY_CLIENTS_WISHLIST: displayClientWishlist(); 
                    break; // search products
                case PLACE_AN_ORDER: placeAnOrder();
                    break; // receive shipment
                case LOGOUT: Logout();
                    break; // exit program; edit if you want a specific exit
                default: System.out.println("Input is unclear!"); 
                    break; // default behavior
            }
        }
    }
}