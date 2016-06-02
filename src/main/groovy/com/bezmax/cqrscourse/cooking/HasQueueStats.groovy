package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.infrastructure.MessageStats

interface HasQueueStats {
    List<MessageStats> getQueueStats();
}