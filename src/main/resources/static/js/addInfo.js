document.addEventListener('DOMContentLoaded', function () {
    const queryParams = new URLSearchParams(window.location.search);
    const email = queryParams.get('email');

    if (email) {
        document.getElementById('email').value = email;
    }
});

document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    const nickname = document.getElementById('nickname').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;

    // 쿼리 스트링에서 provider 값을 추출
    const queryParams = new URLSearchParams(window.location.search);
    const provider = queryParams.get('provider');

    const signupData = {
        nickname: nickname,
        email: email,
        phoneNumber: phone,
        provider: provider // provider 값을 포함
    };

    fetch('/signup/social', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(signupData)
    })
        .then(response => response.json())
        .then(data => {
            console.log('회원가입 성공:', data);
            window.location.href = "/";
        })
        .catch((error) => {
            console.error('회원가입 실패:', error);
            // 오류 처리
        });
});

document.addEventListener('DOMContentLoaded', function () {
    // 쿼리 스트링에서 이메일과 프로바이더 추출
    const queryParams = new URLSearchParams(window.location.search);
    const email = queryParams.get('email');
    const provider = queryParams.get('provider');

    // 이메일 필드에 이메일 설정
    if (email) {
        document.getElementById('email').value = email;
    }

    let isNicknameAvailable = false;
    let isEmailAvailable = false; // 이메일 중복 확인 로직이 있을 경우 사용

    const signupButton = document.getElementById('signupButton');
    const signupForm = document.getElementById('signupForm');
    const nicknameCheckButton = document.querySelector('.check-button.nickname');

    // 초기에는 회원가입 버튼을 비활성화합니다.
    signupButton.disabled = true;

    // 닉네임 중복 검사
    nicknameCheckButton.addEventListener('click', function () {
        const nickname = document.getElementById('nickname').value;
        if (nickname.trim() === '') { // 빈 문자열 검사
            alert('닉네임을 입력해주세요.');
            return;
        }
        checkDuplicate('nickname', nickname, function (isAvailable) {
            isNicknameAvailable = isAvailable;
            toggleSignupButton(); // 중복 검사 후 회원가입 버튼 활성화 상태 업데이트
        });
    });

    // 회원가입 요청 함수
    signupForm.addEventListener('submit', function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작 방지

        if (isNicknameAvailable && isEmailAvailable) {
            const nickname = document.getElementById('nickname').value;
            const phone = document.getElementById('phone').value;

            const signupData = {
                nickname: nickname,
                email: email,
                phoneNumber: phone,
                provider: provider
            };

            fetch('/signup/social', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(signupData)
            })
                .then(response => response.json())
                .then(data => {
                    console.log('회원가입 성공:', data);
                    window.location.href = "/";
                })
                .catch((error) => {
                    console.error('회원가입 실패:', error);
                });
        }
    });

    // 중복 검사 함수
    function checkDuplicate(type, value, callback) {
        const queryParams = new URLSearchParams({[type]: value});
        fetch(`http://localhost:8080/check/${type}?${queryParams}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (response.status === 409) { // 오류 코드 409 처리
                    return response.json().then(data => {
                        throw new Error(data.message); // 오류 메시지를 throw
                    });
                }
                if (!response.ok) { // 기타 오류 처리
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                alert(data.message); // 성공 시 메시지 표시
                callback(data.status === 'success');
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message); // 오류 메시지 표시
                callback(false);
            });
    }

    // 회원가입 버튼 활성화 상태를 결정하는 함수
    function toggleSignupButton() {
        signupButton.disabled = !(isNicknameAvailable);
    }
});

