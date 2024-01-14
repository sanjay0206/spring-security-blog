package com.springboot.blog.security;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.springboot.blog.security.UserPermission.*;
import static com.springboot.blog.utils.AppConstants.ROLE_PREFIX;

@Getter
@Slf4j
public enum UserRole {
    USER(Sets.newHashSet(
            USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
            CATEGORY_READ,
            POST_READ,
            COMMENT_READ, COMMENT_CREATE, COMMENT_UPDATE, COMMENT_DELETE
    )),
    AUTHOR(Sets.newHashSet(
            USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
            CATEGORY_READ, CATEGORY_CREATE,
            POST_READ, POST_CREATE, POST_UPDATE, POST_DELETE,
            COMMENT_READ
    )),
    ADMIN(Sets.newHashSet(
            USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
            POST_READ, POST_CREATE, POST_UPDATE, POST_DELETE,
            COMMENT_READ, COMMENT_CREATE, COMMENT_UPDATE, COMMENT_DELETE,
            CATEGORY_READ, CATEGORY_CREATE, CATEGORY_UPDATE, CATEGORY_DELETE
    ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        log.info("Current user: {} -> {}", this.name(), permissions);
        permissions.add(new SimpleGrantedAuthority(ROLE_PREFIX + this.name()));
        return permissions;
    }
}