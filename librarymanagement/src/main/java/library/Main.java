package library;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/inventory_management";
    private static final String username = "root";
    private static final String password = "rootroot";
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Class.forName("com.mysql.cj.jdbc.Driver");

        boolean running = true;

        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("1. Sign up 2. Sign in ");
            int choice = scanner.nextInt();
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
            User user = authenticateUser(connection, usern, passw);

            if (user != null) {
                if ("librarian".equalsIgnoreCase(user.getRole())) {
                    // Features for Librarian
                } else {
                    // Features for patrons
                }
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
            System.out.println("Success. Created user!")
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User authenticateUser(Connection connection, String usern, String passw) {
        // Verify if user exists with correct username and password
        String readQuery = "Select * FROM users WHERE username = (?) AND password = (?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(readQuery)) {
            preparedStatement.setString(1, usern);
            preparedStatement.setString(2, passw);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
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

}