package com.ddf.boot.capableadmin.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * Pretty-Admin UserDetails实现了
 * </p>
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 14:50
 */
@Data
@NoArgsConstructor
public class PrettyAdminUserDetails implements UserDetails {

    private Long userId;

    private String username;

    @JsonIgnore
    private String password;

    private boolean enabled;

    private Collection<? extends GrantedAuthority> authorities;

    private Set<String> roles;

    private Set<String> permissions;

    public PrettyAdminUserDetails(SysUser user, Set<String> roles, Set<String> permissions) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.getEnabled() != null ? user.getEnabled() : true;
        this.roles = roles;
        this.permissions = permissions;

        // 将权限转换为GrantedAuthority
        if (permissions != null) {
            this.authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null && permissions != null) {
            authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
