package controller;

import dbAccess.DBCountries;
import dbAccess.DBCustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Country;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Screen to add customers
 */

public class AddCustomer implements Initializable {
    public Label customerIdLabel;
    public Label customerNameLabel;
    public Label customerAddressLabel;
    public Label customerPostalCode;
    public Label customerDivisionLabel;
    public Label customerCountryLabel;
    public Label customerPhoneLabel;
    public TextField customerId;
    public TextField customerName;
    public TextField customerAddress;
    public TextField customerPostal;
    public TextField customerPhone;
    public ComboBox comboBoxRegion;
    public ComboBox comboBoxCountry;

    /**
     * Initializes the combo boxes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComboBox();
    }

    /**
     * Setups up the country combo boxes
     */
    //sets up combobox to populate them with countries.
    public void setupComboBox(){
        comboBoxCountry.setItems(DBCountries.getAllCountries());
        comboBoxCountry.getSelectionModel().selectedItemProperty().addListener((model, name, value) -> {
            if(value.toString().equals("U.S")){ comboBoxRegion.setItems(DBCountries.getUSRegions());}
            if(value.toString().equals("UK")){ comboBoxRegion.setItems(DBCountries.getUKRegions()); }
            if(value.toString().equals("Canada")){ comboBoxRegion.setItems(DBCountries.getCARegions()); }
        });
    }

    /**
     * On save click, the Customer details are grabbed from the field
     * Error Checking -> Creates a New Customer
     * @param actionEvent
     * @throws IOException
     */
    //save button functionality
    public void customerSaveButton(ActionEvent actionEvent) throws IOException {
        String name = customerName.getText();
        String address = customerAddress.getText();
        String postal = customerPostal.getText();
        String phone = customerPhone.getText();
        String country = (String) comboBoxCountry.getValue();
        String region = (String) comboBoxRegion.getValue();
        String errMsg = "";

        if(name == ""){ errMsg+="Missing name\n"; }
        if(address == ""){ errMsg+="Missing address\n";}
        if(postal == "") {errMsg+="Missing postal\n"; }
        if(phone == "") { errMsg+="Missing phone\n";}
        if(country == null) { errMsg+="Missing country\n"; }
        if(region == null){errMsg+= "Missing region\n";}
        if (errMsg.length() != 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errMsg);
            alert.showAndWait();
        }else{
            int idCounter = 0;
            DBCustomer dbCustomer = new DBCustomer();
            for (Customer customer:dbCustomer.getAllCustomers()){
                if (customer.getId() > idCounter){
                    idCounter = customer.getId();
                }
            }
            customerId.setText(String.valueOf(++idCounter));
            DBCustomer updateDBCustomer = new DBCustomer();
            Customer customer = new Customer();
            customer.setId(idCounter);
            customer.setName(name);
            customer.setAddress(address);
            customer.setPostalCode(postal);
            customer.setPhoneNumber(phone);
            customer.setDivision(region);
            customer.setCountry(country);
            updateDBCustomer.addCustomer(customer);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Customers.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Logs the user out
     * @param actionEvent
     * @throws IOException
     */
    //log out button
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

    //cancel button
    public void customerCancelButton(ActionEvent actionEvent) throws IOException {
        Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?");
        Optional<ButtonType> optButton = alertUser.showAndWait();
        if (optButton.isPresent() && optButton.get() == ButtonType.OK){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Customers.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
