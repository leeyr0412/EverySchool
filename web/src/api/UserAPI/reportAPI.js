import { baseAxios } from '../Axios.jsx';

// 모든 신고 내역 조회
export const receivedReportInfo = async () => {
  const schoolYear = sessionStorage.getItem('year');
  const schoolNum = sessionStorage.getItem('schoolId');

  try {
    const statusCodes = ['7001', '7002', '7003'];
    const requests = statusCodes.map((code) =>
      baseAxios.get(`/report-service/v1/web/${schoolYear}/schools/${schoolNum}/reports?status=${code}`, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem('token')}`
        }
      })
    );
    const responses = await Promise.all(requests);
    const allContents = responses.flatMap((response) => response.data.data.content);
    return allContents;
  } catch (error) {
    console.log(error);
    return [];
  }
};

// 신고 상세내역 조회
export const reportDetailInfo = async (reportId) => {
  const schoolNum = sessionStorage.getItem('schoolId');
  const schoolYear = sessionStorage.getItem('year');
  try {
    const response = await baseAxios.get(
      `/report-service/v1/web/${schoolYear}/schools/${schoolNum}/reports/${reportId}`,
      {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem('token')}`
        }
      }
    );
    return response.data.data;
  } catch (error) {
    console.log(error);
  }
};

// 신고 처리중 수정
export const checkReport = async (reportId) => {
  const schoolNum = sessionStorage.getItem('schoolId');
  const schoolYear = sessionStorage.getItem('year');
  try {
    const response = await baseAxios.patch(
      `/report-service/v1/web/${schoolYear}/schools/${schoolNum}/reports/${reportId}`,
      { status: 7002 },
      {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem('token')}`
        }
      }
    );
    console.log(response.data);
  } catch (error) {
    console.log(error);
  }
};

// 신고 처리중 수정
export const completeReport = async (reportId, result) => {
  const schoolNum = sessionStorage.getItem('schoolId');
  const schoolYear = sessionStorage.getItem('year');
  try {
    const response = await baseAxios.patch(
      `/report-service/v1/web/${schoolYear}/schools/${schoolNum}/reports/${reportId}/result`,
      { result: result },
      {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem('token')}`
        }
      }
    );
    console.log(response.data);
  } catch (error) {
    console.log(error);
  }
};

// 악성 전화 민원 리스트 조회
export const BadCallComplain = async () => {
  try {
    const response = await baseAxios.get(`/call-service/v1/calls/reports`, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('token')}`
      }
    });
    return response.data.data;
  } catch (error) {
    console.log(error);
  }
};

// 악성 채팅 민원 리스트 조회
export const BadChatComplain = async () => {
  try {
    const response = await baseAxios.get(`/chat-service/v1/chat-review`, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('token')}`
      }
    });
    return response.data.data;
  } catch (error) {
    console.log(error);
  }
};

// 악성민원 상세 조회
export const CallComplainDetail = async (userCallId) => {
  try {
    const response = await baseAxios.get(`/call-service/v1/calls/detail/${userCallId}`, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('token')}`
      }
    });
    console.log(response.data.data);
    return response.data.data;
  } catch (error) {
    console.log(error);
  }
};

export const ChatComplainDetail = async (reportId, chatId, date) => {
  try {
    const response = await baseAxios.get(`/chat-service/v1/chat-review/${chatId}/${reportId}?date=${date}`, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('token')}`
      }
    });
    return response.data.data;
  } catch (error) {
    console.log(error);
  }
};
