package library;

import java.sql.*;
public class Books {

    private int userId;
    private  int bookId;
    private String name;
    private String author;
    private String genre;
    private Timestamp checkout;
    private double fee;

    public Books(int userId, int bookId, String name, String author, String genre, Timestamp checkout, double fee)
    {
        this.userId = userId;
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.checkout = checkout;
        this.fee = fee;
    }

    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getBookId(){
        return bookId;
    }
    public void setBookId(int bookId){
        this.bookId = bookId;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getAuthor(){
        return author;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public String getGenre(){
        return genre;
    }
    public void setGenre(String genre){
        this.genre = genre;
    }
    public Timestamp getCheckout(){
        return checkout;
    }
    public void setCheckout(Timestamp checkout){
        this.checkout = checkout;
    }
    public double getFee(){
        return fee;
    }
    public void setFree(double fee){
        this.fee = fee;
    }
}
