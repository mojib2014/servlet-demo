package daos;

import dbFactory.ConnectionManager;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    private Connection connection;

    public UserDaoImpl() {
        connection = ConnectionManager.getConnection();
    }

    @Override
    public void create(User user) {
        String query = "INSERT INTO users (first_name, last_name, email) VALUES(?, ?, ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getFirst_name());
            statement.setString(2, user.getLast_name());
            statement.setString(3, user.getEmail());
            int count = statement.executeUpdate();
            if(count == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                int user_id = resultSet.getInt("user_id");
                System.out.println("generated user_id is :" + user_id);
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public void update(User user) {
        String query = "UPDATE users SET first_name = ?, last_name = ?, email = ? WHERE user_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirst_name());
            statement.setString(2, user.getLast_name());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getUser_id());
            int count = statement.executeUpdate();
            if(count == 1) {
                System.out.println("Record updated successfully");
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public void delete(int user_id) {
        String query = "DELETE FROM users WHERE user_id = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user_id);
            int count = statement.executeUpdate();
            if(count == 1) {
                System.out.println("Record deleted successfully");
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public User get(int user_id) {
        String query = "SELECT * FROM users WHERE user_id = ?;";
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user_id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM users;";
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                User user = getUserFromResultSet(resultSet);
                users.add(user);
            }
        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return users;
    }

    private User getUserFromResultSet(ResultSet resultSet) {
        try{
            int user_id = resultSet.getInt("user_id");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            return new User(user_id, first_name, last_name, email);

        }catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }
}
