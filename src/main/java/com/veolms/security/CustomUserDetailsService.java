
package com.veolms.security;

import com.veolms.user.entity.User;
import com.veolms.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials")
                );

        if (user.getPasswordHash() == null) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                ))
                .build();
    }



    public String loadEmailByUserId(String userId) {
        Long id;

        try {
            id = Long.valueOf(userId);
        } catch (NumberFormatException ex) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials")
                );

        return user.getEmail();
    }
}