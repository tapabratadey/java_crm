package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import db.DBConnection;
import javafx.stage.Stage;

import java.sql.*;

public class Login implements Initializable {
    public Label welcomeMsg;
    public TextField usernameText;
    public TextField passwordText;
    public Label usernameTextLabel;
    public Label passwordTextLabel;
    public String loggedUser;
    public Button loginButton;
    public Label loginHeader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle userLang;
        Locale currSystem = Locale.getDefault();
        userLang = ResourceBundle.getBundle("resourceBundle", currSystem);
        welcomeMsg.setText(userLang.getString("welcomeMsg"));
        usernameTextLabel.setText(userLang.getString("username"));
        passwordTextLabel.setText(userLang.getString("password"));
        loginButton.setText(userLang.getString("loginButton"));
        loginHeader.setText(userLang.getString("loginHeader"));
    }
    public void loginButton(ActionEvent actionEvent) throws IOException {
        String username = usernameText.getText();
        String password = passwordText.getText();
        boolean isLogged = tryLogin(username, password);
        if (isLogged){
            loggedUser = username;
            System.out.println("User: " + loggedUser + " logged in.");
            /**
            * AddAppointment newUser = new AddAppointmnent();
            * newUser.setUserId(loggedUser);
            */
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Dashboard.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Dashboard");
            stage.setScene(scene);
            stage.show();
            userLogInTracker(loggedUser, true);
        }else{
            System.out.println("login failed");
            if (Locale.getDefault().toString().equals("en_US")){
                Alert alertUser = new Alert(Alert.AlertType.ERROR, "Wrong username or password.");
                alertUser.showAndWait();
                userLogInTracker(username, false);
            }
            if (Locale.getDefault().toString().equals("en_FR")){
                Alert alertUser = new Alert(Alert.AlertType.ERROR, "Mauvais nom d'utilisateur ou mot de passe.");
                alertUser.showAndWait();
                userLogInTracker(username, false);
            }
        }
    }
    public static Boolean tryLogin(String username, String password){
        try{
            String sql = "select * from users";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getString("User_Name").equals(username) &&
                        rs.getString("Password").equals(password)){
                    return true;
                }
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
        return false;
    }
    public String getLoggedUser(){
        return loggedUser;
    }
    public static void userLogInTracker(String user, boolean isLogged){
        try{
            FileWriter fileToWrite = new FileWriter("login_activity.txt", true);
            PrintWriter writeToFile = new PrintWriter(fileToWrite);
            LocalDateTime timeAtLoc = LocalDateTime.now();
            if (isLogged){ writeToFile.println(user + " logged in at: " + timeAtLoc); }
            if (!isLogged){ writeToFile.println(user + " login failed at: " + timeAtLoc); }
            writeToFile.close();
            System.out.println(user + ": login attempt is recorded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}