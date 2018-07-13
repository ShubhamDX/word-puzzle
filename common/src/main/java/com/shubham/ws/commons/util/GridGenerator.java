package com.shubham.ws.commons.util;

import static com.shubham.ws.commons.constants.Constants.COLUMNS;
import static com.shubham.ws.commons.constants.Constants.DIAGONAL;
import static com.shubham.ws.commons.constants.Constants.HORIZONTAL;
import static com.shubham.ws.commons.constants.Constants.ROWS;
import static com.shubham.ws.commons.constants.Constants.SEQUENCE;
import static com.shubham.ws.commons.constants.Constants.VERTICAL;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : shubham
 */

@Setter
@Getter
public class GridGenerator {

  private Character[][] grid;

  public GridGenerator() {
    grid = new Character[ROWS][COLUMNS];
    for (int i = 0; i < ROWS; i++)
      for (int j = 0; j < COLUMNS; j++)
        grid[i][j] = 0;
  }

  private String reverse(String word) {
    return new StringBuilder(word).reverse().toString();
  }

  public boolean insertWordInGrid(String word) {
    if (null == word || word.length() == 0 || null == grid || grid.length == 0) {
      return false;
    }

    int way, i, j, cnt = ROWS * COLUMNS * SEQUENCE;
    while (cnt > 0) {
      cnt--;
      i = (int) (Math.random() * 1000) % ROWS;
      j = (int) (Math.random() * 1000) % COLUMNS;
      {
        way = (int) (Math.random() * 1000) % SEQUENCE;
        if (way == HORIZONTAL && (insertHorizontally(word, i, j) || insertHorizontally(reverse(word), i, j))) {
          return true;
        } else if (way == VERTICAL && (insertVertically(word, i, j) || insertVertically(reverse(word), i, j))) {
          return true;
        } else if (way == DIAGONAL && (insertDiagonally(word, i, j) || insertDiagonally(reverse(word), i, j))) {
          return true;
        }
      }
    }
    return false;
  }

  /* Method for checking whether index lies within boundary */
  private boolean isValidIndex(int x, int y, int xx, int yy) {
    return (x >= 0 && y >= 0 && x < ROWS && y < COLUMNS && xx >= 0 && yy >= 0 && xx < ROWS && yy < COLUMNS);
  }

  private boolean insertVertically(final String word, final int startX, final int startY) {
    int wordLength = word.length();
    int idx = 0;

    while (idx < wordLength) {
      int tmpStartX = startX - idx;
      int tmpStartY = startY;
      int tmpEndX = tmpStartX + wordLength - 1;
      int tmpEndY = startY;

      if (isValidIndex(tmpStartX, tmpStartY, tmpEndX, tmpEndY)) {
        boolean isValid = true;
        for (int iterator = 0; iterator < wordLength; ++iterator) {
          if (!(grid[tmpStartX + iterator][startY] == 0 || word.charAt(iterator) == grid[tmpStartX + iterator][startY])) {
            isValid = false;
            break;
          }
        }
        if (isValid) {
          for (int iterator = 0; iterator < wordLength; ++iterator) {
            grid[tmpStartX + iterator][startY] = word.charAt(iterator);
          }
          return true;
        }
      }
      idx++;
    }
    return false;
  }

  private boolean insertHorizontally(final String word, final int startX, final int startY) {
    int wordLength = word.length();
    int idx = 0;

    while (idx < wordLength) {
      int tmpStartX = startX;
      int tmpStartY = startY - idx;
      int tmpEndX = tmpStartX;
      int tmpEndY = tmpStartY + wordLength - 1;

      if (isValidIndex(tmpStartX, tmpStartY, tmpEndX, tmpEndY)) {
        boolean isValid = true;
        for (int iterator = 0; iterator < wordLength; ++iterator) {
          if (!(grid[startX][tmpStartY + iterator] == 0 || word.charAt(iterator) == grid[startX][tmpStartY + iterator])) {
            isValid = false;
            break;
          }
        }

        if (isValid) {
          for (int iterator = 0; iterator < wordLength; ++iterator) {
            grid[startX][tmpStartY + iterator] = word.charAt(iterator);
          }
          return true;
        }
      }
      idx++;
    }
    return false;
  }

  private boolean insertDiagonally(final String word, final int startX, final int startY) {
    int wordLength = word.length();

    int idx = 0;
    while (idx < wordLength) {
      int tmpStartX = startX - idx;
      int tmpStartY = startY - idx;
      int tmpEndX = tmpStartX + wordLength - 1;
      int tmpEndY = tmpStartY + wordLength - 1;

      if (isValidIndex(tmpStartX, tmpStartY, tmpEndX, tmpEndY)) {
        boolean isValid = true;
        for (int iterator = 0; iterator < wordLength; ++iterator) {
          if (!(grid[tmpStartX + iterator][tmpStartY + iterator] == 0
              || word.charAt(iterator) == grid[tmpStartX + iterator][tmpStartY + iterator])) {
            isValid = false;
            break;
          }
        }

        if (isValid) {
          for (int iterator = 0; iterator < wordLength; ++iterator) {
            grid[tmpStartX + iterator][tmpStartY + iterator] = word.charAt(iterator);
          }
          return true;
        }
      }
      idx++;
    }

    idx = 0;
    while (idx < wordLength) {
      int tmpStartX = startX + idx;
      int tmpStartY = startY - idx;
      int tmpEndX = tmpStartX - wordLength - 1;
      int tmpEndY = tmpStartY + wordLength - 1;

      if (isValidIndex(tmpStartX, tmpStartY, tmpEndX, tmpEndY)) {
        boolean isValid = true;
        for (int iterator = 0; iterator < wordLength; ++iterator) {
          if (!(grid[tmpStartX - iterator][tmpStartY + iterator] == 0
              || word.charAt(iterator) == grid[tmpStartX - iterator][tmpStartY + iterator])) {
            isValid = false;
            break;
          }
        }

        if (isValid) {
          for (int iterator = 0; iterator < wordLength; ++iterator) {
            grid[tmpStartX - iterator][tmpStartY + iterator] = word.charAt(iterator);
          }
          return true;
        }
      }
      idx++;
    }

    return false;
  }

}
