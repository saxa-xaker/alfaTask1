/*
 * *
 *  * Copyright 2009-2021 The Rcaltd
 *  *
 *  * Licensed under theGNU LESSER GENERAL PUBLIC LICENSE Version 2.1 (the "License");
 *  * you may not use this file except
 *  * in compliance with the License. You may obtain a copy of the License at
 *  *
 *  * https://www.gnu.org/licenses/old-licenses/lgpl-2.1.ru.html
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License
 *  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  * or implied. See the License for the specific language governing permissions and limitations under
 *  * the License.
 *
 */

package ru.rcaltd.alfaTask1.service;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.rcaltd.alfaTask1.repository.Rates;

@Service
public class CurrencyObjectService {

    @Value("${FEIGN_CURRENCYURL}")
    private String feignCurrencyUrl;

    private boolean checkFeignCurrencyUrl() {
        return feignCurrencyUrl == null;
    }

    public Rates getCurrencyObject() {

        if (checkFeignCurrencyUrl()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, " Please, check FEIGN_CURRENCYURL.");
        }

        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, feignCurrencyUrl);
    }
}
