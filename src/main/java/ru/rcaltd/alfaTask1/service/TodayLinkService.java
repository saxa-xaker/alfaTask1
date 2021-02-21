package ru.rcaltd.alfaTask1.service;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;
import ru.rcaltd.alfaTask1.repository.Rates;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class TodayLinkService {


    public Rates getTodayRates(String currencyCode) {

        Properties property = new Properties();
        String feignTodayUrl = null;
        try (
                FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            property.load(fis);
            String feignAppid = property.getProperty("FEIGN_APPID");
            feignTodayUrl = property.getProperty("FEIGN_TODAYURL")
                    + "?app_id=" + feignAppid + "&base=" + currencyCode;
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, feignTodayUrl);
    }
}
