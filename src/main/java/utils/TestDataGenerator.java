package utils;


import java.util.Random;
import java.util.UUID;

public class TestDataGenerator {

    private static final Random random = new Random();

    /* ---------------- First Name ---------------- */
    public static String randomFirstName() {
        String[] firstNames = {"John", "Alex", "Chris", "David", "Emma", "Sophia", "Liam", "Olivia"};
        return firstNames[random.nextInt(firstNames.length)];
    }

    /* ---------------- Last Name ---------------- */
    public static String randomLastName() {
        String[] lastNames = {"Smith", "Johnson", "Brown", "Taylor", "Anderson", "Thomas"};
        return lastNames[random.nextInt(lastNames.length)];
    }

    /* ---------------- Address ---------------- */
    public static String randomStreet() {
        int number = 100 + random.nextInt(900);
        String[] streets = {"Main St", "Park Ave", "Oak St", "Maple Rd", "Elm St"};
        return number + " " + streets[random.nextInt(streets.length)];
    }

    public static String randomCity() {
        String[] cities = {"New York", "Dallas", "Austin", "Seattle", "Boston", "Denver"};
        return cities[random.nextInt(cities.length)];
    }

    public static String randomState() {
        String[] states = {"TX", "NY", "CA", "FL", "WA", "IL"};
        return states[random.nextInt(states.length)];
    }

    public static String randomZipCode() {
        return String.valueOf(10000 + random.nextInt(90000));
    }

    /* ---------------- Phone Number ---------------- */
    public static String randomPhoneNumber() {
        // 10-digit test-safe phone number
        return "9" + (100000000 + random.nextInt(900000000));
    }

    /* ---------------- SSN (Fake/Test) ---------------- */
    public static String randomSSN() {
        // Safe test SSN format (not real)
        int part1 = 100 + random.nextInt(899);
        int part2 = 10 + random.nextInt(89);
        int part3 = 1000 + random.nextInt(8999);
        return part1 + "-" + part2 + "-" + part3;
    }

    /* ---------------- Username ---------------- */
    public static String randomUsername() {
        return "user_" + UUID.randomUUID().toString().substring(0, 5);
    }
    public static String randomUsernameWithFullName(String firstName,String lastName) {
        if (firstName == null || firstName.isEmpty()) firstName = "FN";
        if (lastName == null || lastName.isEmpty()) lastName = "LN";

        String prefix = "user_";

        // Max length we can use for firstName+lastName
        int maxNameLength = 12 - prefix.length(); // 12 - 5 = 7 chars for names

        // Random lengths for firstName and lastName parts
        int firstPartLength = 1 + random.nextInt(Math.min(firstName.length(), maxNameLength));
        int remainingLength = maxNameLength - firstPartLength;
        int lastPartLength = 1 + random.nextInt(Math.min(lastName.length(), remainingLength));

        String firstPart = firstName.substring(0, firstPartLength);
        String lastPart = lastName.substring(0, lastPartLength);

        String username = prefix + firstPart + lastPart;

        // If still less than 12, append random digits
        while (username.length() < 12) {
            username += random.nextInt(10); // append a single digit
        }

        // if it exceeds 12, trim it
        if (username.length() > 12) {
            username = username.substring(0, 12);
        }
        return username.toLowerCase();
    }
}
