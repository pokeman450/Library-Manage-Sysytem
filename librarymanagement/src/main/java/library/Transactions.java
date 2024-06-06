package library;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        double totalFees = 0;
        int count = 0;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "maplestory");
        Statement statement = connection.createStatement();
        String query = "select * from books";
        ResultSet result = statement.executeQuery(query);
        LocalDateTime now = LocalDateTime.now();
        while (result.next()){
            if(result.getTimestamp("checkout")!= null){
                LocalDateTime time = result.getTimestamp("checkout").toLocalDateTime();
                long difference = ChronoUnit.DAYS.between(time,now);
                System.out.println(difference);
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
}
