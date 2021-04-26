package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    public Button logOutButtonText;
    public Button viewAppointmentsText;
    public Button viewReportsText;
    public Button viewCustomersText;
    public Label dashboardHeader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle userLang;
        Locale currSystem = Locale.getDefault();
        userLang = ResourceBundle.getBundle("resourceBundle", currSystem);
        dashboardHeader.setText(userLang.getString("dashboardHeader"));
        logOutButtonText.setText(userLang.getString("logOutButton"));
        viewCustomersText.setText(userLang.getString("viewCustomersText"));
        viewAppointmentsText.setText(userLang.getString("viewAppointmentsText"));
        viewReportsText.setText(userLang.getString("viewReportsText"));

        /**
         * Appointment checkAppointments = new Appointment();
         *  checkAppointments.setAppointmentAlert();
         */
    }
    public void logOutButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Login.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public void viewAppointmentButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Appointments.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public void viewReportsButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Reports.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public void viewCustomersButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Customers.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }


}
