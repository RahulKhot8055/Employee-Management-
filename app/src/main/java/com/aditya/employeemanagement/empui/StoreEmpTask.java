package com.aditya.employeemanagement.empui;

public class StoreEmpTask {
    String taskName;
    String taskNo;

    public StoreEmpTask(String taskName, String taskNo) {
        this.taskName = taskName;
        this.taskNo = taskNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }
}
