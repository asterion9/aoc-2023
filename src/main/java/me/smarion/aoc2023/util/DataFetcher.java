package me.smarion.aoc2023.util;


import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;

public class DataFetcher {

    private static String cookie;

    public static String fetch(int day) throws IOException, InterruptedException {
        if (cookie == null) {
            cookie = Files.asCharSource(Paths.get("target", "cookie").toFile(), Charsets.UTF_8).read();
        }

        String fileName = Hashing.sha256().newHasher().putInt(day).putUnencodedChars(cookie).hash().toString();

        File inputFile = Paths.get("target", "inputs", fileName).toFile();

        if (inputFile.exists()) {
            return Files.asCharSource(inputFile, Charsets.UTF_8).read();
        } else {
            inputFile.getParentFile().mkdirs();
        }

        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://adventofcode.com/2023/day/" + day + "/input"))
                .header("Cookie", cookie)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Files.asCharSink(inputFile, Charsets.UTF_8).write(response.body());

        return response.body();
    }
}