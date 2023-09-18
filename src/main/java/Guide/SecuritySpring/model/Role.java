package Guide.SecuritySpring.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

import java.util.Set;


import static Guide.SecuritySpring.model.Permission.*;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    ADMIN_READ,
                    USER_UPDATE,
                    USER_DELETE,
                    USER_CREATE,
                    USER_READ
            )
    ),
    USER(
            Set.of(
                    USER_UPDATE,
                    USER_DELETE,
                    USER_CREATE,
                    USER_READ
            )
    ),
    GEUST(Collections.emptySet());


    //Roles will have Set of permissions, because we don't want to have duplicates
    private final Set<Permission> permissions;

}
