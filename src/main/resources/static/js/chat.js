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
    eventSource.onmessage = function(event) {
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
            const messages = data.data; // 메시지 배열
            messages.forEach(msg => {
                const str = `<div class='col-6'><div class='alert alert-info'><b>${msg.sender} : ${msg.message}</b> </div></div>`;
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
            let str;

            if (sender === userName) {
                str = "<div class='col-6'><div class='alert alert-secondary'><b>" + sender + " : " + message + "</b></div></div>";
            } else {
                str = "<div class='col-6'><div class='alert alert-warning'><b>" + sender + " : " + message + "</b></div></div>";
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