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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest
class PositiveGiphyLinkServiceTest {

    @Value("${GIPHY_APIKEY}")
    private String giphyApiKey;

    @Value("${GIPHY_POSITIVE}")
    private String giphyPositiveTag;

    @Value("${GIPHY_RATING}")
    private String giphyRating;

    @Value("${GIPHY_URL}")
    private String giphyUrl;


    /*  Gets 403, when giphyApiKey is null,
     * service must return Forbidden. Description: You weren't authorized
     * to make your request; most likely this indicates an issue with your API Key. */
    @Test
    public void givenGiphyApiKeyNull_whenApiKeyNullRetrieve_then403IsReceived()
            throws IOException {

        // Given
        // Make null api key
        String giphyApiKeyNull = null;
        String giphyApiKeyNullUrl = giphyUrl
                + "?api_key=" + giphyApiKeyNull
                + "&tag=" + giphyPositiveTag
                + "&rating=" + giphyRating;

        HttpUriRequest request = new HttpGet(giphyApiKeyNullUrl);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_FORBIDDEN));
    }


    /*  When api key is incorrect,
     * service must return 403 code. Description: You weren't authorized
     * to make your request; most likely this indicates an issue with your API Key. */
    @Test
    public void givenIncorrectGiphyApiKey_whenIncorrectApiKeyRetrieve_then403IsReceived()
            throws IOException {

        // Given
        // Make incorrect api key
        String incorrectGiphyApiKey = "bi75yctrdh___t5h56fg";
        String incorrectGiphyApiKeyUrl = giphyUrl
                + "?api_key=" + incorrectGiphyApiKey
                + "&tag=" + giphyPositiveTag
                + "&rating=" + giphyRating;

        HttpUriRequest request = new HttpGet(incorrectGiphyApiKeyUrl);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_FORBIDDEN));
    }

    /*  Get answer, when api key is absent,
     * service must return 403 code. Description: You weren't authorized
     * to make your request; most likely this indicates an issue with your API Key. */
    @Test
    public void givenWithoutGiphyApiKey_whenWithoutApiKeyUrlRetrieve_then403IsReceived()
            throws IOException {

        // Given
        // Make url without api key
        String withoutGiphyApiKeyUrl = giphyUrl
                + "?api_key="
                + "&tag=" + giphyPositiveTag
                + "&rating=" + giphyRating;

        HttpUriRequest request = new HttpGet(withoutGiphyApiKeyUrl);

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_FORBIDDEN));
    }
}