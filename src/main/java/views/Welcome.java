package views;

import dao.UserDAO;
import model.User;
import service.OTP;
import service.SendOTP;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void screen(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the app");
        System.out.println("Press 1 to login");
        System.out.println("Press 2 to Signup");
        System.out.println("Press 0 to exit");

        int choice = 0;

        try {
            choice = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (choice){
            case 1 -> login();
            case 2 -> signup();
            case 0 -> System.exit(0);
        }
    }

    private void signup() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name : ");
        String name = sc.nextLine();
        System.out.println("Enter your email : ");
        String email = sc.nextLine();

        String genOTP = OTP.getOTP();
        SendOTP.sendOTP(email,genOTP);
        System.out.println("Enter OTP : ");
        String otp = sc.nextLine();
        if (otp.equals(genOTP)){
            User user = new User(name, email);
            int res = UserService.saveUser(user);
            switch (res){
                case 0-> System.out.println("User registered");
                case 1-> System.out.println("User already exists");
            }
        }else{
            System.out.println("Wrong otp");
        }
    }

    private void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email :");
        String email = sc.nextLine();
        try {
            if (UserDAO.isExist(email)){
                String genOTP = OTP.getOTP();
                SendOTP.sendOTP(email,genOTP);
                System.out.println("Enter OTP : ");
                String otp = sc.nextLine();
                if (otp.equals(genOTP)){
                    new UserView(email).home();
                }else{
                    System.out.println("Wrong otp");
                }
            }else{
                System.out.println("User Not found !");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
