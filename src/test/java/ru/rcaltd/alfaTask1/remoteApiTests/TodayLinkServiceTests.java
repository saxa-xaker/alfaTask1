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
class TodayLinkServiceTests {

    @Value("${FEIGN_APPID}")
    private String feignAppId;

    @Value("${FEIGN_TODAYURL}")
    private String feignTodayUrl;


    /*  Gets 401, when feignAppId is null,
    service must return Unauthorized. */
    @Test
    public void givenFeignAppIdNull_whenAppIdNullRetrieve_then401IsReceived()
            throws IOException {

        // Given
        // Make null app_id
        String feignAppIdNull = null;

        String feignAppIdNullUrl = feignTodayUrl
                + "?app_id=" + feignAppIdNull;

        HttpUriRequest request = new HttpGet(feignAppIdNullUrl);

        // When

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    /*  Get answer, when app_id wrong,
    service must return 401 code.  */
    @Test
    public void givenFeignTodayUrlWithWrongAppId_whenWrongAppIdTodayUrlRetrieve_then401IsReceived()
            throws IOException {

        // Given
        //Generate random app_id
        String feignAppIdTest = String.valueOf((int) (Math.random() * 1_000_000));
        HttpUriRequest request = new HttpGet(feignTodayUrl + "?app_id=" + feignAppIdTest);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    /*  Глюк at openexchangerates.ogr When app_id missed,
          service must answer 401 code, but getting 403.  */
    @Test
    public void givenFeignTodayUrlWithoutAppId_whenUrlWithoutAppIdRetrieve_then403IsReceived_but401IsWaiting()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(feignTodayUrl + "?app_id=&base=usd");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_FORBIDDEN));
    }

    /*  Get answer, when feignCurrencyUrl is empty,
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


    /*  200 "OK". Description: If to send request without '&base=usd', than have to get response  200 OK,
     *   because openexcangerates.org api added base attribute automatically and default value is USD  */
    @Test
    public void givenFeignTodayUrlWithoutBase_whenTodayUrlWithoutBaseRetrieve_then200IsReceived()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(feignTodayUrl + "?app_id=" + feignAppId);

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
    public void givenFeignTodayUrlWithoutCurrencyCode_whenUrlWithoutCurrencyCodeRetrieve_then403IsReceived()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet(feignTodayUrl + "?app_id=" + feignAppId + "&base=");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_FORBIDDEN));
    }
}
