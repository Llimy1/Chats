document.addEventListener("DOMContentLoaded", function(){

    const urlParams = new URLSearchParams(window.location.search);
    const type = urlParams.get('type');
    const roomId = urlParams.get('roomId');
    const roomName = urlParams.get('roomName');
    const userName = urlParams.get('userName');

    document.getElementById("roomName").textContent = roomName;

    const sockJs = new SockJS("/stomp/chat");
    const stomp = Stomp.over(sockJs);

    stomp.connect({}, function (){
        console.log("STOMP Connection")
        stomp.subscribe("/sub/chat/" + roomId, function (chat) {
            const content = JSON.parse(chat.body);
            const sender = content.sender;
            const message = content.message; // 추가된 부분
            let str;

            if(sender === userName){
                str = "<div class='col-6'><div class='alert alert-secondary'><b>" + sender + " : " + message + "</b></div></div>";
            }
            else{
                str = "<div class='col-6'><div class='alert alert-warning'><b>" + sender + " : " + message + "</b></div></div>";
            }

            const msgArea = document.getElementById("msgArea");
            msgArea.innerHTML += str;
        });

        if (type === 'enter') {
            stomp.send('/pub/chat/enter', {}, JSON.stringify({roomId: roomId, sender: userName}));
        }
    });

    const sendButton = document.getElementById("button-send");
    sendButton.addEventListener("click", function(e){
        const msgInput = document.getElementById("msg");
        stomp.send('/pub/chat/send', {}, JSON.stringify({roomId: roomId, message: msgInput.value, sender: userName}));
        msgInput.value = '';
    });

    // 채팅방 목록으로 돌아가는 버튼 이벤트 리스너 추가
    document.getElementById("backToList").addEventListener("click", function() {
        stomp.send('/pub/chat/quit', {}, JSON.stringify({roomId: roomId, sender: userName}));
        window.location.href = "/html/chats.html";
    });
});