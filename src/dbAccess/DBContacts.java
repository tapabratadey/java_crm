package dbAccess;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database queries related to Contacts
 *
 */

public class DBContacts {
    /**
     * queries and returns all contacts.
     * @return ObserverableList<String> list of contacts
     */
    public static ObservableList<String> getAllContacts() {
        ObservableList<String> contactsList = FXCollections.observableArrayList();
        try {
            String sql = "select * from contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setContactId(rs.getInt("Contact_ID"));
                contact.setContactName(rs.getString("Contact_Name"));
                contact.setContactEmail(rs.getString("Email"));
                contactsList.add(contact.getContactName());
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return contactsList;
    }
}
