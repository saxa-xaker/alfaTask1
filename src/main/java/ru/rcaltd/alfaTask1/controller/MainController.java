package ru.rcaltd.alfaTask1.controller;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rcaltd.alfaTask1.entity.Currency;
import ru.rcaltd.alfaTask1.entity.GifObject;
import ru.rcaltd.alfaTask1.service.*;

@RestController
public class MainController {

    @Value("${FEIGN_CURRENCYCODE2}")
    private String currencyCode2;
    final CurrencyObjectService currencyObjectService;
    final TodayLinkService todayLinkService;
    final YesterdayLinkService yesterdayLinkService;
    final PositiveGiphyLinkService giphyPositive;
    final NegativeGiphyLinkService giphyNegative;

    public MainController(CurrencyObjectService currencyObjectService, TodayLinkService todayLinkService,
                          YesterdayLinkService yesterdayLinkService,
                          PositiveGiphyLinkService giphyPositive,
                          NegativeGiphyLinkService giphyNegative) {
        this.currencyObjectService = currencyObjectService;
        this.todayLinkService = todayLinkService;
        this.yesterdayLinkService = yesterdayLinkService;
        this.giphyPositive = giphyPositive;
        this.giphyNegative = giphyNegative;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String getRoot() {
        return "Info! Check the weather with Ex: /getGiphy/usd";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy")
    public String getGiphyWithoutCurrencyCode() {
        return "WARNING! Please check the currency code. Ex: /getGiphy/usd";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy/{currencyCode}")
    public String getGiphyUrl(@PathVariable String currencyCode) {

        if (currencyCode.equalsIgnoreCase(currencyCode2)) {
            return "ERROR! Please enter a different value than RUB";
        }

        String currencyObjectString;
        try {
            currencyObjectString = currencyObjectService
                    .getCurrencyObject()
                    .getCurrencyObject()
                    .body()
                    .toString()
                    .replaceAll(".*\\{", "{");
        } catch (FeignException.Forbidden forbidden) {
            return "WARNING! Not allowed!";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return "Please check the FEIGN_CURRENCYURL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return "Please check the FEIGN_APPID value in the application.properties.";
        }

        if (!currencyObjectString.contains(currencyCode.toUpperCase())) {
            return "WARNING! Please check the currency code. Ex: /getGiphy/usd";
        }

        Currency currentCurrency;
        try {
            currentCurrency = todayLinkService.getTodayRates(currencyCode).getTodayRates();
        } catch (FeignException.Forbidden forbidden) {
            return "WARNING! You allowed to get only USD rate in free mode!";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return "Please check the FEIGN_TODAYURL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return "Please check the FEIGN_APPID value in the application.properties.";
        }

        Currency yesterdayCurrency;
        try {
            yesterdayCurrency = yesterdayLinkService.getYesterdayRates(currencyCode).getYesterdayRates();
        } catch (FeignException.Forbidden forbidden) {
            return "WARNING! You allowed to get only USD rate in free mode!";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return "Please check the FEIGN_YESTERDAYURL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return "Please check the FEIGN_APPID value in the application.properties.";
        }

        if (currentCurrency.getRates().get(currencyCode2).equals(yesterdayCurrency.getRates().get(currencyCode2))) {
            return "INFO! Rate has not changed!";
        }

        GifObject positiveGifObject;
        try {
            positiveGifObject = giphyPositive.getGiphy().getGiphyPositive();
        } catch (FeignException.Forbidden forbidden) {
            return "ERROR! Please check the GIPHY_APIKEY value in the application.properties.";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return "ERROR! Please check the GIPHY_URL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return "ERROR! Unauthorized!";
        }

        GifObject negativeGifObject;
        try {
            negativeGifObject = giphyNegative.getGiphy().getGiphyNegative();
        } catch (FeignException.Forbidden forbidden) {
            return "ERROR! Please check the GIPHY_APIKEY value in the application.properties.";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return "ERROR! Please check the GIPHY_URL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return "ERROR! Unauthorized!";
        }

        if (currentCurrency.getRates().get(currencyCode2) > yesterdayCurrency.getRates().get(currencyCode2)) {
            return positiveGifObject.getData().get("image_url").toString();
        } else {
            return negativeGifObject.getData().get("image_url").toString();
        }
    }
}
