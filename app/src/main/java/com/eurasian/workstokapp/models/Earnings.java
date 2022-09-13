package com.eurasian.workstokapp.models;

import java.time.LocalDateTime;

public class Earnings {
    private int requestId;
    private int requestrating;
    private String clientName;
    private String completedWorkTime;
    private double earning;

    public Earnings(int requestId, int requestrating, String clientName, String completedWorkTime, double earning) {
        this.requestId = requestId;
        this.requestrating = requestrating;
        this.clientName = clientName;
        this.completedWorkTime = completedWorkTime;
        this.earning = earning;
    }

    public String getCompletedWorkTime() {
        return completedWorkTime;
    }

    public void setCompletedWorkTime(String completedWorkTime) {
        this.completedWorkTime = completedWorkTime;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getRequestrating() {
        return requestrating;
    }

    public void setRequestrating(int requestrating) {
        this.requestrating = requestrating;
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
