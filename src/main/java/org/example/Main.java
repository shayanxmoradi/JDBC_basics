package org.example;

import java.sql.*;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    static Actions actions;

    public static void main(String[] args) {

        //try with resorcuce
        try (Connection conn = createConnection()) {
            actions = new Actions(conn);
            actions.doAction();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://localhost:5432/exam";
        String user = "postgres";
        String password = "1234";

        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }


    public static class Actions {
        private Connection conn;
        private final Scanner scanner;

        public Actions(Connection conn) {
            this.conn = conn;
            scanner = new Scanner(System.in);
        }


        public void doAction() throws SQLException {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Welcome! For viewing the whole DB, enter 1. For adding a user, enter 2. To exit, enter 0.");
                int menuChoice = scanner.nextInt();

                switch (menuChoice) {
                    case 1 -> showUserTable();

                    case 2 -> addUser();

                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        private void addUser() throws SQLException {
            System.out.println("Enter first name:");
            String firstName = scanner.next();

            System.out.println("Enter last name:");
            String lastName = scanner.next();

            System.out.println("Enter username:");
            String username = scanner.next();

            System.out.println("Enter password:");
            String password = scanner.next();

            String query = "INSERT INTO users (first_name, last_name, user_name, password) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, password);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted.");

                System.out.println("Current state of the DB:");
                showUserTable();
            }
        }

        private void showUserTable() throws SQLException {
            String query = "SELECT * FROM users";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)

            ) {
                while (rs.next()) {
                    System.out.print(rs.getInt("id") + ", ");
                    System.out.print(rs.getString("first_name") + ", ");
                    System.out.print(rs.getString("last_name") + ", ");
                    System.out.print(rs.getString("user_name") + "\n");
                }
            }
        }


    }
}
