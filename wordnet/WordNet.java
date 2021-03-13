import edu.princeton.cs.algs4.Bag;

public class WordNet<string> {

    Bag<Integer>[] wordGraph;
    String[] nouns;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {

    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        throw new IllegalArgumentException();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        throw new IllegalArgumentException();
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        throw new IllegalArgumentException();
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        throw new IllegalArgumentException();
    }
}