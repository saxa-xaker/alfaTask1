package ru.rcaltd.alfaTask1.service;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;
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

    public Rates getNegativeGiphy() {

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
}
