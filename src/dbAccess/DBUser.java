package dbAccess;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUser {
    public static ObservableList getAllUsers() {
        ObservableList<String> usersList = FXCollections.observableArrayList();
        try{
            String sql = "select " +
                    "User_ID " +
                    "from users";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setUserId(rs.getInt("User_ID"));
                usersList.add(String.valueOf(user.getUserId()));
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
        return usersList;
    }
}
