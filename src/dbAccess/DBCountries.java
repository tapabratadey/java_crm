package dbAccess;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;

import java.sql.*;

public class DBCountries {
    public static ObservableList<Countries> getAllCountries(){
        ObservableList<Countries> countryList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Countries country = new Countries(countryId, countryName);
                countryList.add(country);
            }
        }catch (SQLException throwable) {
            throwable.printStackTrace();
        }


        return countryList;
    }

    public static void checkDateConversion(){
        System.out.println("CREATE DATE FIRST");
        String sql = "select Create_Date from countries";
        try{
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Timestamp ts = rs.getTimestamp("Create_Date");
                System.out.println("CD: " + ts.toLocalDateTime().toString());
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }
}
