package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Ivan", "Pavlov", (byte) 52);
        userService.saveUser("Michael", "Banks", (byte) 28);
        userService.saveUser("Tereza", "Palmer", (byte) 37);
        userService.saveUser("Philip", "Gallagher", (byte) 30);

        userService.getAllUsers();

        Util.getConnection().close();
    }
}
