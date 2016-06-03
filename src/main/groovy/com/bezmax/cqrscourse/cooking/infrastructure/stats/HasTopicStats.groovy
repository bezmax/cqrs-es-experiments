package com.bezmax.cqrscourse.cooking.infrastructure.stats

import com.bezmax.cqrscourse.cooking.infrastructure.stats.TopicStats

interface HasTopicStats {
    List<TopicStats> getTopicStats();
}