package com.everyschool.callservice.api.service.donotdisturb;

import com.everyschool.callservice.api.client.UserServiceClient;
import com.everyschool.callservice.api.client.response.UserInfo;
import com.everyschool.callservice.api.controller.donotdisturb.response.DoNotDisturbResponse;
import com.everyschool.callservice.domain.donotdisturb.repository.DoNotDisturbQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class DoNotDisturbQueryService {

    private final UserServiceClient userServiceClient;
    private final DoNotDisturbQueryRepository doNotDisturbQueryRepository;

    /**
     * 내 방해금지 목록 조회
     *
     * @param token 회원 토큰
     * @return 최근에 생성한 방해금지
     */
    public DoNotDisturbResponse searchMyDoNotDisturb(String token) {
        log.debug("call DoNotDisturbQueryService#searchMyDoNotDisturb");
        UserInfo user = userServiceClient.searchUserInfo(token);
        log.debug("user = {}", user);

        return doNotDisturbQueryRepository.findByUserId(user.getUserId());
    }
}
