package com.shubham.ws.commons.util;

/**
 * @author : shubham
 */

public class RandomIdGenerator {
  public static Long getRandomPlayerId() {
    return (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
  }
}
