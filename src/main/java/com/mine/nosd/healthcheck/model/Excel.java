package com.mine.nosd.healthcheck.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document(collection = "excel")
public class Excel {
    @Id
    public String id;
    public byte[] excel;

    public Excel() {
    }

    public Excel(String id, byte[] excel) {
        super();
        this.id = id;
        this.excel = excel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getExcel() {
        return excel;
    }

    public void setExcel(byte[] excel) {
        this.excel = excel;
    }

    @Override
    public String toString() {
        return "Excel [id=" + id + ", excel=" + Arrays.toString(excel) + "]";
    }

}
