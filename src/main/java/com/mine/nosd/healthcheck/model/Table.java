package com.mine.nosd.healthcheck.model;

import java.util.List;

public class Table {
    public List<String> thead;
    public List<List<String>> tbody;

    public int rowCount;
    public int colCount;

    public Table(List<String> thead, List<List<String>> tbody, int rowCount, int colCount) {
        this.thead = thead;
        this.tbody = tbody;
        this.rowCount = rowCount;
        this.colCount = colCount;
    }

    public Table() {
    }

    public List<String> getThead() {
        return thead;
    }

    public void setThead(List<String> thead) {
        this.thead = thead;
    }

    public List<List<String>> getTbody() {
        return tbody;
    }

    public void setTbody(List<List<String>> tbody) {
        this.tbody = tbody;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }
}
