package controller;

import dbAccess.DBAppointments;
import dbAccess.DBContacts;
import dbAccess.DBCustomer;
import dbAccess.DBUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Screen to schedule appointment
 */

public class AddAppointments implements Initializable {

    public ComboBox userIdComboBox;
    public ComboBox customerIdComboBox;
    public ComboBox comboBoxContact;
    public TextField appointmentEndTime;
    public TextField appointmentStartTime;
    public TextField appointmentType;
    public TextField appointmentLocation;
    public TextField appointmentDescription;
    public TextField appointmentTitle;
    public TextField appointmentId;

    public static ObservableList<String> hours = FXCollections.observableArrayList();
    public static ObservableList<String> minutes = FXCollections.observableArrayList();
    public ComboBox endMinComboBox;
    public ComboBox startMinComboBox;
    public ComboBox endHourComboBox;
    public ComboBox startHourComboBox;
    public DatePicker datePicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTimeComboBox();
        setupContactComboBox();
        setupCustomerUserComboBox();
    }

    //sets up hours
    public void setupTimeComboBox(){
        hours.setAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.setAll("00", "15", "30", "45");
        startHourComboBox.setItems(hours);
        startMinComboBox.setItems(minutes);
        endHourComboBox.setItems(hours);
        endMinComboBox.setItems(minutes);
    }

    //sets up contact combo box
    public void setupContactComboBox(){
        comboBoxContact.setItems(DBContacts.getAllContacts());
        comboBoxContact.getSelectionModel().selectFirst();
    }

    //sets up user combo box
    public void setupCustomerUserComboBox(){
        userIdComboBox.setItems(DBUser.getAllUsers());
        customerIdComboBox.setItems(DBCustomer.getAllCustomerId());
    }

    //save button functionality
    public void appointmentSaveButton(ActionEvent actionEvent) throws IOException {
        String title = appointmentTitle.getText();
        String description = appointmentDescription.getText();
        String location = appointmentLocation.getText();
        String contact = (String) comboBoxContact.getValue();
        String type = appointmentType.getText();
        LocalDate date = datePicker.getValue();
        String startHourTime = (String) startHourComboBox.getValue();
        String endHourTime = (String) endHourComboBox.getValue();
        String startMinTime = (String) startMinComboBox.getValue();
        String endMinTime = (String) endMinComboBox.getValue();
        String customerId = (String) customerIdComboBox.getValue();
        String userId = (String) userIdComboBox.getValue();
        String errMsg = "";

        if(title == ""){ errMsg+="Missing title\n"; }
        if(description == ""){ errMsg+="Missing description\n";}
        if(location == "") {errMsg+="Missing location\n"; }
        if(contact == "") { errMsg+="Missing contact\n";}
        if(type == "") { errMsg+="Missing type\n"; }
        if(date == null){errMsg+= "Missing date\n";}
        if(startHourTime == null) {errMsg+= "Missing start hour\n"; }
        if(startMinTime == null){ errMsg+="Missing start minute\n";}
        if(endHourTime == null){errMsg+="Missing end hour\n";}
        if(endMinTime == null){ errMsg+="Missing end minute\n"; }
        if (errMsg.length() != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errMsg);
            alert.showAndWait();
        } else {
            int idCounter = 0;
            DBAppointments dbAppointment = new DBAppointments();
            for (Appointment appt:dbAppointment.getAppointments()){
                if (appt.getId() > idCounter){ idCounter = appt.getId(); }
            }
            appointmentId.setText(String.valueOf(++idCounter));

            //get LocalDateTime
            LocalDateTime startLocalDT = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                    Integer.parseInt(startHourTime), Integer.parseInt(startMinTime));
            LocalDateTime endLocalDT = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                    Integer.parseInt(endHourTime), Integer.parseInt(endMinTime));

            //get ZoneDateTime for EST
            ZonedDateTime startEST = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 8,0,
                    0,0, ZoneId.of("America/New_York"));
            ZonedDateTime endEST = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 22,0,
                    0,0, ZoneId.of("America/New_York"));

            //get ZDT for UTC
            ZonedDateTime startUTC = startEST.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endUTC = endEST.withZoneSameInstant(ZoneOffset.UTC);

            //get UTC to Local
            LocalTime utcToStartLocal = startUTC.toLocalDateTime().toLocalTime();
            LocalTime utcToEndLocal = endUTC.toLocalDateTime().toLocalTime();

            //get ZDT of LDT
            ZonedDateTime startZDT = ZonedDateTime.of(startLocalDT, ZoneId.systemDefault());
            ZonedDateTime endZDT = ZonedDateTime.of(endLocalDT, ZoneId.systemDefault());

            //get UTC ZDT of LDT
            ZonedDateTime startUTCZDT = startZDT.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endUTCZDT = endZDT.withZoneSameInstant(ZoneOffset.UTC);

            //get sql timestamp
            Timestamp startTS = Timestamp.from(Instant.from(startUTCZDT));
            Timestamp endTS = Timestamp.from(Instant.from(endUTCZDT));

            if (startUTCZDT.isBefore(startUTC) ||
                endUTCZDT.isAfter(endUTC)){
                Alert alertUser = new Alert(Alert.AlertType.ERROR, "Please choose a time slot between 8am and 10pm " +
                        "EST");
                alertUser.showAndWait();
            }else{
                DBAppointments dbAppt = new DBAppointments();
                Appointment appointment = new Appointment();
                appointment.setId(idCounter);
                appointment.setTitle(title);
                appointment.setAppointmentDescription(description);
                appointment.setAppointmentLocation(location);
                appointment.setAppointmentContact(contact);
                appointment.setAppointmentType(type);
                appointment.setAppointmentStartTime(startTS);
                appointment.setAppointmentEndTime(endTS);
                appointment.setAppointmentCustomerId(Integer.valueOf(customerId));
                appointment.setUserId(Integer.valueOf(userId));

                if(dbAppt.checkIfOverlap(appointment)){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "An appointment at the given time already exists.");
                    alert.showAndWait();
                }else{
                    dbAppt.addAppointment(appointment);
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Appointments.fxml")));
                    Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }
    }

    //cancel button functionality
    public void customerCancelButton(ActionEvent actionEvent) throws IOException {
        Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?");
        Optional<ButtonType> optButton = alertUser.showAndWait();
        if (optButton.isPresent() && optButton.get() == ButtonType.OK){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Appointments.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    //logout button functionality
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
