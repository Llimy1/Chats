package org.project.shoppingmall.type;

public enum Role {
    USER("ROLE_USER"),
    SELLER("ROLE_SELLER");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
