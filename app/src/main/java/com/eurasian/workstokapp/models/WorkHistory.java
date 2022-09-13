package com.eurasian.workstokapp.models;

public class WorkHistory {
    private int requestId;
    private String clientName;
    private String workTypeName;
    private String completedWorkTime;
    private int executionTimeInMinutes;
    private double earning;

    public WorkHistory(int requestId,String completedWorkTime, String clientName,String WorkTypeName, int executionTimeInMinutes, double earning) {
        this.requestId = requestId;
        this.completedWorkTime = completedWorkTime;
        this.clientName = clientName;
        this.workTypeName = WorkTypeName;
        this.executionTimeInMinutes = executionTimeInMinutes;
        this.earning = earning;
    }

    public String getCompletedWorkTime() {
        return completedWorkTime;
    }

    public void setCompletedWorkTime(String completedWorkTime) {
        this.completedWorkTime = completedWorkTime;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public void setExecutionTimeInMinutes(int executionTimeInMinutes) {
        this.executionTimeInMinutes = executionTimeInMinutes;
    }

    public int getExecutionTimeInMinutes() {
        return executionTimeInMinutes;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public double getEarning() {
        return earning;
    }

    public void setEarning(double earning) {
        this.earning = earning;
    }
}
