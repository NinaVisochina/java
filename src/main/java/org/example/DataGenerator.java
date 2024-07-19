package org.example;

import com.github.javafaker.Faker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

public class DataGenerator {
    private static final String URL = "jdbc:postgresql://localhost:5432/java_krot";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");

            Faker faker = new Faker(new Locale("en-US"));

            String sql = "INSERT INTO animals (name, species, age, weight) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            connection.setAutoCommit(false);

            for (int i = 0; i < 100000; i++) {
                String name = faker.animal().name();
                String species = faker.animal().name();
                int age = faker.number().numberBetween(1, 15);
                double weight = faker.number().randomDouble(2, 1, 100);

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, species);
                preparedStatement.setInt(3, age);
                preparedStatement.setBigDecimal(4, java.math.BigDecimal.valueOf(weight));

                preparedStatement.addBatch();

                if (i % 1000 == 0) {
                    preparedStatement.executeBatch();
                    connection.commit();
                }
            }

            preparedStatement.executeBatch();
            connection.commit();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

//package org.example;
//
//import java.sql.*;
//import java.util.Scanner;
//
//public class Main {
//    // JDBC URL, username, and password of PostgreSQL server
//    private static final String URL = "jdbc:postgresql://localhost:5432/java_krot";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "123456";
//
//    // JDBC variables for opening and managing connection
//    private static Connection connection;
//
//    public static void main(String[] args) {
//        try {
//// Open a connection
//            connection = DriverManager.getConnection(URL, USER, PASSWORD);
//            System.out.println("Database connected successfully!");
//            // Create a statement object to send to the database
//            var command = connection.createStatement();
//           //createTableAnimals(command);
//
//            // SQL query to select data
//            String sql = "SELECT * FROM animals";
//
//            // Execute the query and get a result set
//            var resultSet = command.executeQuery(sql);
//
//            // Process the result set
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String name = resultSet.getString("name");
//                String species = resultSet.getString("species");
//                int age = resultSet.getInt("age");
//                double weight = resultSet.getDouble("weight");
//
//                System.out.println("ID: " + id + ", Name: " + name + ", Species: " + species + ", Age: " + age + ", Weight: " + weight);
//            }
//
//            resultSet.close();
//
//            insertAnimal(connection);
//            updateAnimal(connection);
//            deleteAnimal(connection);
//
//            command.close();
//            connection.close();
//        } catch (SQLException e) {
//            System.out.println("Begin working"+e.getMessage());
//        }
//    }
//    private static void createTableAnimals(Statement command) throws SQLException {
//        String sql = "CREATE TABLE animals (" +
//                "id SERIAL PRIMARY KEY, " +
//                "name VARCHAR(50) NOT NULL, " +
//                "species VARCHAR(50) NOT NULL, " +
//                "age INT NOT NULL, " +
//                "weight DECIMAL(5, 2) NOT NULL" +
//                ")";
//        command.executeUpdate(sql);
//    }
//    private static void insertAnimal(Connection conn) throws SQLException {
//        Animal animal = new Animal();
//
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Вкажіть назву ->_");
//        animal.setName(scanner.nextLine());
//
//        System.out.print("Вкажіть вид тварини ->_");
//        animal.setSpecies(scanner.nextLine());
//
//        System.out.print("Вкажіть вік ->_");
//        animal.setAge(scanner.nextInt());
//
//        System.out.print("Вкажіть вагу ->_");
//        animal.setWeight(scanner.nextDouble());
//
//        String sql = "INSERT INTO animals (name, species, age, weight) VALUES (?, ?, ?, ?)";
//
//        // Create a prepared statement object
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//
//        // Set the values for the prepared statement
//        preparedStatement.setString(1, animal.getName());
//        preparedStatement.setString(2, animal.getSpecies());
//        preparedStatement.setInt(3,animal.getAge());
//        preparedStatement.setBigDecimal(4, java.math.BigDecimal.valueOf(animal.getWeight()));
//
//        // Execute the query
//        int rowsInserted = preparedStatement.executeUpdate();
//        if (rowsInserted > 0) {
//            System.out.println("Тваринку успішно додано!");
//        }
//        preparedStatement.close();
//    }
//    private static void deleteAnimal(Connection conn) throws SQLException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Вкажіть ID тварини, яку потрібно видалити ->_");
//        int id = scanner.nextInt();
//
//        String sql = "DELETE FROM animals WHERE id = ?";
//
//        // Create a prepared statement object
//        PreparedStatement preparedStatement = conn.prepareStatement(sql);
//
//        // Set the ID for the prepared statement
//        preparedStatement.setInt(1, id);
//
//        // Execute the query
//        int rowsDeleted = preparedStatement.executeUpdate();
//        if (rowsDeleted > 0) {
//            System.out.println("Тваринку успішно видалено!");
//        } else {
//            System.out.println("Тваринку з таким ID не знайдено.");
//        }
//        preparedStatement.close();
//    }
//    private static void updateAnimal(Connection conn) throws SQLException {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.print("Вкажіть ID тварини, яку потрібно оновити ->_");
//        int id = scanner.nextInt();
//        scanner.nextLine(); // consume newline
//
//        System.out.print("Вкажіть нову назву ->_");
//        String name = scanner.nextLine();
//
//        System.out.print("Вкажіть новий вид тварини ->_");
//        String species = scanner.nextLine();
//
//        System.out.print("Вкажіть новий вік ->_");
//        int age = scanner.nextInt();
//
//        System.out.print("Вкажіть нову вагу ->_");
//        double weight = scanner.nextDouble();
//
//        String sql = "UPDATE animals SET name = ?, species = ?, age = ?, weight = ? WHERE id = ?";
//
//        // Create a prepared statement object
//        PreparedStatement preparedStatement = conn.prepareStatement(sql);
//
//        // Set the values for the prepared statement
//        preparedStatement.setString(1, name);
//        preparedStatement.setString(2, species);
//        preparedStatement.setInt(3, age);
//        preparedStatement.setBigDecimal(4, java.math.BigDecimal.valueOf(weight));
//        preparedStatement.setInt(5, id);
//
//        // Execute the query
//        int rowsUpdated = preparedStatement.executeUpdate();
//        if (rowsUpdated > 0) {
//            System.out.println("Дані про тварину успішно оновлено!");
//        } else {
//            System.out.println("Тваринку з таким ID не знайдено.");
//        }
//        preparedStatement.close();
//    }
//
//}


