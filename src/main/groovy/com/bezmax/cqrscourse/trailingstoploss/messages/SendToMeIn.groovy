package com.bezmax.cqrscourse.trailingstoploss.messages

import com.bezmax.cqrscourse.trailingstoploss.Message

class SendToMeIn implements Message {
    int seconds
    def message
}
