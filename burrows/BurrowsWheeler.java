/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 11/19/2022
 *  Description:  Given a typical English text file, transform it into a text file
 *  in which sequences of the same character occur near each other many times.
 *  *  i     Original Suffixes          Sorted Suffixes       t    index[i]
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
* Performance requirements:   The running time of your Burrows–Wheeler
*  transform must be proportional to n + R (or better) in the worst case,
* excluding the time to construct the circular suffix array.
* The running time of your Burrows–Wheeler inverse transform
*  must be proportional to n + R (or better) in the worst case.
*  The amount of memory used by both the Burrows–Wheeler transform
*  and inverse transform must be proportional to n + R (or better) in the worst case.
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output

    private static final int R = 256;

    public static void transform() {
        String input = "";
        int first = 0;
        char[] transform = null;
        while (!BinaryStdIn.isEmpty()) {
            input = BinaryStdIn.readString();
            CircularSuffixArray suffixArray = new CircularSuffixArray(input);
            int len = input.length();
            transform = new char[len];
            for (int i = 0; i < len; i++) {
                int index = suffixArray.index(i);
                if (index != 0) {
                    transform[i] = input.charAt(index - 1);
                }
                else {
                    first = i;
                    transform[i] = input.charAt(len - 1);
                }
            }
        }

        // Ouput
        BinaryStdOut.write(first);
        for (char c : transform != null ? transform : new char[0]) {
            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int len = 0;
        char[] aux = null;
        int[] next = null;
        int first = 0;
        while (!BinaryStdIn.isEmpty()) {
            first = BinaryStdIn.readInt();

            String input = BinaryStdIn.readString();
            len = input.length();
            next = new int[len];
            int[] count = new int[R + 1];
            // Key indexed counting for sorting
            for (int i = 0; i < len; i++) {
                count[input.charAt(i) + 1]++;
            }

            for (int r = 0; r < R; r++) {
                count[r + 1] += count[r];
            }

            // sorted character array represent
            // first character for the sorted suffixes
            aux = new char[len];
            for (int i = 0; i < len; i++) {
                next[count[input.charAt(i)]] = i;
                aux[count[input.charAt(i)]] = input.charAt(i);
                count[input.charAt(i)]++;

            }
            
        }


        // output
        int nextIndex = first;
        int n = 0;
        while (n < len) {
            BinaryStdOut.write(aux[nextIndex]);
            nextIndex = next[nextIndex];
            n++;
        }

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        boolean toTransform = (args[0].equals("-"));
        if (toTransform) transform();
        else inverseTransform();

    }

}
