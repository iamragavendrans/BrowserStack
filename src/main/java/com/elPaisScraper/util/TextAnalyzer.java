package com.elPaisScraper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(TextAnalyzer.class);

    public static void analyze(List<String> translatedTitles) {
        Map<String, Integer> wordCounts = getStringIntegerMap(translatedTitles);

        StringBuilder output = new StringBuilder();

        output.append(String.format("%-45s%n", "Repeated words (more than twice):"));
        output.append("+----------------------+------------+\n");
        output.append(String.format("| %-20s | %-10s |%n", "Word", "Count"));
        output.append("+----------------------+------------+\n");

        wordCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 2)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> output.append(
                        String.format("| %-20s | %-10d |%n", entry.getKey(), entry.getValue()))
                );

        output.append("+----------------------+------------+");

        logger.info("\n{}", output);
    }

    private static Map<String, Integer> getStringIntegerMap(List<String> translatedTitles) {
        Map<String, Integer> wordCounts = new HashMap<>();

        for (String title : translatedTitles) {
            if (title == null || title.isBlank()) continue;

            String cleaned = title.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
            String[] words = cleaned.split("\\s+");

            for (String word : words) {
                if (word.length() > 2) {
                    wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                }
            }
        }

        return wordCounts;
    }
}
