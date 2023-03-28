package Operations;

import CustomExceptionhandling.UserException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserCreation {
    Scanner sc;

    public UserCreation() {
        sc = new Scanner(System.in);
    }

    public int usercreation(Connection con) {
        int count = 0;
        try {
            System.out.println("Please enter your username");
            String userName = sc.nextLine();
            System.out.println("Please enter your Email");
            String userEmail = sc.nextLine();
            System.out.println("Please enter your Phone Number");
            String userPhNo1 = sc.nextLine();
            String userPassword = null;
            boolean bl = false;
            do {
                System.out.println("Please enter your password which should be Minimum 8 and maximum 20 characters, \nat least one uppercase letter, \none lowercase letter, \none number \none special character\nNo whiteSpace is allowed");
                userPassword = sc.nextLine();
                bl = Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}", userPassword);
            } while (!bl);

            String query = "INSERT INTO userDetails (userName,userEmail,userPhNo,userPassword) VALUES(?,?,?,?)";
            PreparedStatement pdstmt = con.prepareStatement(query);             // doubt try caytch shiold be wriiten or method signature is enough
            pdstmt.setString(1, userName);
            pdstmt.setString(2, userEmail);
            pdstmt.setString(3, userPhNo1);
            pdstmt.setString(4, userPassword);
            count = pdstmt.executeUpdate();
            if (count == 0) {
                System.out.println("Account not created Suuccessfully");
            } else {
                String query1 = "SELECT userID,userPhNo FROM userdetails";
                Statement stmt = con.createStatement();
                ResultSet rs1 = stmt.executeQuery(query1);
                while (rs1.next()) {
                    int id = rs1.getInt("userID");
                    String phNo = rs1.getString("userPhNo");
                    if (phNo.equalsIgnoreCase(userPhNo1)) {
                        System.out.println("" + id + " is your UserID, Please be remembered");
                    }
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return count;
    }

    public boolean accountLogin(Connection con) {
        boolean value = false;
        try {
            System.out.println("Enter the userName");
            String userName = sc.nextLine();
            System.out.println("Enter the password given at the time of SignIn");
            String userPassword = sc.nextLine();
            String query = "SELECT userName,userPassword FROM userDetails";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String dbPassword = null;
            String dbName = null;
            while (rs.next()) {
                dbName = rs.getString("userName");
                dbPassword = rs.getString("userPassword");
                if (userPassword.equals(dbPassword) && userName.equalsIgnoreCase(dbName)) {
                    String query1 = "SELECT userID FROM userdetails WHERE userPassword=?";
                    PreparedStatement pstmt = con.prepareStatement(query1);
                    pstmt.setString(1, userPassword);
                    ResultSet rs1 = pstmt.executeQuery();
                    while (rs1.next()) {
                        System.out.println("" + rs1.getInt(1) + "is your UserID, Please be remembered");
                    }
                    value = true;
                    break;
                }
            }

            if (userPassword.equals(dbPassword)) {
                value = true;
                DefaultPage.homePage();
            } else {
                throw new UserException("Please enter the correct password and user Name");

            }
        } catch (SQLException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        } catch (UserException e) {
            System.out.println(e);
            accountLogin(con);
        }
        return value;
    }

    //test case(to check the given password)
    public boolean validatePassword(String str) {
        if (str != null && str.length() > 0) {
            //Pattern patern = Pattern.compile("[a-zA-z][a-zA-z]*[0-9]*_*");
            //Matcher matches=patern.matches(str);
            String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}";
            return str.matches(pattern);
        }
        return false;
    }

}
