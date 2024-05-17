package com.aditya.employeemanagement;

public class StoreEmpl {

    String strEmpId, strEmpName, strEmpDesg, strEmpMob, strEmpEdu;
    String strEmpBasicPay;

    public String getStrEmpMail() {
        return strEmpMail;
    }

    public void setStrEmpMail(String strEmpMail) {
        this.strEmpMail = strEmpMail;
    }

    String strEmpMail;

    public String getStrEmpPass() {
        return strEmpPass;
    }

    public void setStrEmpPass(String strEmpPass) {
        this.strEmpPass = strEmpPass;
    }

    String strEmpPass;

    public StoreEmpl(String strEmpId, String strEmpName, String strEmpDesg, String strEmpMob, String strEmpMail, String strEmpEdu, String strEmpBasicPay, String strEmpPass) {
        this.strEmpId = strEmpId;
        this.strEmpName = strEmpName;
        this.strEmpDesg = strEmpDesg;
        this.strEmpMob = strEmpMob;
        this.strEmpMail = strEmpMail;
        this.strEmpEdu = strEmpEdu;
        this.strEmpBasicPay = strEmpBasicPay;
        this.strEmpPass = strEmpPass;
    }

    public String getStrEmpId() {
        return strEmpId;
    }

    public void setStrEmpId(String strEmpId) {
        this.strEmpId = strEmpId;
    }

    public String getStrEmpName() {
        return strEmpName;
    }

    public void setStrEmpName(String strEmpName) {
        this.strEmpName = strEmpName;
    }

    public String getStrEmpDesg() {
        return strEmpDesg;
    }

    public void setStrEmpDesg(String strEmpDesg) {
        this.strEmpDesg = strEmpDesg;
    }

    public String getStrEmpMob() {
        return strEmpMob;
    }

    public void setStrEmpMob(String strEmpMob) {
        this.strEmpMob = strEmpMob;
    }

    public String getStrEmpEdu() {
        return strEmpEdu;
    }

    public void setStrEmpEdu(String strEmpEdu) {
        this.strEmpEdu = strEmpEdu;
    }

    public String getStrEmpBasicPay() {
        return strEmpBasicPay;
    }

    public void setStrEmpBasicPay(String strEmpBasicPay) {
        this.strEmpBasicPay = strEmpBasicPay;
    }
}
