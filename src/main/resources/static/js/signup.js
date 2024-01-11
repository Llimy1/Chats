let isNicknameAvailable = false;
let isEmailAvailable = false;

const signupButton = document.getElementById('signupButton');
const signupForm = document.getElementById('signupForm');
const nicknameCheckButton = document.querySelector('.check-button.nickname');
const emailCheckButton = document.querySelector('.check-button.email');

// 초기에는 회원가입 버튼을 비활성화합니다.
signupButton.disabled = true;

document.addEventListener('DOMContentLoaded', function () {

    // 닉네임 중복 검사
    nicknameCheckButton.addEventListener('click', function () {
        const nickname = document.getElementById('nickname').value;
        if (nickname.trim() === '') { // 빈 문자열 검사
            alert('닉네임을 입력해주세요.');
            return;
        }
        checkDuplicate('nickname', nickname, function (isAvailable) {
            isNicknameAvailable = isAvailable;
            console.log(isNicknameAvailable);
            toggleSignupButton(); // 중복 검사 후 회원가입 버튼 활성화 상태 업데이트
        });
    });

// 이메일 중복 검사
    emailCheckButton.addEventListener('click', function () {
        const email = document.getElementById('email').value;
        if (email.trim() === '') { // 빈 문자열 검사
            alert('이메일을 입력해주세요.');
            return;
        }
        checkDuplicate('email', email, function (isAvailable) {
            isEmailAvailable = isAvailable;
            console.log(isEmailAvailable)
            toggleSignupButton(); // 중복 검사 후 회원가입 버튼 활성화 상태 업데이트
        });
    });

    // 비밀번호 조건 확인 이벤트 리스너
    document.getElementById('password').addEventListener('input', checkPasswordValidity);
    document.getElementById('password-confirm').addEventListener('input', checkPasswordValidity);

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

    function checkPasswordRequirements() {
        const password = document.getElementById('password').value;
        const minCharacters = password.length >= 8 && password.length <= 20;
        const uppercase = /[A-Z]/.test(password);
        const numbers = /[0-9]/.test(password);
        const specialCharacters = /[\W_]/.test(password);

        document.getElementById('min-characters').className = minCharacters ? 'valid' : 'invalid';
        document.getElementById('uppercase').className = uppercase ? 'valid' : 'invalid';
        document.getElementById('numbers').className = numbers ? 'valid' : 'invalid';
        document.getElementById('special-characters').className = specialCharacters ? 'valid' : 'invalid';

    }


    function checkPasswordMatch() {
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('password-confirm').value;
        const messageContainer = document.getElementById('password-match-message');

        if (password && confirmPassword) { // 두 필드 모두에 입력이 있는 경우에만 검사합니다.
            if (password === confirmPassword) {
                messageContainer.textContent = '비밀번호가 일치합니다.';
                messageContainer.className = 'valid';
            } else {
                messageContainer.textContent = '비밀번호가 일치하지 않습니다.';
                messageContainer.className = 'invalid';
            }
        } else {
            messageContainer.textContent = ''; // 입력이 없는 경우 메시지를 비웁니다.
            messageContainer.className = '';
        }
    }

    function checkPasswordValidity() {
        checkPasswordRequirements();
        checkPasswordMatch();

        isPasswordValid = document.querySelectorAll('.password-requirement .valid').length === 4 &&
            document.getElementById('password').value ===
            document.getElementById('password-confirm').value;
    }

    // 회원가입 버튼 활성화 상태를 결정하는 함수
    function toggleSignupButton() {
        signupButton.disabled = !(isNicknameAvailable && isEmailAvailable);
    }
    // 회원가입 요청 함수
    signupButton.addEventListener('click', function (e) {
        e.preventDefault(); // 폼의 기본 제출을 막습니다.

        if (isNicknameAvailable && isEmailAvailable) {
            // 폼 데이터를 객체로 변환
            const formData = new FormData(signupForm);
            const requestData = {
                nickname: formData.get('nickname'),
                email: formData.get('email'),
                password: formData.get('password'),
                phoneNumber: formData.get('phoneNumber'),
                postCode: formData.get('postCode'),
                mainAddress: formData.get('address'),
                detailAddress: formData.get('detailAddress')
            };

            // 회원가입 요청
            fetch('http://localhost:8080/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data); // 성공 시 응답을 콘솔에 로그합니다.
                    window.location.href = "/";
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        }
    });
});
document.getElementById('phone').addEventListener('input', function (e) {
    this.value = this.value.replace(/[^0-9]/g, '');
});

