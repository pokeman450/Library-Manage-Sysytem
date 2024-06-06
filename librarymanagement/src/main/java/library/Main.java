package library;
import java.sql.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/library";
    private static final String username = "root";
    private static final String password = "maplestory";
    public static Scanner scanner = new Scanner(System.in);
    public static User user;
    public static Connect connect;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        connect = new Connect(url,username,password);
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
                libraryMenu(connection);
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
    public static void libraryMenu(Connection connection) throws SQLException, ClassNotFoundException {
        int choice = 0;
        //the choices if the user is an admin
            if(user.getRole().equals("librarian")){
            do{
                System.out.println("Librarian Menu:");
                System.out.println("1) View checked out books");
                System.out.println("2) View books");
                System.out.println("3) View overdue Books");
                System.out.println("4) Total fees");
                System.out.println("5) Add Books");
                System.out.println("6) Remove Books");
                System.out.println("7) exit");
                choice = scanner.nextInt();
                scanner.nextLine();
                switch(choice){
//                    case 1 ->
                    case 2 -> Transactions.viewBooks();
//                    case 3 ->
                    case 4 -> Transactions.totalFees();
                    case 5 -> Transactions.insert();
                    case 6 -> Transactions.removeBook();



                }
            }while(choice!= 7);

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
                switch(choice){
                    case 1 ->viewCheckedOutBooks(connection, user.getId());
                    case 2 -> viewFees(connection, user.getId());
                    case 3 -> checkoutBook(connection);
                }
            } while (choice != 4);
        }
    }

    public static void viewCheckedOutBooks(Connection connection, int userId) {
        String readQuery = "SELECT * FROM books WHERE userId = ? AND books.checkout IS NOT NULL";
        // Get all books that match userId with logged in user and checkout is not null
        try (PreparedStatement createStatement = connection.prepareStatement(readQuery)) {
            createStatement.setInt(1, userId);

            try (ResultSet resultSet = createStatement.executeQuery()) {
                while (resultSet.next() ) {
                    System.out.print("Name: " + resultSet.getString("name"));
                    System.out.print(" Author: " + resultSet.getString("author"));
                    System.out.print(" Genre: " + resultSet.getString("genre"));
                    System.out.println(" Fee: " + resultSet.getDouble("fee"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void viewFees(Connection connection, int userId) {
        double totalFees = 0;
        int count = 0;
        LocalDateTime now = LocalDateTime.now();
        String readQuery = "SELECT * FROM books WHERE userId = ?";

        try (PreparedStatement createStatement = connection.prepareStatement(readQuery)) {
            createStatement.setInt(1, userId);

            try (ResultSet resultSet = createStatement.executeQuery()) {
                if (resultSet.next() ) {
                    if(resultSet.getTimestamp("checkout")!= null){
                        //if it is then we get the checkout datetime
                        LocalDateTime time = resultSet.getTimestamp("checkout").toLocalDateTime();
                        long difference = ChronoUnit.DAYS.between(time, now);
                        //then we get the difference
                        //once we get the difference we calculate how much the person owes
                        if(difference >=6){
                            count+=1;
                            if(difference == 6){
                                totalFees+= resultSet.getInt("fee");
                            }else{
                                totalFees+=resultSet.getDouble("fee")*(difference-6);
                            }

                        }
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkoutBook(Connection connection) {
        System.out.println("Enter the book name to check out: ");
        String bookName = scanner.nextLine();
        String readQuery = "SELECT * FROM books WHERE name = ? AND userId IS NULL";
        String updateQuery = "UPDATE books SET userId = ?, checkout = ? WHERE name = ? AND userId IS NULL";

        try (PreparedStatement readStatement = connection.prepareStatement(readQuery)) {
            readStatement.setString(1, bookName);
            try (ResultSet resultSet = readStatement.executeQuery()) {
                // Checks if book entered is available or exists
                if (resultSet.next()) {
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        // Updates book userId to logged in user and checkout date, time
                        updateStatement.setInt(1, user.getId());
                        updateStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                        updateStatement.setString(3, bookName);
                        updateStatement.executeUpdate();
                        System.out.println("Book checked out successfully!");
                    }
                } else {
                    System.out.println("Book not available for checkout or does not exist.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}