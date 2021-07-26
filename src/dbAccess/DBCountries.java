package dbAccess;

import db.DBConnection;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.*;


/**
 * Database queries related to Countries
 */

public class DBCountries {
    /**
     * Gets list of all countries
     * @return List of all Countries
     */
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

    /**
     * Gets all US states
     * @return List of US Regions
     */
    //gets all US states
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

    /**
     * Gets all UK Regions
     * @return List of UK Regions
     */
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

    /**
     * Gets all Canada's Regions
     * @return List of Ca Regions
     */
    //gets all CA Regions
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
}
