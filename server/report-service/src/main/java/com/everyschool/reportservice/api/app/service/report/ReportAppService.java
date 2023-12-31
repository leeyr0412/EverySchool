package com.everyschool.reportservice.api.app.service.report;

import com.everyschool.reportservice.api.app.controller.report.response.CreateReportResponse;
import com.everyschool.reportservice.api.app.controller.report.response.RemoveReportResponse;
import com.everyschool.reportservice.api.client.SchoolServiceClient;
import com.everyschool.reportservice.api.client.UserServiceClient;
import com.everyschool.reportservice.api.client.response.SchoolUserInfo;
import com.everyschool.reportservice.api.client.response.UserInfo;
import com.everyschool.reportservice.api.app.service.report.dto.CreateReportDto;
import com.everyschool.reportservice.domain.report.Report;
import com.everyschool.reportservice.domain.report.ReportContent;
import com.everyschool.reportservice.domain.report.UploadFile;
import com.everyschool.reportservice.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.everyschool.reportservice.api.app.service.report.WitnessGenerator.*;
import static com.everyschool.reportservice.error.ErrorMessage.NO_SUCH_REPORT;

/**
 * 신고 앱 명령 서비스
 *
 * @author 임우택
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ReportAppService {

    private final ReportRepository reportRepository;
    private final UserServiceClient userServiceClient;
    private final SchoolServiceClient schoolServiceClient;

    /**
     * 신고 등록
     *
     * @param userKey     회원 고유키
     * @param schoolYear  학년도
     * @param schoolId    학교 아이디
     * @param dto         신고 등록 정보
     * @param uploadFiles 업로드 된 파일
     * @return 등록된 신고 정보
     */
    public CreateReportResponse createReport(String userKey, Integer schoolYear, Long schoolId, CreateReportDto dto, List<UploadFile> uploadFiles) {
        //회원 정보 조회
        UserInfo userInfo = userServiceClient.searchByUserKey(userKey);

        //학급 정보 조회
        SchoolUserInfo schoolUserInfo = schoolServiceClient.searchByUserId(userInfo.getUserId());

        ReportContent content = dto.toContent();

        //신고자 정보 제작
        String witness = createWitnessInfo(schoolUserInfo, userInfo.getUserName());

        //신고 엔티티 생성
        Report report = Report.createReport(witness, dto.getDescription(), content, schoolYear, dto.getTypeId(), schoolId, userInfo.getUserId(), uploadFiles);

        //신고 저장
        Report savedReport = reportRepository.save(report);

        return CreateReportResponse.of(savedReport);
    }

    /**
     * 신고 내역 삭제
     *
     * @param reportId 신고 아이디
     * @return 삭제된 신고 정보
     */
    public RemoveReportResponse removeReport(Long reportId) {
        Report report = getReportEntity(reportId);

        report.remove();

        return RemoveReportResponse.of(report);
    }

    /**
     * 신고 엔티티 조회
     *
     * @param reportId 신고 아이디
     * @return 조회된 신고 엔티티
     * @throws NoSuchElementException 등록되지 않은 신고를 조회하는 경우 발생
     */
    private Report getReportEntity(Long reportId) {
        Optional<Report> findReport = reportRepository.findById(reportId);
        if (findReport.isEmpty()) {
            throw new NoSuchElementException(NO_SUCH_REPORT.getMessage());
        }
        return findReport.get();
    }
}
