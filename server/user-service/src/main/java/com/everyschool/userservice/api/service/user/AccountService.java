package com.everyschool.userservice.api.service.user;

import com.everyschool.userservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Transactional
public class AccountService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<com.everyschool.userservice.domain.user.User> findUser = userRepository.findByEmail(email);

        if (findUser.isEmpty()) {
            throw new UsernameNotFoundException("등록되지 않은 사용자입니다.");
        }

        com.everyschool.userservice.domain.user.User user = findUser.get();
        return new User(user.getEmail(), user.getPwd(),
            true, true, true, true,
            new ArrayList<>());
    }

    public com.everyschool.userservice.domain.user.User getUserDetailsByEmail(String email) {
        Optional<com.everyschool.userservice.domain.user.User> findMember = userRepository.findByEmail(email);

        if (findMember.isEmpty()) {
            throw new UsernameNotFoundException("등록되지 않은 사용자입니다.");
        }

        return findMember.get();
    }

    public void saveFcmToken(String userKey, String fcmToken) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        operations.set(userKey, fcmToken, 30, TimeUnit.DAYS);
    }
}
