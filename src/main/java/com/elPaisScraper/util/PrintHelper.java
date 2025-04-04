package com.elPaisScraper.util;

import com.elPaisScraper.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PrintHelper {

    private static final Logger logger = LoggerFactory.getLogger(PrintHelper.class);

    public static void printArticlesAsTable(List<Article> articles) {
        StringBuilder table = new StringBuilder();
        String leftAlignFormat = "| %-40s | %-60s | %-40s |%n";

        table.append("+------------------------------------------+--------------------------------------------------------------+------------------------------------------+\n");
        table.append("| Title                                    | Content (preview)                                            | Image (downloaded path or URL)           |\n");
        table.append("+------------------------------------------+--------------------------------------------------------------+------------------------------------------+\n");

        for (Article article : articles) {
            String contentPreview = article.content().length() > 57
                    ? article.content().substring(0, 57) + "..."
                    : article.content();

            String rawFileName = article.title().replaceAll("[^a-zA-Z0-9\\-_]", "_");
            String shortFileName = rawFileName.length() > 15
                    ? rawFileName.substring(0, 15) + "{...}.jpg"
                    : rawFileName + ".jpg";

            String imageLabel = article.imageUrl() != null
                    ? "images/" + shortFileName
                    : "No image";

            table.append(String.format(leftAlignFormat, trimToLength(article.title(), 40), contentPreview, imageLabel));
        }

        table.append("+------------------------------------------+--------------------------------------------------------------+------------------------------------------+\n");

        logger.info("\n{}", table);
    }

    public static String trimToLength(String input, int maxLen) {
        return input.length() > maxLen ? input.substring(0, maxLen - 3) + "..." : input;
    }


    public static void printTranslatedTitles(List<Article> articles, List<String> translations) {
        StringBuilder table = new StringBuilder();
        table.append("+----------------------------------------------------+----------------------------------------------------+\n");
        table.append("| Title (Spanish)                                    | Title (English - Translated)                       |\n");
        table.append("+----------------------------------------------------+----------------------------------------------------+\n");

        for (int i = 0; i < articles.size(); i++) {
            String spanish = trimToLength(articles.get(i).title(), 50);
            String english = i < translations.size() && translations.get(i) != null
                    ? trimToLength(translations.get(i), 50)
                    : "[Translation Error]";
            table.append(String.format("| %-50s | %-50s |%n", spanish, english));
        }

        table.append("+----------------------------------------------------+----------------------------------------------------+\n");

        logger.info("\n{}", table);
    }

}