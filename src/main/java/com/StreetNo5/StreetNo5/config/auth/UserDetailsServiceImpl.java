package com.StreetNo5.StreetNo5.config.auth;

import com.StreetNo5.StreetNo5.domain.Users;
import com.StreetNo5.StreetNo5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Users findUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with this nickname. -> " + nickname));

        if(findUser != null){
            UserDetailsImpl userDetails = new UserDetailsImpl(findUser);
            return  userDetails;
        }

        return null;
    }
}