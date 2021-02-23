package ru.rcaltd.alfaTask1;

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
class TodayLinkServiceTests {

    @Value("${FEIGN_APPID}")
    private String feignAppId;

    @Value("${FEIGN_TODAYURL}")
    private String feignTodayUrl;

    @Test
    public void givenFeignUrlWithWrongAppId_whenWrongAppIdUrlRetrieve_then401IsReceived()
            throws IOException {

        // Given
        //Generate random app_id
        String feignAppIdTest = String.valueOf((int) (Math.random() * 1_000_000));
        HttpUriRequest request = new HttpGet(feignTodayUrl + "?app_id=" + feignAppIdTest + "&base=usd");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    //    BUG or mistake at openexchangerates.ogr When app_id missed, service must answer 401 code, but 403 is it.
    @Test
    public void givenFeignUrlWithoutAppId_whenUrlWithoutAppIdRetrieve_then403IsReceived()
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

    @Test
    public void givenFeignUrlWithoutBase_whenUrlWithoutBaseRetrieve_then200IsReceived()
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

    //  403 "Not allowed". Description: Changing the API `base` currency is available for Developer, Enterprise and Unlimited plan clients.
    @Test
    public void givenFeignUrlWithoutBaseValue_whenUrlWithoutBaseRetrieve_then403IsReceived()
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
