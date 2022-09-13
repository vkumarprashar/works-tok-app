package com.eurasian.workstokapp.models;

import java.util.List;



public class EarningHistory {
    private double totalEarning;
    private List<Earnings> earnings;

    public EarningHistory(double totalEarning, List<Earnings> earnings) {
        this.totalEarning = totalEarning;
        this.earnings = earnings;
    }

    public double getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(double totalEarning) {
        this.totalEarning = totalEarning;
    }

    public List<Earnings> getEarnings() {
        return earnings;
    }

    public void setEarnings(List<Earnings> earnings) {
        this.earnings = earnings;
    }
}
