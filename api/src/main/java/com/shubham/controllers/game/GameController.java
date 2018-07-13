package com.shubham.controllers.game;

import com.shubham.dtos.request.CreateGameRequest;
import com.shubham.dtos.request.JoinGameRequest;
import com.shubham.dtos.request.PlayGameRequest;
import com.shubham.dtos.request.StartGameRequest;
import com.shubham.dtos.response.ApiResponse;
import com.shubham.services.impl.GameService;
import com.shubham.ws.models.HelloMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author : shubham
 */
@Controller
public class GameController {

  @Autowired
  GameService gameService;

  @MessageMapping("/hello")
  @SendTo("/topic/game")
  public ApiResponse greeting(HelloMessage message) throws Exception {
    return new ApiResponse("Hello Player :" + message.getName());
  }

  @MessageMapping("/create")
  @SendTo("/topic/game")
  public ApiResponse createGame(CreateGameRequest request) throws Exception {
    return new ApiResponse(gameService.newGame(request.getPlayerId(), request.getMaxPlayers()).toString());
  }

  @MessageMapping("/start")
  @SendTo("/topic/game")
  public ApiResponse startGame(StartGameRequest request) throws Exception {
    return new ApiResponse(gameService.startGame(request.getPlayerId(), request.getGameId()));
  }

  @MessageMapping("/join")
  @SendTo("/topic/game")
  public ApiResponse joinGame(JoinGameRequest request) throws Exception {
    return new ApiResponse(gameService.joinGame(request.getGameId(), request.getPlayerId()));
  }

  @MessageMapping("/play")
  @SendTo("/topic/game")
  public ApiResponse playGame(PlayGameRequest request) throws Exception {
    return new ApiResponse(
        gameService.playGame(request.getPlayerId(), request.getGameId(), request.getWord(), request.getPass()));
  }

}
