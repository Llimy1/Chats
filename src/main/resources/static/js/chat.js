document.addEventListener("DOMContentLoaded", function () {

    const urlParams = new URLSearchParams(window.location.search);
    const type = urlParams.get('type');
    const roomId = urlParams.get('roomId');
    const roomName = urlParams.get('roomName');
    const userName = urlParams.get('userName');
    const headers = {Authorization: localStorage.getItem("accessToken")}
    const url = `/notification?userName=${encodeURIComponent(userName)}`;


    const eventSource = new EventSource(url);

    eventSource.addEventListener("sse", event => {
        console.log(event);
    })
    eventSource.onmessage = function (event) {
        // 서버로부터 메시지 수신 시 콘솔에 출력
        console.log("Received message: ", event.data);
    };

    document.getElementById("roomName").textContent = roomName;

    // 저장된 메시지 불러오기
    fetch(`/chat/${roomId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(data => {
            console.log(data.data);
            const messages = data.data; // 메시지 배열
            messages.forEach(msg => {
                // const str = `<div class='col-6'><div class='alert alert-info'><b>${msg.sender} : ${msg.message}</b> </div></div>`;
                const str = `<div class='col-6'>
                <div class='alert alert-info'>
                    <b>${msg.sender} : <span id='message-${msg.id}'>${msg.message}</span></b>
                    <button class='edit-msg-btn' data-msg-id='${msg.id}'>수정</button>
                    <button class='delete-msg-btn' data-msg-id='${msg.id}'>삭제</button>
                </div>
             </div>`;
                const msgArea = document.getElementById("msgArea");
                msgArea.innerHTML += str;
            });
        })
        .catch(error => console.error('Error:', error));

    const sockJs = new SockJS("/stomp/chat");
    const stomp = Stomp.over(sockJs);


    stomp.connect({}, function () {
        console.log("STOMP Connection")
        stomp.subscribe("/sub/chat/" + roomId, function (chat) {
            const content = JSON.parse(chat.body);
            const sender = content.sender;
            const message = content.message; // 추가된 부분
            const isEdited = content.isEdited;
            const isDeleted = content.isDeleted;

            let str;

            if (isEdited) {
                const messageSpan = document.getElementById(`message-${content.id}`);
                if (messageSpan) {
                    messageSpan.innerHTML = content.message;
                    return;
                }
            }

            if (isDeleted) {
                const messageSpan = document.getElementById(`message-${content.id}`);
                if (messageSpan) {
                    messageSpan.innerHTML = content.message;
                    return;
                }
            }

            if (sender === userName) {
                str = `<div class='col-6'>
            <div class='alert alert-secondary'>
                <b>${sender} : <span id='message-${content.id}'>${message}</span></b>
                <button class='edit-msg-btn' data-msg-id='${content.id}'>수정</button>
                <button class='delete-msg-btn' data-msg-id='${content.id}'>삭제</button>
            </div>
           </div>`;
            } else {
                str = `<div class='col-6'>
            <div class='alert alert-warning'>
                <b>${sender} : <span id='message-${content.id}'>${message}</span></b>
                <button class='edit-msg-btn' data-msg-id='${content.id}'>수정</button>
                <button class='delete-msg-btn' data-msg-id='${content.id}'>삭제</button>
            </div>
           </div>`;
            }


            const msgArea = document.getElementById("msgArea");
            msgArea.innerHTML += str;
        }, headers);

        if (type === 'enter') {
            stomp.send('/pub/chat/enter', headers, JSON.stringify({
                roomId: roomId,
                sender: userName
            }));
        }
    });

    document.addEventListener('click', function(e) {
        // 수정 버튼 클릭 이벤트 확인
        if (e.target && e.target.classList.contains('edit-msg-btn')) {
            const messageId = e.target.getAttribute('data-msg-id');
            const messageContainer = document.querySelector(`#message-${messageId}`);
            const originalMessage = messageContainer.innerText;

            // 메시지 내용을 편집 가능한 입력 필드로 변경
            messageContainer.innerHTML = `<input type='text' class='edit-message-input' value='${originalMessage}'>
                                       <button class='confirm-edit-btn' data-msg-id='${messageId}'>수정 완료</button>`;
        }
    });

    document.addEventListener('click', function(e) {
        if (e.target && e.target.classList.contains('confirm-edit-btn')) {
            const messageId = e.target.getAttribute('data-msg-id');
            // 여기에서 'edit-message-input' 클래스 이름을 사용하여 입력 필드를 찾습니다.
            const editInput = document.querySelector(`#message-${messageId} .edit-message-input`);
            if (editInput) { // null 체크를 추가하여 더욱 견고하게 만듭니다.
                const updatedMessage = editInput.value;

                const chatMessageUpdateDto = {
                    id: messageId,
                    roomId: roomId,
                    sender: userName,
                    message: updatedMessage,
                    isEdited: true
                };

                stomp.send("/pub/chat/update", headers, JSON.stringify(chatMessageUpdateDto));

                // 수정된 메시지로 화면 바로 업데이트
                // const messageSpan = document.getElementById(`message-${messageId}`);
                // messageSpan.innerHTML = updatedMessage;
            } else {
                console.error('Cannot find edit input field for message ID:', messageId);
            }
        }
    });

    document.addEventListener('click', function(e) {
        // 삭제 버튼 클릭 이벤트 확인
        if (e.target && e.target.classList.contains('delete-msg-btn')) {
            const messageId = e.target.getAttribute('data-msg-id');

            // 삭제 확인 다이얼로그 표시
            const confirmation = confirm('정말로 이 메시지를 삭제하시겠습니까?');

            // 사용자가 확인을 눌렀을 경우에만 삭제 요청을 보냄
            if (confirmation) {
                const chatMessageDeleteDto = {
                    id: messageId,
                    roomId: roomId,
                    sender: userName,
                    isDeleted: true
                }

                stomp.send("/pub/chat/delete", headers, JSON.stringify(chatMessageDeleteDto));

            } else {
                console.error('Cannot find message container for message ID:', messageId);
            }
        }
    });


    const sendButton = document.getElementById("button-send");
    sendButton.addEventListener("click", function (e) {
        const msgInput = document.getElementById("msg");
        stomp.send('/pub/chat/send', headers, JSON.stringify({
            roomId: roomId,
            message: msgInput.value,
            sender: userName
        }));
        msgInput.value = '';
    });

    // 채팅방 목록으로 돌아가는 버튼 이벤트 리스너 추가
    document.getElementById("backToList").addEventListener("click", function () {
        stomp.disconnect(function () {
            console.log("STOMP DISCONNECT")
            fetch(`/chat/redis/${roomId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.getItem("accessToken")
                },
            }).then(response => response.json())
                .then(data => {
                    const success = data.status;
                    stomp.send('/pub/chat/quit', headers, JSON.stringify({
                        roomId: roomId,
                        sender: userName
                    }));
                    window.location.href = "/html/chats.html";
                    console.log(success);
                });
        }).catch(error => console.error('Error:', error));
    })
});