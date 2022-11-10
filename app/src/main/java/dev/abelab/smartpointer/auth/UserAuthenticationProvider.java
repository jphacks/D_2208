package dev.abelab.smartpointer.auth;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

/**
 * User Authentication Provider
 */
public class UserAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void doAfterPropertiesSet() {}

}
