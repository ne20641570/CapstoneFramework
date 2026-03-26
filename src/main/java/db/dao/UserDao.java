package db.dao;

import db.model.User;
import db.queries.UserQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public void createTableIfNotExists() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.execute(UserQueries.CREATE_TABLE);
    }

    public int insertUser(String firstName, String lastName, String address,
                          String city, String state, String zipCode,
                          String phone, String ssn, String userName) throws Exception {

        PreparedStatement ps = connection.prepareStatement(UserQueries.INSERT_USER);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, address);
        ps.setString(4, city);
        ps.setString(5, state);
        ps.setString(6, zipCode);
        ps.setString(7, phone);
        ps.setString(8, ssn);
        ps.setString(9, userName);

        return ps.executeUpdate();
    }
    public List<String> getAllUserNames() throws Exception {
        List<String> userNames = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement(UserQueries.GET_ALL_USERNAMES);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            userNames.add(rs.getString("UserName"));
        }
        return userNames;
    }
    public List<Integer> getAllUserIds() throws Exception {
        List<Integer> Ids = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement(UserQueries.GET_ALL_IDS);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Ids.add(rs.getInt("Id"));
        }
        return Ids;
    }
    public boolean isUserExists(String userName) throws Exception {
        PreparedStatement ps = connection.prepareStatement(UserQueries.GET_USER_BY_USERNAME);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public int updateAddress(String address, String city, String state,
                             String zipCode, String userName) throws Exception {

        PreparedStatement ps = connection.prepareStatement(UserQueries.UPDATE_ADDRESS);
        ps.setString(1, address);
        ps.setString(2, city);
        ps.setString(3, state);
        ps.setString(4, zipCode);
        ps.setString(5, userName);

        return ps.executeUpdate();
    }

    public int patchPhoneNumber(String phone, String userName) throws Exception {
        PreparedStatement ps = connection.prepareStatement(UserQueries.PATCH_PHONE);
        ps.setString(1, phone);
        ps.setString(2, userName);
        return ps.executeUpdate();
    }

    public int deleteUser(String userName) throws Exception {
        PreparedStatement ps = connection.prepareStatement(UserQueries.DELETE_USER);
        ps.setString(1, userName);
        return ps.executeUpdate();
    }
    public User getUserByUserName(String userName) throws Exception {
        PreparedStatement ps =
                connection.prepareStatement(UserQueries.GET_USER_BY_USERNAME);
        ps.setString(1, userName);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("FirstName"));
            user.setLastName(rs.getString("LastName"));
            user.setAddress(rs.getString("Address"));
            user.setCity(rs.getString("City"));
            user.setState(rs.getString("State"));
            user.setZipCode(rs.getString("ZipCode"));
            user.setPhoneNumber(rs.getString("PhoneNumber"));
            user.setSsn(rs.getString("SSN"));
            user.setUserName(rs.getString("UserName"));
            return user;
        }
        return null;
    }
    public User getUserById(int id) throws Exception {
        PreparedStatement ps =
                connection.prepareStatement(UserQueries.GET_USER_BY_ID);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("FirstName"));
            user.setLastName(rs.getString("LastName"));
            user.setAddress(rs.getString("Address"));
            user.setCity(rs.getString("City"));
            user.setState(rs.getString("State"));
            user.setZipCode(rs.getString("ZipCode"));
            user.setPhoneNumber(rs.getString("PhoneNumber"));
            user.setSsn(rs.getString("SSN"));
            user.setUserName(rs.getString("UserName"));
            return user;
        }
        return null;
    }
}
