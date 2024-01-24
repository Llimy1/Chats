document.querySelector('.login-form').addEventListener('submit', function (event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const loginData = {
        email: username,
        password: password
    };

    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === "success") {
                // 로그인 성공 시, 액세스 토큰을 로컬 스토리지에 저장
                localStorage.setItem('accessToken', data.data);
                console.log('로그인에 성공했습니다.');

                window.location.href = "/html/chats.html";
            } else {
                // 실패 메시지 처리
                console.log('로그인에 실패했습니다.');
            }
        })
        .catch((error) => {
            // 오류 처리
            console.error('Error:', error);
        });
});