import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

    private Map<Integer, List<Integer>> hypernyms;
    private Map<Integer, String> synsets;
    private Map<String, List<Integer>> wordMap;
    private Digraph graph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException(" input to constructor is null ");
        readSynsets(synsets);
        readHypernyms(hypernyms);
        initializeGraph();
        sap = new SAP(graph);
    }

    private void readSynsets(String filename) {
        In in = new In(filename);
        this.synsets = new HashMap<Integer, String>();
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String noun = line[1];
            this.synsets.put(id, noun);
        }
    }

    private void readHypernyms(String filename) {
        In in = new In(filename);
        this.hypernyms = new HashMap<Integer, List<Integer>>();
        wordMap = new HashMap<>();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] items = line.split(",");
            int id = Integer.parseInt(items[0]);
            List<Integer> bag = new ArrayList<Integer>();
            for (int i = 1; i < items.length; i++) {
                bag.add(Integer.parseInt(items[i]));
            }

            this.hypernyms.put(id, bag);
            // build wordMap for searching nouns
            buildWordMap(id);

        }
    }

    private void buildWordMap(Integer id) {
        // set word map
        for (String noun : synsets.get(id).split(" ")) {
            if (!wordMap.containsKey(noun)) {
                List<Integer> bagId = new ArrayList<Integer>();
                bagId.add(id);
                wordMap.put(noun, bagId);
            }
            else {
                wordMap.get(noun).add(id);
            }

        }
    }

    // initialize and validate graph
    private void initializeGraph() {
        graph = new Digraph(hypernyms.size());

        for (int noun : hypernyms.keySet()) {
            for (int hn : hypernyms.get(noun)) {
                graph.addEdge(noun, hn);
            }
        }
        // validate graph
        isRootedDAG(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException(" word is null");
        return wordMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA, nounB);
        int distance = -1;
        if (wordMap.get(nounA).size() == 1 && wordMap.get(nounB).size() == 1) {
            distance = sap.length(wordMap.get(nounA).get(0), wordMap.get(nounB).get(0));
        }
        else {
            distance = sap.length(wordMap.get(nounA), wordMap.get(nounB));
        }

        return distance;
    }

    // check the noun corresponds to wordnet noun
    private void validateNoun(String nounA, String nounB) {
        if ((nounA == null) || !isNoun(nounA))
            throw new IllegalArgumentException();

        if ((nounB == null) || !isNoun(nounB))
            throw new IllegalArgumentException();
    }

    // check whether the given graph is rooted DAG
    // the graph should have exactly one vertex
    // whose out degree is 0 , there should not be
    // any vertex which is not connected
    private void isRootedDAG(Digraph graph) {
        int countRoot = 0;
        for (int v : hypernyms.keySet()) {
            if (graph.outdegree(v) == 0)
                countRoot += 1;
            if (countRoot > 1) throw new IllegalArgumentException(" not a rooted DAG");
        }
        if (countRoot == 0) throw new IllegalArgumentException(" not a rooted DAG");
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA, nounB);
        int sapIndex = -1;
        if (wordMap.get(nounA).size() == 1 && wordMap.get(nounB).size() == 1) {
            sapIndex = sap.ancestor(wordMap.get(nounA).get(0), wordMap.get(nounB).get(0));
        }
        else {
            sapIndex = sap.ancestor(wordMap.get(nounA), wordMap.get(nounB));
        }
        String anc = synsets.get(sapIndex);
        return anc;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        StdOut.println("enter synset and hypernyms");
        while (!StdIn.isEmpty()) {

            String synset = StdIn.readLine();
            String hypernym = StdIn.readLine();
            WordNet wn = new WordNet(synset, hypernym);
            StdOut.println("enter two nouns");
            String nounA = StdIn.readLine();
            String nounB = StdIn.readLine();
            StdOut.println("SAP:");
            StdOut.println(wn.sap(nounA, nounB));
            StdOut.println("distance:");
            StdOut.println(wn.distance(nounA, nounB));
        }
    }

}
