package com.shubham.dtos.response;

import com.shubham.ws.models.GameState;

import java.io.Serializable;

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
public class GameInfoResponse implements Serializable {
  private String message;
  private GameState gameState;
  private Integer gameId;

}
