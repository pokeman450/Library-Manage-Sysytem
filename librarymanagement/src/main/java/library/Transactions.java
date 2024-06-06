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
}
