package controller;

import dbAccess.DBAppointments;
import dbAccess.DBCustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Screen to view customers
 */

public class Customers implements Initializable {
    public TableView customerTable;
    public TableColumn customerIdText;
    public TableColumn customerNameText;
    public TableColumn customerAddressText;
    public TableColumn customerCountryText;
    public TableColumn customerPostalCode;
    public TableColumn customerPhoneText;
    public Button addCustomerText;
    public Button deleteCustomerText;
    public Button dashboardText;
    public Button editCustomerText;
    public Button scheduleAppointmentText;
    public Label customersText;
    public TableColumn customerDivision;

    /**
     * Initializes customer Table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCustomer();
    }

    /**
     * Sets up the customer table
     */
    //sets up customer table
    //lambda expression
    public void setupCustomer(){
        DBCustomer customer = new DBCustomer();
        customerTable.setItems(customer.getAllCustomers());
        customerIdText.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameText.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressText.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerCountryText.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerPhoneText.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerIdText.setSortType(TableColumn.SortType.ASCENDING);
        customerTable.getSortOrder().add(customerIdText);
        customerTable.sort();
    }

    /**
     * Takes the user to the customer add screen
     * @param actionEvent
     * @throws IOException
     */
    // add customer button
    public void addCustomerButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AddCustomer.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Customers");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes a customer
     * @param actionEvent
     */
    // delete customer button functionality
    public void deleteCustomerButton(ActionEvent actionEvent) {
        Customer customerSelected = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (customerSelected == null){
            Alert alertUserErr = new Alert(Alert.AlertType.ERROR, "Please select a customer to delete");
            alertUserErr.showAndWait();
        }else{
            Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? This action will delete customer" +
                    " " +
                    "appointments too.");
            Optional<ButtonType> optButton = alertUser.showAndWait();
            try{
                if (optButton.isPresent() && optButton.get() == ButtonType.OK) {
                        Customer customer = new Customer();
                        DBCustomer dbCustomer = new DBCustomer();
                        Appointment appointment = new Appointment();
                        DBAppointments dbAppointment = new DBAppointments();
                        customer.setId(customerSelected.getId());
                        appointment.setId(customerSelected.getId());
                        dbAppointment.deleteAppointment(appointment);
                        dbCustomer.deleteCustomer(customer);
                        setupCustomer();
                    }
                }catch(IndexOutOfBoundsException | NoSuchElementException err){
                System.out.println(err);
            }
        }
    }

    /**
     * Takes the customer back to the dashboard
     * @param actionEvent
     * @throws IOException
     */
    //dashboard button
    public void dashboardButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Dashboard.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Takes the user to the customer edit screen
     * @param actionEvent
     * @throws IOException
     */
    //edit customer button
    public void editCustomerButton(ActionEvent actionEvent) throws IOException {
        Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (customer == null){
            Alert alertUserErr = new Alert(Alert.AlertType.ERROR, "Please select a customer to edit");
            alertUserErr.showAndWait();
        }else{
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/views/ModifyCustomer.fxml")));
            Parent root = (Parent) loader.load();
            EditCustomer modCustomer = loader.getController();
            int idx = customerTable.getSelectionModel().getSelectedIndex();
            modCustomer.customerToModify(customer, idx);
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
}
