package com.sappyoak.soulbound;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.Set;
import java.util.stream.Collectors;

import com.sappyoak.soulbound.text.TextProvider;

public class UpdateChecker {
    private static final String REPO = "Sappyoak/SoulBound";
    private static final String GITHUB_API_BASE = "https://api.github.com/repos/" + REPO;
    private static final String LATEST_RELEASE_URL = GITHUB_API_BASE + "/releases/latest";

    private final SoulBound plugin;    
    private final String runningVersion;
    private final HttpClient client;

    private String latestRelease = null;

    public UpdateChecker(SoulBound plugin) {
        this.plugin = plugin;
        this.runningVersion = plugin.getDescription().getVersion();
        this.client = HttpClient.newHttpClient();
    }

    public void check() {
        latestRelease = getLatestRelease().join();
        if (latestRelease != runningVersion) {
            Component updateMessage = createUpdateMessage(latestRelease);
            for (Player player : getOnlineOperators()) {
                player.sendMessage(updateMessage);
            }
        }
    }

    private CompletableFuture<String> getLatestRelease() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(LATEST_RELEASE_URL))
            .timeout(Duration.ofSeconds(20))
            .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(response -> {
                return new Gson().fromJson(response.body(), JsonObject.class)
                    .get("tag_name")
                    .getAsString()
                    .replace("v", "");
            });
    }

    private Set<Player> getOnlineOperators() {
        return Bukkit.getOnlinePlayers().stream()
            .filter(player -> player.isOp())
            .collect(Collectors.toSet());
    }

    private Component createUpdateMessage(String latest) {
        return TextProvider.deserialize(
            "<aqua>This server is currently running version: " + runningVersion + "of SoulBound. <newline>" +
            "This is not the latest version. <a:" + LATEST_RELEASE_URL + ">Click here to get version: " + latest + "</a>" 
        );
    }
}
