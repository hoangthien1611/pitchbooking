package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new User(user.getUserName(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
    }
}
