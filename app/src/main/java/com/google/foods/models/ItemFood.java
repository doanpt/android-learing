package com.google.foods.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by sev_user on 08/10/2017.
 */

public class ItemFood implements Parcelable {

    private String idFood;
    private String image;
    private String name;
    private int type;
    private int orderQuantity;
    private int totalQuantity;

    public ItemFood() {
    }

    public ItemFood(String idFood, String image, String name, int type, int orderQuantity, int totalQuantity) {
        this.idFood = idFood;
        this.image = image;
        this.name = name;
        this.type = type;
        this.orderQuantity = orderQuantity;
        this.totalQuantity = totalQuantity;
    }

    protected ItemFood(Parcel in) {
        idFood = in.readString();
        image = in.readString();
        name = in.readString();
        type = in.readInt();
        orderQuantity = in.readInt();
        totalQuantity = in.readInt();
    }

    public static final Creator<ItemFood> CREATOR = new Creator<ItemFood>() {
        @Override
        public ItemFood createFromParcel(Parcel in) {
            return new ItemFood(in);
        }

        @Override
        public ItemFood[] newArray(int size) {
            return new ItemFood[size];
        }
    };

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idFood);
        parcel.writeString(image);
        parcel.writeString(name);
        parcel.writeInt(type);
        parcel.writeInt(orderQuantity);
        parcel.writeInt(totalQuantity);
    }
    @Override
    public boolean equals(Object object) {
        boolean retVal = false;
        if (object instanceof ItemFood){
            ItemFood ptr = (ItemFood) object;
            retVal = ptr.idFood.equals(this.idFood);
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.idFood != null ? this.idFood.hashCode() : 0);
        return hash;
    }
    @Override
    public String toString() {
        return "ItemFood{" +
                "idFood='" + idFood + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", orderQuantity=" + orderQuantity +
                ", totalQuantity=" + totalQuantity +
                '}';
    }
}
