package ru.rcaltd.alfaTask1.controller;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rcaltd.alfaTask1.entity.Currency;
import ru.rcaltd.alfaTask1.entity.GifObject;
import ru.rcaltd.alfaTask1.repository.Rates;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

@RestController
public class MainController {

    final Rates currentRates;
    final Rates yesterdayRates;
    final Rates giphyPositive;
    final Rates giphyNegative;
    private String feignCurrentUrl;
    private String feignYesterdayUrl;
    private String feignAppid;
    private String giphyApiKey;
    private String giphyRating;
    private String giphyPositiveTag;
    private String giphyNegativeTag;
    private String giphyPositiveUrl;
    private String giphyNegativeUrl;


    public MainController() {

        LocalDate yesterday = LocalDate.now().minusDays(1);

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

            giphyApiKey = property.getProperty("GIPHY_APIKEY");
            System.out.println(giphyApiKey);

            giphyRating = property.getProperty("GIPHY_RATING");
            System.out.println(giphyRating);

            giphyPositiveTag = property.getProperty("GIPHY_POSITIVE");
            System.out.println(giphyPositiveTag);

            giphyNegativeTag = property.getProperty("GIPHY_NEGATIVE");
            System.out.println(giphyNegativeTag);

            giphyPositiveUrl = property.getProperty("GIPHY_URL")
                    + "?api_key=" + giphyApiKey
                    + "&tag=" + giphyPositiveTag
                    + "&rating=" + giphyRating;
            System.out.println(giphyPositiveUrl);

            giphyNegativeUrl = property.getProperty("GIPHY_URL")
                    + "?api_key=" + giphyApiKey
                    + "&tag=" + giphyNegativeTag
                    + "&rating=" + giphyRating;
            System.out.println(giphyNegativeUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.yesterdayRates = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, feignYesterdayUrl);

        this.currentRates = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, feignCurrentUrl);

        this.giphyPositive = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, giphyPositiveUrl);

        this.giphyNegative = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, giphyNegativeUrl);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/getRates")
    public String getRates() {
        Currency currentCurrency = currentRates.getCurrentRates();
        Currency yesterdayCurrency = yesterdayRates.getYesterdayRates();

        GifObject positiveGifObject = giphyPositive.getGiphyPositive();
        GifObject negativeGifObject = giphyNegative.getGiphyPositive();

        return (yesterdayCurrency.getRates().get("RUB") < currentCurrency.getRates().get("RUB"))
                ? positiveGifObject.getData().get("image_url").toString()
                : negativeGifObject.getData().get("image_url").toString();
    }

}
