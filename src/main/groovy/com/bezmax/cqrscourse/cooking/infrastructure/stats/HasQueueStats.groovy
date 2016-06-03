package com.bezmax.cqrscourse.cooking.infrastructure.stats

import com.bezmax.cqrscourse.cooking.infrastructure.stats.MessageStats

interface HasQueueStats {
    List<MessageStats> getQueueStats();
}