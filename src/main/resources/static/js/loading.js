document.addEventListener('DOMContentLoaded', function () {
    // 쿼리 스트링에서 accessToken 추출
    const queryParams = new URLSearchParams(window.location.search);
    const accessToken = queryParams.get('accessToken');

    if (accessToken) {
        // 로컬 스토리지에 액세스 토큰 저장
        localStorage.setItem('accessToken', accessToken);

        // 저장 후 다른 페이지로 리디렉션
        window.location.href = '/html/chats.html'; // 원하는 리디렉션 대상 URL로 변경
    }
});
