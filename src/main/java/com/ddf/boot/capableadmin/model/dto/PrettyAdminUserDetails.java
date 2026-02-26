package com.ddf.boot.capableadmin.model.dto;

import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class PrettyAdminUserDetails {

    private Long userId;

    private String username;

    @JsonIgnore
    private String password;

    private boolean enabled;

    private Set<String> roles;

    private Set<String> permissions;

    public PrettyAdminUserDetails(SysUser user, Set<String> roles, Set<String> permissions) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.getEnabled() != null ? user.getEnabled() : true;
        this.roles = roles;
        this.permissions = permissions;
    }
}
