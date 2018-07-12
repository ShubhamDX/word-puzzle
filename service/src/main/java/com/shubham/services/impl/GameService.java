package com.shubham.services.impl;

import static com.shubham.ws.commons.constants.Constants.MAX_PLAYERS;
import static com.shubham.ws.commons.constants.Constants.WINNER_RANK;

import com.shubham.dtos.game.LeaderBoard;
import com.shubham.dtos.response.GameInfoResponse;
import com.shubham.ws.cache.impl.GameCacheImpl;
import com.shubham.ws.commons.constants.Constants;
import com.shubham.ws.commons.enums.GameStatus;
import com.shubham.ws.commons.util.RandomIdGenerator;
import com.shubham.ws.models.Game;
import com.shubham.ws.models.GameState;
import com.shubham.ws.models.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : shubham
 */
@Service
@Slf4j
public class GameService {

  @Autowired
  GridService gridService;

  public Set<Integer> getAllGames() {
    GameCacheImpl gameCache = GameCacheImpl.getInstance();
    return (null == gameCache.getGameIdToGameStateMap() || !(gameCache.getGameIdToGameStateMap().values().size() > 0))
        ? new HashSet<>()
        : (Set<Integer>) gameCache.getGameIdToGameStateMap().keySet();
  }

  public GameState getGame(int id) {
    return GameCacheImpl.getInstance().getGameIdToGameStateMap().containsKey(id)
        ? GameCacheImpl.getInstance().getGameIdToGameStateMap().get(id)
        : new GameState();
  }

  public List<Integer> getActiveGameIds() {
    return GameCacheImpl.getInstance().getGameIdToGameStateMap().values().stream()
        .filter(gameState -> gameState.getGame().getGameStatus().equals(GameStatus.ACTIVE))
        .map(gameState -> gameState.getGame().getGameId()).collect(Collectors.toList());
  }

  public List<Integer> getToBeStartedGameIds() {
    return GameCacheImpl.getInstance().getGameIdToGameStateMap().values().stream()
        .filter(gameState -> gameState.getGame().getGameStatus().equals(GameStatus.TO_BE_STARTED)
            && (gameState.getGame().getMaxPlayers() >= gameState.getGame().getPlayers().size()))
        .map(gameState -> gameState.getGame().getGameId()).collect(Collectors.toList());
  }

  public GameInfoResponse newGame(String admin) {
    return gameCreator(admin, MAX_PLAYERS);
  }

  public GameInfoResponse newGame(String admin, int maxPlayers) {
    return gameCreator(admin, maxPlayers);
  }

  private GameInfoResponse gameCreator(String admin, int maxPlayers) {
    Queue<Player> playerSequence = new ConcurrentLinkedQueue<>();
    if (null == admin || admin.isEmpty()) {
      admin = RandomIdGenerator.getRandomPlayerId().toString();
    }
    List<Player> players = new ArrayList<>();
    Player player = new Player(admin);
    players.add(player);
    playerSequence.add(player);
    GameState gameState = new GameState();
    Game game = new Game();
    game.setAdmin(new Player(admin));
    game.setGameId(GameCacheImpl.getInstance().getGameIdToGameStateMap().size() + 1);
    game.setGameStatus(GameStatus.TO_BE_STARTED);
    game.setGrid(gridService.getGrid());
    game.setPlayers(players);
    game.setPlayerSequence(playerSequence);
    game.setMaxPlayers(maxPlayers);
    gameState.setDiscoveredWords(new HashSet<>());
    gameState.setGame(game);
    gameState.setPlayerWordMap(new HashMap<>());
    gameState.setCurrentPlayerTurn(player);
    return updateGameCache(gameState);
  }

  public String joinGame(Integer gameId, Player player) {
    if (null == gameId || null == player)
      return String.format("GameId or playerId cannot be null to join a game");
    else if (!getToBeStartedGameIds().contains(gameId))
      return String.format("[GameId:%s] : Cannot join this game", gameId);
    else {
      synchronized (GameCacheImpl.getInstance().getGameIdToGameStateMap().get(gameId)) {
        GameCacheImpl gameCache = GameCacheImpl.getInstance();
        GameState gameState = gameCache.getGameIdToGameStateMap().get(gameId);
        if (gameState.getGame().getPlayers().contains(player)) {
          return String.format("[GameId:%s] : PlayerId: %s has already joined", gameId, player);
        }
        // while (null == player || gameState.getGame().getPlayers().contains(player)) {
        // player = new Player(RandomIdGenerator.getRandomPlayerId().toString());
        // }
        gameState.getGame().getPlayerSequence().offer(player);
        gameState.getGame().getPlayers().add(player);
        if (gameState.getGame().getPlayers().size() == gameState.getGame().getMaxPlayers()) {
          gameState.getGame().setGameStatus(GameStatus.ACTIVE);
          updateGameCache(gameState);
          return String.format("[GameId:%s] : Max players joined. Game is started. Game info is: %s", gameId,
              getGame(gameId).toString());
        } else {
          updateGameCache(gameState);
          return String.format("[GameId:%s] : Player %s has joined", gameId, player.getPlayerId());
        }
      }
    }
  }

  public String playGame(Player player, Integer gameId, String word, Boolean pass) {
    if (null == player || null == gameId || null == word || null == pass) {
      return String.format("[GameId:%s] : Either of playerId/gameId/word is missing", gameId);
    }
    GameState gameState = GameCacheImpl.getInstance().getGameIdToGameStateMap().get(gameId);
    if (null == gameState) {
      return String.format("[GameId:%s] : The game doesn't exist", gameId);
    }
    if (word.length() <= 1) {
      return String.format("[GameId:%s] : Word length <= 1 not supported", gameId);
    }
    synchronized (GameCacheImpl.getInstance().getGameIdToGameStateMap().get(gameState.getGame().getGameId())) {
      if (!GameStatus.ACTIVE.equals(gameState.getGame().getGameStatus())) {
        return String.format("[GameId:%s] : Game  is not in active state", gameId);
      }
      if (!gameState.getGame().getPlayers().contains(player)) {
        return String.format("[GameId:%s] : Hey Player %s . You're not in this game", gameId, player);
      }
      if (!gameState.getCurrentPlayerTurn().equals(player)) {
        return String.format("[GameId:%s] : Hey Player %s ,it's not your turn. It's %s turn", gameId, player,
            gameState.getCurrentPlayerTurn());
      }
      if (pass) {
        gameState.getGame().getPlayerSequence().poll();
        if (gameState.getGame().getPlayerSequence().size() == 0) {
          gameState.getGame().setGameStatus(GameStatus.FINISHED);
          updateGameCache(gameState);
          return String.format(
              "[GameId:%s] : All players have passed their turns.The game is over **** Original set of words were %s **** Players and their guessed words were %s *** Leaderboard is %s",
              gameId, gameState.getGame().getGrid().getContainedWords(), gameState.getPlayerWordMap().toString(),
              getLeaderBoard(gameState.getPlayerWordMap()).toString());
        }
        gameState.setCurrentPlayerTurn(gameState.getGame().getPlayerSequence().peek());
        updateGameCache(gameState);
        return String.format("[GameId:%s] : Player %s has passed.The player is out of game. Current player turn is %s", gameId,
            player, gameState.getCurrentPlayerTurn());
      }
      if (gameState.getDiscoveredWords().contains(word)) {
        return String.format(
            "[GameId:%s] : Player %s has guessed the word %s which has already been identified.Please choose another word or pass",
            gameId, player, word);
      }

      if (!gridService.searchWordInGrid(word, gameState.getGame().getGrid())) {
        gameState = updatePlayerSequence(gameState);
        updateGameCache(gameState);
        return String.format(
            "[GameId:%s] : Player %s has guessed the word %s which either doesn't exist in grid or is not an english word. Next player turn is %s ",
            gameId, player, word, gameState.getCurrentPlayerTurn());
      }
      Set<String> discoveredWords = gameState.getDiscoveredWords();
      discoveredWords.add(word);
      gameState.setDiscoveredWords(discoveredWords);
      List<String> playerWords =
          (gameState.getPlayerWordMap().containsKey(player) && null != gameState.getPlayerWordMap().get(player))
              ? gameState.getPlayerWordMap().get(player)
              : new ArrayList<>();
      playerWords.add(word);
      gameState.getPlayerWordMap().put(player, playerWords);
      gameState = updatePlayerSequence(gameState);
      if (gameState.getDiscoveredWords().containsAll(gameState.getGame().getGrid().getContainedWords())) {
        gameState.getGame().setGameStatus(GameStatus.FINISHED);
        updateGameCache(gameState);
        return String.format(
            "[GameId:%s] : All words have been identified.The game is over **** Original set of words were %s **** Players and their guessed words were %s *** Leaderboard is %s",
            gameId, gameState.getGame().getGrid().getContainedWords(), gameState.getPlayerWordMap().toString(),
            getLeaderBoard(gameState.getPlayerWordMap()).toString());
      }

      updateGameCache(gameState);
      return String.format(
          "[GameId:%s] : Player %s has identified word %s. List of discovered words is %s. Next player turn is %s", gameId,
          player, word, gameState.getDiscoveredWords(), gameState.getCurrentPlayerTurn());
    }
  }

  private GameState updatePlayerSequence(GameState gameState) {
    Queue playerSequence = gameState.getGame().getPlayerSequence();
    playerSequence.offer(playerSequence.poll());
    gameState.getGame().setPlayerSequence(playerSequence);
    gameState.setCurrentPlayerTurn(gameState.getGame().getPlayerSequence().peek());
    return gameState;
  }

  private static LeaderBoard getLeaderBoard(Map<Player, List<String>> map) {
    LeaderBoard leaderBoard = new LeaderBoard();
    List<Map.Entry<Player, List<String>>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<Player, List<String>>>() {
      @Override
      public int compare(Map.Entry<Player, List<String>> o1, Map.Entry<Player, List<String>> o2) {
        return o2.getValue().size() - o1.getValue().size();
      }
    });
    Map<Integer, Player> leaderBoardMap = new HashMap<>();
    int rank = WINNER_RANK;
    for (Map.Entry<Player, List<String>> entry : list) {
      leaderBoardMap.put(rank, entry.getKey());
      rank++;
    }
    leaderBoard.setRankPlayerMap(leaderBoardMap);
    leaderBoard.setWinner(leaderBoardMap.get(WINNER_RANK));
    return leaderBoard;
  }

  private GameInfoResponse updateGameCache(GameState gameState) {
    try {
      synchronized (GameCacheImpl.getInstance().getGameIdToGameStateMap().get(gameState.getGame().getGameId())) {
        GameCacheImpl.getInstance().getGameIdToGameStateMap().put(gameState.getGame().getGameId(), gameState);
        return new GameInfoResponse(Constants.SUCCESS_MESSAGE, gameState, gameState.getGame().getGameId());
      }
    } catch (Exception e) {
      log.error(String.format("Error occured in game with gameId : ", gameState.getGame().getGameId()));
      return new GameInfoResponse(Constants.FAILURE_MESSAGE, null, null);
    }

  }

  // public static void main(String[] args) {
  // Map<Player, List<String>> map = new HashMap<>();
  // List<String> l1 = new ArrayList<>();
  // List<String> l2 = new ArrayList<>();
  // List<String> l3 = new ArrayList<>();
  // l1.add("sd");
  // l1.add("df");
  // l2.add("aa");
  // l3.add("sdd");
  // l3.add("kjadfh");
  // l3.add("kabdfkjh");
  // Player p1 = new Player("1");
  // Player p2 = new Player("2");
  // Player p3 = new Player("3");
  // map.put(p1, l1);
  // map.put(p2, l2);
  // map.put(p3, l3);
  // System.out.println(getLeaderBoard(map).toString());
  //
  // }

}
