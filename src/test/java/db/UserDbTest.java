package db;

import com.aventstack.extentreports.ExtentTest;
import db.dao.UserDao;
import db.model.User;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ExtentReportUtil;
import utils.ExtentTestManager;
import utils.TestDataGenerator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static db.client.DbClient.getConnection;

public class UserDbTest {

    private String userName;

    private final UserDao userDao = new UserDao(getConnection());
    ExtentTest tests = ExtentTestManager.getTest();
    ExtentTest test;

    @BeforeMethod
    public void beforeMethod(Method method) {
        tests = ExtentTestManager.startTest(method.getName(), "Test description");
    }

    // ---------------- CREATE TABLE ----------------

    @Test(priority = 1)
    public void createTable() {

        test= ExtentTestManager.startNode(tests,"Creating Users table if not exists");

        try {
            userDao.createTableIfNotExists();
            ExtentReportUtil.logPass(test,"Users table created or already exists");
        } catch (Exception | AssertionError e) {
            ExtentReportUtil.logFail(test,"Table creation failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    // ---------------- CREATE USER ----------------

    @Test(priority = 2)
    public void createUser() {
        test= ExtentTestManager.startNode(tests,"Creating a new user with random data");

        try {
            User user= new User();
            user.setFirstName(TestDataGenerator.randomFirstName());
            user.setLastName(TestDataGenerator.randomLastName());
            user.setAddress(TestDataGenerator.randomStreet());
            user.setCity(TestDataGenerator.randomCity());
            user.setState(TestDataGenerator.randomState());
            user.setZipCode(TestDataGenerator.randomZipCode());
            user.setPhoneNumber(TestDataGenerator.randomPhoneNumber());
            user.setSsn(TestDataGenerator.randomSSN());
            userName = TestDataGenerator.randomUsername();

            int rows = userDao.insertUser(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAddress(),
                    user.getCity(),
                    user.getState(),
                    user.getZipCode(),
                    user.getPhoneNumber(),
                    user.getSsn(),
                    userName
            );

            Assert.assertEquals(rows, 1, "Insert failed");
            ExtentReportUtil.logInfo(test,"User created successfully: " + userName);
            ExtentReportUtil.logInfo(test,"user details Are: ");
            ExtentReportUtil.logInfo(test,"ID: " + user.getId() + ", " +
                    "FirstName: " + user.getFirstName() + ", " +
                    "LastName: " + user.getLastName() + ", " +
                    "Address: " + user.getAddress() + ", " +
                    "City: " + user.getCity() + ", " +
                    "State: " + user.getState() + ", " +
                    "ZipCode: " + user.getZipCode() + ", " +
                    "PhoneNumber: " + user.getPhoneNumber() + ", " +
                    "SSN: " + user.getSsn() + ", " +
                    "UserName: " + user.getUserName());
            ExtentReportUtil.logPass(test,"User Creation is successfully completed");
        } catch (Exception | AssertionError e) {
            ExtentReportUtil.logFail(test,"User creation failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    // ---------------- READ ALL USERS ----------------

    @Test(priority = 3)
    public void readAllUsers() {
        test= ExtentTestManager.startNode(tests,"Reading all users from DB");

        try {
            List<String> userNames = userDao.getAllUserNames();
            Assert.assertFalse(userNames.isEmpty(), "No users found in DB");

            ExtentReportUtil.logInfo(test,"Users found: \n" + String.join("\n", userNames));

            userName = userNames.get(
                    ThreadLocalRandom.current().nextInt(userNames.size())
            );

            ExtentReportUtil.logInfo(test,"Random user selected: " + userName);
            ExtentReportUtil.logPass(test,"Reading all user is successfully completed");
        } catch (Exception | AssertionError e) {
            ExtentReportUtil.logFail(test,"Reading users failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    // ---------------- READ SINGLE USER BY USER NAME----------------

    @Test(priority = 4)
    public void readUserByName() {
        test= ExtentTestManager.startNode(tests,"Reading user by userName");


        try {
            Assert.assertNotNull(userName, "Username is null from previous test");
            ExtentReportUtil.logInfo(test,"Random user selected: " + userName);
            Assert.assertTrue(userDao.isUserExists(userName));
            ExtentReportUtil.logInfo(test,userName+" is exists");

            User user =userDao.getUserByUserName(userName);

            ExtentReportUtil.logInfo(test,"ID: " + user.getId() + ", " +
                    "FirstName: " + user.getFirstName() + ", " +
                    "LastName: " + user.getLastName() + ", " +
                    "Address: " + user.getAddress() + ", " +
                    "City: " + user.getCity() + ", " +
                    "State: " + user.getState() + ", " +
                    "ZipCode: " + user.getZipCode() + ", " +
                    "PhoneNumber: " + user.getPhoneNumber() + ", " +
                    "SSN: " + user.getSsn() + ", " +
                    "UserName: " + user.getUserName());
            ExtentReportUtil.logPass(test,"Reading user by User Name is successfully completed");

        } catch (Exception | AssertionError e) {
            ExtentReportUtil.logFail(test,"User read failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    // ---------------- READ SINGLE USER BY ID----------------

    @Test(priority = 5)
    public void readUserById() {
        test= ExtentTestManager.startNode(tests,"Reading user by ID");


        try {
            // Step 1: Read all IDs
            List<Integer> ids = userDao.getAllUserIds();
            Assert.assertFalse(ids.isEmpty(), "No IDs found in DB");
            StringBuilder allId = new StringBuilder();
            for (Integer id : ids) {
                allId.append(id).append("\n");
            }
            ExtentReportUtil.logInfo(test,"User IDs in DB: " +  allId);

            // Step 2: Pick a random ID
            Random random = new Random();
            int randomId = ids.get(random.nextInt(ids.size()));
            ExtentReportUtil.logInfo(test,"Randomly selected user ID: " + randomId);

            // Step 3: Fetch user by ID
            User user = userDao.getUserById(randomId);
            Assert.assertNotNull(user, "User not found for ID: " + randomId);

            // Step 4: Log user details
            ExtentReportUtil.logInfo(test,"ID: " + user.getId() + ", " +
                    "FirstName: " + user.getFirstName() + ", " +
                    "LastName: " + user.getLastName() + ", " +
                    "Address: " + user.getAddress() + ", " +
                    "City: " + user.getCity() + ", " +
                    "State: " + user.getState() + ", " +
                    "ZipCode: " + user.getZipCode() + ", " +
                    "PhoneNumber: " + user.getPhoneNumber() + ", " +
                    "SSN: " + user.getSsn() + ", " +
                    "UserName: " + user.getUserName());

            ExtentReportUtil.logPass(test,"Reading user by ID successfully completed");

        } catch (Exception | AssertionError e) {
            
            ExtentReportUtil.logFail(test,"User read by ID failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }


    // ---------------- UPDATE USER ----------------

    @Test(priority = 6)
    public void updateUserAddress() {
        test= ExtentTestManager.startNode(tests,"Updating user address");

        try {
            List<String> userNames = userDao.getAllUserNames();
            userName = userNames.get(
                    ThreadLocalRandom.current().nextInt(userNames.size())
            );
            Assert.assertNotNull(userName, "Username is null");
            ExtentReportUtil.logInfo(test,"user name to be updated is: "+userName);

            String newStreet = TestDataGenerator.randomStreet();
            String newCity = TestDataGenerator.randomCity();
            String newState = TestDataGenerator.randomState();
            String newZip = TestDataGenerator.randomZipCode();

            ExtentReportUtil.logInfo(test,"updating address of user: "+userName);
            ExtentReportUtil.logInfo(test,"Data to be updated");
            ExtentReportUtil.logInfo(test,
                    "Street: " + newStreet + "\n" +
                            "City: " + newCity + "\n" +
                            "State: " + newState + "\n" +
                            "ZipCode: " + newZip
            );
            int rows = userDao.updateAddress(newStreet, newCity, newState, newZip, userName);

            User updatedUser = userDao.getUserByUserName(userName);
            Assert.assertNotNull(updatedUser, "Updated user not found");
            ExtentReportUtil.logInfo(test,
                    "ID: " + updatedUser.getId() + "\n" +
                            "FirstName: " + updatedUser.getFirstName() + "\n" +
                            "LastName: " + updatedUser.getLastName() + "\n" +
                            "Address: " + updatedUser.getAddress() + "\n" +
                            "City: " + updatedUser.getCity() + "\n" +
                            "State: " + updatedUser.getState() + "\n" +
                            "ZipCode: " + updatedUser.getZipCode() + "\n" +
                            "PhoneNumber: " + updatedUser.getPhoneNumber() + "\n" +
                            "SSN: " + updatedUser.getSsn() + "\n" +
                            "UserName: " + updatedUser.getUserName()
            );
            Assert.assertEquals(rows, 1);
            ExtentReportUtil.logPass(test,"User address updated for: " + userName);

        } catch (Exception | AssertionError e) {
            
            ExtentReportUtil.logFail(test,"Update failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    // ---------------- PATCH USER ----------------

    @Test(priority = 7)
    public void patchUserPhone() {
        test= ExtentTestManager.startNode(tests,"Patching user phone number");

        try {
            // Step 1: Check userName
            Assert.assertNotNull(userName, "Username is null");
            ExtentReportUtil.logInfo(test,"User selected for phone patch: " + userName);

            // Step 2: Generate new phone number
            String newPhone = TestDataGenerator.randomPhoneNumber();

            ExtentReportUtil.logInfo(test,"patch operation for user: "+userName);
            ExtentReportUtil.logInfo(test,"Data to be patched with Phone");
            ExtentReportUtil.logInfo(test,"New Phone Number: " + newPhone);

            // Step 3: Perform patch
            int rows = userDao.patchPhoneNumber(newPhone, userName);
            Assert.assertEquals(rows, 1, "Patch affected unexpected number of rows");

            // Step 4: Fetch updated user details
            User updatedUser = userDao.getUserByUserName(userName);
            Assert.assertNotNull(updatedUser, "Updated user not found");

            // Step 5: Log updated user details
            ExtentReportUtil.logInfo(test,"Updated User Details After Phone Patch");
            ExtentReportUtil.logInfo(test,
                    "ID: " + updatedUser.getId() + "\n" +
                            "FirstName: " + updatedUser.getFirstName() + "\n" +
                            "LastName: " + updatedUser.getLastName() + "\n" +
                            "Address: " + updatedUser.getAddress() + "\n" +
                            "City: " + updatedUser.getCity() + "\n" +
                            "State: " + updatedUser.getState() + "\n" +
                            "ZipCode: " + updatedUser.getZipCode() + "\n" +
                            "PhoneNumber: " + updatedUser.getPhoneNumber() + "\n" +
                            "SSN: " + updatedUser.getSsn() + "\n" +
                            "UserName: " + updatedUser.getUserName()
            );

            ExtentReportUtil.logPass(test,"User phone patched successfully for: " + userName);

        } catch (Exception | AssertionError e) {
            
            ExtentReportUtil.logFail(test,"Patch failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }


    // ---------------- DELETE USER ----------------

    @Test(priority = 8)
    public void deleteUser() {
        test= ExtentTestManager.startNode(tests,"Deleting a user from DB");

        try {
            List<String> userNames = userDao.getAllUserNames();
            userName = userNames.get(
                    ThreadLocalRandom.current().nextInt(userNames.size())
            );
            Assert.assertNotNull(userName, "Username is null");
            ExtentReportUtil.logInfo(test,"user name to be updated is: "+userName);

            int rows = userDao.deleteUser(userName);
            Assert.assertEquals(rows, 1);

            ExtentReportUtil.logPass(test,"User deleted successfully: " + userName);

        } catch (Exception | AssertionError e) {
            
            ExtentReportUtil.logFail(test,"Delete failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }
}
