package com.shubham.services.impl;

import static com.shubham.ws.commons.constants.Constants.A_ASCII;
import static com.shubham.ws.commons.constants.Constants.DEFAULT_RANDOM_WORDS_COUNT;
import static com.shubham.ws.commons.constants.Constants.WORDS_FILE_NAME;
import static com.shubham.ws.commons.constants.Constants.Z_ASCII;

import com.shubham.ws.commons.dictionary.Dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author : shubham
 */

@Service
public class DictionaryServiceImpl {

  public static Dictionary dictionary;
  private static Random random;
  private static final Logger log = LoggerFactory.getLogger(DictionaryServiceImpl.class);
  static {
    random = new Random();
    dictionary = new Dictionary();
    try {
      try (BufferedReader br = new BufferedReader(new FileReader(WORDS_FILE_NAME))) {
        String line;
        while ((line = br.readLine()) != null) {

          if (line.length() > 1 && (A_ASCII <= (int) line.charAt(0)) && ((int) line.charAt(0) <= Z_ASCII))
            dictionary.insert(line.toLowerCase());

        }

      }
    } catch (IOException e) {
      log.error(String.format("Error in initializing dictionary. ", e));
    }

  }

  public Set<String> getRandomWords() {
    return dictionary.randomWordsSearch(DEFAULT_RANDOM_WORDS_COUNT);
  }

  public Set<String> getRandomWords(int wordsCount) {
    return dictionary.randomWordsSearch(wordsCount);
  }

  public boolean search(String string) {
    return string.length() > 1 && dictionary.search(string);
  }

}
