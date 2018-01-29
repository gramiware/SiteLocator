package com.gramiware.mobile.tslocator.messages;

/**
 * Created by r.ghali on 1/27/2018.
 */

public class BaseMessage {

    private Object _Source;
    private Object _Tag;
    private int _RequestCode;

    public int getRequestCode() {
        return _RequestCode;
    }

    public void setRequestCode(int _RequestCode) {
        this._RequestCode = _RequestCode;
    }

    public Object getSource() {
        return _Source;
    }

    public void setSource(Object _Source) {
        this._Source = _Source;
    }

    public Object getTag() {
        return _Tag;
    }

    public void setTag(Object _Tag) {
        this._Tag = _Tag;
    }

    public BaseMessage() {
    }

    public BaseMessage(Object source, Object tag) {
        this._Source = source;
        this._Tag = tag;
    }
}
