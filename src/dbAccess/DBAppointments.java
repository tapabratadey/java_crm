package dbAccess;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DBAppointments {

    public static ObservableList<Appointment> getAppointments(){
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        try{
            String sql = "select \n" +
                    "Appointment_Id, \n" +
                    "Title, \n" +
                    "Description, \n" +
                    "Location, \n" +
                    "Type, \n" +
                    "Start, \n" +
                    "End, \n" +
                    "customers.Customer_ID, \n" +
                    "users.User_ID, \n" +
                    "contacts.Contact_ID, \n" +
                    "contacts.Contact_Name \n" +
                    "from appointments, \n" +
                    "customers, \n" +
                    "users, \n" +
                    "contacts \n" +
                    "where \n" +
                    "appointments.Customer_ID = customers.Customer_ID and\n" +
                    "appointments.User_ID = users.User_ID\n" +
                    "and \n" +
                    "appointments.Contact_ID = contacts.Contact_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("Appointment_Id"));
                appointment.setTitle(rs.getString("Title"));
                appointment.setAppointmentDescription(rs.getString("Description"));
                appointment.setAppointmentLocation(rs.getString("Location"));
                appointment.setAppointmentType(rs.getString("Type"));
                appointment.setAppointmentStartTime(rs.getTimestamp("Start"));
                appointment.setAppointmentEndTime(rs.getTimestamp("End"));
                appointment.setAppointmentCustomerId(rs.getInt("Customer_ID"));
                appointment.setUserId(rs.getInt("User_ID"));
                appointment.setContactId(rs.getInt("Contact_ID"));
                appointment.setAppointmentContact(rs.getString("Contact_Name"));
                Timestamp startTime = rs.getTimestamp("start");
                Timestamp endTime = rs.getTimestamp("end");
                //time conversions
                DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime startLocalDT = startTime.toLocalDateTime();
                ZonedDateTime startZoneDT = startLocalDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
                String startZoneTime = startZoneDT.toLocalDateTime().format(newFormat);
                appointment.setStartZone(startZoneTime);
                LocalDateTime endLocalDT = endTime.toLocalDateTime();
                ZonedDateTime endZoneDT = endLocalDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
                String endZoneTime = endZoneDT.toLocalDateTime().format(newFormat);
                appointment.setEndZone(endZoneTime);

                //add to list
//                appointmentsList.add(String.valueOf(appointment));
                appointmentsList.add(appointment);
            }
        }catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return appointmentsList;
    }
}
