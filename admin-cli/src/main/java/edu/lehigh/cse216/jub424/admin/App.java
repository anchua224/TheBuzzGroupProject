package edu.lehigh.cse216.jub424.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * App is our basic admin app.  For now, it is a demonstration of the six key 
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help");
        System.out.println("  [s] Select a table you want to make change: ");
        System.out.println("    1. IDEAS");
        //There is only one tables called IDEAS for now, other tables could be added to the menu later when needed
    }
    static void inner_menu(String tableName){
        System.out.println("  [T] Create "+tableName);
        System.out.println("  [D] Drop "+tableName);
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [*] Query for all rows");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        //System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
        System.out.println("  [<] Return to main memu");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~q?<";

        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (NumberFormatException e) {
            e.printStackTrace();
        }*/
        return i;
    }
    

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    public static void main(String[] argv) {
        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
       
        // Number of Tables: a variable that stores the total number of tables in the database
        int numOfTable=1;

        menu();
        while(true){
            String option=getString(in, "Enter an option [q?s]");
            if(option.compareTo("q")==0){
                break;
            }else if(option.compareTo("?")==0){
                menu();
            }else if(option.compareTo("s")!=0){
                System.out.println("Invalid option, re-enter");
                continue;
            }
            int inputInt;
            while(true){
               try{
                   inputInt=getInt(in, "Select a number");
                   if(inputInt<=numOfTable){
                       break;
                   }else{
                       System.out.println("Invalid number, re-enter please");
                       continue;
                   }
                }catch(NumberFormatException e){
                   System.out.println("Not a number, re-enter a number please:");
                   continue;
                }
            }
            
            char action=' ';
            do{
              switch(inputInt){
                  case 1:
                      //inner_menu("IDEAS");
                      action = prompt(in);
                      if (action == '?') {
                          inner_menu("IDEAS");
                      } else if (action == 'T') {
                          db.mIdeaTable.createTable();
                      } else if (action == 'D') {
                          db.mIdeaTable.dropTable();
                      } else if (action == '1') {
                          int id = getInt(in, "Enter the row ID");
                          if (id == -1)
                              continue;
                          IdeaTable.Idea res = db.mIdeaTable.selectOneIdea(id);
                          if (res != null) {
                              System.out.println("  [" + res.id + "] " + res.title);
                              System.out.println("  --> " + res.massage);
                          }
                      } else if (action == '*') {
                          ArrayList<IdeaTable.Idea> res = db.mIdeaTable.selectAllIdeas();
                          if (res == null)
                              continue;
                          System.out.println("  Current Database Contents");
                          System.out.println("  -------------------------");
                          for (IdeaTable.Idea rd : res) {
                              System.out.println("  [" + rd.id + "] " + rd.title);
                          }
                      } else if (action == '-') {
                          int id = getInt(in, "Enter the row ID");
                          if (id == -1)
                              continue;
                          int res = db.mIdeaTable.deleteIdea(id);
                          if (res == -1)
                              continue;
                          System.out.println("  " + res + " rows deleted");
                      } else if (action == '+') {
                          String subject = getString(in, "Enter the subject");
                          String message = getString(in, "Enter the message");
                          if (subject.equals("") || message.equals(""))
                              continue;
                          int res = db.mIdeaTable.insertIdea(subject, message);
                          System.out.println(res + " rows added");
                      } else if (action == '~') {
                          int id = getInt(in, "Enter the row ID :> ");
                          if (id == -1)
                              continue;
                          String newMessage = getString(in, "Enter the new message");
                          String newTitle = getString(in, "Enter the new title");
                          int res = db.mIdeaTable.updateIdea(id, newTitle, newMessage);
                          if (res == -1)
                              continue;
                          System.out.println("  " + res + " rows updated");
                      } else if(action == '<'){
                           
                      }
                      break;
                      //case 2; case 3;.......
              }
            }while(action != '<');
        }


      
        // Always remember to disconnect from the database when the program 
        // exits
        db.disconnect();
    }
}