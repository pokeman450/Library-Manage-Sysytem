package library;

import java.sql.*;

public class Transactions {
    public static void viewBooks() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "maplestory");
        Statement statement = connection.createStatement();
        String query = "select * from books left join user on books.userId = user.userId";
        ResultSet result = statement.executeQuery(query);
        if(!result.isBeforeFirst()){
            System.out.println("Theres no books");
            return;
        }
        System.out.println("-----BOOKS-----");
        while(result.next()){
            System.out.print("{");
            System.out.print("Title: " + result.getString("name"));
            System.out.print(", Author: " + result.getString("author"));
            System.out.print(", Genre: " + result.getString("genre"));
            System.out.print(", Checked out: " + result.getString("checkout"));
            System.out.print(", Checked out by: "+ result.getString("username"));
            System.out.println();
        }
    }

    public static void totalFees() throws SQLException, ClassNotFoundException {
        int totalFees = 0;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "maplestory");
        Statement statement = connection.createStatement();
        String query = "select * from books";
        ResultSet result = statement.executeQuery(query);
    }
}
