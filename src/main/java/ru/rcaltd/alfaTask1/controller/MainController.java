package ru.rcaltd.alfaTask1.controller;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rcaltd.alfaTask1.entity.Currency;
import ru.rcaltd.alfaTask1.service.*;

@RestController
public class MainController {

    final CurrencyObjectService currencyObjectService;
    Currency currentCurrency;
    @Value("${FEIGN_CURRENCYCODE2}")
    private String currencyCode2;
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
        return "Warning! Check currency code. Ex: /getGiphy/usd";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy/{currencyCode}")
    public String getGiphyUrl(@PathVariable String currencyCode) {

        feign.Response currencyObjects = currencyObjectService.getCurrencyObject().getCurrencyObject();
        String currencyObjectString = currencyObjects.body().toString().replaceAll(".*\\{", "{");

        if (currencyObjectString.contains(currencyCode.toUpperCase())) {
            System.out.println("INFO! All right! Let's continue.");
        } else {
            return "WARNING! CHECK THE CURRENCY CODE!";
        }

        try {
            currentCurrency = todayLinkService.getTodayRates(currencyCode).getTodayRates();
        } catch (FeignException.Forbidden forbidden) {
            return "INFO! You allowed to get only USD rate in demo mode!";
        }
        Currency yesterdayCurrency = yesterdayLinkService.getYesterdayRates(currencyCode).getYesterdayRates();

        if (currentCurrency.getRates().get(currencyCode2).equals(yesterdayCurrency.getRates().get(currencyCode2))) {
            return "INFO! Rate has not changed!";
        }

        return (currentCurrency.getRates().get(currencyCode2) > yesterdayCurrency.getRates().get(currencyCode2))
                ? giphyPositive.getGiphy().getGiphyPositive().getData().get("image_url").toString()
                : giphyNegative.getGiphy().getGiphyNegative().getData().get("image_url").toString();
    }
}
