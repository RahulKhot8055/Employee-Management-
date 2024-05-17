package com.aditya.employeemanagement;

public class SimpleEmpData {
    private String empName;
    private String empDesg;
    private String empId;
    private String empMob;
    private String empMail;
    private String empEdu;
    private String empBasePay;

    public String getEmpEdu() {
        return empEdu;
    }

    public void setEmpEdu(String empEdu) {
        this.empEdu = empEdu;
    }

    public String getEmpBasePay() {
        return empBasePay;
    }

    public void setEmpBasePay(String empBasePay) {
        this.empBasePay = empBasePay;
    }
    public SimpleEmpData(String empName, String empDesg, String empId, String empMob, String empMail, String empEdu, String empBasePay) {
        this.empName = empName;
        this. empDesg = empDesg;
        this.empId= empId;
        this.empMob = empMob;
        this.empMail = empMail;
        this.empEdu = empEdu;
        this.empBasePay = empBasePay;
    }

    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public String getEmpName() {
        return empName;
    }
    public void setEmpName(String empName) {
        this.empName = empName;
    }
    public String getEmpDesg() {
        return empDesg;
    }
    public void setEmpDesg(String empDesg) {
        this.empDesg = empDesg;
    }
    public String getEmpMail() {
        return empMail;
    }
    public void setEmpMail(String empMail) {
        this.empMail = empMail;
    }
    public String getEmpMob() {
        return empMob;
    }
    public void setEmpMob(String empMob) {
        this.empMob = empMob;
    }

}