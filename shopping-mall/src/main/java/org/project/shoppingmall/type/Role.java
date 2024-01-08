package org.project.shoppingmall.type;

public enum Role {
    USER("ROLE_USER"),
    SELLER("ROLE_SELLER");

    private String authority;

    Role(String authority) {
        this.authority = authority;
    }
}
