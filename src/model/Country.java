package model;

/**
 * Country Class
 */

public class Country {
    private int id;
    private String name;
    private int divisionId;
    private String division;

    @Override
    public String toString(){
        return name;
    }
    public Country(int id, String name, int divisionId, String division){
        this.id = id;
        this.name = name;
        this.divisionId = divisionId;
        this.division = division;
    }

    public Country(int id, String name){
        this.id = id;
        this.name = name;
    }
    public Country(){ }

    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getDivisionId(){ return divisionId; }
    public String getDivision(){ return division; }
    public void setId(int id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setDivisionId(int divisionId){ this.divisionId = divisionId; }
    public void setDivision(String division){ this.division = division; }
}
