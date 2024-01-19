package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Bookstore {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final String ADDRESS = "jdbc:postgresql://localhost:5432/bookstoredb";
        final String USERNAME = "postgres";
        final String PASSWORD = "postgres";

        try (Connection connection = DriverManager.getConnection(ADDRESS, USERNAME, PASSWORD))
        {
            boolean exit = false;
            while (!exit) {
                System.out.println("Do you want to initialize database?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.println("0. Exit");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> initializeDatabase(connection);
                    case "2" -> exit = true;

                    case "0" -> {
                        System.out.println("Exiting the application.");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }

            exit = false;
            while (!exit) {
                System.out.println("Choose an option:");
                System.out.println("1. Update Book Details");
                System.out.println("2. List Books by Genre");
                System.out.println("3. List Books by Author");
                System.out.println("4. Update Customer Information");
                System.out.println("5. View Customer's Purchase History");
                System.out.println("6. Process New Sale");
                System.out.println("7. Calculate Total Revenue by Genre");
                System.out.println("8. Generate Sales Report");
                System.out.println("9. Generate Revenue Report by Genre");
                System.out.println("0. Exit");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> updateBookDetails(connection);
                    case "2" -> listBooksByAuthor(connection);
                    case "3" -> listBooksByGenre(connection);
                    case "4" -> updateCustomerInformation(connection);
                    case "5" -> viewCustomerPurchaseHistory(connection);
                    case "6" -> handleNewSales(connection);
                    case "7" -> calculateTotalRevenueByGenre(connection);
                    case "8" -> reportOfAllSoldBooks(connection);
                    case "9" -> reportOfTotalRevenueFromEachGenre(connection);
                    case "0" -> {
                        System.out.println("Exiting.");
                        exit = true;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateBookDetails(Connection connection) throws SQLException, NumberFormatException {
        System.out.println("Enter book ID:");
        int bookID = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter book new title or - in case you didn't want to change it:");
        String newTitle = scanner.next();
        System.out.println("Enter book new author name or - in case you didn't want to change it:");
        String newAuthor = scanner.next();
        System.out.println("Enter book new genre name or - in case you didn't want to change it:");
        String newGenre = scanner.next();
        System.out.println("Enter book new price or -1 in case you didn't want to change it:");
        float newPrice = Float.parseFloat(scanner.next());
        System.out.println("Enter book new quantity in stock or -1 in case you didn't want to change it:");
        int newQuantity= Integer.parseInt(scanner.next());

        PreparedStatement statement = null;
        if (!newTitle.trim().equals("-")) {
            statement = connection.prepareStatement(
                    "UPDATE TABLE Books SET Title = ? WHERE BookID = ?;");
            statement.setString(1, newTitle);
            statement.setInt(2, bookID);
            statement.addBatch();
        }

        if (!newAuthor.trim().equals("-")) {
            statement = connection.prepareStatement(
                    "UPDATE TABLE Books SET Author = ? WHERE BookID = ?;");
            statement.setString(1, newAuthor);
            statement.setInt(2, bookID);
            statement.addBatch();
        }

        if (!newGenre.trim().equals("-")) {
            statement = connection.prepareStatement(
                    "UPDATE TABLE Books SET Genre = ? WHERE BookID = ?;");
            statement.setString(1, newGenre);
            statement.setInt(2, bookID);
            statement.addBatch();
        }

        if (newPrice != -1) {
            statement = connection.prepareStatement(
                    "UPDATE TABLE Books SET Price = ? WHERE BookID = ?;");
            statement.setFloat(1, newPrice);
            statement.setInt(2, bookID);
            statement.addBatch();
        }

        if (newQuantity != -1) {
            statement = connection.prepareStatement(
                    "UPDATE TABLE Books SET QuantityInStock = ? WHERE BookID = ?;");
            statement.setInt(1, newQuantity);
            statement.setInt(2, bookID);
            statement.addBatch();
        }

        if (statement != null) {
            statement.executeBatch();
            statement.clearBatch();
        }
    }

    private static void listBooksByGenre(Connection connection) throws SQLException {
        System.out.println("Enter genre:");
        String genre = scanner.next();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT Title, Author, Price, QuantityInStock FROM Books WHERE Genre = ?;");
        statement.setString(1, genre);
        System.out.println("Genre: " + genre);
        System.out.println(statement.executeQuery());
    }

    private static void listBooksByAuthor(Connection connection) throws SQLException {
        System.out.println("Enter author:");
        String author = scanner.next();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT Title, Genre, Price, QuantityInStock FROM Books WHERE Author = ?;");
        System.out.println("Author: " + author);
        statement.setString(1, author);
        System.out.println(statement.executeQuery());
    }

    private static void updateCustomerInformation(Connection connection) throws SQLException, NumberFormatException {
        System.out.println("Enter customer ID:");
        int customerID = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter customer new name or - in case you didn't want to change it:");
        String newName = scanner.next();
        System.out.println("Enter customer new email or - in case you didn't want to change it:");
        String newEmail = scanner.next();
        System.out.println("Enter customer new phone number or - in case you didn't want to change it:");
        String newPhone = scanner.next();

        PreparedStatement statement = null;
        if (!newName.trim().equals("-")) {
            statement = connection.prepareStatement(
                    "UPDATE TABLE Customers SET Name = ? WHERE CustomerID = ?;");
            statement.setString(1, newName);
            statement.setInt(2, customerID);
            statement.addBatch();
        }

        if (!newEmail.trim().equals("-")) {
            statement = connection.prepareStatement(
                    "UPDATE TABLE Customers SET Email = ? WHERE CustomerID = ?;");
            statement.setString(1, newEmail);
            statement.setInt(2, customerID);
            statement.addBatch();
        }

        if (!newPhone.trim().equals("-")) {
            statement = connection.prepareStatement(
                    "UPDATE TABLE Customers SET Phone = ? WHERE CustomerID = ?;");
            statement.setString(1, newPhone);
            statement.setInt(2, customerID);
            statement.addBatch();
        }

        if (statement != null) {
            statement.executeBatch();
            statement.clearBatch();
        }
    }

    private static void viewCustomerPurchaseHistory(Connection connection) throws SQLException {
        System.out.println("Enter customer ID:");
        int customerID = Integer.parseInt(scanner.nextLine());

        PreparedStatement statement = connection.prepareStatement(
                "SELECT C.Name AS CustomerName, B.Title, B.Author, B.Genre, S.DateOfSale " +
                "FROM Customers C JOIN Sales S ON C.CustomerID = S.CustomerID JOIN Books B on B.BookID = S.BookID" +
                "WHERE C.CustomerID = ?;");
        statement.setInt(1, customerID);
        System.out.println(statement.executeQuery());
    }

    private static void  handleNewSales(Connection connection) throws SQLException, NumberFormatException {
        System.out.println("Enter customer ID:");
        int customerID = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter book ID:");
        int bookID = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter books count:");
        int count = Integer.parseInt(scanner.nextLine());

        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(" SELECT Price FROM Books WHERE BookID = ?;");
        statement.setInt(1, bookID);

        float price = statement.getResultSet().getFloat(1) * count;

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatters);

        statement = connection.prepareStatement(
                " INSERT INTO Sales (BookID, CustomerID, DateOfSale, QuantitySold, TotalPrice)" +
                        "VALUES(?, ?, ?, ?, ?);");
        statement.setInt(1, bookID);
        statement.setInt(2, customerID);
        statement.setString(3, date);
        statement.setInt(4, count);
        statement.setFloat(5, price);
        statement.executeQuery();
        connection.commit();
        connection.setAutoCommit(true);
    }

    private static void calculateTotalRevenueByGenre(Connection connection) throws SQLException {
        System.out.println("Enter genre:");
        String genre = scanner.next();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT B.Genre, SUM(S.Price) TotalRevenue" +
                        "FROM SALES S JOIN Books B ON S.BookID = B.BookID" +
                        "GROUP BY B.Genre" +
                        "WHERE B.Genre = ?;");
        statement.setString(1, genre);
        System.out.println(statement.executeQuery());
    }

    private static void reportOfAllSoldBooks(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT C.Name AS CustomerName, B.Title, S.DateOfSale" +
                        "FROM Customers C JOIN Sales S ON C.CustomerID = S.CustomerID JOIN Books B on B.BookID = S.BookID;");
        System.out.println(statement.executeQuery());
    }

    private static void reportOfTotalRevenueFromEachGenre(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT B.Genre, SUM(S.Price) TotalRevenue" +
                        "FROM SALES S JOIN Books B ON S.BookID = B.BookID" +
                        "GROUP BY B.Genre;");
        System.out.println(statement.executeQuery());
    }


    private static void initializeDatabase(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.addBatch("""
                DO $$
                BEGIN
                    IF NOT EXISTS (
                        SELECT 1
                        FROM information_schema.tables
                        WHERE table_name = 'Books'
                    ) THEN
                        CREATE TABLE Books(
                            BookID SERIAL PRIMARY KEY,
                            Title TEXT NOT NULL,
                            Author VARCHAR(40) NOT NULL,
                            Genre VARCHAR(30) NOT NULL,
                            Price REAL NOT NULL CHECK(Price > 0),
                            QuantityInStock INTEGER NOT NULL CHECK(QuantityInStock >= 0)
                        );
                    END IF;
                END $$;             
                """);
        statement.addBatch("""
                DO $$
                BEGIN
                    IF NOT EXISTS (
                        SELECT 1
                        FROM information_schema.tables
                        WHERE table_name = 'Customers'
                    ) THEN
                        CREATE TABLE Customers(
                            CustomerID SERIAL PRIMARY KEY,
                            Name VARCHAR(20) NOT NULL,
                            Email VARCHAR(60) UNIQUE,
                            Phone VARCHAR(20) NOT NULL
                        );
                    END IF;
                END $$;
                """);
        statement.addBatch("""
                DO $$
                BEGIN
                    IF NOT EXISTS (
                        SELECT 1
                        FROM information_schema.tables
                        WHERE table_name = 'Sales'
                    ) THEN
                        CREATE TABLE Sales (
                            SaleID SERIAL PRIMARY KEY,
                            BookID INTEGER,
                            CustomerID INTEGER,
                            DateOfSale DATE,
                            QuantitySold INTEGER NOT NULL CHECK(QuantitySold >= 0),
                            TotalPrice REAL NOT NULL CHECK(TotalPrice >= 0),
                            CONSTRAINT fk_book FOREIGN KEY (BookID) REFERENCES Books(BookID) ON DELETE SET NULL,
                            CONSTRAINT fk_customer FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE SET NULL
                        );
                    END IF;
                END $$;
                """);

        statement.addBatch("""
            CREATE OR REPLACE FUNCTION update_books_quantity_in_stock()
            RETURNS TRIGGER AS $$
            BEGIN
                UPDATE Books
                SET QuantityInStock = QuantityInStock - NEW.QuantitySold
                WHERE BookID = NEW.BookID;
                RETURN NEW;
            END;
            $$ LANGUAGE plpgsql;
            """);
        statement.addBatch("""
            CREATE TRIGGER update_books_quantity
            AFTER INSERT ON Sales
            FOR EACH ROW
            EXECUTE FUNCTION update_books_quantity_in_stock();
            """);

        statement.executeBatch();
        statement.clearBatch();
    }
}
