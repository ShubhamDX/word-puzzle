package com.shubham.dtos.game;

import com.shubham.ws.models.Player;

import java.util.Map;

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
public class LeaderBoard {
  private Map<Integer, Player> rankPlayerMap;
  private Player winner;
}
