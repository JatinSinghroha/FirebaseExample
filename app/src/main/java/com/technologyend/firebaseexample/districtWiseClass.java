package com.technologyend.firebaseexample;

public class districtWiseClass {

        private String districtName;
        private String confirmed, recovered, active, dead;

        public districtWiseClass(){

        }

    public districtWiseClass(String districtName, String confirmed, String recovered, String active, String dead) {
        this.districtName = districtName;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.active = active;
        this.dead = dead;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDead() {
        return dead;
    }

    public void setDead(String dead) {
        this.dead = dead;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }


}
