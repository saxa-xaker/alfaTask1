/*
 * *
 *  * Copyright 2009-2022 The Rcaltd
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest
class OldLinkServiceTests {

    @Value("${FEIGN_APPID}")
    private String feignAppId;

    @Value("${FEIGN_OLDURL}")
    private String feignOldUrl;

    @Value("${FEIGN_DAYSTOBACK}")
    private int feignDaysToBack;

    /*      Get answer, when app_id wrong,
    service must return 401 code. */
    @Test
    public void givenFeignOldUrlWithWrongAppId_whenWrongAppIdUrlRetrieve_then401IsReceived()
            throws IOException {

        // Given
        //Generate random app_id
        String feignAppIdTest = String.valueOf((int) (Math.random() * 1_000_000_000));
        HttpUriRequest request = new HttpGet(feignOldUrl
                + LocalDate.now().minusDays(feignDaysToBack) +
                ".json?app_id=" + feignAppIdTest + "&base=usd");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    /*      Possible "Глюк" at openexchangerates.ogr
            When app_id missed, service must answer 401 code, but getting 403. */
    @Test
    public void givenFeignOldUrlWithoutAppId_whenUrlWithoutAppIdRetrieve_then403IsReceived()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(feignOldUrl
                + LocalDate.now().minusDays(feignDaysToBack) +
                ".json?app_id=&base=usd");
        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_FORBIDDEN));
    }

    /* We send request without currency code (for ex: USD or EUR) and get normal answer, because
            the USD code is default code (see openexchangerates.org) */
    @Test
    public void givenFeignOldUrlWithoutBase_whenUrlWithoutBaseRetrieve_then200IsReceived()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(feignOldUrl
                + LocalDate.now().minusDays(feignDaysToBack) +
                ".json?app_id=" + feignAppId);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    /*  403 "Not allowed". Description: Changing the API `base` currency is available
    for Developer, Enterprise and Unlimited plan clients.  */
    @Test
    public void givenFeignOldUrlWithoutBaseValue_whenUrlWithoutBaseRetrieve_then403IsReceived()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(feignOldUrl
                + LocalDate.now().minusDays(feignDaysToBack) +
                ".json?app_id=" + feignAppId + "&base=");
        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_FORBIDDEN));
    }

    /*  400 "Bad Request". Description: Client requested rates for an unsupported base currency.  */
    @Test
    public void givenFeignOldUrlWithZeroDate_whenOldUrlZeroDateRetrieve_then400IsReceived()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(feignOldUrl + "0" + ".json?app_id=" + feignAppId + "&base=usd");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    /*  400 "Bad Request". Description: Client requested rates for an unsupported base currency.  */
    @Test
    public void givenFeignOldUrlWithoutDate_whenOldUrlWithoutDateRetrieve_then400IsReceived()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(feignOldUrl + ".json?app_id=" + feignAppId + "&base=USD");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_BAD_REQUEST));
    }
}
