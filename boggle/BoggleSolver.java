import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/*
Plan: create DFS to search through the board
Create R-Trie to do prefix search
Optimization - check search path for prefix in trie
if the prefix search result in the complete word
add it to the result
 */
public class BoggleSolver {

    private HashSet<String> dict;
    private HashMap<String, Integer> result;
    private int maxRowSize = 0;
    private int maxColSize = 0;
    private boolean[][] visited;
    private TrieSx trie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        dict = new HashSet<String>();
        Collections.addAll(dict, dictionary);
        trie = new TrieSx(dictionary);
    }

    private void resetVisited() {
        for (int i = 0; i <= maxRowSize; i++) {
            for (int j = 0; j <= maxColSize; j++) {
                visited[i][j] = false;
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        result = new HashMap<>();
        maxRowSize = board.rows() - 1;
        maxColSize = board.cols() - 1;
        visited = new boolean[maxRowSize + 1][maxColSize + 1];
        // do dfs for each dice in the board
        for (int i = 0; i <= maxRowSize; i++) {
            for (int j = 0; j <= maxColSize; j++) {
                String word = "";
                resetVisited();
                dfs(i, j, board, word);
            }
        }

        return result.keySet();
    }

    private void dfs(int row, int col, BoggleBoard board, String path) {
        if (row < 0 || row > maxRowSize) return;
        if (col < 0 || col > maxColSize) return;
        if (visited[row][col]) return;

        char c = board.getLetter(row, col);
        String next;
        if (c != 'Q')
            next = path + c;
        else next = path + "QU";
        // check the validity
        if (checkDictionary(next) < 0) return;
        else if (checkDictionary(next) > 0) addToResult(next);

        visited[row][col] = true;
        // dfs
        dfs(row - 1, col, board, next);
        dfs(row - 1, col - 1, board, next);
        dfs(row - 1, col + 1, board, next);
        dfs(row, col - 1, board, next);
        dfs(row, col + 1, board, next);
        dfs(row + 1, col, board, next);
        dfs(row + 1, col - 1, board, next);
        dfs(row + 1, col + 1, board, next);

        visited[row][col] = false;
    }

    private void addToResult(String word) {
        result.put(word, getScore(word));
    }

    private int getScore(String word) {
        int len = word.length();
        if (len <= 4) return 1;
        else if (len <= 5) return 2;
        else if (len <= 6) return 3;
        else if (len <= 7) return 5;
        else return 11;
    }

    // return 0 when a valid prefix of words in the dictionary
    // return -1 when prefix is not part of  a word
    // returns > 0 when prefix is a word in the dictionary
    private int checkDictionary(String prefix) {
        return trie.isPrefix(prefix);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return result.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
