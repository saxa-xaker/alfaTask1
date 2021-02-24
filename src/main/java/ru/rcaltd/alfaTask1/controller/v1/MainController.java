package ru.rcaltd.alfaTask1.controller.v1;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.rcaltd.alfaTask1.entity.GifObject;
import ru.rcaltd.alfaTask1.service.*;
import ru.rcaltd.alfaTask1.utils.StringValidator;

@RestController
@RequestMapping("/api/v1")
public class MainController {

    final CurrencyObjectService currencyObjectService;
    final TodayLinkService todayLinkService;
    final OldLinkService oldLinkService;
    final PositiveGiphyLinkService giphyPositive;
    final NegativeGiphyLinkService giphyNegative;
    final StringValidator stringValidator;
    @Value("${FEIGN_CURRENCYCODE2}")
    private String currencyCode2;
    private String currencyObjectString;

    public MainController(CurrencyObjectService currencyObjectService,
                          TodayLinkService todayLinkService,
                          OldLinkService oldLinkService,
                          PositiveGiphyLinkService giphyPositive,
                          NegativeGiphyLinkService giphyNegative, StringValidator stringValidator) {
        this.currencyObjectService = currencyObjectService;
        this.todayLinkService = todayLinkService;
        this.oldLinkService = oldLinkService;
        this.giphyPositive = giphyPositive;
        this.giphyNegative = giphyNegative;
        this.stringValidator = stringValidator;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String getRoot() {
        return "To begin, check the /api/v1/getHelp";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/getHelp")
    public String getHelp() {
        return "Hi, there!\n" +
                "This service is intended return link with animated " +
                "gif, depending on today und yesterday currency rates.\n" +
                "If the today rate with Russian rouble (RUB) " +
                "is higher, than yesterday rate, its return url with gif " +
                "from \"Rich\" category.\n " +
                "Another way is return gif from \"Broke\" category. " +
                "And another one way return message, something about, rate has not been changed.\n" +
                "To get gif, send request with currency code, like \"USD\" (default value) " +
                "or \"GBP\" (Great Britain Pound).\n" +
                "So whole request will be looks like: /api/v1/getGiphy/USD\n" +
                "You can check available currencies codes, sending request to /api/v1/getCurrencies\n" +
                "So good luck!";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getCurrencies", produces = "application/json")
    public String getCurrencies() {
        if (currencyObjectString == null) {
            String currencyRawString;
            try {
                currencyRawString = currencyObjectService
                        .getCurrencyObject()
                        .getCurrencyObject()
                        .body()
                        .toString();
            } catch (FeignException.Forbidden forbidden) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You allowed to get only" +
                        " USD rate in free mode!", forbidden);
            } catch (FeignException.MethodNotAllowed methodNotAllowed) {
                throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Please, check the" +
                        " FEIGN_CURRENCYURL value in the application.properties.");
            } catch (FeignException.Unauthorized unauthorized) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please, check the" +
                        " FEIGN_APPID value in the application.properties.");
            } catch (FeignException.GatewayTimeout gatewayTimeout) {
                throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Please, check the" +
                        " openexchangerates.org/api", gatewayTimeout);
            } catch (FeignException.InternalServerError internalServerError) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please, check the" +
                        " openexchangerates.org/api", internalServerError);
            }
            if (currencyRawString == null) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Tried to get currencies, " +
                        "but something went wrong...");
            }
            currencyObjectString = currencyRawString.replaceAll(".*\\{", "{");

            return currencyObjectString;
        }
        return currencyObjectString;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy")
    public HttpStatus getGiphyWithoutCurrencyCode() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please, " +
                "check the available currency codes at /api/v1/getCurrencies");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGiphy/{currencyCode}", produces = "application/json")
    public GifObject getGiphyUrl(@PathVariable String currencyCode) {

        // Reject, if blocked by validator
        if (!stringValidator.validate(currencyCode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please, " +
                    "check the available currency codes at /api/v1/getCurrencies");
        }

        // Reject, if found self comparison
        if (currencyCode.equalsIgnoreCase(currencyCode2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please, enter a different value than RUB");
        }

        // If list of currencies = null, trying to get it.
        if (currencyObjectString == null) {
            getCurrencies();
        }

        // Reject, If currency code, given by url was not found in the list of currencies
        if (!currencyObjectString.contains(currencyCode.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please, check " +
                    "the available currency codes at /api/v1/getCurrencies");
        }

        // Getting the current rate
        Double currentRate;
        try {
            currentRate = todayLinkService.getTodayRates(currencyCode)
                    .getTodayRates().getRates().get(currencyCode2);
        } catch (FeignException.Forbidden forbidden) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You allowed to get only" +
                    " USD rate in free mode!");
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Please, check the" +
                    " FEIGN_TODAYURL value in the application.properties.", methodNotAllowed);
        } catch (FeignException.Unauthorized unauthorized) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please, check the" +
                    " FEIGN_APPID value in the application.properties.", unauthorized);
        } catch (FeignException.GatewayTimeout gatewayTimeout) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Please, check the" +
                    " openexchangerates.org/api", gatewayTimeout);
        } catch (FeignException.InternalServerError internalServerError) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please, check the" +
                    " openexchangerates.org/api");
        }
        if (currentRate == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Tried to get rates, " +
                    "but something went wrong...");
        }

        // Getting an old rate
        Double oldRate;
        try {
            oldRate = oldLinkService.getOldRates(currencyCode)
                    .getOldRates().getRates().get(currencyCode2);
        } catch (FeignException.Forbidden forbidden) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You allowed to get only" +
                    " USD rate in free mode!", forbidden);
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Please, check the" +
                    " FEIGN_TODAYURL value in the application.properties.", methodNotAllowed);
        } catch (FeignException.Unauthorized unauthorized) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please, check the" +
                    " FEIGN_APPID value in the application.properties.", unauthorized);
        } catch (FeignException.GatewayTimeout gatewayTimeout) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Please, check the" +
                    " openexchangerates.org/api", gatewayTimeout);
        } catch (FeignException.InternalServerError internalServerError) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please, check the" +
                    " openexchangerates.org/api");
        }
        if (oldRate == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Tried to get old rates, " +
                    "but something went wrong...");
        }

        // If rate has not been changed since, return some flat phrase
        if (currentRate.equals(oldRate)) {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Not changes - not giphy!");
        }
        // If current rate higher, than yesterday, return positive object and vice versa
        return currentRate > oldRate
                ? giphyPositive.getPositiveGifObjectString()
                : giphyNegative.getNegativeGifObjectString();
    }
}
