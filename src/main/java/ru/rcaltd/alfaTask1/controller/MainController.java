package ru.rcaltd.alfaTask1.controller;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    String curObjStr;
    String currencyObjectString;
    final CurrencyObjectService currencyObjectService;
    final TodayLinkService todayLinkService;
    final YesterdayLinkService yesterdayLinkService;
    final PositiveGiphyLinkService giphyPositive;
    final NegativeGiphyLinkService giphyNegative;

    public MainController(CurrencyObjectService currencyObjectService,
                          TodayLinkService todayLinkService,
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
        return "To begin, check the /getHelp";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getHelp")
    public String getHowToUse() {
        return "Hi, there!\n" +
                "This service is intended return link with animated " +
                "gif, depending on today und yesterday currency rates.\n" +
                "If the today rate with Russian rouble (RUB) " +
                "is higher, than yesterday rate, its return url with gif " +
                "from \"Rich\" category.\n " +
                "Another way is return gif from \"Broke\" category. " +
                "And other way return message, something about, rate has not been changed.\n" +
                "To get gif, send request with currency code, like \"USD\" (default value) " +
                "or \"GBP\" (Great Britain Pound).\n" +
                "So whole request will be looks like: /getGiphy/USD\n" +
                "You can check available currencies codes, sending request to /getCurrencies\n" +
                "So good luck!";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getCurrencies")
    public String getCurrencies() {
        try {
            curObjStr = currencyObjectService
                    .getCurrencyObject()
                    .getCurrencyObject()
                    .body()
                    .toString();
        } catch (FeignException.Forbidden forbidden) {
            return HttpStatus.FORBIDDEN.toString() + " You allowed to get only" +
                    " USD rate in free mode!";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return HttpStatus.METHOD_NOT_ALLOWED.toString() + " Please check the" +
                    " FEIGN_CURRENCYURL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return HttpStatus.UNAUTHORIZED.toString() + " Please check the" +
                    " FEIGN_APPID value in the application.properties.";
        } catch (FeignException.GatewayTimeout gatewayTimeout) {
            return HttpStatus.GATEWAY_TIMEOUT.toString() + " Please check the" +
                    " openexchangerates.org/api";
        } catch (FeignException.InternalServerError internalServerError) {
            return HttpStatus.INTERNAL_SERVER_ERROR.toString() + " Please check the" +
                    " openexchangerates.org/api";
        }
        if (curObjStr == null)
            return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
        currencyObjectString = curObjStr.replaceAll(".*\\{", "{");

        return currencyObjectString;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy")
    public String getGiphyWithoutCurrencyCode() {
        return HttpStatus.FORBIDDEN + " Please check /help page.";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy/{currencyCode}")
    public String getGiphyUrl(@PathVariable String currencyCode) {

        if (currencyCode.equalsIgnoreCase(currencyCode2)) {
            return HttpStatus.BAD_REQUEST + " Please enter a different value than RUB";
        }
        if (currencyObjectString == null) {
            getCurrencies();
        }
        if (!currencyObjectString.contains(currencyCode.toUpperCase())) {
            return HttpStatus.NOT_FOUND + " Please check the available currencies codes /getCurrencies";
        }

        Currency currentCurrency;
        try {
            currentCurrency = todayLinkService.getTodayRates(currencyCode).getTodayRates();

        } catch (FeignException.Forbidden forbidden) {
            return HttpStatus.FORBIDDEN.toString() + " You allowed to get only" +
                    " USD rate in free mode!";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return HttpStatus.METHOD_NOT_ALLOWED.toString() + " Please check the" +
                    " FEIGN_YESTERDAYURL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return HttpStatus.UNAUTHORIZED.toString() + " Please check the" +
                    " FEIGN_APPID value in the application.properties.";
        } catch (FeignException.GatewayTimeout gatewayTimeout) {
            return HttpStatus.GATEWAY_TIMEOUT.toString() + " Please check the" +
                    " openexchangerates.org/api";
        } catch (FeignException.InternalServerError internalServerError) {
            return HttpStatus.INTERNAL_SERVER_ERROR.toString() + " Please check the" +
                    " openexchangerates.org/api";
        }
        if (currentCurrency == null)
            return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();

        Currency yesterdayCurrency;
        try {
            yesterdayCurrency = yesterdayLinkService
                    .getYesterdayRates(currencyCode).getYesterdayRates();
        } catch (FeignException.Forbidden forbidden) {
            return HttpStatus.FORBIDDEN.toString() + " You allowed to get only" +
                    " USD rate in free mode!";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return HttpStatus.METHOD_NOT_ALLOWED.toString() + " Please check the" +
                    " FEIGN_YESTERDAYURL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return HttpStatus.UNAUTHORIZED.toString() + " Please check the" +
                    " FEIGN_APPID value in the application.properties.";
        } catch (FeignException.GatewayTimeout gatewayTimeout) {
            return HttpStatus.GATEWAY_TIMEOUT.toString() + " Please check the" +
                    " openexchangerates.org/api";
        } catch (FeignException.InternalServerError internalServerError) {
            return HttpStatus.INTERNAL_SERVER_ERROR.toString() + " Please check the" +
                    " openexchangerates.org/api";
        }
        if (yesterdayCurrency == null)
            return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();

        if (currentCurrency.getRates().get(currencyCode2)
                .equals(yesterdayCurrency.getRates().get(currencyCode2))) {
            return HttpStatus.NOT_MODIFIED + " Rate has not changed!";
        }

        GifObject positiveGifObject;
        try {
            positiveGifObject = giphyPositive.getGiphy().getGiphyPositive();
        } catch (FeignException.Forbidden forbidden) {
            return HttpStatus.FORBIDDEN.toString() + " Please check the" +
                    " GIPHY_APIKEY value in the application.properties.";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return HttpStatus.METHOD_NOT_ALLOWED.toString() + " Please check the" +
                    " GIPHY_URL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return HttpStatus.UNAUTHORIZED.toString() + " Please check the" +
                    " GIPHY_APIKEY value in the application.properties.";
        } catch (FeignException.GatewayTimeout gatewayTimeout) {
            return HttpStatus.GATEWAY_TIMEOUT.toString() + " Please check the" +
                    " api.giphy.com";
        } catch (FeignException.InternalServerError internalServerError) {
            return HttpStatus.INTERNAL_SERVER_ERROR.toString() + " Please check the" +
                    " api.giphy.com";
        }
        if (positiveGifObject == null)
            return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();

        GifObject negativeGifObject;
        try {
            negativeGifObject = giphyNegative.getGiphy().getGiphyNegative();
        } catch (FeignException.Forbidden forbidden) {
            return HttpStatus.FORBIDDEN.toString() + " Please check the" +
                    " GIPHY_APIKEY value in the application.properties.";
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            return HttpStatus.METHOD_NOT_ALLOWED.toString() + " Please check the" +
                    " GIPHY_URL value in the application.properties.";
        } catch (FeignException.Unauthorized unauthorized) {
            return HttpStatus.UNAUTHORIZED.toString() + " Please check the" +
                    " GIPHY_APIKEY value in the application.properties.";
        } catch (FeignException.GatewayTimeout gatewayTimeout) {
            return HttpStatus.GATEWAY_TIMEOUT.toString() + " Please check the" +
                    " api.giphy.com";
        } catch (FeignException.InternalServerError internalServerError) {
            return HttpStatus.INTERNAL_SERVER_ERROR.toString() + " Please check the" +
                    " api.giphy.com";
        }
        if (negativeGifObject == null)
            return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();

        return currentCurrency.getRates().get(currencyCode2) > yesterdayCurrency.getRates().get(currencyCode2)
                ? positiveGifObject.getData().get("image_url").toString()
                : negativeGifObject.getData().get("image_url").toString();
    }
}
