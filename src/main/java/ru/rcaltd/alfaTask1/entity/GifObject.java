package ru.rcaltd.alfaTask1.entity;

import lombok.Data;

import java.util.HashMap;

@Data
public class GifObject {

    private HashMap<String, Object> data;
    private HashMap<String, Object> meta;

    public GifObject() {
    }
}
