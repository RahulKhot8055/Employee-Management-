package com.aditya.employeemanagement;
public class StoreTask {
    String taskName;
    String taskNo;
    String taskStatus;

    public StoreTask(String taskName, String taskNo, String taskStatus) {
        this.taskName = taskName;
        this.taskNo = taskNo;
        this.taskStatus = taskStatus;
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


    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

}
