package com.parser.ytparser.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "channels", schema = "public", catalog = "product-service")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column
    private String channelName;
    @Column
    private String channelUrl;
    @Column
    private BigDecimal subscriberCount;
}
