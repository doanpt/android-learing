package com.dvt.jsoup;

import android.util.Log;

import com.dvt.item.ExamResultForReportItem;
import com.dvt.item.ExamScheduleItem;
import com.dvt.item.LearningResultItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Android on 12/14/2015.
 */
public class HtmlParse {

    private String learningResultURL = "http://qlcl.edu.vn/examre/ket-qua-hoc-tap.htm?code=";
    private String examSchuduleURL = "http://qlcl.edu.vn/examplanuser/ke-hoach-thi.htm?code=";
    private String examResultURL = "http://qlcl.edu.vn/viewstudent/ket-qua-thi.htm?code=";
    private String code = "";
    private String file = "";
    private ArrayList<LearningResultItem> arrLearnResult = new ArrayList<>();
    private ArrayList<LearningResultItem> arrExamResult = new ArrayList<>();
    private ArrayList<ExamScheduleItem> arrExamSchedule = new ArrayList<>();
    private ArrayList<ExamResultForReportItem> arrExamReport = new ArrayList<>();

    public ArrayList<ExamScheduleItem> getArrExamSchedule() {
        return arrExamSchedule;
    }

    public void setArrLearnResult(ArrayList<LearningResultItem> arrLearnResult) {
        this.arrLearnResult = arrLearnResult;
    }

    public void setArrExamSchedule(ArrayList<ExamScheduleItem> arrExamSchedule) {
        this.arrExamSchedule = arrExamSchedule;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HtmlParse(String file) {
        this.file = file;
    }

    public void getResultLearning() {
        learningResultURL += code;
        arrLearnResult.clear();
        Document document = null;
        try {
            //document = Jsoup.connect(learningResultURL).get();
            document = Jsoup.parse(file);
            Elements tbTables = document.getElementsByClass("kTable");
            Elements trElements = tbTables.get(0).getElementsByTag("tr");
            int maxSize = trElements.size();
            for (int i = 0; i < maxSize; i++) {
                if (i < 2 || i == maxSize - 1) {
                    continue;
                } else {
                    arrLearnResult.add(getAllTDElementsLearing(trElements.get(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExamResult() {
        examResultURL += code;
        arrExamResult.clear();
        Document document = null;
        try {
            //document = Jsoup.connect(examResultURL).get();
            document = Jsoup.parse(file);
            Elements tbTables = document.getElementsByClass("kTable");
            Elements trElements = tbTables.get(0).getElementsByTag("tr");
            int maxSize = trElements.size();
            for (int i = 0; i < maxSize; i++) {
                if (i < 1 || i == maxSize - 1) {
                    continue;
                } else {
                    arrExamResult.add(getAllTDElementsExam(trElements.get(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExamSchedule() {
        examSchuduleURL += code;
        arrExamSchedule.clear();
        Document document = null;
        try {
            //document = Jsoup.connect(examSchuduleURL).get();
            document = Jsoup.parse(file);
            Elements tbTables = document.getElementsByClass("kTable");
            Elements trElements = tbTables.get(0).getElementsByTag("tr");
            int maxSize = trElements.size();
            for (int i = 0; i < maxSize; i++) {
                if (i < 1 || i == maxSize - 1) {
                    continue;
                } else {
                    arrExamSchedule.add(getAllTDElementsExamSchedule(trElements.get(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ExamResultForReportItem> getArrExamReport() {
        return arrExamReport;
    }

    public void setArrExamReport(ArrayList<ExamResultForReportItem> arrExamReport) {
            this.arrExamReport = arrExamReport;
    }

    public ArrayList<LearningResultItem> getArrLearnResult() {
        return arrLearnResult;
    }

    private LearningResultItem getAllTDElementsLearing(Element element) {
        Elements tdElements = element.getElementsByTag("td");
        LearningResultItem learningResultItem;
        try {
            String tdName = tdElements.get(1).text();
            String tdPoint1 = tdElements.get(3).text();
            String tdPoint2 = tdElements.get(4).text();
            String tdPoint3 = tdElements.get(9).text();
            String tdPointPGA = tdElements.get(11).text();
            learningResultItem = new LearningResultItem(tdName, tdPoint1, tdPoint2, tdPoint3, tdPointPGA);
        } catch (Exception e) {
            return null;
        }
        return learningResultItem;
    }

    private ExamScheduleItem getAllTDElementsExamSchedule(Element element) {
        Elements tdElements = element.getElementsByTag("td");
        ExamScheduleItem examScheduleItem;
        try {
            String tdName = tdElements.get(1).text();
            String Ngay = tdElements.get(2).text();
            if (Ngay.equals("Chủ Nhật")) {
                Ngay = "CN";
            } else {
                Ngay = Ngay.substring(0, 1) + "." + Ngay.substring(Ngay.indexOf(" ") + 1);
            }
            String Thang = tdElements.get(3).text();
            String ngayThang = Ngay + "-" + Thang;
            String cathi = tdElements.get(4).text();
            String soBaoDanh = tdElements.get(5).text();
            String PhongThi = tdElements.get(7).text();
            examScheduleItem = new ExamScheduleItem(tdName, ngayThang, cathi, soBaoDanh, PhongThi);
        } catch (Exception e) {
            return null;
        }
        return examScheduleItem;
    }

    private LearningResultItem getAllTDElementsExam(Element element) {
        Elements tdElements = element.getElementsByTag("td");
        LearningResultItem learningResultItem;

        try {
            String tdName = tdElements.get(2).text();
            String tdPoint1 = tdElements.get(4).text();
            String tdPoint2 = tdElements.get(5).text();
            String tdPoint2_2 = tdElements.get(6).text();
            if (!"".equals(tdPoint2_2)) {
                tdPoint2 = tdPoint2_2;
            }
            String tdPoint3 = tdElements.get(7).text();
            String tdPointPGA = tdElements.get(8).text();
            learningResultItem = new LearningResultItem(tdName, tdPoint1, tdPoint2, tdPoint3, tdPointPGA);
        } catch (Exception e) {
            return null;
        }
        return learningResultItem;
    }

    public String getExamReport() {
        examResultURL += code;
        arrExamReport.clear();
        String ttsv = "";
        Document document = null;
        try {
            //document = Jsoup.connect(examResultURL).get();
            document = Jsoup.parse(file);
            Element tableName = document.getElementsByClass("kPanel").get(0);
            Elements strongS = tableName.getElementsByTag("strong");
            String name = strongS.get(0).text();
            String masv = strongS.get(1).text();
            String lop = strongS.get(2).text();
            ttsv = name + "-!!" + masv + "-!!" + lop;
            Log.d("TTSV", name + "-!!" + masv + "-!!" + lop);
            Elements tbTables = document.getElementsByClass("kTable");
            Elements trElements = tbTables.get(0).getElementsByTag("tr");
            int maxSize = trElements.size();
            for (int i = 0; i < maxSize; i++) {
                if (i < 1 || i == maxSize - 1) {
                    continue;
                } else {
                    ExamResultForReportItem examResultItem = getAllTDElementsExamReport(trElements.get(i));
                    arrExamReport.add(examResultItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ttsv;
    }

    private ExamResultForReportItem getAllTDElementsExamReport(Element element) {
        Elements tdElements = element.getElementsByTag("td");
        ExamResultForReportItem examResultItem;
        try {
            String name = tdElements.get(2).text();
            String tinchi = tdElements.get(3).text();
            String tdPoint3 = tdElements.get(7).text();
            String tdPointPGA = tdElements.get(8).text();
            examResultItem = new ExamResultForReportItem(name, tdPoint3, tdPointPGA, tinchi);
        } catch (Exception e) {
            return null;
        }
        return examResultItem;
    }

    public ArrayList<LearningResultItem> getArrExamResult() {
        return arrExamResult;
    }

    public void setArrExamResult(ArrayList<LearningResultItem> arrExamResult) {
        this.arrExamResult = arrExamResult;
    }

    public String getInforCode() {
        examResultURL += code;
        String ttsv = "";
        Document document = null;
        try {
            //document = Jsoup.connect(examResultURL).get();
            document = Jsoup.parse(file);
            Element tableName = document.getElementsByClass("kPanel").get(0);
            Elements strongS = tableName.getElementsByTag("strong");
            String name = strongS.get(0).text();
            String masv = strongS.get(1).text();
            String lop = strongS.get(2).text();
            ttsv = name + "-!!" + masv + "-!!" + lop;
            Log.d("TTSV", name + "-!!" + masv + "-!!" + lop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ttsv;
    }
}

