package dbAccess;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.*;

public class DBCustomer {
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> customersList = FXCollections.observableArrayList();
        try{
            String sql = "select " +
                    "Customer_ID, " +
                    "Customer_Name, " +
                    "Address, " +
                    "Postal_Code, " +
                    "Phone, " +
                    "first_level_divisions.Division, " +
                    "countries.Country " +
                    "from customers, " +
                    "first_level_divisions, " +
                    "countries where " +
                    "customers.Division_ID = first_level_divisions.Division_ID and " +
                    "countries.Country_ID = first_level_divisions.Country_ID;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getInt("Customer_ID"));
                customer.setName(rs.getString("Customer_Name"));
                customer.setAddress(rs.getString("Address"));
                customer.setPostalCode(rs.getString("Postal_Code"));
                customer.setPhoneNumber(rs.getString("Phone"));
                customer.setDivision(rs.getString("Division"));
                customer.setCountry(rs.getString("Country"));
                customersList.add(customer);
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
        return customersList;
    }
    public static ObservableList<String> getAllCustomerId(){
        ObservableList<String> customersList = FXCollections.observableArrayList();
        try{
            String sql = "select " +
                    "Customer_ID " +
                    "from customers";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getInt("Customer_ID"));
                customersList.add(String.valueOf(customer.getId()));
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
        return customersList;
    }
    public void modifyCustomer(Customer customer){
        try{
            String sql = "update " +
                    "customers, " +
                    "first_level_divisions, " +
                    "countries " +
                    "SET " +
                    "Customer_Name = \"" + customer.getName() + "\", " +
                    "Address = \"" + customer.getAddress() + "\", " +
                    "Postal_Code = \"" + customer.getPostalCode() + "\", " +
                    "Phone = \"" + customer.getPhoneNumber() + "\"," +
                    "first_level_divisions.Division = \"" +customer.getDivision()+ "\", " +
                    "countries.Country = \"" + customer.getCountry() + "\" " +
                    "where " +
                    "customers.Division_ID = first_level_divisions.Division_ID " +
                    "and countries.Country_ID = first_level_divisions.Country_ID " +
                    "and customers.Customer_ID = \"" + customer.getId() + "\" ";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteCustomer(Customer customer) {
        try{
            String sql = "delete from customers where Customer_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public void addCustomer(Customer customer) {
        try{
            String sql = "INSERT INTO " +
                    "customers(Customer_ID, " +
                    "Customer_Name, " +
                    "Address, " +
                    "Postal_Code, " +
                    "Phone, " +
                    "Division_Id) " +
                    "VALUES (?,?,?,?,?,(SELECT Division_ID from first_level_divisions where Division = ?))";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPostalCode());
            ps.setString(5, customer.getPhoneNumber());
            ps.setString(6, customer.getDivision());
            ps.executeUpdate();
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
    }
}