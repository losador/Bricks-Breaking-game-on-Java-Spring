package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Account;

import java.sql.*;

public class AccountServiceJDBC implements AccountService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String SELECT = "SELECT * FROM user WHERE email = ?";
    public static final String DELETE = "DELETE FROM user";
    public static final String INSERT = "INSERT INTO user (login, password, email) VALUES (?, ?, ?)";

    @Override
    public void addUser(Account user) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new AccountException("Problem inserting score", e);
        }
    }

    @Override
    public Account getUser(String email) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT)
        ) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return new Account(rs.getString(1), rs.getString(2), rs.getString(3));
        } catch (SQLException e) {
            throw new AccountException("Problem inserting score", e);
        }
    }

    @Override
    public boolean getEmail(String email) {
        return false;
    }
}
