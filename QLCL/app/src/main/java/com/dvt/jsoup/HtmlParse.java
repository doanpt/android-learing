package com.dvt.jsoup;

import android.content.Context;
import android.util.Log;

import com.dvt.item.ExamResultForReportItem;
import com.dvt.item.ExamScheduleItem;
import com.dvt.item.LearningResultItem;
import com.dvt.util.CommonMethod;
import com.dvt.util.CommonValue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Android on 12/14/2015.
 */
public class HtmlParse {


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

    public HtmlParse() {
//        this.file = file;
    }

    public String getResultLearning(int id, String mcode, Context context) {
        String ttsv = "";
        try {
            Document document = null;
            if (id == 1) {
                String learningResultURL = "http://qlcl.edu.vn/examre/ket-qua-hoc-tap.htm?code=";
                learningResultURL += mcode;
                document = Jsoup.connect(learningResultURL).get();
            } else {
                String dataExam = CommonMethod.getInstance().readFromFile(CommonValue.LEARNING_FILE, context);
                document = Jsoup.parse(dataExam);
            }
            arrLearnResult.clear();
            Element tableName = document.getElementsByClass("kPanel").get(0);
            Elements strongS = tableName.getElementsByTag("strong");
            String name = strongS.get(0).text();
            String masv = strongS.get(1).text();
            String lop = strongS.get(2).text();
            ttsv = name + "-!!" + masv + "-!!" + lop;
            Log.d("TTSV", name + "-!!" + masv + "-!!" + lop);

            //document = Jsoup.parse(file);
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
        return ttsv;
    }

    public String getExamResult(int id, String mcode, Context context) {
        String ttsv = "";
        try {
            Document document = null;
            if (id == 1) {
                String examResultURL = "http://qlcl.edu.vn/viewstudent/ket-qua-thi.htm?code=";
                examResultURL += mcode;
                document = Jsoup.connect(examResultURL).get();
            } else {
                String dataExam = CommonMethod.getInstance().readFromFile(CommonValue.EXAM_RESULT_FILE, context);
                document = Jsoup.parse(dataExam);
            }
            arrExamResult.clear();
            Element tableName = document.getElementsByClass("kPanel").get(0);
            Elements strongS = tableName.getElementsByTag("strong");
            String name = strongS.get(0).text();
            String masv = strongS.get(1).text();
            String lop = strongS.get(2).text();
            ttsv = name + "-!!" + masv + "-!!" + lop;
            //document = Jsoup.parse(file);
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
        return ttsv;
    }

    public String getExamSchedule(int id, String mcode, Context context) {
        String ttsv = "";
        try {
            Document document = null;
            if (id == 1) {
                String examSchuduleURL = "http://qlcl.edu.vn/examplanuser/ke-hoach-thi.htm?code=";
                examSchuduleURL += mcode;
                document = Jsoup.connect(examSchuduleURL).get();
            } else {
                String dataExam = CommonMethod.getInstance().readFromFile(CommonValue.EXAM_SCHEDULE_FILE, context);
                document = Jsoup.parse(dataExam);
            }
            arrExamSchedule.clear();
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
                    arrExamSchedule.add(getAllTDElementsExamSchedule(trElements.get(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ttsv;
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

    public String getExamReport(int id, String mcode, Context context) {
        String ttsv = "";
        try {
            Document document = null;
            if (id == 1) {
                String examResultURL = "http://qlcl.edu.vn/viewstudent/ket-qua-thi.htm?code=";
                examResultURL += mcode;
                document = Jsoup.connect(examResultURL).get();
            } else {
                String dataExam = CommonMethod.getInstance().readFromFile(CommonValue.EXAM_RESULT_FILE, context);
                document = Jsoup.parse(dataExam);
            }
            arrExamReport.clear();
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

    public String getInforCode(Context context) {
        String ttsv = "";
        Document document = null;
        try {
            String file = CommonMethod.getInstance().readFromFile(CommonValue.LEARNING_FILE, context);
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

    public void getAllDataForFirstStart(Context context,String code) {
        try {
            getDataExamResult(context,code);
            getDataSchedule(context,code);
            getDataLearn(context,code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDataExamResult(Context context, String code) throws IOException {
        String examResultURL = "http://qlcl.edu.vn/viewstudent/ket-qua-thi.htm?code=";
        examResultURL += code;
        Document documentLearn = Jsoup.connect(examResultURL).get();
        String dataLearn = documentLearn.toString();
        CommonMethod.getInstance().writeToFile(dataLearn, CommonValue.EXAM_RESULT_FILE, context);
    }

    public void getDataSchedule(Context context, String code) throws IOException {
        String examSchuduleURL = "http://qlcl.edu.vn/examplanuser/ke-hoach-thi.htm?code=";
        examSchuduleURL += code;
        Document documentLearn = Jsoup.connect(examSchuduleURL).get();
        String dataLearn = documentLearn.toString();
        CommonMethod.getInstance().writeToFile(dataLearn, CommonValue.EXAM_SCHEDULE_FILE, context);
    }

    public void getDataLearn(Context context, String code) throws IOException {
        String learningResultURL = "http://qlcl.edu.vn/examre/ket-qua-hoc-tap.htm?code=";
        learningResultURL += code;
        Document documentLearn = Jsoup.connect(learningResultURL).get();
        String dataLearn = documentLearn.toString();
        CommonMethod.getInstance().writeToFile(dataLearn, CommonValue.LEARNING_FILE, context);
    }
}

