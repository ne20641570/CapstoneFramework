package db.queries;

public class UserQueries {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "FirstName VARCHAR(50) NOT NULL," +
                    "LastName VARCHAR(50) NOT NULL," +
                    "Address VARCHAR(255)," +
                    "City VARCHAR(50)," +
                    "State VARCHAR(50)," +
                    "ZipCode VARCHAR(10)," +
                    "PhoneNumber VARCHAR(15)," +
                    "SSN VARCHAR(11)," +
                    "UserName VARCHAR(50) UNIQUE NOT NULL" +
                    ")";

    public static final String INSERT_USER =
            "INSERT INTO users (FirstName, LastName, Address, City, State, ZipCode, PhoneNumber, SSN, UserName) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String GET_ALL_USERNAMES =
            "SELECT UserName FROM users";

    public static final String GET_ALL_IDS =
            "SELECT Id FROM users";

    public static final String GET_USER_BY_USERNAME =
            "SELECT * FROM users WHERE UserName = ?";

    public static final String GET_USER_BY_ID =
            "SELECT * FROM users WHERE Id = ?";

    public static final String UPDATE_ADDRESS =
            "UPDATE users SET Address = ?, City = ?, State = ?, ZipCode = ? WHERE UserName = ?";

    public static final String PATCH_PHONE =
            "UPDATE users SET PhoneNumber = ? WHERE UserName = ?";

    public static final String DELETE_USER =
            "DELETE FROM users WHERE UserName = ?";
}
