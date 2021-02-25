
/*  These tests working normally, only if the alfatask1 app is running and listen http://localhost:8080  */

package ru.rcaltd.alfaTask1.restApiTests;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

class MainV1ControllerTest {


    /*  Gets 200, when root url requested,
    service must return Ok. */
    @Test
    public void givenRootUrl_whenRootUrlRetrieve_then200IsReceived()
            throws IOException {

        // Given
        // Make incorrect url
        String rootUrl = "http://localhost:8080/";

        HttpUriRequest request = new HttpGet(rootUrl);

        // When

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    /*  Gets 200, when url is correct,
    service must return Ok. */
    @Test
    public void givenCorrectCurrencyCode_whenCorrectCurrencyCodeUrlRetrieve_then200IsReceived()
            throws IOException {

        // Given
        // Make icorrect currency code
        String correctCurrencyCodeUrl = "http://localhost:8080/api/v1/getGiphy/USD";

        HttpUriRequest request = new HttpGet(correctCurrencyCodeUrl);

        // When

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    /*  Gets 404, when url is incorrect,
    service must return Not found. */
    @Test
    public void givenIncorrectUrl_whenIncorrectUrlRetrieve_then404IsReceived()
            throws IOException {

        // Given
        // Make incorrect url
        String incorrectUrl = "http://localhost:8080/hsrhdbtydrtydbgfc";

        HttpUriRequest request = new HttpGet(incorrectUrl);

        // When

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_NOT_FOUND));
    }

    /*  Gets 400, when url is incorrect,
    service must return Bad request. */
    @Test
    public void givenIncorrectCurrencyCode_whenCurrencyCodeUrlRetrieve_then400IsReceived()
            throws IOException {

        // Given
        // Make incorrect currency code
        String incorrectCurrencyCodeUrl = "http://localhost:8080/api/v1/getGiphy/sdifu234";

        HttpUriRequest request = new HttpGet(incorrectCurrencyCodeUrl);

        // When

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_BAD_REQUEST));
    }

}