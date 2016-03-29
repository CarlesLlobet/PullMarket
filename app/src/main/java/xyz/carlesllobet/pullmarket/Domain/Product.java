package xyz.carlesllobet.pullmarket.Domain;

import android.net.Uri;

/**
 * Created by CarlesLlobet on 31/01/2016.
 */
public class Product {

    //private variables
    Integer id;
    String name;
    String description;
    Integer sector;
    Integer preu;
    Uri pic;

    // Empty constructor
    public Product() {

    }

    // constructor
    public Product(Integer id, String name, String description, Uri pic, Integer sector, Integer preu) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sector = sector;
        this.preu = preu;
        this.pic = pic;
    }

    public Integer getId() {return this.id;}

    public void setId(Integer id) {this.id = id;}

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
    public Integer getPreu() {
        return this.preu;
    }

    // setting punctuation
    public void setPreu(Integer price) {
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
