package model;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Timestamp;

public class Appointment {
    private int id;
    private String title;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentContact;
    private String appointmentType;
    private Timestamp appointmentStartTime;
    private Timestamp appointmentEndTime;
    private int appointmentCustomerId;
    private int contactId;
    private int userId;
    private String startZoneTime;
    private String endZoneTime;

    public void setId(int id){ this.id = id; }
    public int getId(){ return id; }
    public void setTitle(String title){this.title = title;}
    public String getTitle(){return title;}
    public void setAppointmentDescription(String description){
        this.appointmentDescription = description;
    }
    public String getAppointmentDescription(){
        return appointmentDescription;
    }
    public void setAppointmentLocation(String appointmentLocation){
        this.appointmentLocation = appointmentLocation;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentContact(String appointmentContact) {
        this.appointmentContact = appointmentContact;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public void setAppointmentStartTime(Timestamp appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public void setAppointmentEndTime(Timestamp appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public void setAppointmentCustomerId(int appointmentCustomerId) {
        this.appointmentCustomerId = appointmentCustomerId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAppointmentContact() {
        return appointmentContact;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public Timestamp getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public Timestamp getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public int getAppointmentCustomerId() {
        return appointmentCustomerId;
    }

    public int getContactId() {
        return contactId;
    }

    public int getUserId() {
        return userId;
    }

    public void setStartZone(String startZoneTime) {
        this.startZoneTime = startZoneTime;
    }

    public void setEndZone(String endZoneTime) {
        this.endZoneTime = endZoneTime;
    }

    public String getStartZoneTime() {
        return startZoneTime;
    }

    public String getEndZoneTime() {
        return endZoneTime;
    }
}

