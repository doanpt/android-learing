package com.dvt.adapter;

/**
 * Created by Android on 12/14/2015.
 */
public class ExamResultItem {
    private String name;
    private String point1;
    private String point2;
    private String inchi;
    public ExamResultItem(String name,String point1, String point2,String inchi) {
        this.inchi=inchi;
        this.name=name;
        this.point1 = point1;
        this.point2 = point2;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ExamResultItem){
            ExamResultItem temp = (ExamResultItem) o;
            if (this.getName().equals(temp.getName()))
                return true;
        }
        return false;
    }

    public ExamResultItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint1() {
        return point1;
    }

    public void setPoint1(String point1) {
        this.point1 = point1;
    }

    public String getPoint2() {
        return point2;
    }

    public void setPoint2(String point2) {
        this.point2 = point2;
    }

}
