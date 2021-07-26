package model;

/**
 * Customer Class
 * Customer Class holds:
 * 1. Customer id
 * 2. Customer name
 * 3. Customer address
 * 4. Customer postal code
 * 5. Customer phone number
 * 6. Customer division
 * 7. Customer division Id
 * 8. Customer country
 * 9. Setter functions
 * 10. Getter functions
 */

public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String division;
    private int divisionId;
    private String country;

    public void setId(Integer id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }
    public void setPostalCode(String pc){
        this.postalCode = pc;
    }
    public String getPostalCode(){
        return postalCode;
    }
    public void setDivision(String division){
        this.division = division;
    }
    public String getDivision(){
        return division;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return country;
    }
    public String toString(){ return name; }
    public void setDivisionId(int divisionId) { this.divisionId = divisionId; }
    public int getDivisionId(){ return divisionId; }
}
