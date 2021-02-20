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

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

@RestController
public class MainController {

    static String feignCurrentUrl;
    static String feignYesterdayUrl;
    final CurrencyRates currentRates;
    final CurrencyRates yesterdayRates;
    String feignAppid;

    public MainController() {

        LocalDate yesterday = LocalDate.now().minusDays(100);

        Properties property = new Properties();
        try (
                FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            property.load(fis);

            feignAppid = property.getProperty("FEIGN_APPID");
            System.out.println(feignAppid);

            feignCurrentUrl = property.getProperty("FEIGN_CURRENTURL") + "?app_id=" + feignAppid;
            System.out.println(feignCurrentUrl);

            feignYesterdayUrl = property.getProperty("FEIGN_YESTERDAYURL") + yesterday + ".json?app_id=" + feignAppid;
            System.out.println(feignYesterdayUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.yesterdayRates = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(CurrencyRates.class, feignYesterdayUrl);

        this.currentRates = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(CurrencyRates.class, feignCurrentUrl);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/getRates")
    Currency getRates() {
        System.out.println("ALL RIGHT");
        Currency currentCurrency = currentRates.getCurrentRates();
        System.out.println("Today = " + currentCurrency.getRates().get("RUB"));
        Currency yesterdayCurrency = yesterdayRates.getYesterdayRates();
        System.out.println("Yesterday = " + yesterdayCurrency.getRates().get("RUB"));
        return yesterdayCurrency;
    }

}
