package com.springboot.blog.security;

import lombok.Getter;

@Getter
public enum UserPermission {
    POST_READ("SCOPE_post:read"),
    POST_UPDATE("SCOPE_post:update"),
    POST_CREATE("SCOPE_post:create"),
    POST_DELETE("SCOPE_post:delete"),

    COMMENT_READ("SCOPE_comment:read"),
    COMMENT_UPDATE("SCOPE_comment:update"),
    COMMENT_CREATE("SCOPE_comment:create"),
    COMMENT_DELETE("SCOPE_comment:delete"),

    USER_READ("SCOPE_user:read"),
    USER_UPDATE("SCOPE_user:update"),
    USER_CREATE("SCOPE_user:create"),
    USER_DELETE("SCOPE_user:delete"),

    CATEGORY_READ("SCOPE_category:read"),
    CATEGORY_UPDATE("SCOPE_category:update"),
    CATEGORY_CREATE("SCOPE_category:create"),
    CATEGORY_DELETE("SCOPE_category:delete");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

}
