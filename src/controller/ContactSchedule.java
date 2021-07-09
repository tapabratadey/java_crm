package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Shows Schedule of Contacts
 */

public class ContactSchedule implements Initializable {
    public Button logOutButtonText;
    public Button scheduleAppointmentText;
    public Button editAppointment;
    public Button dashboardText;
    public TableView appointmentTable;
    public TableColumn appointmentId;
    public TableColumn appointmentTitle;
    public TableColumn appointmentDescription;
    public TableColumn appointmentLocation;
    public TableColumn appointmentContact;
    public TableColumn appointmentType;
    public TableColumn appointmentStartTime;
    public TableColumn appointmentEndTime;
    public TableColumn appointmentCustomerId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTable.setItems(getContactSchedule());
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentStartTime.setCellValueFactory(new PropertyValueFactory<>("appointmentStartTime"));
        appointmentEndTime.setCellValueFactory(new PropertyValueFactory<>("appointmentEndTime"));
        appointmentCustomerId.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerId"));
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

    // Reports dashboard button
    public void dashboardButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Reports.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    // Queries Schedule of Contacts
    public ObservableList getContactSchedule(){
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        try{
            String sql = "select \n" +
                    "contacts.Contact_Name, \n" +
                    "Appointment_Id, \n" +
                    "Title, \n" +
                    "Type, \n" +
                    "Description, \n" +
                    "Start, \n" +
                    "End, \n" +
                    "users.User_ID, \n" +
                    "customers.Customer_ID \n" +
                    "from appointments, \n" +
                    "customers, \n" +
                    "users, \n" +
                    "contacts \n" +
                    "where \n" +
                    "appointments.Customer_ID = customers.Customer_ID and\n" +
                    "appointments.Contact_ID = contacts.Contact_ID and\n" +
                    "appointments.User_ID = users.User_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Appointment appointment = new Appointment();
                appointment.setAppointmentContact(rs.getString("Contact_Name"));
                appointment.setId(rs.getInt("Appointment_Id"));
                appointment.setTitle(rs.getString("Title"));
                appointment.setAppointmentType(rs.getString("Type"));
                appointment.setAppointmentDescription(rs.getString("Description"));
                appointment.setAppointmentStartTime(rs.getTimestamp("Start"));
                appointment.setAppointmentEndTime(rs.getTimestamp("End"));
                appointment.setAppointmentCustomerId(rs.getInt("Customer_ID"));
                appointmentsList.add(appointment);
            }
        }
        catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        return appointmentsList;
    }
}
