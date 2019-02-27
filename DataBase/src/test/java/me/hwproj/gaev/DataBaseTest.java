package me.hwproj.gaev;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {

    private @NotNull Connection connection;

    private static <T> void checkEquals(Collection<T> collection1, Collection<T> collection2) {
        assertEquals(new HashSet<>(collection1), new HashSet<>(collection2));
    }

    @BeforeAll
    static void init() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:testdb.db");
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists phones (name varchar(50), number varchar(50))");
        }
        DataBase.erasePhoneBook(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @AfterAll
    static void clean() {
        File file = new File("testdb.db");
        file.delete();
    }

    @Test
    void insertPhone() throws SQLException {
        DataBase.insertPhone(connection, "a", "b");
        checkEquals(Collections.singleton(new DataBase.Phone("a", "b")), DataBase.getPhones(connection));
    }

    @Test
    void getNumbersByName() throws SQLException {
        DataBase.insertPhone(connection, "a", "b");
        DataBase.insertPhone(connection, "c", "d");
        DataBase.insertPhone(connection, "a", "e");
        checkEquals(Arrays.asList("b", "e"), DataBase.getNumbersByName(connection, "a"));
        checkEquals(Collections.emptyList(), DataBase.getNumbersByName(connection, "b"));
    }

    @Test
    void getNamesByNumber() throws SQLException {
        DataBase.insertPhone(connection, "a", "b");
        DataBase.insertPhone(connection, "c", "d");
        DataBase.insertPhone(connection, "b", "b");
        checkEquals(Arrays.asList("a", "b"), DataBase.getNamesByNumber(connection, "b"));
        checkEquals(Collections.emptyList(), DataBase.getNamesByNumber(connection, "a"));
    }

    @Test
    void deletePhone() throws SQLException {
        DataBase.insertPhone(connection, "a", "b");
        DataBase.deletePhone(connection, "a", "b");
        checkEquals(Collections.emptyList(), DataBase.getPhones(connection));
    }

    @Test
    void changeName() throws SQLException {
        DataBase.insertPhone(connection, "a", "b");
        DataBase.changeName(connection, "a", "b", "c");
        checkEquals(Collections.singleton(new DataBase.Phone("c", "b")), DataBase.getPhones(connection));
    }

    @Test
    void changeNumber() throws SQLException {
        DataBase.insertPhone(connection, "a", "b");
        DataBase.changeNumber(connection, "a", "b", "c");
        checkEquals(Collections.singleton(new DataBase.Phone("a", "c")), DataBase.getPhones(connection));
    }

    @Test
    void getPhones() throws SQLException {
        DataBase.insertPhone(connection, "a", "b");
        DataBase.insertPhone(connection, "c", "d");
        DataBase.insertPhone(connection, "e", "f");
        checkEquals(Arrays.asList(new DataBase.Phone("a", "b"),
                new DataBase.Phone("c", "d"),
                new DataBase.Phone("e", "f")), DataBase.getPhones(connection));
    }

    @Test
    void erasePhoneBook() throws SQLException {
        DataBase.insertPhone(connection, "a", "b");
        DataBase.insertPhone(connection, "c", "d");
        DataBase.insertPhone(connection, "b", "b");
        DataBase.erasePhoneBook(connection);
        assertEquals(Collections.emptyList(), DataBase.getPhones(connection));
        DataBase.erasePhoneBook(connection);
        assertEquals(Collections.emptyList(), DataBase.getPhones(connection));
    }
}