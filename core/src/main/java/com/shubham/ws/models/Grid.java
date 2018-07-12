package com.shubham.ws.models;

import java.util.List;

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
@ToString(exclude = "containedWords")
public class Grid {

  Character[][] grid;
  List<String> containedWords;

  // @Override
  // public String toString() {
  // StringBuilder sb = new StringBuilder();
  // for (int i = 0; i < grid.length; i++) {
  // Arrays.stream(grid[i]).forEach(j -> sb.append(j).append(','));
  // sb.append('\n');
  // }
  // return sb.toString();
  // }
}
