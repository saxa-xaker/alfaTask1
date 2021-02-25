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

import java.time.LocalDate;

@Service
public class OldLinkService {

    @Value("${FEIGN_APPID}")
    private String feignAppId;

    @Value("${FEIGN_OLDURL}")
    private String feignOldUrl;

    @Value("${FEIGN_DAYSTOBACK}")
    private int feignDaysToBack;

    // If the feignAppId = null, then we must to throw exception.INTERNAL_SERVER_ERROR
    private boolean checkFeignAppId() {
        return feignAppId == null;
    }

    // If the feignOldUrl = null, then we must to throw exception.INTERNAL_SERVER_ERROR
    private boolean checkFeignOldUrl() {
        return feignOldUrl == null;
    }

    // If the feignDaysToBack < 1, then we must to throw exception.BAD_REQUEST
    private boolean checkFeignDaysToBack() {
        return feignDaysToBack >= 1;
    }

    public Rates getOldRates(String currencyCode) {

        if (checkFeignAppId()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, " Please, check FEIGN_APPID.");
        }

        if (checkFeignOldUrl()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, " Please, check FEIGN_OLDURL.");
        }

        if (!checkFeignDaysToBack()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Please, sure FEIGN_DAYSTOBACK >= 1.");
        }

        String feignOldUrlFull = feignOldUrl
                + LocalDate.now().minusDays(feignDaysToBack)
                + ".json?app_id=" + feignAppId
                + "&base=" + currencyCode;

        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, feignOldUrlFull);
    }
}
