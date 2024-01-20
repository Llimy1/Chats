document.addEventListener("DOMContentLoaded", function(){

    const urlParams = new URLSearchParams(window.location.search);
    const roomId = urlParams.get('roomId');

    const sockJs = new SockJS("/stomp/chat");
    const stomp = Stomp.over(sockJs);

    stomp.connect({}, function (){
        console.log("STOMP Connection")
        stomp.subscribe("/sub/chat/" + roomId, function (chat) {
            const content = JSON.parse(chat.body);
            const sender = content.sender;
            const message = content.message; // 추가된 부분
            let str = '';

            if(sender === 'name'){
                str = "<div class='col-6'><div class='alert alert-secondary'><b>" + sender + " : " + message + "</b></div></div>";
            }
            else{
                str = "<div class='col-6'><div class='alert alert-warning'><b>" + sender + " : " + message + "</b></div></div>";
            }

            const msgArea = document.getElementById("msgArea");
            msgArea.innerHTML += str;
        });

        stomp.send('/pub/chat/enter', {}, JSON.stringify({roomId: roomId, sender: "name"}));
    });

    const sendButton = document.getElementById("button-send");
    sendButton.addEventListener("click", function(e){
        const msgInput = document.getElementById("msg");
        stomp.send('/pub/chat/send', {}, JSON.stringify({roomId: roomId, message: msgInput.value, sender: "name"}));
        msgInput.value = '';
    });
});