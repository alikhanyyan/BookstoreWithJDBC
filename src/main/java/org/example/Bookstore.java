package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import static org.example.MenuCodes.*;

public class Bookstore {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final String ADDRESS = "jdbc:postgresql://localhost:5432/bookstoredb";
        final String USERNAME = "postgres";
        final String PASSWORD = "postgres";

        try (Connection connection = DriverManager.getConnection(ADDRESS, USERNAME, PASSWORD))
        {
            while (true) {
                System.out.println("Choose an option:");
                System.out.println(MenuCodes.valueOf(String.valueOf(UPDATE_BOOK_DETAILS)) + ". Update Book Details");
                System.out.println(MenuCodes.valueOf(String.valueOf(LIST_BOOKS_BY_GENRE)) + ". List Books by Genre");
                System.out.println(MenuCodes.valueOf(String.valueOf(LIST_BOOKS_BY_AUTHOR)) + ". List Books by Author");
                System.out.println(MenuCodes.valueOf(String.valueOf(UPDATE_CUSTOMER_INFORMATION)) + ". Update Customer Information");
                System.out.println(MenuCodes.valueOf(String.valueOf(VIEW_CUSTOMER_PURCHASE_HISTORY)) + ". View Customer's Purchase History");
                System.out.println(MenuCodes.valueOf(String.valueOf(PROCESS_NEW_SALE)) + ". Process New Sale");
                System.out.println(MenuCodes.valueOf(String.valueOf(CALCULATE_TOTAL_REVENUE_BY_GENRE)) + ". Calculate Total Revenue by Genre");
                System.out.println(MenuCodes.valueOf(String.valueOf(GENERATE_SALES_REPORT)) + ". Generate Sales Report");
                System.out.println(MenuCodes.valueOf(String.valueOf(GENERATE_REVENUE_REPORT_BY_GENRE)) + ". Generate Revenue Report by Genre");
                System.out.println(MenuCodes.valueOf(String.valueOf(EXIT)) + ". Exit");

                String choice = scanner.nextLine();

                switch (MenuCodes.valueOf(choice)) {
                    case UPDATE_BOOK_DETAILS -> updateBookDetails(connection);
                    case LIST_BOOKS_BY_GENRE -> listBooksByAuthor(connection);
                    case LIST_BOOKS_BY_AUTHOR -> listBooksByGenre(connection);
                    case UPDATE_CUSTOMER_INFORMATION -> updateCustomerInformation(connection);
                    case VIEW_CUSTOMER_PURCHASE_HISTORY -> viewCustomerPurchaseHistory(connection);
                    case PROCESS_NEW_SALE -> handleNewSales(connection);
                    case CALCULATE_TOTAL_REVENUE_BY_GENRE -> calculateTotalRevenueByGenre(connection);
                    case GENERATE_SALES_REPORT -> reportOfAllSoldBooks(connection);
                    case GENERATE_REVENUE_REPORT_BY_GENRE -> reportOfTotalRevenueFromEachGenre(connection);
                    case EXIT -> {
                        System.out.println("Exiting.");
                        return;
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
        ResultSet resultSet = statement.executeQuery();

        printResultSet(resultSet);
    }

    private static void listBooksByAuthor(Connection connection) throws SQLException {
        System.out.println("Enter author:");
        String author = scanner.next();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT Title, Genre, Price, QuantityInStock FROM Books WHERE Author = ?;");
        System.out.println("Author: " + author);
        statement.setString(1, author);
        ResultSet resultSet = statement.executeQuery();

        printResultSet(resultSet);
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
        ResultSet resultSet = statement.executeQuery();

        printResultSet(resultSet);
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
        ResultSet resultSet = statement.executeQuery();

        printResultSet(resultSet);
    }

    private static void reportOfAllSoldBooks(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT C.Name AS CustomerName, B.Title, S.DateOfSale" +
                        "FROM Customers C JOIN Sales S ON C.CustomerID = S.CustomerID JOIN Books B on B.BookID = S.BookID;");
        ResultSet resultSet = statement.executeQuery();

        printResultSet(resultSet);
    }

    private static void reportOfTotalRevenueFromEachGenre(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT B.Genre, SUM(S.Price) TotalRevenue" +
                        "FROM SALES S JOIN Books B ON S.BookID = B.BookID" +
                        "GROUP BY B.Genre;");
        ResultSet resultSet = statement.executeQuery();

        printResultSet(resultSet);
    }

    private static void printResultSet(ResultSet resultSet) throws SQLException {
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.print(resultSet.getMetaData().getColumnName(i) + "\t");
        }
        System.out.println();

        while (resultSet.next()) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.print(resultSet.getString(i) + "\t");
            }
            System.out.println();
        }
    }


}
