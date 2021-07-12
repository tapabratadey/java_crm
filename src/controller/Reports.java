package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;

/**
 * Screen to view additional reports
 */

public class Reports implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Logs out the user
     * @param actionEvent
     * @throws IOException
     */
    // log out button redirecting to the login screen
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

    /**
     * Takes the user back to the dashboard
     * @param actionEvent
     * @throws IOException
     */
    // dashboard button
    public void dashboardButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Dashboard.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Takes the user to the Schedule of Contacts Screen
     * @param actionEvent
     * @throws IOException
     */
    // Contact's Schedule view
    public void viewContactSchedule(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/ContactSchedule.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Takes the user to the operational countries page
     * @param actionEvent
     * @throws IOException
     */
    // Tally of Countries view
    public void viewRegionsTally(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/OperationalCountries.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Take the user to the Appointment Stats page
     * @param actionEvent
     * @throws IOException
     */
    // Appointment Stats view
    public void viewAppointmentStats(ActionEvent actionEvent)throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AppointmentType.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}
