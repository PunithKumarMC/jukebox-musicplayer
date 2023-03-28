package Impl;

import CustomExceptionhandling.UserException;
import Operations.DefaultPage;
import Operations.UserCreation;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class MainImpl {
    public static void main(String[] args) throws SQLException, UserException, UnsupportedAudioFileException, LineUnavailableException, IOException, ClassNotFoundException {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wynkdb", "root", "Punith@9535");
        } catch (SQLException se) {
            se.printStackTrace();
        }
        MainImpl.supportMain(con);
    }

    public static void supportMain(Connection con) {
        try {
            Scanner sc = new Scanner(System.in);
            //Operations.DefaultPage dp=new Operations.DefaultPage();   created static
            System.out.println("Hi Welcome to the modern jukebox Stop-Listen-Go");
            System.out.println("Choose your choice what you want to do today with the Jukebox");
            System.out.println("1.SignUp(create a account to enjoy the jukebox \n2.SignIn(If you are the existing user) \n3.exit (Exit from the Jukebox app))");
            System.out.println("Enter your choice");
            int userchoice = sc.nextInt();
            UserCreation uc = new UserCreation();
            switch (userchoice) {
                case 1:
                    System.out.println("Please enter all details to create a new account");
                    int count = uc.usercreation(con);
                    if (count == 1) {
                        System.out.println("User Account Created Successfully!!!");
                        DefaultPage.homePage();
                    } else {
                        System.out.println("User Account Not  Created Successfully");
                    }
                    break;
                case 2:
                    System.out.println("Login with your credentials");
                    boolean value = uc.accountLogin(con);
                    if (value) {
                        System.out.println("Successfully Login to your Account!!! ");
                        DefaultPage.homePage();
                    }
                    break;
                case 3:
                    System.out.println("please wait exiting from the Jukebox...");
                    System.out.println("Thank you for using Jukebox, Please visit again!!!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice please choose the correct option");
                    System.out.println("Please re enter your option and details");
                    MainImpl.supportMain(con);
            }
        } catch (SQLException | UserException | UnsupportedAudioFileException | LineUnavailableException | IOException |
                 ClassNotFoundException se) {
            se.printStackTrace();
        }
    }
}
