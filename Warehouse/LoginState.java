import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class LoginState extends WareState {
  private static final int CLIENT_LOGIN = 0;
  private static final int CLERK_LOGIN = 1;
  private static final int MANAGER_LOGIN = 2;
  private static final int EXIT = 3;
  private static LoginState instance;
  private static Warehouse warehouse;
  private LoginState() {
      super();
      warehouse = Warehouse.instance();
  }

  public static LoginState instance() {
    if (instance == null) {
      instance = new LoginState();
    }
    return instance;
  }

  private void client() {
     try {
        String ClientID = WareContext.instance().getToken("Enter ClientID: ");
        Client client = warehouse.searchClientByID(ClientID);
        if (client != null){
            WareContext.instance().setClientID(ClientID);
            WareContext.instance().setLogin(WareContext.IsClient);
            (WareContext.instance()).changeState(0);
        }
        else {
            System.out.println("ClientID Not Found");
        }
      } catch (Exception e) {
        System.out.println("Error,, Try Again");
      }
  } 

  private void clerk() {
   try {
    String clerkPass = WareContext.instance().getToken("Enter Clerk Password: ");
    if (clerkPass.contentEquals("clerk")){
        (WareContext.instance()).setLogin(WareContext.IsClerk);
        (WareContext.instance()).changeState(1);
    }
    else {
        System.out.println("Incorect Password");
    }
  } catch (Exception e) {
    System.out.println("Error,, Try Again");
  }
 } 
  
  private void manager(){
    try {
        String manPass = WareContext.instance().getToken("Enter Manager Password: ");
        if (manPass.contentEquals("manager")){
            (WareContext.instance()).setLogin(WareContext.IsManager);
            (WareContext.instance()).changeState(2);
        }
        else {
            System.out.println("Incorect Password");
        }
      } catch (Exception e) {
        System.out.println("Error,, Try Again");
      }
  } 

  private void exit(){
    (WareContext.instance()).changeState(3);
  } 

  private void display_menu(){
    System.out.println("Choose an option");
    System.out.println(CLIENT_LOGIN + " - Login ss Client");
    System.out.println(CLERK_LOGIN + " - Login as Sales Clerk");
    System.out.println(MANAGER_LOGIN + " - Login as Manager");
    System.out.println(EXIT + " - end");
}
// Method for running basic clerk tasks
public void run(){
    int command;
    boolean running = true;
        while (running) {
            display_menu();
            if ((command = WareContext.instance().getCommand()) == EXIT) {
                running = false;
            }
        // case switch tasks
        switch(command){
            case CLIENT_LOGIN: client();
                break; // add new client
            case CLERK_LOGIN: clerk(); 
                break; // display menu
            case MANAGER_LOGIN: manager(); 
                break; // search for client
            case EXIT: exit(); 
                break; // exit program; edit if you want a specific exit
            default: System.out.println("Input is unclear!"); 
                break; // default behavior
        }
    }
}
}
