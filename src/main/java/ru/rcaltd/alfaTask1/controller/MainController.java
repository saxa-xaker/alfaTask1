package ru.rcaltd.alfaTask1.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rcaltd.alfaTask1.entity.Currency;
import ru.rcaltd.alfaTask1.entity.GifObject;
import ru.rcaltd.alfaTask1.service.NegativeGiphyLinkService;
import ru.rcaltd.alfaTask1.service.PositiveGiphyLinkService;
import ru.rcaltd.alfaTask1.service.TodayLinkService;
import ru.rcaltd.alfaTask1.service.YesterdayLinkService;

@RestController
public class MainController {

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

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy/{currencyCode}")
    public String getGiphy(@PathVariable String currencyCode) {

        Currency currentCurrency = todayLinkService.getTodayRates(currencyCode).getCurrentRates();
        Currency yesterdayCurrency = yesterdayLinkService.getYesterdayRates(currencyCode).getYesterdayRates();

        GifObject positiveGifObject = giphyPositive.getPositiveGiphy().getGiphyPositive();
        GifObject negativeGifObject = giphyNegative.getNegativeGiphy().getGiphyNegative();
        if (yesterdayCurrency.getRates().get("RUB").equals(currentCurrency.getRates().get("RUB")))
            return "Info! Rate has not changed";
        return (yesterdayCurrency.getRates().get("RUB") < currentCurrency.getRates().get("RUB"))
                ? positiveGifObject.getData().get("image_url").toString()
                : negativeGifObject.getData().get("image_url").toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy")
    public String getGiphyWithoutCurrencyCode() {
        return "Warning! Check currency code. Ex: /getGiphy/USD";
    }
}
