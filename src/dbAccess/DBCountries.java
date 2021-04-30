package dbAccess;

import db.DBConnection;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.*;

public class DBCountries {
    public static ObservableList<String> getAllCountries(){
        ObservableList<String> countryList = FXCollections.observableArrayList();
        try{
            String sql = "select * from countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Country country = new Country();
                country.setName(rs.getString("Country"));
//                country.setDivisionId(rs.getInt("Division_ID"));
                countryList.add(country.getName());
            }
        }catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return countryList;
    }

    public static ObservableList<String> getUSRegions(){
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try{
            String sql = "select * from first_level_divisions where COUNTRY_ID = 1";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Country region = new Country();
                region.setDivision(rs.getString("Division"));
                divisionList.add(region.getDivision());
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
        return divisionList;
    }
    public static ObservableList<String> getUKRegions(){
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try{
            String sql = "select * from first_level_divisions where COUNTRY_ID = 2";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Country ukRegions = new Country();
                ukRegions.setDivision(rs.getString("Division"));
                divisionList.add(ukRegions.getDivision());
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
        return divisionList;
    }
    public static ObservableList<String> getCARegions(){
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try{
            String sql = "select * from first_level_divisions where COUNTRY_ID = 3";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Country caRegions = new Country();
                caRegions.setDivision(rs.getString("Division"));
                divisionList.add(caRegions.getDivision());
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
        return divisionList;
    }

    /*public static void checkDateConversion(){
        System.out.println("CREATE DATE FIRST");
        String sql = "select Create_Date from countries";
        try{
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Timestamp ts = rs.getTimestamp("Create_Date");
                System.out.println("CD: " + ts.toLocalDateTime().toString());
            }
        }catch(SQLException throwable){
            throwable.printStackTrace();
        }
    }*/
}
