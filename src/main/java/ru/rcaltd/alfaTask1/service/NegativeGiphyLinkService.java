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
import feign.FeignException;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.rcaltd.alfaTask1.entity.GifObject;
import ru.rcaltd.alfaTask1.repository.Rates;

@Service
public class NegativeGiphyLinkService {

    @Value("${GIPHY_APIKEY}")
    private String giphyApiKey;

    @Value("${GIPHY_NEGATIVE}")
    private String giphyNegativeTag;

    @Value("${GIPHY_RATING}")
    private String giphyRating;

    @Value("${GIPHY_URL}")
    private String giphyUrl;

    private boolean checkGiphyApiKey() {
        return giphyApiKey == null;
    }

    private boolean checkGiphyNegativeTag() {
        return giphyNegativeTag == null;
    }

    private boolean checkGiphyRating() {
        return giphyRating == null;
    }

    private boolean checkGiphyUrl() {
        return giphyUrl == null;
    }


    public Rates getGiphy() {

        if (checkGiphyApiKey() && checkGiphyNegativeTag()
                && checkGiphyRating() && checkGiphyUrl()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please, check giphy settings.");
        }
        String giphyNegativeUrl = giphyUrl
                + "?api_key=" + giphyApiKey
                + "&tag=" + giphyNegativeTag
                + "&rating=" + giphyRating;

        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringMvcContract())
                .target(Rates.class, giphyNegativeUrl);
    }

    public GifObject getNegativeGifObjectString() {
        GifObject negativeGifObject;
        try {
            negativeGifObject = getGiphy().getGiphyNegative();
        } catch (FeignException.Forbidden forbidden) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please, check the" +
                    " GIPHY_APIKEY value in the application.properties.", forbidden);
        } catch (FeignException.MethodNotAllowed methodNotAllowed) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Please, check the" +
                    " GIPHY_URL value in the application.properties.");
        } catch (FeignException.Unauthorized unauthorized) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please, check the" +
                    " GIPHY_APIKEY value in the application.properties.", unauthorized);
        } catch (FeignException.GatewayTimeout gatewayTimeout) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Please, check the" +
                    " api.giphy.com");
        } catch (FeignException.InternalServerError internalServerError) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please, check the" +
                    " api.giphy.com");
        }
        if (negativeGifObject == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Tried to get negative gif, " +
                    "but something went wrong...");
        }

        return negativeGifObject;
    }
}