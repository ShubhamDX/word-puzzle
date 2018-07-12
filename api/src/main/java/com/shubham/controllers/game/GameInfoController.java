package com.shubham.controllers.game;

import com.shubham.dtos.request.GameInfoRequest;
import com.shubham.dtos.response.ApiResponse;
import com.shubham.services.impl.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author : shubham
 */

@Controller
public class GameInfoController {

  @Autowired
  GameService gameService;

  @MessageMapping("/getAllGames")
  @SendTo("/topic/game")
  public ApiResponse getAllGames() throws Exception {
    return new ApiResponse(gameService.getAllGames().size() > 0 ? gameService.getAllGames().toString() : "No games exist :(");
  }

  @MessageMapping("/getGameInfo")
  @SendTo("/topic/game")
  public ApiResponse getGameInfo(GameInfoRequest request) {
    return new ApiResponse(null == request.getGameId() ? "Enter gameId you want to get info for.."
        : gameService.getGame(request.getGameId()).toString());
  }

  @MessageMapping("/getActiveGames")
  @SendTo("/topic/game")
  public ApiResponse getActiveGameIds() {
    return new ApiResponse(gameService.getActiveGameIds().toString());
  }

  @MessageMapping("/getJoinableGames")
  @SendTo("/topic/game")
  public ApiResponse gettoBeStartedGameIds() {
    return new ApiResponse(gameService.getToBeStartedGameIds().toString());
  }

}
