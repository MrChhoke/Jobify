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
     * The permissions to apply vacancy.
     */
    APPLY_VACANCY,

    /**
     * The permission to view own applications.
     */
    VIEW_OWN_APPLICATIONS,

    /**
     * The permission to create a recruiter.
     */
    CREATE_RECRUITER,

    /**
     * The permission to view a recruiter.
     */
    VIEW_RECRUITER,

    /**
     * The permission to edit a recruiter.
     */
    EDIT_RECRUITER,

    /**
     * The permission to delete a recruiter.
     */
    DELETE_RECRUITER,

    /**
     * The permission to view recruiter own applications.
     */
    VIEW_RECRUITER_OWN_APPLICATIONS,

    /**
     * The permission to view recruiter own vacancies.
     */
    VIEW_RECRUITER_OWN_VACANCIES,

    /**
     * The permission to generate a vacancy report.
     */
    GENERATE_VACANCY_REPORT,

    /**
     * The permission to view vacancy applications.
     */
    VIEW_VACANCY_APPLICATIONS,

    /**
     * The permission to view person applications.
     */
    VIEW_PERSON_APPLICATIONS,

    /**
     * The permission to update own profile.
     */
    UPDATE_OWN_PROFILE,

    /**
     * The permission to get own profile.
     */
    GET_OWN_PROFILE;



    public static final Set<Permission> USER_PERMISSIONS = Set.of(
            VIEW_VACANCY,
            APPLY_VACANCY,
            GET_OWN_PROFILE,
            VIEW_OWN_APPLICATIONS,
            UPDATE_OWN_PROFILE
    );

    public static final Set<Permission> RECRUITER_PERMISSIONS = Set.of(
            CREATE_VACANCY,
            VIEW_VACANCY,
            EDIT_VACANCY,
            DELETE_VACANCY,
            GET_OWN_PROFILE,
            UPDATE_OWN_PROFILE,
            VIEW_RECRUITER_OWN_APPLICATIONS,
            VIEW_RECRUITER_OWN_VACANCIES
    );

    public static final Set<Permission> ADMIN_PERMISSIONS = Set.of(
            CREATE_VACANCY,
            VIEW_VACANCY,
            EDIT_VACANCY,
            DELETE_VACANCY,
            CREATE_RECRUITER,
            VIEW_RECRUITER,
            EDIT_RECRUITER,
            DELETE_RECRUITER,
            APPLY_VACANCY,
            GET_OWN_PROFILE,
            UPDATE_OWN_PROFILE,
            GENERATE_VACANCY_REPORT,
            VIEW_VACANCY_APPLICATIONS,
            VIEW_PERSON_APPLICATIONS
    );

    @Override
    public String getAuthority() {
        return toString().replace("[", "").replace("]", "");
    }
}
