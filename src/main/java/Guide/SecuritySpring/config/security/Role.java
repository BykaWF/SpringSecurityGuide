package Guide.SecuritySpring.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;


import static Guide.SecuritySpring.config.security.Permission.*;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    ADMIN_READ
            )
    ),
    USER(
            Set.of(
                    USER_READ
            )
    ),
    MANAGER_TRAINEE(
            Set.of(
                    MANAGER_READ
            )
    );


    //Roles will have Set of permissions, because we don't want to have duplicates
    private final Set<Permission> permissions;


    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
