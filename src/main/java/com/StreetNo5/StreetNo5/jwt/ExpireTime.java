package com.StreetNo5.StreetNo5.jwt;

public class ExpireTime {
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;               //30분
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;     //30일
    public static final long REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS = REFRESH_TOKEN_EXPIRE_TIME / 1000L;
    public static final long USER_COMMENT_ALERT_TIME_FOR_REDIS = REFRESH_TOKEN_EXPIRE_TIME / 1000L;
}
