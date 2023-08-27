package hung.endmd4.service.Users;

import hung.endmd4.model.Users;
import hung.endmd4.util.ConnectDB;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserIMPL implements IUsers<Users, Integer> {
    @Override
    public void addUser(Users user) {
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            String procedureCall = "{call PROC_Register(?, ?, ?, ?, ?)}";
            CallableStatement callst = connection.prepareCall(procedureCall);
            callst.setString(1, user.getUsername());
            callst.setString(2, user.getEmail());
            callst.setString(3, user.getFirstName());
            callst.setString(4, user.getLastName());
            callst.setString(5, user.getPassword());
            callst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
    }

    @Override
    public void updateUser(Users user) {

    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public Users getUserById(Integer id) {
        Connection connection = null;
        Users user = null;

        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("CALL PROC_FindUserById(?)");
            callSt.setInt(1, id);

            ResultSet resultSet = callSt.executeQuery();
            if (resultSet.next()) {
                user = new Users();
                user.setUserId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRoleId(resultSet.getInt("role_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }

        return user;
    }

    @Override
    public List<Users> getAllUsers() {
        List<Users> usersList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("CALL PROC_FindAllUsers()");
            ResultSet resultSet = callSt.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                int roleId = resultSet.getInt("role_id");
                int status = resultSet.getInt("status");

                Users user = new Users(username, email, firstName, lastName, password, roleId, status);
                user.setUserId(userId);
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return usersList;
    }

    public Users login(String username, String password) {
        Connection connection = null;
        Users user = null;
        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("CALL PROC_Login(?, ?)");
            callSt.setString(1, username);
            callSt.setString(2, password);

            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                int status = rs.getInt("status");
                int role = rs.getInt("role_id");
                if (status == 1) {
                    user = new Users(); // Tạo đối tượng Users
                    user.setRoleId(role);
                    user.setStatus(status);
                    user.setUserId(rs.getInt("user_id"));
                    user.setEmail(rs.getString("email"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }
        return user;
    }

    public Users blockUser(Integer userId) {
        Connection connection = null;
        Users user = null;

        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("CALL PROC_BlockUser(?)");
            callSt.setInt(1, userId);
            callSt.executeUpdate();
            user = getUserById(userId); // Lấy thông tin người dùng sau khi khóa

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }

        return user;
    }

    public Users UnLockUser(Integer userId) {
        Connection connection = null;
        Users user = null;

        try {
            connection = ConnectDB.getConnection();
            CallableStatement callSt = connection.prepareCall("CALL PROC_UnLockUser(?)");
            callSt.setInt(1, userId);
            callSt.executeUpdate();
            user = getUserById(userId);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection();
        }

        return user;
    }

    public boolean isUsernameRegistered(String username) {
        try (Connection connection = ConnectDB.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailRegistered(String email) {
        try (Connection connection = ConnectDB.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
