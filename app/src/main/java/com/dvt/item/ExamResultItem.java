package com.dvt.item;

/**
 * Created by Doanp on 7/9/2016.
 */

public class ExamResultItem {

    private String subjectName;
    private String point1;
    private String point2;
    private String point3;
    private String mediumScore;
    private String semester;
    private String linkClass;

    public String getLinkClass() {
        return linkClass;
    }

    public void setLinkClass(String linkClass) {
        this.linkClass = linkClass;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public ExamResultItem(String subjectName, String point1, String point2, String point3, String mediumScore,String semester,String link) {
        this.subjectName = subjectName;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.mediumScore = mediumScore;
        this.semester=semester;
        this.linkClass=link;
    }

    public ExamResultItem() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public String getPoint3() {
        return point3;
    }

    public void setPoint3(String point3) {
        this.point3 = point3;
    }

    public String getMediumScore() {
        return mediumScore;
    }

    public void setMediumScore(String mediumScore) {
        this.mediumScore = mediumScore;
    }
}
