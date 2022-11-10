package dev.abelab.smartpointer.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.abelab.smartpointer.domain.model.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Login User Details
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUserDetails extends UserModel implements UserDetails {

    /**
     * authorities
     */
    Collection<? extends GrantedAuthority> authorities;

    public LoginUserDetails(final UserModel userModel, final Collection<? extends GrantedAuthority> authorities) {
        this.setId(userModel.getId());
        this.setRoomId(userModel.getRoomId());
        this.setName(userModel.getName());
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return this.getId();
    }

    @Override
    public String getPassword() {
        return null;
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
        return true;
    }

}
