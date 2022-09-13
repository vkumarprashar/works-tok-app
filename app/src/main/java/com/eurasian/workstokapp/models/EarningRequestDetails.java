package com.eurasian.workstokapp.models;

public class EarningRequestDetails {
    private String workTypeName;
    private Double esp;
    private Double wrp;
    private Double hbp;
    private Double scr;
    private Double ccr;
    private Double qer;

    public EarningRequestDetails(String workTypeName, Double esp, Double wrp, Double hbp, Double scr, Double ccr, Double qer) {
        this.workTypeName = workTypeName;
        this.esp = esp;
        this.wrp = wrp;
        this.hbp = hbp;
        this.scr = scr;
        this.ccr = ccr;
        this.qer = qer;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public Double getEsp() {
        return esp;
    }

    public void setEsp(Double esp) {
        this.esp = esp;
    }

    public Double getWrp() {
        return wrp;
    }

    public void setWrp(Double wrp) {
        this.wrp = wrp;
    }

    public Double getHbp() {
        return hbp;
    }

    public void setHbp(Double hbp) {
        this.hbp = hbp;
    }

    public Double getScr() {
        return scr;
    }

    public void setScr(Double scr) {
        this.scr = scr;
    }

    public Double getCcr() {
        return ccr;
    }

    public void setCcr(Double ccr) {
        this.ccr = ccr;
    }

    public Double getQer() {
        return qer;
    }

    public void setQer(Double qer) {
        this.qer = qer;
    }
}
