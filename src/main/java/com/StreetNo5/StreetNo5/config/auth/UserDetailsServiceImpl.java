package com.StreetNo5.StreetNo5.config.auth;

import com.StreetNo5.StreetNo5.entity.User;
import com.StreetNo5.StreetNo5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User findUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with this nickname. -> " + nickname));


        return new org.springframework.security.core.userdetails.User(findUser.getNickname(), findUser.getPassword(), AuthorityUtils.createAuthorityList(findUser.getRole().toString()));
    }
}