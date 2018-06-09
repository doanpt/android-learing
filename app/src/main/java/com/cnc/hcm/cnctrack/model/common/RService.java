package com.cnc.hcm.cnctrack.model.common;

/**
 * Created by Android on 09/06/2018.
 */

public class RService {
    public String _id;
    public String name;
    public String category;
    public Double price;
    public Double tax;
    public Unit unit;
    public String photo;

    public RService(String _id, String name, String category, Double price, Double tax, Unit unit, String photo) {
        this._id = _id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.tax = tax;
        this.unit = unit;
        this.photo = photo;
    }
}
