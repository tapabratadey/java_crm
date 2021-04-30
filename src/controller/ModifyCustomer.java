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

public class ModifyCustomer implements Initializable {
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

    public void setupComboBox(){
        comboBoxCountry.setItems(DBCountries.getAllCountries());
        comboBoxCountry.getSelectionModel().selectedItemProperty().addListener((model, name, value) -> {
            System.out.println("Country: " + value.toString());
            if(value.toString().equals("US")){ comboBoxRegion.setItems(DBCountries.getUSRegions());}
            if(value.toString().equals("UK")){ comboBoxRegion.setItems(DBCountries.getUKRegions()); }
            if(value.toString().equals("Canada")){ comboBoxRegion.setItems(DBCountries.getCARegions()); }
        });
    }

    public void customerToModify(Customer customer, int idx) {
        customerToModify = customer;
        customerIdx = idx;
        customerId.setText(Integer.toString(customerToModify.getId()));
        customerName.setText(customerToModify.getName());
        customerAddress.setText(customerToModify.getAddress());
        customerPostal.setText(customerToModify.getPostalCode());
        customerPhone.setText(customerToModify.getPhoneNumber());
        comboBoxCountry.setValue(customerToModify.getCountry());
        comboBoxRegion.setValue(customerToModify.getDivision());
    }

    public void customerSaveButton(ActionEvent actionEvent) {
        try{
            int id = customerToModify.getId();
            String name = customerToModify.getName();
            String address = customerToModify.getAddress();
            String postal = customerToModify.getPostalCode();
            String phone = customerToModify.getPhoneNumber();
            String country = comboBoxCountry.getValue().toString();
            System.out.println("SAVE: " + country);
            String region = comboBoxRegion.getValue().toString();
            System.out.println("SAVE: " + region);
            if (name == "" ||
                address == "" ||
                postal == "" ||
                phone == ""){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Customer fields cannot be empty");
                alert.showAndWait();
            }else{
                Customer updateCustomer = new Customer();
                DBCustomer updateDBCustomer = new DBCustomer();
                Customer customer = new Customer();
                customer.setId(id);
                customer.setName(name);
                customer.setAddress(address);
                customer.setPostalCode(postal);
                customer.setPhoneNumber(phone);
                customer.setDivision(region);
                customer.setCountry(country);
                updateDBCustomer.modifyCustomer(customer);
//                Customers customerController = new Customers();
//                customerController.setupCustomer();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Customers.fxml")));
                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }catch(NumberFormatException | IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input");
            alert.showAndWait();
        }
    }

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
