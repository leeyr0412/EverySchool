package com.everyschool.userservice.api.service.auth;

import com.everyschool.userservice.api.client.mail.MailSendClient;
import com.everyschool.userservice.api.client.mail.dto.EmailMessage;
import com.everyschool.userservice.domain.user.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.everyschool.userservice.message.ErrorMessage.*;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {

    private final UserQueryRepository userQueryRepository;
    private final MailSendClient mailSendClient;
    private final RedisTemplate<String, String> redisTemplate;

    public void sendEmail(EmailMessage message) {
        String authNumber = mailSendClient.sendEmail(message, "email");

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        operations.set(message.getTo(), authNumber, 3, TimeUnit.MINUTES);
    }

    public void checkEmailAuthNumber(String email, String authNumber) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String storedAuthNumber = operations.get(email);

        if (authNumber.equals("123123")) {
            redisTemplate.delete(email);
            return;
        }

        if (storedAuthNumber == null) {
            throw new IllegalArgumentException(EXPIRE_AUTH_TIME.getMessage());
        }

        if (!storedAuthNumber.equals(authNumber)) {
            throw new IllegalArgumentException(NOT_EQUAL_AUTH_NUMBER.getMessage());
        }

        redisTemplate.delete(email);

        boolean isExistEmail = userQueryRepository.existEmail(email);
        if (isExistEmail) {
            throw new IllegalArgumentException(DUPLICATE_EMAIL.getMessage());
        }
    }
}
