package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repasitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
   /* private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);*/


    private final UserRepository userRepository;


        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            User user = userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new CurrentUser(user);

        }


    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user != null) {
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            if (Objects.equals(username, "adminn")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            logger.debug(String.format("User with name: %s and password: %s created.", user.getUsername(), user.getPassword()));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("User " + username + " not found!");
        }

    }*/

}
