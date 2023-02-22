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

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {

        try(Connection connection = Util.getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(("CREATE TABLE IF NOT EXISTS users(id int(100) NOT NULL AUTO_INCREMENT PRIMARY KEY," + " username VARCHAR(100)," + " lastname VARCHAR(100), " + " age int)"));
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Таблица не создалась.");
        }
    }

    public void dropUsersTable() {
       // connection = Util.getConnection();

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL_DROP);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Таблица не удалилась.");
        }
    }

    public void saveUser(String name, String lastName, byte age) {

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
            connection.commit();

            System.out.println("User с именем – " + name + " добавлен в базу данных.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("User с именем – " + name + " НЕ добавлен в базу данных.");
        }
    }

    public void removeUserById(long id) {

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("User с id – " + id + " удалён из базы данных.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("User с id – " + id + " НЕ удалён из базы данных, так как не был найден.");
        }
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL)) {
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

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Нет возможности вытащить всех users из БД.");
        }
        return users;
    }

    public void cleanUsersTable() {

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_TRUNCATE)) {
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("Таблица Users была очищена.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось очистить таблицу Users.");
        }
    }
}
