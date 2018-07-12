package com.shubham.ws.models;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@ToString
public class GameState {

  private Set<String> discoveredWords;
  private Map<Player, List<String>> playerWordMap;
  private Player currentPlayerTurn;
  private Game game;
}
