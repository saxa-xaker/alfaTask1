package ru.rcaltd.alfaTask1.entity;

import lombok.Data;

@Data
public class Rates {

    private String currencyCode;
    private double currencyRate;

    public Rates() {
    }

}
