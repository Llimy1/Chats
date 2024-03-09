let localStreamElement = document.querySelector('#localStream');

const myKey = Math.random().toString(36).substring(2, 11);
let pcListMap = new Map();
const urlParams = new URLSearchParams(window.location.search);
const roomId = urlParams.get('roomId');
let otherKeyList = [];
let localStream = undefined;
const headers = {Authorization: localStorage.getItem("accessToken")}

const startCam = async () => {
    if (navigator.mediaDevices !== undefined) {
        await navigator.mediaDevices.getUserMedia({audio: true, video: true})
            .then(async (stream) => {
                console.log("Stream found");

                localStream = stream;

                stream.getAudioTracks()[0].enabled = true;
                localStreamElement.srcObject = localStream;
            }).catch(error => {
                console.error("Error accessing media devices: ", error);
            })
    }
}

const connectSocket = async () => {
    const socket = new SockJS("/stomp/chat");
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, function () {
        console.log('Connected to WebRTC server');

        stompClient.subscribe(`/sub/peer/iceCandidate/${myKey}/${roomId}`, candidate => {
            const key = JSON.parse(candidate.body).key;
            const message = JSON.parse(candidate.body).body;

            pcListMap.get(key).addIceCandidate(new RTCIceCandidate({
                candidate: message.candidate,
                sdpMLineIndex: message.sdpMLineIndex,
                sdpMid: message.sdpMid
            }));
        }, headers)

        stompClient.subscribe(`/sub/peer/offer/${myKey}/${roomId}`, offer => {
            const key = JSON.parse(offer.body).key;
            const message = JSON.parse(offer.body).body;

            pcListMap.set(key, createPeerConnection(key));

            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription({
                type: message.type,
                sdp: message.sdp
            }));
            sendAnswer(pcListMap.get(key), key);
        }, headers);

        stompClient.subscribe(`/sub/peer/answer/${myKey}/${roomId}`, answer => {
            const key = JSON.parse(answer.body).key;
            const message = JSON.parse(answer.body).body;

            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(message));
        }, headers)

        stompClient.subscribe(`/sub/call/key`, message => {
            stompClient.send(`/pub/send/key`, {}, JSON.stringify(myKey));
        }, headers);

        stompClient.subscribe(`/sub/send/key`, message => {
            const key = JSON.parse(message.body);

            if (myKey !== key && otherKeyList.find((mapKey) => mapKey === myKey) === undefined) {
                otherKeyList.push(key);
            }
        }, headers);
    });
}

let onTrack = (event, otherKey) => {

    if (document.getElementById(`${otherKey}`) === null) {

        const video = document.createElement('video');

        video.autopley = true;
        video.controls = true;
        video.id = otherKey;
        video.srcObject = event.stream[0];

        document.getElementById('remoteStreamDiv').appendChild(video);
    }
};

const createPeerConnection = (otherKey) => {
    const pc = new RTCPeerConnection();
    try {

        pc.addEventListener('icecandidate', (event) => {
            onIceCandidate(event, otherKey);
        });

        pc.addEventListener('track', (event) => {
            onTrack(event, otherKey);
        });

        if (localStream !== undefined) {
            localStream.getTracks().forEach(track => {
                pc.addTrack(track, localStream);
            });
        }

        console.log('PeerConnection created');
    } catch (error) {
        console.error('PeerConnection failed: ', error);
    }
    return pc;
}

let onIceCandidate = (event, otherKey) => {
    if (event.candidate) {
        console.log("ICE candidate");
        stompClient.send(`/pub/peer/iceCandidate/${otherKey}/${roomId}`, headers, JSON.stringify({
                key: myKey,
                body: event.candidate
            }));
    }
};

let sendOffer = (pc, otherKey) => {
    pc.createOffer().then(offer => {
        setLocalAndSendMessage(pc, offer);
        stompClient.send(`/pub/peer/offer/${otherKey}/${roomId}`, headers, JSON.stringify({
            key : myKey,
            body : offer
        }));
        console.log('Send offer');
    });
};

let sendAnswer = (pc, otherKey) => {
    pc.createAnswer().then(answer => {
        setLocalAndSendMessage(pc, answer);
        stompClient.send(`/pub/peer/answer/${otherKey}/${roomId}`, headers, JSON.stringify({
            key : myKey,
            body : answer
        }));
        console.log('Send answer');
    });
};

const setLocalAndSendMessage = (pc, sessionDescription) => {
    pc.setLocalDescription(sessionDescription);
}

document.querySelector('#enterRoomBtn').addEventListener('click', async () => {
    await startCam();

    if (localStream !== undefined) {
        document.querySelector('#localStream').style.display = 'block';
        document.querySelector('#startSteamBtn').style.display = '';
    }
    document.querySelector('#enterRoomBtn').disabled = true;

    await connectSocket();
});

document.querySelector('#startSteamBtn').addEventListener('click', async () => {
    await stompClient.send(`/pub/call/key`, headers, {});

    setTimeout(() => {
        otherKeyList.map((key) => {
            if (!pcListMap.has(key)) {
                pcListMap.set(key, createPeerConnection(key));
                sendOffer(pcListMap.get(key), key);
            }
        });
    }, 1000);
});

