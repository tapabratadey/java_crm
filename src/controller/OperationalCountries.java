package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Retrieves customers in operational countries. US, UK, Canada.
 */
public class OperationalCountries implements Initializable {
    public Label UKLabel;
    public Label USLabel;
    public Label CALabel;

    /**
     * Logs out the user
     * @param actionEvent
     * @throws IOException
     */
    // log out button redirecting to the login screen
    public void logOutButton(ActionEvent actionEvent) throws IOException {
        Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?");
        Optional<ButtonType> optButton = alertUser.showAndWait();
        if (optButton.isPresent() && optButton.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Login.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Takes the user back to the Reports Dashboard
     * @param actionEvent
     * @throws IOException
     */
    // Reports dashboard button
    public void dashboardButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Reports.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Queries the users in each country
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUSCustomers();
        getUKCustomers();
        getCanadaCustomers();
    }
    public void getUSCustomers(){
        try {
            String sql = "select \n" +
                    "customers.Customer_Name \n" +
                    "from \n" +
                    "customers, \n" +
                    "first_level_divisions \n" +
                    "where \n" +
                    "customers.Division_ID = first_level_divisions.Division_ID \n" +
                    "and first_level_divisions.Country_ID = '1'";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder str = new StringBuilder();

            while(rs.next()){
                str.append(String.format("%1$-10s \n",
                        rs.getString("Customer_Name")));
            }
            USLabel.setText(str.toString());
        }
        catch(SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        }
    }
    public void getUKCustomers(){
        try {
            String sql = "select \n" +
                    "customers.Customer_Name \n" +
                    "from \n" +
                    "customers, \n" +
                    "first_level_divisions \n" +
                    "where \n" +
                    "customers.Division_ID = first_level_divisions.Division_ID \n" +
                    "and first_level_divisions.Country_ID = '2'";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder str = new StringBuilder();

            while(rs.next()){
                str.append(String.format("%1$-10s \n",
                        rs.getString("Customer_Name")));
            }
            UKLabel.setText(str.toString());
        }
        catch(SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        }
    }
    public void getCanadaCustomers(){
        try {
            String sql = "select \n" +
                    "customers.Customer_Name \n" +
                    "from \n" +
                    "customers, \n" +
                    "first_level_divisions \n" +
                    "where \n" +
                    "customers.Division_ID = first_level_divisions.Division_ID \n" +
                    "and first_level_divisions.Country_ID = '3'";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder str = new StringBuilder();

            while(rs.next()){
                str.append(String.format("%1$-10s \n",
                        rs.getString("Customer_Name")));
            }
            CALabel.setText(str.toString());
        }
        catch(SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        }
    }
}
