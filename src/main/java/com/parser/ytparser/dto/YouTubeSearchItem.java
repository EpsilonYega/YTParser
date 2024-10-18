package com.parser.ytparser.dto;

import lombok.Data;

@Data
public class YouTubeSearchItem {
    private YouTubeId id;
    private YouTubeSnippet snippet;
}
