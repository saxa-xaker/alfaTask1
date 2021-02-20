package ru.rcaltd.alfaTask1.repository;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.rcaltd.alfaTask1.entity.Currency;
import ru.rcaltd.alfaTask1.entity.GifObject;

@FeignClient(name = "rates")
public interface Rates {

    @RequestMapping(method = RequestMethod.GET)
    @Headers("Content-Type: application/json")
    Currency getCurrentRates();

    @RequestMapping(method = RequestMethod.GET)
    @Headers("Content-Type: application/json")
    Currency getYesterdayRates();

    @RequestMapping(method = RequestMethod.GET)
    @Headers("Content-Type: application/json")
    GifObject getGiphyPositive();

    @RequestMapping(method = RequestMethod.GET)
    @Headers("Content-Type: application/json")
    GifObject getGiphyNegative();


}
