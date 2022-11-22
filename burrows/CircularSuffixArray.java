/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 11/19/2022
 *  Description: Circular Suffix array for Burrows Wheeler transformation
 *  i     Original Suffixes          Sorted Suffixes       t    index[i]
--    -----------------------     -----------------------    --------
 0    A B R A C A D A B R A !     ! A B R A C A D A B R A    11
 1    B R A C A D A B R A ! A     A ! A B R A C A D A B R    10
 2    R A C A D A B R A ! A B     A B R A ! A B R A C A D    7
*3    A C A D A B R A ! A B R     A B R A C A D A B R A !   *0
 4    C A D A B R A ! A B R A     A C A D A B R A ! A B R    3
 5    A D A B R A ! A B R A C     A D A B R A ! A B R A C    5
 6    D A B R A ! A B R A C A     B R A ! A B R A C A D A    8
 7    A B R A ! A B R A C A D     B R A C A D A B R A ! A    1
 8    B R A ! A B R A C A D A     C A D A B R A ! A B R A    4
 9    R A ! A B R A C A D A B     D A B R A ! A B R A C A    6
10    A ! A B R A C A D A B R     R A ! A B R A C A D A B    9
11    ! A B R A C A D A B R A     R A C A D A B R A ! A B    2
*
* Performance requirements:   On typical English text, your data type must use
* space proportional to n + R (or better) and the constructor must take
* time proportional to n log n (or better). The methods length() and index()
* must take constant time in the worst case.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class CircularSuffixArray {
    private int len;
    private int[] sortedSuffix;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        len = s.length();
        char[] arr = s.toCharArray();

        ArrayList<CircularSuffix> sorted = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            sorted.add(new CircularSuffix(i, arr));
        }

        Collections.sort(sorted);
        // Test sorting
        /*
        for (CircularSuffix item : sorted) {
            String i = new String(item.getRotatedStr());
            StdOut.println(i + "-" + item.startIndex);
        }*/

        // create sorted suffix index map
        sortedSuffix = new int[len];

        int sortIndex = 0;
        for (CircularSuffix suffix : sorted) {
            sortedSuffix[sortIndex] = suffix.getStartIndex();
            sortIndex++;
        }
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {

        validate(i);
        return sortedSuffix[i];
    }

    private void validate(int i) {
        if (i < 0 || i >= len) throw new IllegalArgumentException();
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private int startIndex;
        private char[] str;
        private int len;

        public CircularSuffix(int start, char[] input) {
            startIndex = start;
            setStr(input);
            len = input.length;
        }

        public int compareTo(CircularSuffix that) {

            int index = 0;
            int leftIndex = startIndex;
            int rightIndex = that.getStartIndex();
            while (index < len - 1 && str[leftIndex] == that.getStr()[rightIndex]) {
                index++;
                leftIndex++;
                rightIndex++;
                leftIndex = (leftIndex >= len) ? leftIndex % len : leftIndex;
                rightIndex = (rightIndex >= len) ? rightIndex % len : rightIndex;
            }
            return str[leftIndex] - that.getStr()[rightIndex];
        }

        public char[] getStr() {
            return str;
        }
        /*
        // test
        public char[] getRotatedStr() {
            char[] tst = new char[len];
            int start = startIndex;
            for (int j = 0; j < len; j++) {
                tst[j] = str[start];
                start++;
                start = (start >= len) ? start % len : start;
            }
            return tst;
        }*/

        private void setStr(char[] str) {
            this.str = str;
        }

        public int getStartIndex() {
            return startIndex;
        }


    }

    // unit testing (required)
    public static void main(String[] args) {
        String[] s = new In(args[0]).readAllLines();
        String fullMsg = String.join("\n", s);
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(fullMsg);

        for (int i = 0; i < fullMsg.length(); i++) {
            StdOut.println(circularSuffixArray.index(i));
        }
    }


}
