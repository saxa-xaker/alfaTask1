package ru.rcaltd.alfaTask1.entity;

import lombok.Data;

import java.util.HashMap;

@Data
public class Currency {

    private String disclaimer;

    private String license;

    private long timestamp;

    private String base;

    private HashMap<String, Double> rates;

    public Currency() {
    }
}
