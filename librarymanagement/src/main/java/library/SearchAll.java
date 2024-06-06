package library;

import java.sql.*;

public class SearchAll {

    //title, author, or genre
static Connection getConnection() throws SQLException, ClassNotFoundException {
String url= "jdbc:mysql://localhost:3306/library";
    String user = "root";
    String password = "root";
    Connection conn = DriverManager.getConnection(url, user, password);
    Class.forName("com.mysql.cj.jdbc.Driver");
return  conn;
}
    public void getByTitle(String name) throws SQLException, ClassNotFoundException {
String quiry ="SELECT * FROM books where name like ? ";
        PreparedStatement ps = getConnection().prepareStatement(quiry);
        ps.setString(1,name+"%");

        ResultSet rs = ps.executeQuery();
        while(rs.next()){

            System.out.println(rs.getNString("name")+" "+rs.getNString("author")+" "+rs.getNString("genre"));

        }

    }
    public void getByAuthor(String author) throws SQLException, ClassNotFoundException {
        String quiry ="SELECT * FROM books where author like ? ";
        PreparedStatement ps = getConnection().prepareStatement(quiry);
        ps.setString(1,author+"%");

        ResultSet rs = ps.executeQuery();
        while(rs.next()){

            System.out.println(rs.getNString("author")+" "+rs.getNString("author")+" "+rs.getNString("author"));

        }

    }
    public void getBygenre(String genre) throws SQLException, ClassNotFoundException {
        String quiry ="SELECT * FROM books where genre like ? ";
        PreparedStatement ps = getConnection().prepareStatement(quiry);
        ps.setString(1,genre+"%");

        ResultSet rs = ps.executeQuery();
        while(rs.next()){

            System.out.println(rs.getNString("genre")+" "+rs.getNString("author")+" "+rs.getNString("name"));

        }

    }

}
