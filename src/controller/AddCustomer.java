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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComboBox();
    }

    public void setupComboBox(){
        comboBoxCountry.setItems(DBCountries.getAllCountries());
        comboBoxCountry.getSelectionModel().selectedItemProperty().addListener((model, name, value) -> {
            if(value.toString().equals("US")){ comboBoxRegion.setItems(DBCountries.getUSRegions());}
            if(value.toString().equals("UK")){ comboBoxRegion.setItems(DBCountries.getUKRegions()); }
            if(value.toString().equals("Canada")){ comboBoxRegion.setItems(DBCountries.getCARegions()); }
        });
    }

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

    public void customerSaveButton(ActionEvent actionEvent) {
        try{
            int idCounter = 0;
            DBCustomer dbCustomer = new DBCustomer();
            for (Customer customer:dbCustomer.getAllCustomers()){
                if (customer.getId() > idCounter){
                    idCounter = customer.getId();
                }
            }
            customerId.setText(String.valueOf(++idCounter));
            String name = customerName.getText();
            String address = customerAddress.getText();
            String postal = customerPostal.getText();
            String phone = customerPhone.getText();
            String country = comboBoxCountry.getValue().toString();
            String region = comboBoxRegion.getValue().toString();
            if (name == "" ||
                    address == "" ||
                    postal == "" ||
                    phone == "" ||
                    country == null||
                    region == null){
                throw new NullPointerException("Invalid Input. Try again.");
            }else{
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
        }catch(NumberFormatException | IOException | NullPointerException e){
            System.out.println(e);
            Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION, "Invalid input. Try again.");
            Optional<ButtonType> optButton = alertUser.showAndWait();
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
