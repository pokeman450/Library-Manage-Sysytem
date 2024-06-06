package library;
import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/library";
    private static final String username = "root";
    private static final String password = "maplestory";
    public static Scanner scanner = new Scanner(System.in);
    public static User user;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        boolean running = true;

        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("1. Sign up 2. Sign in ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                System.out.println("Enter username: ");
                String username1 = scanner.nextLine();
                System.out.println("Enter password: ");
                String password1 = scanner.nextLine();
                System.out.println("Enter role: librarian / patron ");
                String role = scanner.nextLine();
                createUser(connection, username1, password1, role);
            }
            // Authentication
            System.out.println("Enter username: ");
            String usern = scanner.nextLine();
            System.out.println("Enter password: ");
            String passw = scanner.nextLine();
            user = authenticateUser(connection, usern, passw);

            if (user != null) {
                libraryMenu();
            } else {
                System.out.println("Authentication failed. Try Again.");
            }
        }

    }

    public static void createUser(Connection connection, String username1, String password1, String role) {
        String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            preparedStatement.executeUpdate();
            System.out.println("Success. Created user!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User authenticateUser(Connection connection, String usern, String passw) {
        // Verify if user exists with correct username and password
        String readQuery = "Select * FROM user WHERE username = (?) AND password = (?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(readQuery)) {
            preparedStatement.setString(1, usern);
            preparedStatement.setString(2, passw);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void libraryMenu(){
        int choice = 0;
        //the choices if the user is an admin
        if(user.getRole().equals("librarian")){
            do{
                System.out.println("librarian Menu:");
                System.out.println("1) View checked out books");
                System.out.println("2) View books");
                System.out.println("3) View overdue Books");
                System.out.println("4) Total fees");
                System.out.println("5) exit");
                choice = scanner.nextInt();
                scanner.nextLine();
//                switch(choice){
//                    case 1 ->
//                    case 2 ->
//                    case 3 ->
//                    case 4 ->
//                    case 5 ->
//
//                }
            }while(choice!= 5);

        }else {
            //choices if the user isnt an librarian
            do {
                System.out.println("User Menu:");
                System.out.println("1) View checked out books");
                System.out.println("2) View fees");
                System.out.println("3) Check out book");
                System.out.println("4) Exit");
                choice = scanner.nextInt();
                scanner.nextLine();
//                switch(choice){
//                    case 1 ->
//                    case 2 ->
//                }
            } while (choice != 4);
        }
    }

}