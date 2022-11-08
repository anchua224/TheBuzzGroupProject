package edu.lehigh.cse216.jub424.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {
    /**
     * Print the main menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help");
        System.out.println("  [s] Select a table you want to make change: ");
        System.out.println("    1. IDEAS");
        System.out.println("    2. LIKES");
        System.out.println("    3. DISLIKES");
        System.out.println("    4. COMMENTS");
        System.out.println("    5. USER");
        // Other tables could be added to the menu later when needed
    }

    /**
     * Print the inner menus for our program
     * 
     * @param tableName name of table
     */
    static void inner_menu(String tableName) {
        switch (tableName) {
            case "IDEAS":
                System.out.println("  [T] Create Table " + tableName);
                System.out.println("  [D] Drop Table " + tableName);
                System.out.println("  [1] Query for a specific idea");
                System.out.println("  [*] Query for all ideass");
                System.out.println("  [-] Delete an idea");
                System.out.println("  [+] Insert a new idea");
                System.out.println("  [~] Update an idea");
                System.out.println("  [0] Invalidate an idea");
                System.out.println("  [?] Help (Display menu)");
                System.out.println("  [<] Return to main menu");
            break;
            case "LIKES":
                System.out.println("  [T] Create Table " + tableName);
                System.out.println("  [D] Drop Table " + tableName);
                System.out.println("  [-] Remove like from idea");
                System.out.println("  [+] Like an idea");
                System.out.println("  [g] Get the count of likes related to a certain idea");
                System.out.println("  [0] delete all the likes related to a certain idea");
                System.out.println("  [?] Help (Display menu)");
                System.out.println("  [<] Return to main menu");
            break;
            case "DISLIKES":
                System.out.println("  [T] Create Table " + tableName);
                System.out.println("  [D] Drop Table " + tableName);
                System.out.println("  [-] Remove dislike from idea");
                System.out.println("  [+] Dislike an idea");
                System.out.println("  [g] Get the count of dislikes related to a certain idea");
                System.out.println("  [0] delete all the dislikes related to a certain idea");
                System.out.println("  [?] Help (Display menu)");
                System.out.println("  [<] Return to main menu");
            break;
            case "COMMENTS":
                System.out.println("  [T] Create Table " + tableName);
                System.out.println("  [D] Drop Table " + tableName);
                System.out.println("  [1] Query for a specific comment");
                System.out.println("  [*] Query for all comments");
                System.out.println("  [-] Delete a comment");
                System.out.println("  [+] Inset a new comment");
                System.out.println("  [~] Update a comment");
                System.out.println("  [?] Help (Display menu)");
                System.out.println("  [<] Return to main menu");
            break;
            case "USER":
                System.out.println("  [T] Create Table " + tableName);
                System.out.println("  [D] Drop Table " + tableName);
                System.out.println("  [1] Query for a specific user");
                System.out.println("  [*] Query for all users");
                System.out.println("  [-] Delete a user");
                System.out.println("  [+] Inset a new user");
                System.out.println("  [~] Update a user");
                System.out.println("  [?] Help (Display menu)");
                System.out.println("  [<] Return to main menu");
            break;
        }
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param actions A String contains all valid actions to a certain table
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in, String actions) {
        // The valid actions:
        // String actions = "TD1*-+~?<";

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
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided. May be "".
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
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } /*
           * catch (NumberFormatException e) {
           * e.printStackTrace();
           * }
           */
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options. Ignored by this program.
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

        // Number of Tables: a variable that stores the total number of tables in the
        // database
        int numOfTable = 5;

        // print the menu when the program start
        menu();

        while (true) {
            // check whether the input option for main menu is correct, if not, ask for
            // input again
            String option = getString(in, "Enter an option [q?s]");
            if (option.compareTo("q") == 0) {
                break;
            } else if (option.compareTo("?") == 0) {
                menu();
                continue;
            } else if (option.compareTo("s") != 0) {
                System.out.println("Invalid option, re-enter");
                continue;
            }
            // select a table need to be changed, check if input is valid
            int inputInt;
            while (true) {
                try {
                    inputInt = getInt(in, "Select a number");
                    if (inputInt <= numOfTable) {
                        break;
                    } else {
                        System.out.println("Invalid number, re-enter please");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Not a number, re-enter a number please:");
                    continue;
                }
            }

            // create/drop/update/delete/read/insert... from tables
            char action = ' ';
            do {
                switch (inputInt) {
                    // Table IDEAS
                    case 1:
                        action = prompt(in, "TD10*-+~?<");
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
                        } else if (action == '0') {
                            int id =getInt(in, "Enter the row ID");
                            if (id == -1)
                                continue;
                            int res = db.mIdeaTable.invalidateIdea(id);
                            if (res == -1)
                                continue;
                            System.out.println("  " + res + " rows invalidated");
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
                            int valid = getInt(in, "Is the idea valid (1-yes, 0-no)");
                            if (subject.equals("") || message.equals(""))
                                continue;
                            int res = db.mIdeaTable.insertIdea(subject, message, valid);
                            System.out.println(res + " rows added");
                        } else if (action == '~') {
                            int id = getInt(in, "Enter the row ID ");
                            if (id == -1)
                                continue;
                            String newMessage = getString(in, "Enter the new message");
                            String newTitle = getString(in, "Enter the new title");
                            int res = db.mIdeaTable.updateIdea(id, newTitle, newMessage);
                            if (res == -1)
                                continue;
                            System.out.println("  " + res + " rows updated");
                        }
                        break;
                    // Table LIKES
                    case 2:
                        action = prompt(in, "TD-+g0?<");
                        if (action == '?') {
                            inner_menu("LIKES");
                        } else if (action == 'T') {
                            db.mLikesTable.createTable();
                        } else if (action == 'D') {
                            db.mLikesTable.dropTable();
                        } else if (action == '-') {
                            int id = getInt(in, "Enter the row ID");
                            String email = getString(in, "Enter your email");
                            if (id == -1)
                                continue;
                            int res = db.mLikesTable.removeLike(id, email);
                            if (res == -1)
                                continue;
                            System.out.println("  " + res + " rows deleted");
                        } else if (action == '+') {
                            int id = getInt(in, "Enter the row ID");
                            String email = getString(in, "Enter your email");
                            if (id == -1)
                                continue;
                            int res = db.mLikesTable.likeIdea(id, email);
                            System.out.println(res + " rows added");
                        } else if (action == '0') {
                            int id = getInt(in, "Enter the row ID");
                            if (id == -1)
                                continue;
                            int res = db.mLikesTable.deleteAllLikes(id);
                            System.out.println(res + " rows deleted");
                        } else if (action == 'g') {
                            int id = getInt(in, "Enter the row ID");
                            if (id == -1)
                                continue;
                            int count = db.mLikesTable.getLikeCount(id);
                            System.out.println(count + " likes related to idea " + id);
                        }
                        break;
                    // Table Dislikes
                    case 3:
                        action = prompt(in, "TD-+g0?<"); 
                        if (action == '?') {
                            inner_menu("DISLIKES");
                        } else if (action == 'T') {
                            db.mDislikesTable.createTable();
                        } else if (action == 'D') {
                            db.mDislikesTable.dropTable();
                        } else if (action == '-') {
                            int id = getInt(in, "Enter the row ID");
                            String email = getString(in, "Enter your email");
                            if (id == -1)
                                continue;
                            int res = db.mDislikesTable.removeDislike(id, email);
                            if (res == -1)
                                continue;
                            System.out.println("  " + res + " rows deleted");
                        } else if (action == '+') {
                            int id = getInt(in, "Enter the row ID");
                            String email = getString(in, "Enter your email");
                            if (id == -1)
                                continue;
                            int res = db.mDislikesTable.DislikeIdea(id, email);
                            System.out.println(res + " rows added");
                        } else if (action == '0') {
                            int id = getInt(in, "Enter the row ID");
                            if (id == -1)
                                continue;
                            int res = db.mDislikesTable.deleteAllDislikes(id);
                            System.out.println(res + " rows deleted");
                        } else if (action == 'g') {
                            int id = getInt(in, "Enter the row ID");
                            if (id == -1)
                                continue;
                            int count = db.mDislikesTable.getDislikeCount(id);
                            System.out.println(count + " dislikes related to idea " + id);
                        }
                        break;
                    // Comments table
                    case 4:
                        action = prompt(in, "TD1*-+~?<");
                        if (action == '?') {
                            inner_menu("COMMENTS");
                        } else if (action == 'T') {
                            db.mIdeaTable.createTable();
                        } else if (action == 'D') {
                            db.mIdeaTable.dropTable();
                        } else if (action == '1') {
                            int id = getInt(in, "Enter the row ID");
                            if (id == -1)
                                continue;
                            CommentsTable.Comments res = db.mCommentsTable.selectOneComments(id);
                            if (res != null) {
                                System.out.println("  [" + res.com_id + "] " + res.content);
                            }
                        } else if (action == '*') {
                            ArrayList<CommentsTable.Comments> res = db.mCommentsTable.selectAllComments();
                            if (res == null)
                                continue;
                            System.out.println("  Current Database Contents");
                            System.out.println("  -------------------------");
                            for (CommentsTable.Comments rd : res) {
                                System.out.println("  [" + rd.com_id + "] " + rd.content);
                            }
                        } else if (action == '-') {
                            int id = getInt(in, "Enter the comment ID");
                            if (id == -1)
                                continue;
                            int res = db.mCommentsTable.deleteComments(id);
                            if (res == -1)
                                continue;
                            System.out.println("  " + res + " rows deleted");
                        } else if (action == '+') {
                            int id = getInt(in, "Enter the idea ID you want to comment");
                            String content = getString(in, "Enter the content");
                            String email = getString(in, "Enter your email");
                            if (content.equals(""))
                                continue;
                            int res = db.mCommentsTable.insertComments(id, content, email);
                            System.out.println(res + " rows added");
                        } else if (action == '~') {
                            int id = getInt(in, "Enter the comment ID ");
                            if (id == -1)
                                continue;
                            String newComment = getString(in, "Enter the new comment");
                            int res = db.mCommentsTable.updateComments(id, newComment);
                            if (res == -1)
                                continue;
                            System.out.println("  " + res + " rows updated");
                        }
                    break;
                    // User table
                    case 5:
                        action = prompt(in, "TD1*-+~?<");
                        if (action == '?') {
                            inner_menu("USER");
                        } else if (action == 'T') {
                            db.mUserTable.createTable();
                        } else if (action == 'D') {
                            db.mUserTable.dropTable();
                        } else if (action == '1') {
                            String id = getString(in, "Enter the email");
                            if (id.equals(""))
                                continue;
                            UserTable.User res = db.mUserTable.selectOneUser(id);
                            if (res != null) {
                                System.out.println("  [" + res.user_id + "] ");
                                System.out.println("  --> " + res.email);
                                System.out.println("  --> " + res.name);
                                System.out.println("  --> " + res.GI);
                                System.out.println("  --> " + res.SO);
                                System.out.println("  --> " + res.note);
                            }
                        } else if (action == '*') {
                            ArrayList<UserTable.User> res = db.mUserTable.selectAllUsers();
                            if (res == null)
                                continue;
                            System.out.println("  Current Database Contents");
                            System.out.println("  -------------------------");
                            for (UserTable.User rd : res) {
                                System.out.println("  [" + rd.user_id + "] ");
                                System.out.println("  --> " + rd.email);
                                System.out.println("  --> " + rd.name);
                                System.out.println("  --> " + rd.GI);
                                System.out.println("  --> " + rd.SO);
                                System.out.println("  --> " + rd.note);
                            }
                        } else if (action == '-') {
                            String id = getString(in, "Enter the email");
                            if (id.equals(""))
                                continue;
                            int res = db.mUserTable.deleteUser(id);
                            if (res == -1)
                                continue;
                            System.out.println("  " + res + " rows deleted");
                        } else if (action == '+') {
                            String email = getString(in, "Enter your email");
                            String name = getString(in, "Enter your name");
                            String GI = getString(in, "Enter your gender identity");
                            String SO = getString(in, "Enter your sexual orientation");
                            String note = getString(in, "Enter your note");
                            if (email.equals("") || name.equals("") || GI.equals("") || SO.equals("") || note.equals(""))
                                continue;
                            int res = db.mUserTable.insertUser(email, name, GI, SO, note);
                            System.out.println(res + " rows added");
                        } else if (action == '~') {
                            String id = getString(in, "Enter the email ");
                            if (id.equals(""))
                                continue;
                            String newName = getString(in, "Enter your new name");
                            String newGI = getString(in, "Enter your new gender identity");
                            String newSO = getString(in, "Enter your new sexual orientation");
                            String newNote = getString(in, "Enter your new note");
                            int res = db.mUserTable.updateUser(id, newName, newGI, newSO, newNote);
                            if (res == -1)
                                continue;
                            System.out.println("  " + res + " rows updated");
                        }
                    break;
                }
            } while (action != '<');
        }
        // Always remember to disconnect from the database when the program
        // exits
        db.disconnect();
    }
}