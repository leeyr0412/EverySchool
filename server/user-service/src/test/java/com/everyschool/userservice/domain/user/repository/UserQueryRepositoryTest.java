package com.everyschool.userservice.domain.user.repository;

import com.everyschool.userservice.IntegrationTestSupport;
import com.everyschool.userservice.api.controller.user.response.UserClientResponse;
import com.everyschool.userservice.api.controller.user.response.UserInfoResponse;
import com.everyschool.userservice.api.service.user.dto.SearchEmailDto;
import com.everyschool.userservice.domain.user.Parent;
import com.everyschool.userservice.domain.user.User;
import com.everyschool.userservice.domain.user.UserType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static com.everyschool.userservice.domain.user.UserType.*;
import static org.assertj.core.api.Assertions.*;

class UserQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("이미 등록된 이메일이 존재하면 true를 반환한다.")
    @Test
    void existEmail() {
        //given
        User user = saveUser();

        //when
        boolean isExistEmail = userQueryRepository.existEmail("ssafy@gmail.com");

        //then
        assertThat(isExistEmail).isTrue();
    }

    @DisplayName("회원 고유키로 회원 정보를 조회한다.")
    @Test
    void findByUserKey() {
        //given
        User user = saveUser();

        //when
        Optional<UserInfoResponse> response = userQueryRepository.findByUserKey(user.getUserKey());

        //then
        assertThat(response).isPresent();
    }

    @DisplayName("이름과 생년월일로 회원 이메일을 조회한다.")
    @Test
    void findEmailByNameAndBirth() {
        //given
        User user = saveUser();

        //when
        Optional<SearchEmailDto> findSearchEmail = userQueryRepository.findEmailByNameAndBirth("김싸피", "2001-01-01");

        //then
        assertThat(findSearchEmail).isPresent();
        assertThat(findSearchEmail.get().getEmail()).isEqualTo("ssafy@gmail.com");
    }

    @DisplayName("회원 고유키로 회원 id를 조회한다.")
    @Test
    void findIdByUserKey() {
        //given
        User user = saveUser();

        //when
        Optional<UserClientResponse> response = userQueryRepository.findIdByUserKey(user.getUserKey());

        //then
        assertThat(response).isPresent();
    }

    private User saveUser() {
        Parent parent = Parent.builder()
            .email("ssafy@gmail.com")
            .pwd(passwordEncoder.encode("ssafy1234@"))
            .name("김싸피")
            .birth("2001-01-01")
            .userKey(UUID.randomUUID().toString())
            .userCodeId(PARENT.getCode())
            .parentType("M")
            .build();
        return parentRepository.save(parent);
    }
}