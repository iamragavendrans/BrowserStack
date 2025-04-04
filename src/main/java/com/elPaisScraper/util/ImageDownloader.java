package com.elPaisScraper.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class ImageDownloader {

    public static void download(String urlString, String filename) {
        try {
            URL url = URI.create(urlString).toURL();

            BufferedImage img = ImageIO.read(url);
            File outputDir = new File("src/main/resources/Images/");

            if (!outputDir.exists()) {
                boolean created = outputDir.mkdirs();
                if (!created) {
                    System.out.println("Failed to create image directory");
                    return;
                }
            }

            File outputFile = new File(outputDir, filename + ".jpg");
            ImageIO.write(img, "jpg", outputFile);

        } catch (Exception e) {
            System.out.println("Image download failed: " + e.getMessage());
        }
    }
}
