package com.parser.ytparser.service;

import com.parser.ytparser.dto.YouTubeChannelItem;
import com.parser.ytparser.dto.YouTubeChannelResponse;
import com.parser.ytparser.dto.YouTubeSearchItem;
import com.parser.ytparser.dto.YouTubeSearchResponse;
import com.parser.ytparser.model.ChannelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";
    private static final String CHANNEL_URL = "https://www.googleapis.com/youtube/v3/channels";

    private final RestTemplate restTemplate;

    @Autowired
    public YouTubeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ChannelInfo> searchChannels(String query, BigDecimal minSubscribers) {
        List<ChannelInfo> channels = new ArrayList<>();

        // 1. Выполните запрос к SEARCH_URL с параметром q=query
        String searchUrl = UriComponentsBuilder.fromHttpUrl(SEARCH_URL)
                .queryParam("part", "snippet")
                .queryParam("q", query)
                .queryParam("type", "channel")
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        YouTubeSearchResponse searchResponse = restTemplate.getForObject(searchUrl, YouTubeSearchResponse.class);

        if (searchResponse != null && searchResponse.getItems() != null) {
            // 2. Получите ID каналов из ответа
            List<String> channelIds = new ArrayList<>();
            for (YouTubeSearchItem item : searchResponse.getItems()) {
                channelIds.add(item.getId().getChannelId());
            }

            // 3. Выполните запрос к CHANNEL_URL для получения информации о каналах
            String channelIdsParam = String.join(",", channelIds);
            String channelUrl = UriComponentsBuilder.fromHttpUrl(CHANNEL_URL)
                    .queryParam("part", "statistics,snippet")
                    .queryParam("id", channelIdsParam)
                    .queryParam("key", apiKey)
                    .build()
                    .toUriString();

            YouTubeChannelResponse channelResponse = restTemplate.getForObject(channelUrl, YouTubeChannelResponse.class);

            // 4. Фильтруйте каналы по количеству подписчиков
            if (channelResponse != null && channelResponse.getItems() != null) {
                for (YouTubeChannelItem channelItem : channelResponse.getItems()) {
                    BigDecimal subscriberCount = channelItem.getStatistics().getSubscriberCount();
                    if (subscriberCount.doubleValue() >= minSubscribers.doubleValue()) {
                        ChannelInfo channelInfo = new ChannelInfo();
                        channelInfo.setChannelName(channelItem.getSnippet().getTitle());
                        channelInfo.setChannelUrl("https://www.youtube.com/channel/" + channelItem.getId());
                        channelInfo.setSubscriberCount(subscriberCount);
                        // Добавьте другие параметры, такие как количество видео, язык и т.д.
                        channels.add(channelInfo);
                    }
                }
            }
        }
        return channels;
    }
}