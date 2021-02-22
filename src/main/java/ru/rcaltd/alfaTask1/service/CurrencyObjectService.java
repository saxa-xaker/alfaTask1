package ru.rcaltd.alfaTask1.service;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;
import ru.rcaltd.alfaTask1.repository.Rates;

@Service
public class CurrencyObjectService {

    @Value("${FEIGN_CURRENCYURL}")
    private String feignCurrencyUrl;

    public Rates getCurrencyObject() {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, feignCurrencyUrl);
    }
}
