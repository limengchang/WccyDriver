package com.bfbyxx.wccydriver.sqlite;

import com.bfbyxx.wccydriver.utils.ToolUtil;

import java.util.UUID;

public class Phrase {
    private String mId; // 短语的唯一标识
    private String mTitle; // 标题
    private String mTime; // 时间
    private String mContent; // 短语的内容
    private int mFavorite; // 是否是收藏状态：0表示未收藏，1表示已收藏；

    public Phrase(String title,String content) {
        this(UUID.randomUUID().toString(), ToolUtil.getNowDateTime(),title, content, 0);
    }

    public Phrase(String title,String content, int favorite) {
        this(UUID.randomUUID().toString(), ToolUtil.getNowDateTime(),title, content, favorite);
    }

    public Phrase(String id,String time,String title, String content, int favorite) {
        mId = id;
        mTime = time;
        mTitle = title;
        mContent = content;
        mFavorite = favorite;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getFavorite() {
        return mFavorite;
    }

    public void setFavorite(int favorite) {
        mFavorite = favorite;
    }

    @Override
    public String toString() {
        return "\n Phrase id: " + mId
                + " title: " + mTitle
                + " content: " + mContent
                + " favorite: " + mFavorite;
    }
}
