package main;

import db.DBConnection;
import dbAccess.DBCountries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Login.fxml")));
        primaryStage.setTitle("Scheduler Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        DBConnection.startConnection();
        DBCountries.checkDateConversion();
        launch(args);
        DBConnection.closeConnection();
    }
}

/***
 * 1. List of Customers and appointments(CRUD) if del Customer then delete appointments
 * 3. Schedule appointments (CRUD)
 * 4. View appointments
 * 6. Reports
 * 7. Alert appointment
 * 8. Input validation
 * 5. Readme.txt
 * 6. Comments
 * 7. JavaDocs
 * 8. log_activity.txt
 * 9. two lambda expressions
 */