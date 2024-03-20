import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class simpleJDBC {
    public static void main(String[] args) throws SQLException {
        // Unique table names.  Either the user supplies a unique identifier as a command line argument, or the program makes one up.
        String tableName = "";
        int sqlCode = 0;      // Variable to hold SQLCODE
        String sqlState = "00000";  // Variable to hold SQLSTATE

        if (args.length > 0)
            tableName += args[0];
        else
            tableName += "exampletbl";


        // Register the driver.  You must register the driver before you can use it.
        try {
            DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
        } catch (Exception cnfe) {
            System.out.println("Class not found");
        }
        // This is the url you must use for DB2.
        //Note: This url may not valid now ! Check for the correct year and semester and server name.
        String url = "jdbc:db2://winter2024-comp421.cs.mcgill.ca:50000/comp421";

        //REMEMBER to remove your user id and password before submitting your code!!
        //String your_userid = null;
        //String your_password = null;
        String your_userid = "cs421g34";
        String your_password = "iLoveDatabases";
        //AS AN ALTERNATIVE, you can just set your password in the shell environment in the Unix (as shown below) and read it from there.
        //$  export SOCSPASSWD=yoursocspasswd
        if (your_userid == null && (your_userid = System.getenv("SOCSUSER")) == null) {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        if (your_password == null && (your_password = System.getenv("SOCSPASSWD")) == null) {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        Connection con = DriverManager.getConnection(url, your_userid, your_password);
        Statement statement = con.createStatement();

        //Create menu enum
        enum mainMenu {
            top,
            orderHistory,
            appointments,
            addItem,
            viewCart,
            cancel,
            quit,
        }
        mainMenu mainMenuState = mainMenu.top;
        boolean running = true;
        String inputString = "";
        Scanner userInput = new Scanner(System.in);
        int count = 0;
        //Main loop
        while (running) {
            switch (mainMenuState) {
                case top:
                    System.out.println("Select following an option:");
                    System.out.println("1. View customer order history");
                    System.out.println("2. View appointment by stylist");
                    System.out.println("3. Add item to the store");
                    System.out.println("4. Cancel an order");
                    System.out.println("5. View customer cart");
                    System.out.println("6. Quit");
                    System.out.println("Input corresponding numerical value of choice");
                    inputString = userInput.nextLine();
                    switch (inputString) {
                        case "1":
                            mainMenuState = mainMenu.orderHistory;
                            break;
                        case "2":
                            mainMenuState = mainMenu.appointments;
                            break;
                        case "3":
                            mainMenuState = mainMenu.addItem;
                            break;
                        case "4":
                            mainMenuState = mainMenu.viewCart;
                            break;
                        case "5":
                            mainMenuState = mainMenu.cancel;
                            break;
                        case "6":
                            mainMenuState = mainMenu.quit;
                            break;
                        default:
                            System.out.println("Invalid choice");
                            break;
                    }
                    break;
                // break;
                case orderHistory:
                    String fname = "";
                    String lname = "";
                    String email = "";
                    System.out.println("Enter the customer's first name");
                    fname = userInput.nextLine();
                    System.out.println("Enter the customer's last name");
                    lname = userInput.nextLine();
                    System.out.println("Enter the customer's email");
                    email = userInput.nextLine();
                    try {
                        String querySQL = "SELECT i.name, co.itemId, ca.orderId FROM Contain co, Cart ca, Item i WHERE co.cartId = ca.cartId AND ca.firstName = \'" + fname + "\' AND ca.lastName = \'" + lname + "\' AND ca.emailAddress = \'" + email + "\' AND co.itemId = i.itemId";
                        System.out.println(querySQL);
                        java.sql.ResultSet rs = statement.executeQuery(querySQL);
                        while (rs.next()) {
                            String name = rs.getString(1);
                            String iid = rs.getString(2);
                            String oid = rs.getString(3);
                            System.out.println("Name:  " + name);
                            System.out.println("ItemID:  " + iid);
                            System.out.println("name:  " + oid);
                        }
                        System.out.println("DONE");
                        mainMenuState = mainMenu.top;

                    } catch (SQLException e) {
                        sqlCode = e.getErrorCode(); // Get SQLCODE
                        sqlState = e.getSQLState(); // Get SQLSTATE
                        // Your code to handle errors comes here;
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                        System.out.println("Returning to main menu");
                        mainMenuState = mainMenu.top;
                    }
                    break;
                case appointments:
                    String stylistName = "";
                    System.out.println("Enter the name of the stylist");
                    stylistName = userInput.nextLine();
                    try {
                        String querySQL = "SELECT a.stylist, a.appointmentDay, a.appointmentTimeOfDay, a.Location, a.firstName, a.lastName, a.emailAddress, i.name, a.itemId FROM Appointment a, Item i WHERE i.itemId = a.itemId AND a.stylist = \'" + stylistName + "\'";
                        System.out.println(querySQL);
                        java.sql.ResultSet rs = statement.executeQuery(querySQL);
                        while (rs.next()) {
                            String stylist = rs.getString(1);
                            String appointmentDay = rs.getString(2);
                            String appointmentTimeOfDay = rs.getString(3);
                            String Location = rs.getString(4);
                            String firstName = rs.getString(5);
                            String lastName = rs.getString(6);
                            String emailAddress = rs.getString(7);
                            String itemname = rs.getString(8);
                            String itemId = rs.getString(9);
                            System.out.println("Stylist Name:  " + stylist);
                            System.out.println("Day:  " + appointmentDay);
                            System.out.println("Time:  " + appointmentTimeOfDay);
                            System.out.println("Location:  " + Location);
                            System.out.println("Customer Name:  " + firstName + " " + lastName);
                            System.out.println("Customer Email:  " + emailAddress);
                            System.out.println("Item To Try:  " + itemname);
                            System.out.println("Item ID:  " + itemId);
                        }
                        System.out.println("DONE");
                        mainMenuState = mainMenu.top;

                    } catch (SQLException e) {
                        sqlCode = e.getErrorCode(); // Get SQLCODE
                        sqlState = e.getSQLState(); // Get SQLSTATE
                        // Your code to handle errors comes here;
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                        System.out.println("Returning to main menu");
                        mainMenuState = mainMenu.top;
                    }
                    break;
                case addItem:
                    String itemId = "";
                    System.out.println("Enter a unique item id");
                    itemId = userInput.nextLine();
                    count = 0;
                    try {
                        String querySQL = "SELECT count(itemid) FROM item where itemid = " + "\'" + itemId + "\'";
                        System.out.println(querySQL);
                        java.sql.ResultSet rs = statement.executeQuery(querySQL);
                        while (rs.next()) {
                            count = rs.getInt(1);

                        }
                    } catch (SQLException e) {
                        sqlCode = e.getErrorCode(); // Get SQLCODE
                        sqlState = e.getSQLState(); // Get SQLSTATE
                        // Your code to handle errors comes here;
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                        System.out.println("Returning to main menu");
                        mainMenuState = mainMenu.top;
                    }
                    if (count > 0) {
                        System.out.println("This item already exists in the database.");
                        continue;
                    }
                    System.out.println("The id entered is unique");
                    String name = "";
                    boolean finalSale = false;
                    String brand = "";
                    String color = "";
                    double price = 0;
                    String material = "";
                    String size = "";
                    String information = "";
                    String country = "";
                    String description = "";
                    String supplier = "";
                    String cemail = "";
                    System.out.println("Enter the name of the item");
                    name = userInput.nextLine();
                    System.out.println("Enter whether the item is up for FINAL SALE");
                    try {
                        finalSale = userInput.nextBoolean();
                    } catch (Exception e) {
                        System.out.println("Invalid value.");
                        System.out.println("Returning to main menu");
                        mainMenuState = mainMenu.top;
                        userInput.nextLine();
                        continue;
                    }
                    userInput.nextLine();
                    System.out.println("Enter the item's brand");
                    brand = userInput.nextLine();
                    System.out.println("Enter the item's price");
                    try {
                        price = userInput.nextDouble();
                    } catch (Exception e) {
                        System.out.println("Invalid value.");
                        System.out.println("Returning to main menu");
                        mainMenuState = mainMenu.top;
                        userInput.nextLine();
                        continue;
                    }
                    userInput.nextLine();
                    System.out.println("Enter the item's material");
                    material = userInput.nextLine();
                    System.out.println("Enter the item's size");
                    size = userInput.nextLine();
                    System.out.println("Enter the item's information");
                    information = userInput.nextLine();
                    System.out.println("Enter the item's manufacturing country");
                    country = userInput.nextLine();
                    System.out.println("Enter the supplier's email address");
                    cemail = userInput.nextLine();
                    System.out.println("Enter the item's color");
                    color = userInput.nextLine();
                    System.out.println("Enter a description of the model");
                    description = userInput.nextLine();
                    System.out.println("Enter the name of the supplier");
                    supplier = userInput.nextLine();
                    // Inserting Data into the table
                    try {
                        String insertSQL = "INSERT INTO " + "Item" + " VALUES ( \'" + name + "\', \'" + finalSale + "\', \'" + brand + "\',\'" + color + "\', \'" + price + "\', \'" + material + "\', \'" + size + "\', \'" + information + "\', \'" + country + "\', \'" + itemId + "\', \'" + description + "\', \'" + supplier + "\', \'" + cemail + "\' ) ";
                        System.out.println(insertSQL);
                        statement.executeUpdate(insertSQL);
                        System.out.println("The item has been successfully inserted into the database!");
                        mainMenuState = mainMenu.top;
                    } catch (SQLException e) {
                        sqlCode = e.getErrorCode(); // Get SQLCODE
                        sqlState = e.getSQLState(); // Get SQLSTATE
                        // Your code to handle errors comes here;
                        // something more meaningful than a print would be good
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                        System.out.println("Returning to main menu");
                        mainMenuState = mainMenu.top;
                    }
                    break;
                case viewCart:

                    int cartId = -1;
                    while (cartId == -1) {
                        System.out.println("Enter the customer's first name:");
                        String firstName = userInput.nextLine();
                        System.out.println("Enter the customer's last name:");
                        String lastName = userInput.nextLine();
                        System.out.println("Enter the customer's email address:");
                        String emailAddress = userInput.nextLine();
                
                        String getCartId= "SELECT cartId FROM Cart WHERE firstName = \'" + firstName + "\' AND lastName = \'" + lastName + "\' AND emailAddress = \'" + emailAddress + "\'";
                        try {
                            java.sql.ResultSet vcartId = statement.executeQuery(getCartId);
                            if (vcartId.next()) {
                                cartId = vcartId.getInt(1);
                            } else {
                                System.out.println("Invalid credentials. Please try again.");
                            }
                        } catch (SQLException e) {
                            System.out.println(e);
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    }
                    count = 0;
                    boolean viewingCart = true;
                    while(viewingCart){
                        String listCartItems = "SELECT co.itemId, i.name FROM Contain co, Item i WHERE co.cartId = \'" + cartId + "\' AND i.itemId = co.itemId";
                        String countCartItems = "SELECT COUNT(co.itemId) FROM Contain co, Item i WHERE co.cartId = \'" + cartId + "\' AND i.itemId = co.itemId";
                        try{
                            java.sql.ResultSet cartItemsCountRS = statement.executeQuery(countCartItems);
                            while (cartItemsCountRS.next()) {
                                count = cartItemsCountRS.getInt(1);
                            }
                            if (count < 1) {

                                System.out.println("The cart is empty. Returning to main menu.");
                                mainMenuState = mainMenu.top;
                                break;
                            }
//                            while (cartItemsCountRS.next()) {
//                                count = cartItemsCountRS.getInt(1);
//                            }
                            java.sql.ResultSet cartItemsRS = statement.executeQuery(listCartItems);
                            int counter = 1;
                            int[] mapA = new int[count+1];
                            while(cartItemsRS.next()){
                                int vitemId = cartItemsRS.getInt(1);
                                String vitemName = cartItemsRS.getString(2);
                                System.out.println(counter + ". Item ID: " + vitemId + " Item Name: " + vitemName);
                                mapA[counter] = vitemId;
                                counter++;
                            }

                            System.out.println("Select the item to remove from the cart by menu value, or '0' to return to main menu:");
                            int itemToRemove = userInput.nextInt();
                            userInput.nextLine();
                            if (itemToRemove != 0) {
                                String removeItemSQL = "DELETE FROM Contain WHERE cartId = \'" + cartId + "\' AND itemId = \'" + mapA[itemToRemove] + "\'";
                                statement.executeUpdate(removeItemSQL);
                                System.out.println("Item removed from cart.");
                            }
                            else{
                                viewingCart = false;
                                mainMenuState = mainMenu.top;
                            }
                        }
                        catch (SQLException e) {
                            System.out.println(e);
                            return;
                        }
                    }
                    break;
                case cancel:
                    break;
                case quit:
                    running = false;
                    break;
            }
            // break;
        }

// //
//        // Creating a table
//        try {
//            String createSQL = "CREATE TABLE " + tableName + " (id INTEGER, name VARCHAR (25)) ";
//            System.out.println(createSQL);
//            statement.executeUpdate(createSQL);
//            System.out.println("DONE");
//        } catch (SQLException e) {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }

//        // Inserting Data into the table
//        try {
//            String insertSQL = "INSERT INTO " + tableName + " VALUES ( 1 , \'Vicki\' ) ";
//            System.out.println(insertSQL);
//            statement.executeUpdate(insertSQL);
//            System.out.println("DONE");
//
//            insertSQL = "INSERT INTO " + tableName + " VALUES ( 2 , \'Vera\' ) ";
//            System.out.println(insertSQL);
//            statement.executeUpdate(insertSQL);
//            System.out.println("DONE");
//            insertSQL = "INSERT INTO " + tableName + " VALUES ( 3 , \'Franca\' ) ";
//            System.out.println(insertSQL);
//            statement.executeUpdate(insertSQL);
//            System.out.println("DONE");
//
//        } catch (SQLException e) {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
//        // Querying a table
//        try {
//            String querySQL = "SELECT id, name from " + tableName + " WHERE NAME = \'Vicki\'";
//            System.out.println(querySQL);
//            java.sql.ResultSet rs = statement.executeQuery(querySQL);
//
//            while (rs.next()) {
//                int id = rs.getInt(1);
//                String name = rs.getString(2);
//                System.out.println("id:  " + id);
//                System.out.println("name:  " + name);
//            }
//            System.out.println("DONE");
//        } catch (SQLException e) {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
//        //Updating a table
//        try {
//            String updateSQL = "UPDATE " + tableName + " SET NAME = \'Mimi\' WHERE id = 3";
//            System.out.println(updateSQL);
//            statement.executeUpdate(updateSQL);
//            System.out.println("DONE");
//
//            // Dropping a table
//            String dropSQL = "DROP TABLE " + tableName;
//            System.out.println(dropSQL);
//            statement.executeUpdate(dropSQL);
//            System.out.println("DONE");
//        } catch (SQLException e) {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
        // Finally but importantly close the statement and connection
        System.out.println("Disconecting from database");
        statement.close();
        con.close();
    }
}