package com.ByteDance.Gotlin.im.info;

/**
 * @author: Hx
 * @date: 2022年06月12日 19:03
 */
public class Message {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_PICTURE = 1;
    public static final int MYSELF = 1;
    public static final int FRIENDS = 0;

    public String content;
    public int type;
    public int from;

    public Message(String content, int type, int from) {
        this.content = content;
        this.type = type;
        this.from = from;
    }
}
