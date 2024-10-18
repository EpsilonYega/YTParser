package com.parser.ytparser.service;

import com.parser.ytparser.dto.YouTubeChannelItem;
import com.parser.ytparser.dto.YouTubeChannelResponse;
import com.parser.ytparser.dto.YouTubeSearchItem;
import com.parser.ytparser.dto.YouTubeSearchResponse;
import com.parser.ytparser.model.ChannelInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.TableStyleType.headerRow;

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
        String searchUrl = UriComponentsBuilder.fromHttpUrl(SEARCH_URL)
                .queryParam("part", "snippet")
                .queryParam("q", query)
                .queryParam("type", "channel")
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        YouTubeSearchResponse searchResponse = restTemplate.getForObject(searchUrl, YouTubeSearchResponse.class);

        if (searchResponse != null && searchResponse.getItems() != null) {
            List<String> channelIds = new ArrayList<>();
            for (YouTubeSearchItem item : searchResponse.getItems()) {
                channelIds.add(item.getId().getChannelId());
            }

            String channelIdsParam = String.join(",", channelIds);
            String channelUrl = UriComponentsBuilder.fromHttpUrl(CHANNEL_URL)
                    .queryParam("part", "statistics,snippet")
                    .queryParam("id", channelIdsParam)
                    .queryParam("key", apiKey)
                    .build()
                    .toUriString();

            YouTubeChannelResponse channelResponse = restTemplate.getForObject(channelUrl, YouTubeChannelResponse.class);

            if (channelResponse != null && channelResponse.getItems() != null) {
                for (YouTubeChannelItem channelItem : channelResponse.getItems()) {
                    BigDecimal subscriberCount = channelItem.getStatistics().getSubscriberCount();
                    if (subscriberCount.doubleValue() >= minSubscribers.doubleValue()) {
                        ChannelInfo channelInfo = new ChannelInfo();
                        channelInfo.setChannelName(channelItem.getSnippet().getTitle());
                        channelInfo.setChannelUrl("https://www.youtube.com/channel/" + channelItem.getId());
                        channelInfo.setSubscriberCount(subscriberCount);
                        channels.add(channelInfo);
                    }
                }
            }
        }
        writeChannelsToExcel(channels, "channels.xlsx");
        return channels;
    }
    public void writeChannelsToExcel(List<ChannelInfo> channels, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("YouTube Channels");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Channel Name");
        headerRow.createCell(1).setCellValue("Channel URL");
        headerRow.createCell(2).setCellValue("Subscriber Count");

        int rowNum = 1;
        for (ChannelInfo channel : channels) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(channel.getChannelName());
            row.createCell(1).setCellValue(channel.getChannelUrl());
            row.createCell(2).setCellValue(channel.getSubscriberCount().doubleValue());
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                workbook.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}