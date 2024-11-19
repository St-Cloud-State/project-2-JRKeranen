import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class WareContext {
  
  private static int currentState;
  private static Warehouse warehouse;
  private static WareContext context;
  private int currentUser;
  private String ClientID;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  public static final int IsClient = 0;
  public static final int IsClerk = 1;
  public static final int IsManager = 2;
  private static WareState[] states;
  private int[][] nextState;

  public boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("\nEnter command: "));
        if (value >= 0 && value <= 15) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void setLogin(int code)
  {currentUser = code;}

  public void setClientID(String ID)
  { ClientID = ID;}

  public int getLogin()
  { return currentUser;}

  public String getClient()
  { return ClientID;}

  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }

  public Float getFloat(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Float num = Float.valueOf(item);
        return num.floatValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }

  private WareContext() { //constructor
    System.out.println("In Warecontext constructor");
    
    warehouse = Warehouse.instance();
  
    // set up the FSM and transition table;
    states = new WareState[4];
    states[0] = ClientMenuState.instance();
    states[1] = ClerkMenuState.instance(); 
    states[2]=  ManagerMenuState.instance();
    states[3]=  LoginState.instance();
    nextState = new int[4][4];
    nextState[0][0] = 3; nextState[0][1] = 0; nextState[0][2] = 1; nextState[0][3] = -2;
    nextState[1][0] = 3; nextState[1][1] = 1; nextState[1][2] = 0;  nextState[1][3] = 2;
    nextState[2][0] = 3; nextState[2][1] = 2; nextState[2][2] = 1;  nextState[2][3] = 0;
    nextState[3][0] = 0; nextState[3][1] = 1; nextState[3][2] = 2;  nextState[3][3] = -1;
    currentState = 3;
    }

  public void changeState(int transition)
  {
    //System.out.println("current state " + currentState + " \n \n ");
    currentState = nextState[currentState][transition];
    if (currentState == -2) 
      {System.out.println("Error has occurred"); terminate();}
    if (currentState == -1) 
      { //System.out.println("current state " + currentState + " \n \n ");
      terminate();}
    states[currentState].run();
  }

  private void terminate()
  {
   System.out.println(" Goodbye \n ");
   System.exit(0);
  }

  public static WareContext instance() {
    if (context == null) {
       System.out.println("calling constructor");
      context = new WareContext();
    }

    return context;
  }

  public void process(){
    states[currentState].run();
  }
  
  public static void main (String[] args){
    WareContext.instance().process(); 
  }


}
