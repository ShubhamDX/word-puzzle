package com.shubham.ws.commons.dictionary;

import static com.shubham.ws.commons.constants.Constants.A_ASCII;
import static com.shubham.ws.commons.constants.Constants.EMPTY_STRING;
import static com.shubham.ws.commons.constants.Constants.NOT_TO_PROCEED;
import static com.shubham.ws.commons.constants.Constants.TO_PROCEED;
import static com.shubham.ws.commons.constants.Constants.Z_ASCII;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import lombok.NoArgsConstructor;

/**
 * @author : shubham; Trie implementation for storing dictionary and searching a particular word and random words. Single
 *         character words are not allowed
 */

@NoArgsConstructor
public class Dictionary {
  private static Map<Character, Node> roots;
  private static Random random;

  static {
    roots = new HashMap<>();
    random = new Random();
  }

  public boolean search(String string) {
    if (roots.containsKey(string.charAt(0))) {
      if (string.length() == 1 && roots.get(string.charAt(0)).endOfWord) {
        return true;
      }
      return searchFor(string.substring(1), roots.get(string.charAt(0)));
    } else {
      return false;
    }
  }

  public Set<String> randomWordsSearch(int numOfWords) {
    Set<String> randomWords = new HashSet<>();
    random.ints(numOfWords, A_ASCII, Z_ASCII + 1).forEach(i -> randomWords.add((char) i + randomSearch(roots.get((char) i))));
    return randomWords;
  }

  private String randomSearch(Node node) {
    if (null == node || node.children.size() <= 0) {
      return EMPTY_STRING;
    }
    if (node.endOfWord) {
      if (random.ints(1, NOT_TO_PROCEED, TO_PROCEED + 1).findFirst().getAsInt() == NOT_TO_PROCEED) {
        return EMPTY_STRING;
      }
    }
    List<Character> keysAsArray = new ArrayList<>(node.children.keySet());
    Character character = keysAsArray.get(random.nextInt(keysAsArray.size()));
    return character + randomSearch(node.children.get(character));
  }

  public void insert(String string) {
    if (!roots.containsKey(string.charAt(0))) {
      roots.put(string.charAt(0), new Node());
    }

    insertWord(string.substring(1), roots.get(string.charAt(0)));
  }

  private void insertWord(String string, Node node) {
    final Node nextChild;
    if (node.children.containsKey(string.charAt(0))) {
      nextChild = node.children.get(string.charAt(0));
    } else {
      nextChild = new Node();
      node.children.put(string.charAt(0), nextChild);
    }

    if (string.length() == 1) {
      nextChild.endOfWord = true;
    } else {
      insertWord(string.substring(1), nextChild);
    }
  }

  private boolean searchFor(String string, Node node) {
    if (string.length() == 0)
      return node.endOfWord;

    return node.children.containsKey(string.charAt(0)) && searchFor(string.substring(1), node.children.get(string.charAt(0)));

  }

  private class Node {
    public Boolean endOfWord = false;
    public Map<Character, Node> children = new HashMap<>();
  }
}
