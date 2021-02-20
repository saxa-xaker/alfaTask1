package ru.rcaltd.alfaTask1.controller;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rcaltd.alfaTask1.entity.Currency;
import ru.rcaltd.alfaTask1.repository.CurrencyRates;

@RestController
public class MainController {

    final CurrencyRates currencyRates;
    String url = "https://openexchangerates.org/api/latest.json?app_id=80aa77df391c4d529ee8affddb60e2a2";

    public MainController() {
        this.currencyRates = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(CurrencyRates.class, url);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getRates")
    Currency getRates() {
        return currencyRates.getRates();
    }
}
