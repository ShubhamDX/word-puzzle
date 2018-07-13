var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#games").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/game', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}
function createGame(val) {
    stompClient.send("/app/create", {}, JSON.stringify(val));
}

function joinGame(val) {
    stompClient.send("/app/join", {}, JSON.stringify(val));
}

function startGame(val) {
    stompClient.send("/app/start", {}, JSON.stringify(val));
}

function playGame(val) {
    stompClient.send("/app/play", {}, JSON.stringify(val));
}

function pass(val) {
    stompClient.send("/app/play", {}, JSON.stringify(val));
}

function getAllGames() {
    stompClient.send("/app/getAllGames");
}

function getActiveGames() {
    stompClient.send("/app/getActiveGames");
}

function getToBeStartedGames() {
    stompClient.send("/app/getJoinableGames");
}

function getGameInfo(val) {
    stompClient.send("/app/getGameInfo",{}, JSON.stringify(val));
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#games").append("<tr><td>" + message + "</td></tr>");
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $("#create").click(function() {
                createGame({
                    'playerId': $("#playerIdddd").val(),
                    'maxPlayers': $("#maxPlayers").val()
                });
            });
    $( "#getAllGames" ).click(function() { getAllGames(); });
    $( "#getActiveGames" ).click(function() { getActiveGames(); });
    $( "#getToBeStartedGames" ).click(function() { getToBeStartedGames(); });
    $( "#gameInfo" ).click(function() { getGameInfo({
        'gameId': $("#gameInfoId").val()
        });
    });
    $("#join").click(function() {
                    joinGame({
                        'playerId': $("#playerId").val(),
                        'gameId': $("#gameId").val()
                    });
                });
    $("#play").click(function() {
                        playGame({
                            'playerId': $("#playerIdd").val(),
                            'gameId': $("#gameIdd").val(),
                            'word': $("#word").val(),
                            'pass': false
                        });
                    });
    $("#pass").click(function() {
                        pass({
                            'playerId': $("#player_id").val(),
                            'gameId': $("#game_id").val(),
                            'word': null,
                            'pass': true


    }); });
    $("#start").click(function() {
                        startGame({
                            'playerId': $("#playerIddd").val(),
                            'gameId': $("#gameIddd").val()
                        });
                    });

});