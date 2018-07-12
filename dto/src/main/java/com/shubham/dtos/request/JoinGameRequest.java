package com.shubham.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shubham.ws.models.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : shubham
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinGameRequest {

  private Player playerId;
  private Integer gameId;

}
