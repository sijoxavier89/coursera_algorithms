import edu.princeton.cs.algs4.StdOut;

// Data structure to support prefix query
public class TrieSx {
    private static final int R = 26;
    private Node root;
    private int count;

    private static class Node {
        private int value;
        private Node[] next = new Node[R];
    }

    public TrieSx() {

    }

    public TrieSx(String[] dictionary) {
        if (dictionary.length > 0) {
            for (String s : dictionary) {
                if (s.length() > 2)
                    put(s);
            }
        }
    }

    public void put(String word) {
        if (word == null || word.isEmpty())
            throw new IllegalArgumentException("input string is empty");
        root = put(root, word, count + 1, 0);
    }

    private Node put(Node x, String key, int value, int d) {
        if (x == null) {
            x = new Node();
        }

        if (d == key.length()) {
            x.value = value;
            count += 1;
        }
        else {
            char c = key.charAt(d);
            x.next[c - 65] = put(x.next[c - 65], key, value, d + 1);
        }
        return x;
    }

    public int size() {
        return count;
    }

    // returns 0 if trie contains prefix
    // returns -1 if trie not contain prefix
    // returns > 0 if the prefix a valid word
    public int isPrefix(String prefix) {
        return isPrefix(root, prefix, 0);
    }

    private int isPrefix(Node x, String prefix, int d) {
        if (x == null) return -1;

        if (d == prefix.length() && x.value != 0) return x.value;
        else if (d == prefix.length()) {
            return 0;
        }
        else {
            char c = prefix.charAt(d);
            return isPrefix(x.next[c - 65], prefix, d + 1);
        }
    }

    public static void main(String[] args) {
        String[] dictionary = new String[] {
                "SHE", "SELLS", "SEA", "SHELLS", "BY", "THE", "SHORE"
        };

        TrieSx trie = new TrieSx(dictionary);
        StdOut.println("size:" + trie.size());
        StdOut.println("isPrefix(S)");
        StdOut.println(trie.isPrefix("S"));
        StdOut.println();
        StdOut.println("isPrefix(SH)");
        StdOut.println(trie.isPrefix("SH"));
        StdOut.println("isPrefix(SHE)");
        StdOut.println(trie.isPrefix("SHE"));
        StdOut.println();
        StdOut.println("isPrefix(B)");
        StdOut.println(trie.isPrefix("B"));
        StdOut.println();
        StdOut.println("isPrefix(SHOR)");
        StdOut.println(trie.isPrefix("SHOR"));
        StdOut.println();

        StdOut.println("isPrefix(HE)");
        StdOut.println(trie.isPrefix("HE"));
        StdOut.println();

        StdOut.println("isPrefix(HELL)");
        StdOut.println(trie.isPrefix("HELL"));
        StdOut.println();

        StdOut.println("isPrefix(SHORE)");
        StdOut.println(trie.isPrefix("SHORE"));
        StdOut.println();

        StdOut.println("isPrefix(BY)");
        StdOut.println(trie.isPrefix("BY"));
        StdOut.println();

        StdOut.println("isPrefix(SELLS)");
        StdOut.println(trie.isPrefix("SELLS"));
        StdOut.println();

        StdOut.println("isPrefix(SEA)");
        StdOut.println(trie.isPrefix("SEA"));
        StdOut.println();

    }
}
