package com.menachi.class3demo.Model;

public class LastUpdates {
    String tabelName;
    String lastUpdate;
    int countOfRecords;

    public LastUpdates(){

    }

    public LastUpdates(String tabelName, int countOfRecords, String lastUpdate) {
        this.tabelName = tabelName;
        this.countOfRecords = countOfRecords;
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public int getCountOfRecords() {
        return countOfRecords;
    }

    public void setCountOfRecords(int countOfRecords) {
        this.countOfRecords = countOfRecords;
    }

    public String getTabelName() {
        return tabelName;
    }

    public void setTabelName(String tabelName) {
        this.tabelName = tabelName;
    }
}
