package com.diamondLounge.settings.security;

import com.diamondLounge.MVC.services.UserService;
import com.diamondLounge.entity.db.Authority;
import com.diamondLounge.entity.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        UserBuilder userBuilder;

        if (user != null) {
            String[] authorities = user.getAuthorities().stream()
                    .map(Authority::getAuthority)
                    .toArray(String[]::new);

            userBuilder = withUsername(email)
                    .disabled(false)
                    .password(user.getPassword())
                    .authorities(authorities);
        } else {
            throw new UsernameNotFoundException("User not found!");
        }

        return userBuilder.build();
    }

}
