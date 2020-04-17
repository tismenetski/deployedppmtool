package com.tismenetski.ppmtool.security;
//A Class to hold our security constants like Addresses , Token Secret , Prefixes and Headers
public class SecurityConstants {

    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String H2_URL = "h2-console/**";
    public static final String SECRET = "SecretToGenerateJWTs";
    public static final String TOKEN_PREFIX = "Bearer "; //needs to be appended at the start of a token authorization , example : "Bearer 12353456fsdfasdasdad"
    public static final String HEADER_STRING = "Authorization"; // Header for json authorization
    public static final long EXPIRATION_TIME = 300000_000; // 30 seconds
}
