package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.infrastructure.TopicStats

interface HasTopicStats {
    List<TopicStats> getTopicStats();
}