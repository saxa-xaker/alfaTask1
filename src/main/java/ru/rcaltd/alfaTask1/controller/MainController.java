package ru.rcaltd.alfaTask1.controller;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rcaltd.alfaTask1.entity.Currency;
import ru.rcaltd.alfaTask1.entity.GifObject;
import ru.rcaltd.alfaTask1.repository.Rates;
import ru.rcaltd.alfaTask1.service.TodayLinkService;
import ru.rcaltd.alfaTask1.service.YesterdayLinkService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@RestController
public class MainController {
    final YesterdayLinkService yesterdayLinkService;
    final TodayLinkService todayLinkService;
    String giphyPositiveUrl;
    String giphyNegativeUrl;
    final Rates giphyPositive;
    final Rates giphyNegative;

    public MainController(YesterdayLinkService yesterdayLinkService, TodayLinkService todayLinkService) {

        Properties property = new Properties();

        try (
                FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            property.load(fis);

            String giphyApiKey = property.getProperty("GIPHY_APIKEY");

            String giphyRating = property.getProperty("GIPHY_RATING");

            String giphyPositiveTag = property.getProperty("GIPHY_POSITIVE");

            String giphyNegativeTag = property.getProperty("GIPHY_NEGATIVE");

            giphyPositiveUrl = property.getProperty("GIPHY_URL")
                    + "?api_key=" + giphyApiKey
                    + "&tag=" + giphyPositiveTag
                    + "&rating=" + giphyRating;

            giphyNegativeUrl = property.getProperty("GIPHY_URL")
                    + "?api_key=" + giphyApiKey
                    + "&tag=" + giphyNegativeTag
                    + "&rating=" + giphyRating;

        } catch (
                IOException e) {
            e.printStackTrace();
        }

        this.todayLinkService = todayLinkService;
        this.yesterdayLinkService = yesterdayLinkService;
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

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy/{currencyCode}")
    public String getGiphy(@PathVariable String currencyCode) {

        Currency currentCurrency = todayLinkService.getTodayRates(currencyCode).getCurrentRates();
        Currency yesterdayCurrency = yesterdayLinkService.getYesterdayRates(currencyCode).getYesterdayRates();

        GifObject positiveGifObject = giphyPositive.getGiphyPositive();
        GifObject negativeGifObject = giphyNegative.getGiphyPositive();

        return (yesterdayCurrency.getRates().get("RUB") < currentCurrency.getRates().get("RUB"))
                ? positiveGifObject.getData().get("image_url").toString()
                : negativeGifObject.getData().get("image_url").toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy")
    public String getGiphyWithoutCurrencyCode() {
        return "Error! You have to point currency code.";
    }
}
