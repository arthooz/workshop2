package pl.coderslab.workshop2.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import pl.coderslab.workshop2.DBUtil;
import pl.coderslab.workshop2.models.User;

public class UserDao {

  private static final String CREATE_USER_QUERY =
      "INSERT INTO users(username, email, password, user_group_id) VALUES (?, ?, ?, ?)";
  private static final String READ_USER_QUERY = "SELECT * FROM users where id = ?";
  private static final String UPDATE_USER_QUERY =
      "UPDATE users SET username = ?, email = ?, password = ?, user_group_id = ? where id = ?";
  private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
  private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";


  public User create(User user) {
    try (Connection conn = DBUtil.getConnection()) {
      PreparedStatement statement =
          conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, user.getUserName());
      statement.setString(2, user.getEmail());
      statement.setString(3, user.getPassword());
      statement.setInt(4, user.getUserGroupId());
      statement.executeUpdate();
      ResultSet resultSet = statement.getGeneratedKeys();
      if (resultSet.next()) {
        user.setId(resultSet.getInt(1));
      }
      return user;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public User read(int userId) {
    try (Connection conn = DBUtil.getConnection()) {
      PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
      statement.setInt(1, userId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setUserGroupId(resultSet.getInt("user_group_id"));
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void update(User user) {
    try (Connection conn = DBUtil.getConnection()) {
      PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
      statement.setString(1, user.getUserName());
      statement.setString(2, user.getEmail());
      statement.setString(3, user.getPassword());
      statement.setInt(4, user.getUserGroupId());
      statement.setInt(5, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void delete(int userId) {
    try (Connection conn = DBUtil.getConnection()) {
      PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
      statement.setInt(1, userId);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public User[] findAll() {
    try (Connection conn = DBUtil.getConnection()) {
      User[] users = new User[0];
      PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setUserGroupId(resultSet.getInt("user_group_id"));
        users = addToArray(user, users);
      }
      return users;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private User[] addToArray(User u, User[] users) {
    User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
    tmpUsers[users.length] = u;
    return tmpUsers;
  }
}
