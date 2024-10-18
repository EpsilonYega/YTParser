package com.parser.ytparser.dto;

import java.util.List;

public class YouTubeChannelResponse {
    private List<YouTubeChannelItem> items;

    public List<YouTubeChannelItem> getItems() {
        return items;
    }

    public void setItems(List<YouTubeChannelItem> items) {
        this.items = items;
    }
}
