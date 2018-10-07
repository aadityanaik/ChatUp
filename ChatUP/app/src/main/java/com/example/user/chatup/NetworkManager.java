package com.example.user.chatup;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkManager {
    String getDataFromURL(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(500);
        connection.setReadTimeout(500);

        Scanner scanner = new Scanner(
                new InputStreamReader(connection.getInputStream())
        );

        StringBuilder response = new StringBuilder();

        while(scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }

        connection.disconnect();

        return response.toString();
    }
}
