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
public class OldLinkService {

    @Value("${FEIGN_APPID}")
    private String feignAppId;

    @Value("${FEIGN_OLDURL}")
    private String feignOldUrl;

    @Value("${FEIGN_DAYSTOBACK}")
    private int feignDaysToBack;

    public Rates getOldRates(String currencyCode) {

        String feignOldUrlFull = feignOldUrl
                + LocalDate.now().minusDays(feignDaysToBack)
                + ".json?app_id=" + feignAppId
                + "&base=" + currencyCode;

        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, feignOldUrlFull);
    }
}
