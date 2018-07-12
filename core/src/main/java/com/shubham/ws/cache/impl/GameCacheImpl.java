package com.shubham.ws.cache.impl;

import com.shubham.ws.cache.ICache;
import com.shubham.ws.models.GameState;

import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : shubham
 */
@Getter
@Setter
public class GameCacheImpl implements ICache {

  private ConcurrentHashMap<Integer, GameState> gameIdToGameStateMap;
  private static volatile GameCacheImpl instance = null;

  private GameCacheImpl() {
    gameIdToGameStateMap = new ConcurrentHashMap<>();
  }

  public static GameCacheImpl getInstance() {
    if (null == instance) {
      synchronized (GameCacheImpl.class) {
        if (null == instance) {
          instance = new GameCacheImpl();
        }
      }
    }
    return instance;
  }

}
