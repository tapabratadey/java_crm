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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Screen to edit appointments
 */

public class EditAppointment implements Initializable {
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
    Appointment appointmentToModify;
    int appointmentIdx;

    public static ObservableList<String> hours = FXCollections.observableArrayList();
    public static ObservableList<String> minutes = FXCollections.observableArrayList();
    public ComboBox endMinComboBox;
    public ComboBox startMinComboBox;
    public ComboBox endHourComboBox;
    public ComboBox startHourComboBox;
    public DatePicker datePicker;

    /**
     * Initializes the combo boxes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTimeComboBox();
        setupContactComboBox();
        setupCustomerUserComboBox();
    }

    /**
     * Sets up the time combo boxes
     */
    //sets up time combo box
    public void setupTimeComboBox(){
        hours.setAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.setAll("00", "15", "30", "45");
        startHourComboBox.setItems(hours);
        startMinComboBox.setItems(minutes);
        endHourComboBox.setItems(hours);
        endMinComboBox.setItems(minutes);
    }

    /**
     * Sets up the Contact Combo Box
     */
    // sets up contact combo box
    public void setupContactComboBox(){
        comboBoxContact.setItems(DBContacts.getAllContacts());
        comboBoxContact.getSelectionModel().selectFirst();
    }

    /**
     * Sets up the User and Customer Combo Boxes
     */
    // sets up customer combo box
    public void setupCustomerUserComboBox(){
        userIdComboBox.setItems(DBUser.getAllUsers());
        customerIdComboBox.setItems(DBCustomer.getAllCustomerId());
    }

    /**
     * Populates the Appointment form
     * @param appointment
     * @param idx
     */
    // edit appointment functionality
    public void editAppointment(Appointment appointment, int idx) {
        appointmentToModify = appointment;
        appointmentIdx = idx;
        appointmentId.setText(Integer.toString(appointment.getId()));
        appointmentTitle.setText(appointment.getTitle());
        appointmentDescription.setText(appointment.getAppointmentDescription());
        appointmentLocation.setText(appointment.getAppointmentLocation());
        comboBoxContact.setValue(appointment.getAppointmentContact());
        appointmentType.setText(appointment.getAppointmentType());
        Integer startHour = appointment.getAppointmentStartTime().toLocalDateTime().getHour();
        startHourComboBox.setValue(String.valueOf(startHour));
        Integer startMin = appointment.getAppointmentStartTime().toLocalDateTime().getMinute();
        startMinComboBox.setValue(String.valueOf(startMin));
        LocalDate startDate = appointment.getAppointmentStartTime().toLocalDateTime().toLocalDate();
        datePicker.setValue(startDate);
        Integer endHour = appointment.getAppointmentEndTime().toLocalDateTime().getHour();
        endHourComboBox.setValue(String.valueOf(endHour));
        Integer endMin = appointment.getAppointmentEndTime().toLocalDateTime().getMinute();
        endMinComboBox.setValue(String.valueOf(endMin));
        customerIdComboBox.setValue(appointment.getAppointmentCustomerId());
        userIdComboBox.setValue(appointment.getUserId());
    }

    /**
     * Saves the appointment
     * Grabs the data from the form
     * Error Checks -> Time Conversions -> Add to DB
     * @param actionEvent
     * @throws IOException
     */
    // appointment save button functionality
    public void appointmentSaveButton(ActionEvent actionEvent) throws IOException {
        int id = Integer.valueOf(appointmentId.getText());
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
        Integer customerId = (Integer) customerIdComboBox.getValue();
        Integer userId = (Integer) userIdComboBox.getValue();
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

            // Start Time
            //get LocalDateTime (User time)
            LocalDateTime startLocalDT = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                    Integer.parseInt(startHourTime), Integer.parseInt(startMinTime));
            ZonedDateTime starting = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 8,0,
                    0,0, ZoneId.of("America/New_York"));
            String User = startLocalDT.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            //=============================
            //      FIND USER'S ZONE
            //=============================

            LocalDateTime ldt = LocalDateTime.parse(User, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println(ldt);

            ZoneId UserZoneId = ZoneId.of(ZoneId.systemDefault().toString());
            ZonedDateTime UserZonedDateTime = ldt.atZone(UserZoneId);
            System.out.println("User Time: " + UserZonedDateTime);

            //=============================================
            // CONVERT USER'S ZONE TO UTC (ADD TO DATABASE)
            //=============================================

            ZoneId UTCZoneId = ZoneId.of("UTC");
            ZonedDateTime UTCZonedDateTime = UserZonedDateTime.withZoneSameInstant(UTCZoneId);
            System.out.println("Date (UTC) : " + UTCZonedDateTime);

            //====================================================
            // CONVERT USER'S ZONE TO EST (COMPARE BUSINESS HOURS)
            //====================================================

            ZoneId ESTZoneId = ZoneId.of("America/New_York");
            ZonedDateTime ESTDateTime = UTCZonedDateTime.withZoneSameInstant(ESTZoneId);
            System.out.println("Date (EST) : " + ESTDateTime);



            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.println("\n---DateTimeFormatter---");
            System.out.println("Date (User Zone) : " + format.format(UserZonedDateTime));
            System.out.println("Date (UTC) : " + format.format(UTCZonedDateTime));
            System.out.println("Date (EST) : " + format.format(ESTDateTime));

            // End Time
            //get LocalDateTime (User time)
            LocalDateTime endLocalDT = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                    Integer.parseInt(endHourTime), Integer.parseInt(endMinTime));
            ZonedDateTime closing = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 22,0,
                    0,0, ZoneId.of("America/New_York"));
            String UserEnd = endLocalDT.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            //=============================
            //      FIND USER'S ZONE
            //=============================

            LocalDateTime ldtEnd = LocalDateTime.parse(UserEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println(ldtEnd);

            ZoneId UserZoneIdEnd = ZoneId.of(ZoneId.systemDefault().toString());
            ZonedDateTime UserZonedDateTimeEnd = ldtEnd.atZone(UserZoneIdEnd);
            System.out.println("User Time: " + UserZonedDateTimeEnd);

            //=============================================
            // CONVERT USER'S ZONE TO UTC (ADD UTCZonedDateTimeEnd TO DATABASE)
            //=============================================

            ZoneId UTCZoneIdEnd = ZoneId.of("UTC");
            ZonedDateTime UTCZonedDateTimeEnd = UserZonedDateTimeEnd.withZoneSameInstant(UTCZoneIdEnd);
            System.out.println("Date (UTC) : " + UTCZonedDateTimeEnd);

            //====================================================
            // CONVERT USER'S ZONE TO EST (COMPARE userDateTimeEnd BUSINESS HOURS)
            //====================================================

            ZoneId ESTZoneIdEnd = ZoneId.of("America/New_York");
            ZonedDateTime ESTDateTimeEnd = UTCZonedDateTimeEnd.withZoneSameInstant(ESTZoneIdEnd);
            System.out.println("Date (EST) : " + ESTDateTimeEnd);

            System.out.println("\n---DateTimeFormatter---");
            System.out.println("Date (User Zone) : " + format.format(UserZonedDateTimeEnd));
            System.out.println("Date (UTC) : " + format.format(UTCZonedDateTimeEnd));
            System.out.println("Date (EST) : " + format.format(ESTDateTimeEnd));

            //====================================================
            // CONVERT TO ZDT TO TIMESTAMP
            //====================================================
            Timestamp startTimestamp = Timestamp.valueOf(format.format(UTCZonedDateTime));
            Timestamp endTimestamp = Timestamp.valueOf(format.format(UTCZonedDateTimeEnd));

            if (ESTDateTime.isBefore(starting) ||
                    ESTDateTimeEnd.isAfter(closing)){
                Alert alertUser = new Alert(Alert.AlertType.ERROR, "Please choose a time slot between 8am and 10pm " +
                        "EST");
                alertUser.showAndWait();
            }else{
                DBAppointments dbAppt = new DBAppointments();
                Appointment appointment = new Appointment();
                appointment.setId(id);
                appointment.setTitle(title);
                appointment.setAppointmentDescription(description);
                appointment.setAppointmentLocation(location);
                appointment.setAppointmentContact(contact);
                appointment.setAppointmentType(type);
                appointment.setAppointmentStartTime(startTimestamp);
                appointment.setAppointmentEndTime(endTimestamp);
                appointment.setAppointmentCustomerId(customerId);
                appointment.setUserId(userId);

                if(dbAppt.checkIfOverlap(appointment)){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "An appointment at the given time already exists.");
                    alert.showAndWait();
                }else{
                    dbAppt.updateAppointment(appointment);
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Appointments.fxml")));
                    Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }
    }

    /**
     * Cancels the Appointment Edit Action
     * @param actionEvent
     * @throws IOException
     */
    // cancel button functionality
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

    /**
     * Logs out an user
     * @param actionEvent
     * @throws IOException
     */
    // log out button functionality
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
