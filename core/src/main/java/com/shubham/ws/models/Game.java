package com.shubham.ws.models;

import com.shubham.ws.commons.enums.GameStatus;

import java.util.List;
import java.util.Queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : shubham
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {

  private int gameId;
  private Player admin;
  private List<Player> players;
  private GameStatus gameStatus;
  private Grid grid;
  private Queue<Player> playerSequence;
  private int maxPlayers;
}
