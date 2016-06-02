package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.infrastructure.MessageStats

interface HasMessageStats {
    List<MessageStats> getMessageStats();
}