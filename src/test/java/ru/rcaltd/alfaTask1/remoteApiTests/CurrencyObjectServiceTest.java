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

package ru.rcaltd.alfaTask1.remoteApiTests;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CurrencyObjectServiceTest {

    @Value("${FEIGN_CURRENCYURL}")
    private String feignCurrencyUrl;


    /*  Get 200 answer, when feignCurrencyUrl is correct,
    service must return 200 Ok.  */
    @Test
    public void givenFeignCurrencyCorrectUrl_whenCorrectUrlRetrieve_then200IsReceived()
            throws IOException {

        // Given
        // Generate wrong currencies link, cutting link right side
        HttpUriRequest request = new HttpGet(feignCurrencyUrl);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    /*  #Very stupid test# Get answer, when feignCurrencyUrl is empty,
    service must return ClientProtocolException. */
    @Test
    public void givenFeignEmptyUrl_whenEmptyUrlRetrieve_thenClientProtocolExceptionIsReceived()
            throws IOException {

        // Given
        // Make null currencies link
        String feignCurrencyEmptyUrl = "";
        HttpUriRequest request = new HttpGet(feignCurrencyEmptyUrl);

        // When
        try {
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        }

        // Then
        catch (ClientProtocolException protocolException) {
            assertEquals("org.apache.http.ProtocolException: Target host is not specified",
                    protocolException.getCause().toString());
        }
    }

    /*  Get answer, when feignCurrencyUrl is incorrect,
    service must return 405 code. */
    @Test
    public void givenFeignIncorrectUrl_whenIncorrectUrlRetrieve_then405IsReceived()
            throws IOException {

        // Given
        // Make incorrect currencies link, by cutting link right side
        String feignCurrencyIncorrectUrl = feignCurrencyUrl.substring(0, 39);
        HttpUriRequest request = new HttpGet(feignCurrencyIncorrectUrl);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_METHOD_NOT_ALLOWED));
    }
}