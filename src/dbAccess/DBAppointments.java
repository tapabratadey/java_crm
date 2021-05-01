package dbAccess;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
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
//            Statement statement = DBConnection.getConnection().createStatement();
//            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, statement.RETURN_GENERATED_KEYS);

//            System.out.println("ksdasasdf234234dffhajksf" + appointment.getTitle());
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
//            ps.setInt(9, appointment.getContactId());
            ps.executeUpdate();
//            ResultSet rs = ps.getGeneratedKeys();
//            rs.next();
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
    }

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
                    "Contact_ID = \""+appointment.getContactId()+"\" "+
                    "where " +
                    "Appointment_ID = \""+appointment.getId()+"\"";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            System.out.println(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}