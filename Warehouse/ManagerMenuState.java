import java.util.*;

public class ManagerMenuState extends WareState {
    // Clerk tasks
    static final int ADD_PRODUCT = 1;
    static final int DISPLAY_WAITLIST = 2;
    static final int RECIVE_SHIPMENT = 3;
    static final int BECOME_A_CLERK = 4;
    static final int LOGOUT = 5;
    private static Warehouse warehouse;
    private static ManagerMenuState instance;
    private Scanner input = new Scanner(System.in);

    // Constructor
    private ManagerMenuState(){
        super();
        warehouse = Warehouse.instance();
        // Instantiate WareContext here
    }

    public static ManagerMenuState instance() {
        if (instance == null) {
            instance = new ManagerMenuState();
        }
        return instance;
      }

    public void addProduct(){
        Product result;
    do {
      String name = WareContext.instance().getToken("Enter  name");
      String manufacturer = WareContext.instance().getToken("Enter manufacturer");
      int quantity = WareContext.instance().getNumber("Enter quantity");
      Float price = WareContext.instance().getFloat("Enter price");
      result = warehouse.addProduct(name, quantity, manufacturer, price);
      Boolean test = warehouse.ProductExists(result);
      System.out.println(test);
      if ((result != null) || (test == false)) {
        System.out.println(result);
      } else {
        System.out.println("Product could not be added");
      }
      if (!WareContext.instance().yesOrNo("Add more Products?")) {
        break;
      }
    } while (true);
    }

    public void displayWaitlist(){
        try {
            String productID = WareContext.instance().getToken("Enter product ID: ");
            Product product = warehouse.searchProductByID(productID);
            warehouse.DisplayProductWaitlist(product);
          } catch (Exception e) {
            System.out.println("Error,, Try Again");
          }
    }

    public void recieveShipment(){
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

    public void becomeClerk(){
        try {
            String clerkPass = WareContext.instance().getToken("Enter Clerk Password: ");
            if (clerkPass.contentEquals("clerk")){
                (WareContext.instance()).changeState(2);
            }
            else {
                System.out.println("Incorect Password");
            }
          } catch (Exception e) {
            System.out.println("Error,, Try Again");
          }
    }

    public void Logout(){
        (WareContext.instance()).changeState(0);
    }

    private void display_menu(){
        System.out.println("Choose an operation to execute");
        System.out.println(ADD_PRODUCT + " - Add product");
        System.out.println(DISPLAY_WAITLIST + " - Display waitlist");
        System.out.println(RECIVE_SHIPMENT + " - Recieve shipment");
        System.out.println(BECOME_A_CLERK + " - Become a clerk");
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
                case ADD_PRODUCT: addProduct();
                    break; // add new client
                case DISPLAY_WAITLIST: displayWaitlist(); 
                    break; // display menu
                case RECIVE_SHIPMENT: recieveShipment(); 
                    break; // search for client
                case BECOME_A_CLERK: becomeClerk(); 
                    break; // Adding a new product
                case LOGOUT: Logout(); 
                    break; // exit program; edit if you want a specific exit
                default: System.out.println("Input is unclear!"); 
                    break; // default behavior
            }
        }
    }
}