package dbAccess;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Database queries related to Appointments
 */

public class DBAppointments {
    // queries all appointments
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

                // Formatting to UTC
                DateFormat UTCFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                String UTCString = UTCFormat.format(startTime);
                LocalDateTime ldt = LocalDateTime.parse(UTCString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                ZoneId UTCZoneId = ZoneId.of("UTC");
                ZonedDateTime UTCZonedDateTime = ldt.atZone(UTCZoneId);

                // Formatting to User Zone
                ZoneId UserZoneId = ZoneId.of(ZoneId.systemDefault().toString());
                ZonedDateTime userDateTime = UTCZonedDateTime.withZoneSameInstant(UserZoneId);

                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss");
                String startZoneTime = format.format(userDateTime);
                appointment.setStartZone(startZoneTime);

                //end time
                // Formatting to UTC
                String UTCEndString = UTCFormat.format(endTime);
                LocalDateTime ldtEnd = LocalDateTime.parse(UTCEndString, DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                        "HH:mm:ss.S"));
                ZoneId UTCEndZoneId = ZoneId.of("UTC");
                ZonedDateTime UTCZonedDateTimeEnd = ldtEnd.atZone(UTCEndZoneId);

                // Formatting to User Zone
                ZoneId UserZoneIdEnd = ZoneId.of(ZoneId.systemDefault().toString());
                ZonedDateTime userDateTimeEnd = UTCZonedDateTimeEnd.withZoneSameInstant(UserZoneIdEnd);

                String endZoneTime = format.format(userDateTimeEnd);
                appointment.setEndZone(endZoneTime);
                //add to List
                appointmentsList.add(appointment);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentsList;
    }

    /**
     * Deletes the Appointments
     * @param appointment
     */
    //queries to delete appointments
    public void deleteAppointment(Appointment appointment) {
        try{
            String sql = "delete " +
                    "from " +
                    "appointments " +
                    "where " +
                    "Appointment_ID = \"" +appointment.getId()+ "\"";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
    }

    /**
     * Checks if appointments overlap or not
     * @param appointment
     * @return
     */
    //checks if appointments overlap or nto
    public boolean checkIfOverlap(Appointment appointment) {
        try{
            String sql ="select * from appointments "
                    + "where (\""+appointment.getAppointmentStartTime()+"\" " +
                    "between Start AND end OR \""+appointment.getAppointmentEndTime()+"\" BETWEEN Start " +
                    "AND End OR \""+appointment.getAppointmentStartTime()+"\"" +
                    " < Start " +
                    "AND \""+appointment.getAppointmentEndTime()+"\" > " +
                    "End) "
                    + "AND (Customer_ID = \""+appointment.getAppointmentCustomerId()+"\")";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return true;
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
        return false;
    }

    /**
     * Schedules a new appointment
     * @param appointment
     */
    //add a new appointment
    public void addAppointment(Appointment appointment) {
        try{
            String sql = "insert into" +
                    " appointments(Appointment_ID, " +
                    "Title, " +
                    "Description," +
                    "Location, " +
                    "Type, " +
                    "Start, " +
                    "End, " +
                    "Customer_ID, " +
                    "User_ID, " +
                    "Contact_ID) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,(select Contact_ID from contacts where Contact_Name = \""+appointment.getAppointmentContact()+"\"))";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, appointment.getId());
            ps.setString(2, appointment.getTitle());
            ps.setString(3, appointment.getAppointmentDescription());
            ps.setString(4, appointment.getAppointmentLocation());
            ps.setString(5, appointment.getAppointmentType());
            ps.setTimestamp(6, appointment.getAppointmentStartTime());
            ps.setTimestamp(7, appointment.getAppointmentEndTime());
            ps.setInt(8, appointment.getAppointmentCustomerId());
            ps.setInt(9, appointment.getUserId());
            ps.executeUpdate();
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
    }

    /**
     * Updates Appointments
     * @param appointment
     */
    //updates an appointment
    public void updateAppointment(Appointment appointment) {
        try{
            String sql = "update " +
                    "appointments " +
                    "SET " +
                    "Title = \"" + appointment.getTitle() + "\", " +
                    "Description = \"" + appointment.getAppointmentDescription() + "\", " +
                    "Location = \"" + appointment.getAppointmentLocation() + "\", " +
                    "Type = \"" +appointment.getAppointmentType()+ "\", " +
                    "Start = \"" + appointment.getAppointmentStartTime() + "\", " +
                    "End = \""+ appointment.getAppointmentEndTime() +"\", " +
                    "Customer_ID = \""+ appointment.getAppointmentCustomerId() +"\", " +
                    "User_ID = \""+ appointment.getUserId() +"\", " +
                    "Contact_ID = (select Contact_ID from contacts where Contact_Name = \""+appointment.getAppointmentContact()+
                    "\") "+
                    "where " +
                    "Appointment_ID = \""+appointment.getId()+"\"";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves monthly appointments
     * @param month
     * @param year
     * @return
     */
    //gets monthly appointments
    public static ObservableList<Appointment> getMonthlyAppointments(String month, String year){
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
                    "appointments.Contact_ID = contacts.Contact_ID and\n" +
                    "monthname(start) = \"" +month+ "\" and\n" +
                    "year(start) = \"" +year+ "\"";
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

    /**
     * Retrieves weekly appointments
     * @param weekNumber
     * @param year
     * @return
     */
    //gets weekly appointments
    public static ObservableList<Appointment> getWeeklyAppointments(String weekNumber, String year){
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
                    "appointments.Contact_ID = contacts.Contact_ID and\n" +
                    "week(start) = (select week(curdate())) and\n" +
                    "year(start) = \"" +year+ "\"";
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
                appointmentsList.add(appointment);
            }
        }catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return appointmentsList;
    }

}