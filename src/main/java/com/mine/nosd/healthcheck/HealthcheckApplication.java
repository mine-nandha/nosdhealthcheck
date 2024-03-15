package com.mine.nosd.healthcheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class HealthcheckApplication implements CommandLineRunner {
    @Value("${SERVER_PORT}")
    String port;

    public static void main(String[] args) {
        SpringApplication.run(HealthcheckApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String url = "http://localhost:" + port + "/";
        System.out.println("Started on " + url);
        openURL(url);
    }

    public static void openURL(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        try {
            if (os.contains("win")) {
                // For Windows
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                // For macOS
                rt.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                // For Linux/Unix
                String[] browsers = {"xdg-open", "gnome-open", "kde-open", "firefox", "opera", "mozilla", "konqueror", "epiphany", "netscape"};
                for (String browser : browsers) {
                    if (Runtime.getRuntime().exec(new String[]{"which", browser}).getInputStream().read() != -1) {
                        rt.exec(new String[]{browser, url});
                        break;
                    }
                }
            } else {
                System.out.println("Unsupported operating system.");
            }
        } catch (IOException e) {
            System.out.println("Error opening browser: " + e.getMessage());
        }
    }
}
