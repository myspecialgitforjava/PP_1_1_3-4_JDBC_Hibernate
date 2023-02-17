package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String SQL_DROP = "DROP TABLE IF EXISTS users";
    private static final String SQL_INSERT = "INSERT INTO users(username, lastname, age) VALUES (?, ?, ?)";
    private static final String SQL_DELETE_ID = "DELETE FROM users WHERE id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM users";
    private static final String SQL_TRUNCATE = "TRUNCATE TABLE users";

    private static Connection connection;
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        connection = Util.getConnection();

        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(("CREATE TABLE IF NOT EXISTS users(id int(100) NOT NULL AUTO_INCREMENT PRIMARY KEY," + " username VARCHAR(100)," + " lastname VARCHAR(100), " + " age int)"));
            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Таблица не создалась.");

            try {
                connection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    public void dropUsersTable() {
        connection = Util.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL_DROP);
            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Таблица не удалилась.");

            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            connection.close();
            } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        connection = Util.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();

            connection.setAutoCommit(false);
            connection.commit();

            System.out.println("User с именем – " + name + " добавлен в базу данных.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("User с именем – " + name + " НЕ добавлен в базу данных.");

            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            connection.close();
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    public void removeUserById(long id) {
        connection = Util.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            connection.setAutoCommit(false);
            connection.commit();
            System.out.println("User с id – " + id + " удалён из базы данных.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("User с id – " + id + " НЕ удалён из базы данных, так как не был найден.");

            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            connection.close();
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }

    public List<User> getAllUsers() {
        connection = Util.getConnection();

        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("username"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
                System.out.println(user);
            }

            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Нет возможности вытащить всех users из БД.");

            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            connection.close();
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
        return users;
    }

    public void cleanUsersTable() {
        connection = Util.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_TRUNCATE)) {
            preparedStatement.executeUpdate();
            connection.setAutoCommit(false);
            connection.commit();
            System.out.println("Таблица Users была очищена.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось очистить таблицу Users.");

            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            connection.close();
        } catch (SQLException exc) {
            throw new RuntimeException(exc);
        }
    }
}
