/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: Feb 13, 2022
 *  Description: Outcast
 * Given a list of WordNet nouns x1, x2, ..., xn, which noun is the least related to the others? To identify an outcast,
 * compute the sum of the distances between each noun and every other one:
 * di   =   distance(xi, x1)   +   distance(xi, x2)   +   ...   +   distance(xi, xn)
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {

        wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = "";
        int distance = 0;
        int totalDistance = 0;
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {

                int currentDist = wordNet.distance(nouns[i], nouns[j]);
                distance += currentDist;
            }
            if (distance > totalDistance) {
                totalDistance = distance;
                outcast = nouns[i];
            }
            distance = 0;
        }
        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }

    }
}
