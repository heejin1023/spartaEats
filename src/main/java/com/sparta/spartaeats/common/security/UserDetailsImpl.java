package com.sparta.spartaeats.common.security;

import com.sparta.spartaeats.common.type.UserRoleEnum;
import com.sparta.spartaeats.user.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User loginUserInfo;

    public UserDetailsImpl(User loginUserInfo) {
        this.loginUserInfo = loginUserInfo;
    }

    public User getUser() {
        return loginUserInfo;
    }

    public String userId() {
        return loginUserInfo.getUserId();
    }

    @Override
    public String getPassword() {
        return loginUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return loginUserInfo.getUserName();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = loginUserInfo.getUserRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
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