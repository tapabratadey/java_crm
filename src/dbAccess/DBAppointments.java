package dbAccess;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAppointments {
    public static ObservableList<String> getAppointments(){
        ObservableList<String> appointmentsList = FXCollections.observableArrayList();
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
            System.out.println(sql);
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("Appointment_Id"));
//                country.setDivisionId(rs.getInt("Division_ID"));
//                appointmentsList.add(appointment.getName());
            }
        }catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return appointmentsList;
    }
}
