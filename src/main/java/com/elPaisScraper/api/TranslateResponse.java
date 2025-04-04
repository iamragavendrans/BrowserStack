package com.elPaisScraper.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslateResponse {

    @JsonProperty("trans")
    private String translatedText;

    public String getTranslatedText() {
        return translatedText;
    }

}
