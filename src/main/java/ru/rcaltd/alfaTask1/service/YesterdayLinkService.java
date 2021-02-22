package ru.rcaltd.alfaTask1.service;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;
import ru.rcaltd.alfaTask1.repository.Rates;

import java.time.LocalDate;

@Service
public class YesterdayLinkService {

    @Value("${FEIGN_APPID}")
    private String feignAppid;

    @Value("${FEIGN_YESTERDAYURL}")
    private String feignYesterdayUrl;

    public Rates getYesterdayRates(String currencyCode) {

        String feignYesterdayUrlFull = feignYesterdayUrl
                + LocalDate.now().minusDays(1)
                + ".json?app_id=" + feignAppid
                + "&base=" + currencyCode;

        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, feignYesterdayUrlFull);
    }
}
