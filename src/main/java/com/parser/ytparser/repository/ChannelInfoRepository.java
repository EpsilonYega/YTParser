package com.parser.ytparser.repository;

import com.parser.ytparser.model.ChannelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelInfoRepository extends JpaRepository<ChannelInfo, Long> {
}
