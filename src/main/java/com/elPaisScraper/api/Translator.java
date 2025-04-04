package com.elPaisScraper.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Translator {

    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final String API_KEY = dotenv.get("RAPIDAPI_KEY");
    private static final String HOST = "google-translate113.p.rapidapi.com";
    private static final String ENDPOINT = "https://google-translate113.p.rapidapi.com/api/v1/translator/text";

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Logger logger = LoggerFactory.getLogger(Translator.class);

    public static String translate(String originalText) {
        try {
            if (API_KEY == null || API_KEY.isBlank()) {
                System.err.println("Missing API Key. Please set RAPIDAPI_KEY in environment variables.");
                return "[Translation API Key Missing]";
            }

            TranslateRequest requestBody = new TranslateRequest("es", "en", originalText);
            String requestJson = mapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", HOST)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                try {
                    TranslateResponse translated = mapper.readValue(response.body(), TranslateResponse.class);
                    if (translated.getTranslatedText() != null && !translated.getTranslatedText().isBlank()) {
                        return translated.getTranslatedText();
                    } else {
                        logger.error("Translation returned null/blank for input: {}", originalText);
                        return "[Translation Missing in Response]";
                    }
                } catch (Exception parseError) {
                    logger.error("Parsing error for input: \"{}\" — {}", originalText, parseError.getMessage());
                    return "[Translation Parsing Error]";
                }
            } else {
                logger.error("API call failed for input: \"{}\" — Status: {}", originalText, response.statusCode());
                return "[Translation Failed: Status " + response.statusCode() + "]";
            }

        } catch (Exception e) {
            logger.error("Translation Exception for input: \"{}\" — {}", originalText, e.getMessage());
            return "[Translation Exception]";
        }
    }
}
