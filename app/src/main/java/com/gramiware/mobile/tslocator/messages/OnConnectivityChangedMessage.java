package com.gramiware.mobile.tslocator.messages;

/**
 * Created by r.ghali on 1/27/2018.
 */

public class OnConnectivityChangedMessage extends BaseMessage {

    public boolean IsConnected;

    public OnConnectivityChangedMessage(boolean isConnected) {
        IsConnected = isConnected;
    }
}
