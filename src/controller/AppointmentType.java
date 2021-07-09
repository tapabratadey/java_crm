package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Show Number of Appointments per month by Type
 */

public class AppointmentType implements Initializable {
    public Label statsLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){getStats();}

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

    // Reports dashboard button
    public void dashboardButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Reports.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    // get appointment stat results
    public void getStats() {
        try {
            String sql = "select \n" +
                    "Type, \n" +
                    "month(start) as 'Month', \n" +
                    "Count(*) as 'Total' \n" +
                    "from \n" +
                    "appointments \n" +
                    "group by \n" +
                    "Type, \n" +
                    "month(start)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder str = new StringBuilder();
            str.append(String.format("%1$-30s %2$-10s %3$s", "Type", "Month", "Total"));
            str.append(System.getProperty("line.separator"));
            str.append(String.join("", Collections.nCopies(50, "-")));
            str.append("\n");
            str.append(System.getProperty("line.separator"));

            while(rs.next()){
                str.append(String.format("%1$-30s %2$-10s %3$s \n",
                        rs.getString("Type"),
                        rs.getString("Month"),
                        rs.getInt("Total")));
            }
            statsLabel.setText(str.toString());
        }
        catch(SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        }
    }
}
