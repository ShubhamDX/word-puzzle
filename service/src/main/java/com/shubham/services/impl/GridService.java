package com.shubham.services.impl;

import static com.shubham.ws.commons.constants.Constants.A_ASCII;
import static com.shubham.ws.commons.constants.Constants.RANDOM_WORDS_COUNT;
import static com.shubham.ws.commons.constants.Constants.ROWS;
import static com.shubham.ws.commons.constants.Constants.Z_ASCII;

import com.shubham.ws.commons.util.GridGenerator;
import com.shubham.ws.models.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    Grid grid = new Grid();
    GridGenerator gridGenerator = new GridGenerator();
    Set<String> randomWords = dictionaryService.getRandomWords(RANDOM_WORDS_COUNT);
    List<String> containedWords = new ArrayList<>();
    int wordsToInsert = 10 + random.nextInt(5);
    int maxRandomFetch = 3;
    for (String word : randomWords) {
      if (wordsToInsert == 0)
        break;
      else {
        if (randomWords.size() == 0 && maxRandomFetch == 0) {
          break;
        } else if (randomWords.size() == 0 && maxRandomFetch > 0)
          randomWords = dictionaryService.getRandomWords(RANDOM_WORDS_COUNT);
        else {
          if (word.length() <= ROWS - 2) {
            boolean isInserted = gridGenerator.insertWordInGrid(word);
            if (isInserted) {
              --wordsToInsert;
              containedWords.add(word);
            }
          }
        }
      }
    }
    grid.setGrid(completeGrid(gridGenerator.getGrid()));
    grid.setContainedWords(containedWords);
    return grid;
  }

  private Character[][] completeGrid(Character[][] grid) {

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] == 0) {
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
