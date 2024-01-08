function checkPasswordRequirements() {
    const password = document.getElementById('password').value;
    const minCharacters = password.length >= 8 && password.length <= 20;
    const uppercase = /[A-Z]/.test(password);
    const lowercase = /[a-z]/.test(password);
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
