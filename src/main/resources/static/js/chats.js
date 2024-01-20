// 모달 관련 코드
const modal = document.getElementById('myModal');
const btn = document.querySelector('.create-room'); // .getElementsByClassName 대신 .querySelector 사용
const span = document.querySelector('.close'); // .getElementsByClassName 대신 .querySelector 사용

// 채팅방을 클릭할 때 실행될 함수
function onRoomClick(roomName) {
    fetch(`/chat/${encodeURIComponent(roomName)}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('응답을 받는 데 실패했습니다.');
        })
        .then(data => {
            const roomId = data.data;
            window.location.href = `/html/chat.html?roomId=${encodeURIComponent(roomId)}`;
        })
        .catch(error => {
            console.error('채팅방 ID 조회 중 오류 발생:', error);
        });
}

btn.onclick = function() {
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

// 폼 제출과 AJAX 요청 관련 코드
document.getElementById('createRoomForm').onsubmit = function(event) {
    event.preventDefault();
    const roomName = document.getElementById('roomName').value;
    const accessToken = localStorage.getItem("accessToken"); // 액세스 토큰을 여기에 설정하세요

    const roomData = {
        roomName
    }
    fetch('/chat/room', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': accessToken
        },
        body: JSON.stringify({ roomName }) // JSON 포맷으로 전송
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('네트워크 응답이 올바르지 않습니다.');
        })
        .then(data => {
            console.log(data);
            // 성공적으로 방을 생성한 후 처리
            modal.style.display = "none";
            // 채팅방 리스트를 갱신하는 로직을 여기에 추가할 수 있습니다.
            window.location.reload();
        })
        .catch(error => {
            console.error('요청 중 문제가 발생했습니다:', error);
        });
};

document.addEventListener('DOMContentLoaded', function() {
    // 채팅방 리스트를 가져오는 함수
    function fetchChatRooms() {
        fetch('/chat/room', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('서버로부터 응답을 받는데 실패했습니다.');
            })
            .then(data => {
                const chatRooms = data.data
                const chatListContainer = document.querySelector('.chat-list');
                // 채팅방 리스트가 비어있지 않다면 화면에 표시
                if (chatRooms && chatRooms.length > 0) {
                    chatListContainer.innerHTML = ''; // 기존 내용을 비웁니다.
                    chatRooms.forEach(room => {
                        const roomElement = document.createElement('div');
                        roomElement.className = 'chat-room';
                        roomElement.innerHTML = `
                            <span class="chat-name">${room}</span>
                        `;
                        chatListContainer.appendChild(roomElement);

                        roomElement.addEventListener('click', () => onRoomClick(room));

                    });
                } else {
                    // 채팅방이 없을 경우 메시지를 유지합니다.
                    chatListContainer.innerHTML = '<div class="no-chat-rooms">현재 채팅방이 없습니다.</div>';
                }
            })
            .catch(error => {
                console.error('채팅방 리스트를 가져오는데 실패했습니다:', error);
            });
    }
    // 채팅방 리스트를 가져옵니다.
    fetchChatRooms();
});
