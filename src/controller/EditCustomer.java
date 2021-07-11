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
 * Screen to edit customers
 */

public class EditCustomer implements Initializable {
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
    Customer customerToModify;
    int customerIdx;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComboBox();
    }

    // sets up country combo box
    public void setupComboBox(){
        comboBoxCountry.setItems(DBCountries.getAllCountries());
        comboBoxCountry.getSelectionModel().selectedItemProperty().addListener((model, name, value) -> {
            if(value.toString().equals("U.S")){ comboBoxRegion.setItems(DBCountries.getUSRegions());}
            if(value.toString().equals("UK")){ comboBoxRegion.setItems(DBCountries.getUKRegions()); }
            if(value.toString().equals("Canada")){ comboBoxRegion.setItems(DBCountries.getCARegions()); }
        });
    }

    //populates customer fields
    public void customerToModify(Customer customer, int idx) {
        customerToModify = customer;
        customerIdx = idx;
        customerId.setText(Integer.toString(customer.getId()));
        customerName.setText(customer.getName());
        customerAddress.setText(customer.getAddress());
        customerPostal.setText(customer.getPostalCode());
        customerPhone.setText(customer.getPhoneNumber());
        comboBoxCountry.setValue(customer.getCountry());
        comboBoxRegion.setValue(customer.getDivision());
    }

    //save button functionality
    public void customerSaveButton(ActionEvent actionEvent) throws IOException {
        String id = customerId.getText();
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

            DBCustomer updateDBCustomer = new DBCustomer();
            Customer customer = new Customer();
            customer.setId(Integer.parseInt(id));
            customer.setName(name);
            customer.setAddress(address);
            customer.setPostalCode(postal);
            customer.setPhoneNumber(phone);
            customer.setDivision(region);
            customer.setCountry(country);
            updateDBCustomer.modifyCustomer(customer);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Customers.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    // log out button functionality
    public void logOutButton(ActionEvent actionEvent) throws IOException {
        Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?");
        Optional<ButtonType> optButton = alertUser.showAndWait();
        if (optButton.isPresent() && optButton.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Login.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    //cancel button functionality
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
