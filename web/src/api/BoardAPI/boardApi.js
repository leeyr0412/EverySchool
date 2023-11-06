import { baseAxios } from '../Axios.jsx';

export const clickNotiRegister = async (e, data) => {
  e.preventDefault();
  const formData = new FormData();
  formData.append('title', data.title);
  formData.append('content', data.content);
  formData.append('isUsedComment', false);
  // formData.append('files', data.fileName);

  data.fileName.forEach((image) => {
    formData.append('files', image);
  });

  const schoolNum = sessionStorage.getItem('schoolId');
  const schoolYear = sessionStorage.getItem('year');

  try {
    const response = await baseAxios.post(
      `/board-service/v1/web/${schoolYear}/schools/${schoolNum}/boards/communications`,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${sessionStorage.getItem('token')}`
        }
      }
    );
    console.log(response.data);
    return response.data;
  } catch (error) {
    return 0;
  }
};
