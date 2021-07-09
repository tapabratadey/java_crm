package controller;

import dbAccess.DBCustomer;
import javafx.collections.ObservableList;
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
import model.Customer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.io.IOException;
import java.net.URL;
import java.util.*;


/**
 * Screen to view Appointments
 */

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
    public RadioButton monthlyView;
    public RadioButton weeklyView;
    public RadioButton allView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAppointments();
    }

    // sets up appointment table
    //lambda expression
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
        allView.setSelected(true);
    }

    // scheduling button
    public void scheduleAppointmentButton(ActionEvent actionEvent) throws IOException {
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/ScheduleAppointment.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Schedule Appointments");
        stage.setScene(scene);
        stage.show();
    }

    // appointment editing button
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

    // appointment delete button
    public void deleteAppointment(ActionEvent actionEvent) {
        Appointment appointmentSelected = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if (appointmentSelected == null){
            Alert alertUserErr = new Alert(Alert.AlertType.ERROR, "Please select an appointment to delete");
            alertUserErr.showAndWait();
        }else{
            Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? This action will cancel the " +
                    "appointment. Id: " +
                    appointmentSelected.getId() + " Type: " + appointmentSelected.getAppointmentType());
            Optional<ButtonType> optButton = alertUser.showAndWait();
            try{
                if (optButton.isPresent() && optButton.get() == ButtonType.OK) {
                    DBAppointments dbAppointment = new DBAppointments();
                    dbAppointment.deleteAppointment(appointmentSelected);
                    setupAppointments();
                }
            }catch(IndexOutOfBoundsException | NoSuchElementException err){
                System.out.println(err);
            }
        }
    }

    // dashboard button
    public void dashboardButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Dashboard.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    // logout button
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

    //monthly view toggle
    public void monthlyViewHandler(ActionEvent actionEvent) {
        allView.setSelected(false);
        weeklyView.setSelected(false);
        Calendar calendar = Calendar.getInstance();
        String getMonth = new SimpleDateFormat("MMMM").format(calendar.getTime());
        String getYear = new SimpleDateFormat("y").format(calendar.getTime());
        ObservableList<Appointment> appointmentsList = DBAppointments.getMonthlyAppointments(getMonth, getYear);
        if (appointmentsList.size() == 0){
            Alert alertUser = new Alert(Alert.AlertType.ERROR, "No appointments available this month.");
            Optional<ButtonType> optButton = alertUser.showAndWait();
        }else{
            appointmentTable.setItems(DBAppointments.getMonthlyAppointments(getMonth, getYear));
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
    }

    //weekly view toggle
    public void weeklyViewHandler(ActionEvent actionEvent) {
        allView.setSelected(false);
        monthlyView.setSelected(false);
        Calendar calendar = Calendar.getInstance();
        String getWeek = new SimpleDateFormat("w").format(calendar.getTime());
        String getYear = new SimpleDateFormat("y").format(calendar.getTime());
        ObservableList<Appointment> appointmentsList = DBAppointments.getWeeklyAppointments(getWeek, getYear);
        if (appointmentsList.size() == 0){
            Alert alertUser = new Alert(Alert.AlertType.ERROR, "No appointments available this week");
            Optional<ButtonType> optButton = alertUser.showAndWait();
        }else {
            appointmentTable.setItems(DBAppointments.getWeeklyAppointments(getWeek, getYear));
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
    }

    //all view toggle
    public void allViewHandler(ActionEvent actionEvent) {
        monthlyView.setSelected(false);
        weeklyView.setSelected(false);
        setupAppointments();
    }
}
