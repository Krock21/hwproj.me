package me.hwproj.gaev;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Provides interface to do things with relation databases with table phone, which contains pairs of (name, number).
 * first argument of the main share of methods is connection to database
 */
public class DataBase {

    /**
     * Entity, that describes pair (name, number).
     */
    public static class Phone {
        private String name;
        private String number;

        private Phone() {
        }

        public Phone(String name, String number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object phone) {
            if (phone == this) {
                return true;
            }
            if (!(phone instanceof Phone)) {
                return false;
            }
            return name.equals(((Phone) phone).name) && number.equals(((Phone) phone).number);
        }

        @Override
        public int hashCode() {
            return 37 * name.hashCode() + number.hashCode();
        }

        @Override
        public String toString() {
            return name + ", " + number;
        }
    }

    /**
     * insert phone (name, number) to database.
     *
     * @throws SQLException if something wrong with connection
     */
    public static void insertPhone(@NotNull Connection connection, @NotNull String name, @NotNull String number) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into phones values (?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, number);
            statement.executeUpdate();
        }
    }

    /**
     * returns collection of numbers, for which (name, number) stored in database.
     *
     * @throws SQLException if something wrong with connection
     */
    public static Collection<String> getNumbersByName(@NotNull Connection connection, @NotNull String name) throws SQLException {
        var answer = new ArrayList<String>();
        try (PreparedStatement statement = connection.prepareStatement("select number from phones where name = ?")) {
            statement.setString(1, name);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    answer.add(set.getString("number"));
                }
            }
        }
        return answer;
    }


    /**
     * returns collection of names, for which (name, number) stored in database.
     *
     * @throws SQLException if something wrong with connection
     */
    public static Collection<String> getNamesByNumber(@NotNull Connection connection, @NotNull String number) throws SQLException {
        var answer = new ArrayList<String>();
        try (PreparedStatement statement = connection.prepareStatement("select name from phones where number = ?")) {
            statement.setString(1, number);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    answer.add(set.getString("name"));
                }
            }
        }
        return answer;
    }

    /**
     * removes (name, number) from database
     *
     * @throws SQLException if something wrong with connection
     */
    public static void deletePhone(@NotNull Connection connection, @NotNull String name, @NotNull String number) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from phones where name = ? and number = ?")) {
            statement.setString(1, name);
            statement.setString(2, number);
            statement.executeUpdate();
        }
    }

    /**
     * changes name to newName in phone (name, number) in database.
     *
     * @throws SQLException if something wrong with connection
     */
    public static void changeName(@NotNull Connection connection, @NotNull String name, @NotNull String number, @NotNull String newName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update phones set name = ? where name = ? and number = ?")) {
            statement.setString(1, newName);
            statement.setString(2, name);
            statement.setString(3, number);
            statement.executeUpdate();
        }
    }

    /**
     * changes number to newNumber in phone (name, number) in database.
     *
     * @throws SQLException if something wrong with connection
     */
    public static void changeNumber(@NotNull Connection connection, @NotNull String name, @NotNull String number, @NotNull String newNumber) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update phones set number = ? where name = ? and number = ?")) {
            statement.setString(1, newNumber);
            statement.setString(2, name);
            statement.setString(3, number);
            statement.executeUpdate();
        }
    }

    /**
     * returns Collection of Phones, stored in database.
     *
     * @throws SQLException if something wrong with connection
     */
    public static Collection<Phone> getPhones(@NotNull Connection connection) throws SQLException {
        var answer = new ArrayList<Phone>();
        try (Statement statement = connection.createStatement()) {
            try (ResultSet set = statement.executeQuery("select name, number from phones")) {
                while (set.next()) {
                    answer.add(new Phone(set.getString("name"), set.getString("number")));
                }
            }
        }
        return answer;
    }

    /**
     * erases PhoneBook
     *
     * @throws SQLException if something wrong with connection
     */
    public static void erasePhoneBook(@NotNull Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("delete from phones");
        }
    }


    public static void main(String... args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:db.db");
             Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            try (Statement createStatement = connection.createStatement()) {
                createStatement.executeUpdate("create table if not exists phones (name varchar(50), number varchar(50))");
            }
            System.out.println(
                    "0 - exit\n" +
                            "1 - add phone (name, number)\n" +
                            "2 - find all numbers by name\n" +
                            "3 - find all names by number\n" +
                            "4 - delete preset (name, number)\n" +
                            "5 - change name to newName in (name, number)\n" +
                            "6 - change number to newNumber in (name, number)\n" +
                            "7 - print all phones in phonebook\n" +
                            "8 - erase phonebook\n");
            while (true) {
                System.out.println("Write your type of query: ");
                int typeOfQuery = scanner.nextInt();
                scanner.nextLine();
                String name;
                String number;
                String newName;
                String newNumber;
                switch (typeOfQuery) {
                    case 0:
                        return;
                    case 1:
                        System.out.println("Write name: ");
                        name = scanner.nextLine();
                        System.out.println("Write number: ");
                        number = scanner.nextLine();
                        insertPhone(connection, name, number);
                        break;
                    case 2:
                        System.out.println("Write name: ");
                        name = scanner.nextLine();
                        for (String item : getNumbersByName(connection, name)) {
                            System.out.println(item);
                        }
                        break;
                    case 3:
                        System.out.println("Write number: ");
                        number = scanner.nextLine();
                        for (String item : getNamesByNumber(connection, number)) {
                            System.out.println(item);
                        }
                        break;
                    case 4:
                        System.out.println("Write name: ");
                        name = scanner.nextLine();
                        System.out.println("Write number: ");
                        number = scanner.nextLine();
                        deletePhone(connection, name, number);
                        break;
                    case 5:
                        System.out.println("Write name: ");
                        name = scanner.nextLine();
                        System.out.println("Write number: ");
                        number = scanner.nextLine();
                        System.out.println("Write newName: ");
                        newName = scanner.nextLine();
                        changeName(connection, name, number, newName);
                        break;
                    case 6:
                        System.out.println("Write name: ");
                        name = scanner.nextLine();
                        System.out.println("Write number: ");
                        number = scanner.nextLine();
                        System.out.println("Write newNumber: ");
                        newNumber = scanner.nextLine();
                        changeNumber(connection, name, number, newNumber);
                        break;
                    case 7:
                        for (Phone item : getPhones(connection)) {
                            System.out.println(item.name + ": " + item.number);
                        }
                        break;
                    case 8:
                        erasePhoneBook(connection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
