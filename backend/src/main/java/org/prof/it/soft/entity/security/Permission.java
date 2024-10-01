package org.prof.it.soft.entity.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public enum Permission implements GrantedAuthority {

    /**
     * The permission to create a vacancy.
     */
    CREATE_VACANCY,

    /**
     * The permission to view a vacancy.
     */
    VIEW_VACANCY,

    /**
     * The permission to edit a vacancy.
     */
    EDIT_VACANCY,

    /**
     * The permission to delete a vacancy.
     */
    DELETE_VACANCY,

    /**
     * The permission to create a recruiter.
     */
    CREATE_RECRUITER,

    VIEW_RECRUITER,

    /**
     * The permission to edit a recruiter.
     */
    EDIT_RECRUITER,

    /**
     * The permission to delete a recruiter.
     */
    DELETE_RECRUITER;

    public static final Set<Permission> USER_PERMISSIONS = Set.of(
            VIEW_VACANCY
    );

    public static final Set<Permission> RECRUITER_PERMISSIONS = Set.of(
            CREATE_VACANCY,
            VIEW_VACANCY,
            EDIT_VACANCY,
            DELETE_VACANCY
    );

    public static final Set<Permission> ADMIN_PERMISSIONS = Set.of(
            CREATE_VACANCY,
            VIEW_VACANCY,
            EDIT_VACANCY,
            DELETE_VACANCY,
            CREATE_RECRUITER,
            VIEW_RECRUITER,
            EDIT_RECRUITER,
            DELETE_RECRUITER
    );

    @Override
    public String getAuthority() {
        return toString().replace("[", "").replace("]", "");
    }
}
