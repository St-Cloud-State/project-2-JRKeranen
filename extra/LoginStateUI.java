import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class LoginstateUI extends LibState implements ActionListener{
  private static final int CLERK_LOGIN = 0;
  private static final int USER_LOGIN = 1;
  private static final int EXIT = 2;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
  private LibContext context;
  private JFrame frame;
  private static Loginstate instance;
  private AbstractButton userButton, logoutButton, clerkButton;
  //private ClerkButton clerkButton;
  private LoginstateUI() {
      super();
      /*userButton = new JButton("user");
      clerkButton =  new JButton("clerk");
      logoutButton = new JButton("logout");
      userButton.addActionListener(this);
      logoutButton.addActionListener(this);
      clerkButton.addActionListener(this); */
 //     ((ClerkButton)clerkButton).setListener();
  }

  public static Loginstate instance() {
    if (instance == null) {
      instance = new LoginstateUI();
    }
    return instance;
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.userButton)) 
       {//System.out.println("user \n"); 
         this.user();}
    else if (event.getSource().equals(this.logoutButton)) 
       (LibContext.instance()).changeState(2);
    else if (event.getSource().equals(this.clerkButton)) 
       this.clerk();
  } 

 

  public void clear() { //clean up stuff
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());   
  }  

  private void clerk() {
     //System.out.println("In clerk \n");
    (LibContext.instance()).setLogin(LibContext.IsClerk);
     clear();
    (LibContext.instance()).changeState(0);
  } 
  
  private void user(){
    String userID = JOptionPane.showInputDialog(
                     frame,"Please input the user id: ");
    if (Library.instance().searchMembership(userID) != null){
      (LibContext.instance()).setLogin(LibContext.IsUser);
      (LibContext.instance()).setUser(userID);  
       clear();
      (LibContext.instance()).changeState(1);
    }
    else 
      JOptionPane.showMessageDialog(frame,"Invalid user id.");
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
        boolean running = true;

        while(running){
            // tasks
            System.out.println();
            display_menu();
            int command = input.nextInt();
            System.out.println();
            // case switch tasks
            switch(command){
                case ADD_PRODUCT: addProduct();
                    break; // add new client
                case DISPLAY_WAITLIST: displayWaitlist(); 
                    break; // display menu
                case RECIVE_SHIPMENT: recieveShipment(); 
                    break; // search for client
                case BECOME_A_CLERK: becomeClerk(); 
                    break; // Adding a new product
                case LOGOUT: logout(); 
                    break; // exit program; edit if you want a specific exit
                default: System.out.println("Input is unclear!"); 
                    break; // default behavior
            }
        }
    }
}
