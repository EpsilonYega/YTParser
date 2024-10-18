package com.parser.ytparser.dto;

import lombok.Data;

@Data
public class YouTubeChannelItem {
    private String id;
    private YouTubeSnippet snippet;
    private YouTubeStatistics statistics;
}
