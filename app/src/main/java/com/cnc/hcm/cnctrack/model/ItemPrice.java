package com.cnc.hcm.cnctrack.model;

public class ItemPrice {

    public static final int TYPE_SERVICES = 0;
    public static final int TYPE_PRODUCTS = 1;

    private int type;
    private String _id;
    private String name;
    private double tax;
    private double price;
    private long quantity;

    public ItemPrice(int type, String _id, String name, double tax, double price, long quantity) {
        this.type = type;
        this._id = _id;
        this.name = name;
        this.tax = tax;
        this.price = price;
        this.quantity = quantity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
