package controller;
import db.DBConnection;
import dbAccess.DBAppointments;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


import java.sql.*;
import java.time.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


/**
 * Global Dashboard Screen
 */

public class Dashboard implements Initializable {
    public Button logOutButtonText;
    public Button viewAppointmentsText;
    public Button viewReportsText;
    public Button viewCustomersText;
    public Label dashboardHeader;
    public Label appointmentAlert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
          setAppointmentAlert();
    }

    // button redirecting to the appointment screen
    public void viewAppointmentButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Appointments.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    // button redirecting to the report screen
    public void viewReportsButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Reports.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    // button redirecting to the customer screen
    public void viewCustomersButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Customers.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }

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
    //displays upcoming appointments in the next 15 minutes.
    //appointment ID, date, and time
    public void setAppointmentAlert() {
        try {
            String sql = "select \n" +
                    "Appointment_Id, \n" +
                    "Start \n" +
                    "from \n" +
                    "appointments, \n" +
                    "customers \n" +
                    "where \n" +
                    "appointments.Customer_ID = customers.Customer_ID and\n" +
                    "date(start) = curdate()";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            LocalDateTime timeNow = LocalDateTime.now();
            if (rs.next() == false){
                appointmentAlert.setText("No upcoming appointments.");
            }
            while (rs.next()) {
                String customer = rs.getString("Appointment_Id");
                Timestamp start = rs.getTimestamp("Start");


                System.out.println("customer");
                //appointment start time conversion
                DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime startLocalDT = start.toLocalDateTime();
                ZonedDateTime startZoneDT = startLocalDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
                ZonedDateTime zoneUTC = startZoneDT.withZoneSameInstant(ZoneId.systemDefault());
                String startZoneTime = startZoneDT.toLocalDateTime().format(newFormat);

                DateTimeFormatter format = DateTimeFormatter.ofPattern("kk:mm");
                LocalTime appointmentLocalTime = LocalTime.parse(zoneUTC.toString().substring(11,16), format);
                String appointmentTime = appointmentLocalTime.toString();

                //Current System Time Conversion
                ZonedDateTime startZoneDT2 = timeNow.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
                ZonedDateTime zoneUTC2 = startZoneDT2.withZoneSameInstant(ZoneId.systemDefault());
                LocalTime appointmentLocalTime2 = LocalTime.parse(zoneUTC2.toString().substring(11, 16), format);

                long diff = ChronoUnit.MINUTES.between(appointmentLocalTime2, appointmentLocalTime);
                if (diff > 0 && diff <= 15){
                    String string = String.format("Appointment at %s with %s", startZoneTime, customer);
                    appointmentAlert.setText(string);
                }
                else{
                    appointmentAlert.setText("No upcoming appointments.");
                }
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}
