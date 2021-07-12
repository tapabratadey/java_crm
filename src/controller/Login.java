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
import java.time.*;
import java.sql.*;

/**
 * Login Screen
 */

public class Login implements Initializable {
    public Label welcomeMsg;
    public TextField usernameText;
    public TextField passwordText;
    public Label usernameTextLabel;
    public Label passwordTextLabel;
    public String loggedUser;
    public Button loginButton;
    public Label loginHeader;
    public Label getZone;
    public Label zoneLabel;

    /**
     * Initializes Login fields and sets up the ResourceBundle package
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Locale.setDefault(Locale.FRENCH);
        ResourceBundle userLang;
        Locale currSystem = Locale.getDefault();
        userLang = ResourceBundle.getBundle("resourceBundle", currSystem);
        welcomeMsg.setText(userLang.getString("welcomeMsg"));
        usernameTextLabel.setText(userLang.getString("username"));
        passwordTextLabel.setText(userLang.getString("password"));
        loginButton.setText(userLang.getString("loginButton"));
        loginHeader.setText(userLang.getString("loginHeader"));
        zoneLabel.setText(userLang.getString("zone"));
        getZone.setText(findZone());
    }

    /**
     * Finds the zone of the user during login
     * @return zone in string
     */
    // finds the zone of the user during login
    private String findZone() {
        String location;
        ZoneId zone = ZoneId.systemDefault();
        return zone.toString();
    }

    /**
     * If authenticated -> takes the user to the dashboard
     * else -> err
     * @param actionEvent
     * @throws IOException
     */
    //login button functionality
    public void loginButton(ActionEvent actionEvent) throws IOException {

        String username = usernameText.getText();
        String password = passwordText.getText();
        boolean isLogged = tryLogin(username, password);
        if (isLogged){
            loggedUser = username;
            System.out.println("User: " + loggedUser + " logged in.");
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
            if (Locale.getDefault().toString().equals("fr")){
                System.out.println("kasdf");
                Alert alertUser = new Alert(Alert.AlertType.ERROR, "Mauvais nom d'utilisateur ou mot de passe.");
                alertUser.showAndWait();
                userLogInTracker(username, false);
            }
        }
    }

    /**
     * Checks if the user credentials are valid or not
     * @param username
     * @param password
     * @return boolean
     */
    // checks for password during login
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

    /**
     * Returns the logged user
     * @return returns the logged user
     */
    //gets logged user to print
    public String getLoggedUser(){
        return loggedUser;
    }

    /**
     * Login Tracker
     * Adds Login Status Messages and writes them in a file + Terminal
     * @param user
     * @param isLogged
     */
    //log tracker writes on login_activity.txt
    public static void userLogInTracker(String user, boolean isLogged){
        try{
            FileWriter fileToWrite = new FileWriter("login_activity.txt", true);
            PrintWriter writeToFile = new PrintWriter(fileToWrite);
            LocalDateTime timeAtLoc = LocalDateTime.now();
            if (isLogged){ writeToFile.println(user + " logged in (success) at: " + timeAtLoc); }
            if (!isLogged){ writeToFile.println(user + " login failed at: " + timeAtLoc); }
            writeToFile.close();
            System.out.println(user + ": login attempt is recorded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
