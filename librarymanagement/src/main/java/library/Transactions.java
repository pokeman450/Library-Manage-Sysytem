package library;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Transactions {
    public static void viewBooks() throws SQLException, ClassNotFoundException {
        //connects to the mysql database

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(Main.connect.getUrl(), Main.connect.getUser(), Main.connect.getPass());
        Statement statement = connection.createStatement();
        //gets all the books that are checked out and not checked out
        String query = "select * from books left join user on books.userId = user.userId";
        ResultSet result = statement.executeQuery(query);
        if(!result.isBeforeFirst()){
            //if theres no books then it will say
            System.out.println("Theres no books");
            return;
        }
        System.out.println("-----BOOKS-----");
        //prints out all the books with the person who checked it out
        //if no one checked it out then the datetime on the bd will be null same with the name
        while(result.next()){
            System.out.print("{Book Id: "+result.getInt("bookId"));
            System.out.print(", Title: " + result.getString("name"));
            System.out.print(", Author: " + result.getString("author"));
            System.out.print(", Genre: " + result.getString("genre"));
            System.out.print(", Checked out: " + result.getString("checkout"));
            System.out.print(", Checked out by: "+ result.getString("username"));
            System.out.println();
        }
    }

    public static void totalFees() throws SQLException, ClassNotFoundException {
        double totalFees = 0;
        int count = 0;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(Main.connect.getUrl(), Main.connect.getUser(), Main.connect.getPass());        Statement statement = connection.createStatement();
        //gets the books
        String query = "select * from books";
        ResultSet result = statement.executeQuery(query);
        LocalDateTime now = LocalDateTime.now();

        while (result.next()){
            //checks if its checked out
            if(result.getTimestamp("checkout")!= null){
                //if it is then we get the checkout datetime
                LocalDateTime time = result.getTimestamp("checkout").toLocalDateTime();
                long difference = ChronoUnit.DAYS.between(time,now);
                //then we get the difference
                //once we get the difference we calculate how much the person owes
                if(difference >=6){
                    count+=1;
                    if(difference == 6){
                        totalFees+= result.getInt("fee");
                    }else{
                        totalFees+=result.getDouble("fee")*(difference-6);
                    }

                }
            }

        }
        System.out.println("There were "+count+" late books.");
        System.out.println("Total fee from all the late books are: "+totalFees+" dollars!");
    }
    public static void insert() throws ClassNotFoundException, SQLException {
        String title,author,genre;
        int fee;
        //asks what book they want to add, the title, author, and genre
        System.out.print("Whats the book title? ");
        title = Main.scanner.nextLine();
        System.out.print("Whos the book by? ");
        author = Main.scanner.nextLine();
        System.out.print("Whats the books genre? ");
        genre = Main.scanner.nextLine();

        //connects to the database and adds it into the database
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(Main.connect.getUrl(), Main.connect.getUser(), Main.connect.getPass());
        String insertSQL = "INSERT INTO books (userId, name, author, genre, checkout ,fee) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(insertSQL);
        connection.setAutoCommit(false);
        insert.setNull(1, Types.INTEGER);
        insert.setString(2,title);
        insert.setString(3,author);
        insert.setString(4,genre);
        insert.setNull(5,Types.TIMESTAMP);
        insert.setInt(6,10);
        insert.addBatch();

        insert.executeBatch();
        connection.commit();
        System.out.println("Added a book into the library");

    }
     public static void removeBook() throws ClassNotFoundException, SQLException {
        //asks for the books id
        System.out.print("Whats the Id of the book you want to remove? ");
        int bookId = Main.scanner.nextInt();
        Main.scanner.nextLine();
        //connects to the database
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(Main.connect.getUrl(), Main.connect.getUser(), Main.connect.getPass());
        //grabs all the books from the database
        String query = "select * from books where books.bookId = '"+bookId+"'";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);
        //if the book we're trying to delete is in there
        if(result.isBeforeFirst()){
            //then we rmeove the book from the database
            String remove = "delete from books where books.bookId = '"+bookId+"'";
            PreparedStatement insert = connection.prepareStatement(remove);
            insert.execute();
            System.out.println("Book with the id of "+bookId+" has been removed");

        }else{
            //else we wouldnt find it
            System.out.println("Couldnt find the book");
        }


     }
    public static void  returnBook() throws ClassNotFoundException, SQLException {

        System.out.print("Whats the Id of the book you want to checkout? ");
        int bookId = Main.scanner.nextInt();
        Main.scanner.nextLine();
        //connects to the database
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(Main.connect.getUrl(), Main.connect.getUser(), Main.connect.getPass());
        String  quiry = "update books set userId = null , checkout = null where bookId = ?" ;
        PreparedStatement ps = connection.prepareStatement(quiry);
        ps.setInt(1,bookId);
        if(ps.executeUpdate() == 1){
            System.out.println("updated successfully");
        } else if (ps.executeUpdate() == 0) {
            System.out.println("you entered wrong id number");
        }
    }
}
