package inventory;


import java.util.*;
import java.sql.*;

public class Main {
    
    static Scanner scan = new Scanner(System.in);
    static ProductDAO pd = new ProductDAO();
    static SaleDAO sd = new SaleDAO();
    static String username;
    static String password;
    public static void main(String[] args) {;
        System.out.println("Enter usernamename: ");
        username = scan.nextLine();
        System.out.println("Enter password: ");
        password = scan.nextLine();
        try{
            User user = UserDAO.authenticate(username,password);
            if(username!= null){
                System.out.println("Current user: "+user.getUser());
                if(user.getRole().equals("admin")){
                    adminMenu();
                } else{
                    userMenu();
                }} else{
                    System.out.println("Username or password is incorrect");
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    private static void adminMenu(){
        while(true){
            try {
                while (true){
                    System.out.println("""
                    
                    1) Add Product
                    2) Search Product
                    3) View Products
                    4) Update Product
                    5) Delete Product
                    6) Record Sale
                    7) Generate Sale Reports
                    8) Generate Inventory Reports
                    9) Exit
                    """);

                    int input = Integer.parseInt(scan.nextLine());
                    switch (input){
                        case 1 -> addProduct();
                        case 2 -> searchProduct();
                        case 3 -> viewProduct();
                        case 4 -> updateProduct();
                        case 5 -> deleteProduct();
                        case 6 -> recordSale();
                        case 7 -> generateSaleReport();
                        case 8 -> generateInventorySaleReport();
                        case 9 -> System.exit(0);
                        default -> System.out.println("Invalid, Try 1-9");
                    }
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid, Try number 1-9");
            }
        }
    }
    // If role is admin load user console
    private static void userMenu(){
        while (true){
            try {
                while (true){
                    System.out.println("""
                    
                    1) Search Product
                    2) View Products
                    3) Record Sale
                    4) Exit
                    """);

                    int input = Integer.parseInt(scan.nextLine());
                    switch (input){
                        case 1 -> searchProduct();
                        case 2 -> viewProduct();
                        case 3 -> recordSale();
                        case 4 -> System.exit(0);
                        default -> System.out.println("Invalid, Try 1-4");
                    }
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid, Try number 1-4");
            }
        }
    }
    // Print out the report joined between sales and products
    private static void generateInventorySaleReport() {
        ArrayList<String> saleProducts = sd.getSaleInventoryReport();
        System.out.println("\n--== Currently Viewing All Sales of Products==--");
        for (String saleProduct : saleProducts){
            System.out.println(saleProduct);
        }
    }

    // Print out sale records
    private static void generateSaleReport() {
        ArrayList<Sale> sales = sd.getSalesReport();
        System.out.println("\n--== Currently Viewing All Sales ==--");
        for (Sale sale : sales){
            System.out.println(sale);
        }
    }

    // Allow user to add in sale, it will take away from quality of product
    private static void recordSale() {
        System.out.println("\n--== Currently Recording Sale ==--");
        System.out.println("Enter Product Id");
        int id = Integer.parseInt(scan.nextLine());
        System.out.println("Enter Product Quantity");
        int quantity = Integer.parseInt(scan.nextLine());
        sd.RecordSale(id,quantity);
    }

    // Delete Product
    private static void deleteProduct() {
        System.out.println("\n--== Currently Deleting Product ==--");
        System.out.println("Enter Product Name");
        String name = scan.nextLine();
        pd.deleteProduct(name);
    }

    // Update product based on ID
    private static void updateProduct() {
        System.out.println("\n--== Currently Updating Product ==--");
        System.out.println("Enter Product Id");
        int id = Integer.parseInt(scan.nextLine());
        System.out.println("Enter Product Name");
        String name = scan.nextLine();
        System.out.println("Enter Product Quantity");
        int quantity = Integer.parseInt(scan.nextLine());
        System.out.println("Enter Product Price");
        double price = Double.parseDouble(scan.nextLine());
        pd.updateProduct(id,name,quantity,price);
        if (pd.getProduct(name) != null){
            System.out.println(pd.getProduct(name));
        }
    }

    // Print out inventory
    private static void viewProduct() {
        ArrayList<Product> products = pd.getAllProducts();
        System.out.println("All Products:");
        for (Product product : products){
            System.out.println(product);
        }
    }

    // Search for a specific product based on name
    private static void searchProduct() {
        System.out.println("Searching for product");
        System.out.println("Enter Product Name");
        String name = scan.nextLine();
        if (pd.getProduct(name) != null){
            System.out.println(pd.getProduct(name));
        }
    }

    // Add new product to inventory
    private static void addProduct() {
        System.out.println("\n--== Currently Adding Product ==--");
        System.out.println("Enter Product Name");
        String name = scan.nextLine();
        System.out.println("Enter Product Quantity");
        int quantity = Integer.parseInt(scan.nextLine());
        System.out.println("Enter Product Price");
        double price = Double.parseDouble(scan.nextLine());
        pd.addProduct(new Product(0,name,quantity,price));
    }

}
