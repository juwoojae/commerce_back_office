package com.example.commerce_back_office.domain;

/**
 * User 의 권한을 모아둔 Enum
 */
public enum UserRole {

    CONSUMER(Authority.CONSUMER),
    ADMIN(Authority.ADMIN),;

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }
    public String getAuthority() {return this.authority;}

    public static class Authority {
       public static final String CONSUMER = "ROLE_CONSUMER";
       public static final String ADMIN = "ROLE_ADMIN";
    }
    }
