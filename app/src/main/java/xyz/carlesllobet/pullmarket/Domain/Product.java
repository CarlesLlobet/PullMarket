package xyz.carlesllobet.pullmarket.Domain;

import android.net.Uri;

/**
 * Created by CarlesLlobet on 31/01/2016.
 */
public class Product {

    //private variables
    Long id;
    String name;
    String description;
    Integer sector;
    Double preu;
    Uri pic;

    // Empty constructor
    public Product() {

    }

    // constructor
    public Product(Long id, String name, String description, Uri pic, Integer sector, Double preu) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sector = sector;
        this.preu = preu;
        this.pic = pic;
    }

    public Long getId() {return this.id;}

    public void setId(Long id) {this.id = id;}

    // getting name
    public String getName() {
        return this.name;
    }

    // setting name
    public void setName(String name) {
        this.name = name;
    }

    // getting name
    public String getDescription() {
        return this.description;
    }

    // setting name
    public void setDescription(String desc) {
        this.description = desc;
    }

    // getting punctuation
    public Integer getSector() {
        return this.sector;
    }

    // setting punctuation
    public void setSector(Integer sect) {
        this.sector = sect;
    }

    // getting punctuation
    public Double getPreu() {
        return this.preu;
    }

    // setting punctuation
    public void setPreu(Double price) {
        this.preu = price;
    }

    //getting foto
    public Uri getFoto() {
        return this.pic;
    }

    //getting foto
    public void setFoto(Uri pic) {
        this.pic = pic;
    }
}
