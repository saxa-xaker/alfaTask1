package ru.rcaltd.alfaTask1.repository;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.rcaltd.alfaTask1.entity.Currency;

@FeignClient(name = "currencyRates")
public interface CurrencyRates {

    @RequestMapping(method = RequestMethod.GET)
    @Headers("Content-Type: application/json")
    Currency getRates();
}
