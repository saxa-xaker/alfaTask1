package ru.rcaltd.alfaTask1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rcaltd.alfaTask1.entity.Currency;
import ru.rcaltd.alfaTask1.service.NegativeGiphyLinkService;
import ru.rcaltd.alfaTask1.service.PositiveGiphyLinkService;
import ru.rcaltd.alfaTask1.service.TodayLinkService;
import ru.rcaltd.alfaTask1.service.YesterdayLinkService;

@RestController
public class MainController {

    @Value("${FEIGN_CURRENCYCODE2}")
    private String currenceCode2;

    final TodayLinkService todayLinkService;
    final YesterdayLinkService yesterdayLinkService;
    final PositiveGiphyLinkService giphyPositive;
    final NegativeGiphyLinkService giphyNegative;

    public MainController(TodayLinkService todayLinkService,
                          YesterdayLinkService yesterdayLinkService,
                          PositiveGiphyLinkService giphyPositive,
                          NegativeGiphyLinkService giphyNegative) {

        this.todayLinkService = todayLinkService;
        this.yesterdayLinkService = yesterdayLinkService;
        this.giphyPositive = giphyPositive;
        this.giphyNegative = giphyNegative;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String getRoot() {
        return "Info! Check the weather with Ex: /getGiphy/USD";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy")
    public String getGiphyWithoutCurrencyCode() {
        return "Warning! Check currency code. Ex: /getGiphy/USD";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy/{currencyCode}")
    public String getGiphyUrl(@PathVariable String currencyCode) {

        Currency currentCurrency = todayLinkService.getTodayRates(currencyCode).getCurrentRates();
        Currency yesterdayCurrency = yesterdayLinkService.getYesterdayRates(currencyCode).getYesterdayRates();

        if (currentCurrency.getRates().get(currenceCode2).equals(yesterdayCurrency.getRates().get(currenceCode2))) {
            return "Info! Rate has not changed";
        }

        return (currentCurrency.getRates().get(currenceCode2) > yesterdayCurrency.getRates().get(currenceCode2))
                ? giphyPositive.getGiphy().getGiphyPositive().getData().get("image_url").toString()
                : giphyNegative.getGiphy().getGiphyNegative().getData().get("image_url").toString();
    }
}
