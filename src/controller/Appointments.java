package controller;

import model.Appointment;
import dbAccess.DBAppointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class Appointments implements Initializable {
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
        setupAppointments();
    }

    public void setupAppointments(){
        appointmentTable.setItems(DBAppointments.getAppointments());
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStartTime.setCellValueFactory(new PropertyValueFactory<>("appointmentStartTime"));
        appointmentEndTime.setCellValueFactory(new PropertyValueFactory<>("appointmentEndTime"));
        appointmentCustomerId.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerId"));
    }

    public void scheduleAppointmentButton(ActionEvent actionEvent) throws IOException {
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/ScheduleAppointment.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Schedule Appointments");
        stage.setScene(scene);
        stage.show();
    }

    public void editAppointment(ActionEvent actionEvent) throws IOException {
        Appointment appointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if (appointment == null){
            Alert alertUserErr = new Alert(Alert.AlertType.ERROR, "Please select an appointment to edit");
            alertUserErr.showAndWait();
        }else{
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/views/EditAppointment.fxml")));
            Parent root = (Parent) loader.load();
            EditAppointment editApp = loader.getController();
            int idx = appointmentTable.getSelectionModel().getSelectedIndex();
            editApp.editAppointment(appointment, idx);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void deleteAppointment(ActionEvent actionEvent) {
    }

    public void dashboardButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Dashboard.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

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
}
