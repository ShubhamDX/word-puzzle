package com.shubham.services.impl;

import static com.shubham.ws.commons.constants.Constants.A_ASCII;
import static com.shubham.ws.commons.constants.Constants.Z_ASCII;

import com.shubham.ws.models.Grid;

import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : shubham
 */
@Service
public class GridService {
  @Autowired
  DictionaryServiceImpl dictionaryService;

  private static Random random;
  static {
    random = new Random();
  }

  public Grid getGrid() {
    Grid gridObj = new Grid();
    Character[][] grid = new Character[5][5];
    grid[0][4] = 'e';
    grid[0][3] = 'h';
    grid[0][2] = 't';
    grid[4][0] = 's';
    grid[3][1] = 'p';
    grid[2][2] = 'i';
    grid[1][3] = 'n';
    grid[1][4] = 'y';
    grid[2][4] = 'b';
    grid = completeGrid(grid);
    gridObj.setGrid(grid);
    gridObj.setContainedWords(Arrays.asList("spine", "the", "bye"));
    return gridObj;

  }

  private Character[][] completeGrid(Character[][] grid) {

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] == null) {
          grid[i][j] = (char) random.ints(1, A_ASCII, Z_ASCII + 1).findFirst().getAsInt();
        }
      }
    }
    return grid;
  }

  public boolean searchWordInGrid(String word, Grid gridObject) {
    Character[][] grid = gridObject.getGrid();
    if (null == word || word.length() == 0 || null == grid || grid.length == 0) {
      return false;
    }
    if (gridObject.getContainedWords().contains(word))
      return true;
    if (dictionaryService.search(word)) {

      for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[0].length; j++) {
          if (grid[i][j] == word.charAt(0)) {
            if (searchHorizontally(word.substring(1), grid, i, j)) {
              return true;
            } else if (searchVertically(word.substring(1), grid, i, j)) {
              return true;
            } else if (searchDiagonally(word.substring(1), grid, i, j)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private boolean searchHorizontally(final String word, final Character[][] grid, final int i, final int j) {
    String tmpWord = word;
    int k = j;
    while (isValidIndex(i, ++k, grid)) {
      if (tmpWord.charAt(0) == grid[i][k]) {
        if (tmpWord.length() == 1)
          return true;
        tmpWord = tmpWord.substring(1);
      }
    }
    tmpWord = word;
    k = j;
    while (isValidIndex(i, --k, grid)) {
      if (tmpWord.charAt(0) == grid[i][k]) {
        if (tmpWord.length() == 1)
          return true;
        tmpWord = tmpWord.substring(1);
      }
    }
    return false;
  }

  private boolean searchVertically(final String word, final Character[][] grid, final int i, final int j) {
    String tmpWord = word;
    int k = i;
    while (isValidIndex(++k, j, grid)) {
      if (tmpWord.charAt(0) == grid[k][j]) {
        if (tmpWord.length() == 1)
          return true;
        tmpWord = tmpWord.substring(1);
      }
    }
    tmpWord = word;
    k = i;
    while (isValidIndex(--k, j, grid)) {
      if (tmpWord.charAt(0) == grid[k][j]) {
        if (tmpWord.length() == 1)
          return true;
        tmpWord = tmpWord.substring(1);
      }
    }
    return false;
  }

  private boolean searchDiagonally(final String word, final Character[][] grid, final int i, final int j) {
    String tmpWord = word;
    int k = i;
    int l = j;
    while (isValidIndex(++k, ++l, grid)) {
      if (tmpWord.charAt(0) == grid[k][l]) {
        if (tmpWord.length() == 1)
          return true;
        tmpWord = tmpWord.substring(1);
      }
    }
    tmpWord = word;
    k = i;
    l = j;
    while (isValidIndex(++k, --l, grid)) {
      if (tmpWord.charAt(0) == grid[k][l]) {
        if (tmpWord.length() == 1)
          return true;
        tmpWord = tmpWord.substring(1);
      }
    }
    tmpWord = word;
    k = i;
    l = j;
    while (isValidIndex(--k, ++l, grid)) {
      if (tmpWord.charAt(0) == grid[k][l]) {
        if (tmpWord.length() == 1)
          return true;
        tmpWord = tmpWord.substring(1);
      }
    }
    tmpWord = word;
    k = i;
    l = j;
    while (isValidIndex(--k, --l, grid)) {
      if (tmpWord.charAt(0) == grid[k][l]) {
        if (tmpWord.length() == 1)
          return true;
        tmpWord = tmpWord.substring(1);
      }
    }
    return false;
  }

  private <T> boolean isValidIndex(int x, int y, T[][] grid) {
    return (x >= 0 && y >= 0 && x < grid.length && y < grid[0].length);
  }

  public static void main(String[] args) {
    // Set<String> words = dictionary.randomWordsSearch(DEFAULT_RANDOM_WORDS_COUNT);
    // words.stream().forEach(i -> System.out.println(i));
    // System.out.println(dictionary.search("bye"));
    // dictionaryService
  }
}
