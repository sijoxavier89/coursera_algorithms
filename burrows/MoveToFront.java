/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 11/09/2022
 *  Description: Move to Front encoding as part of
 * coursera algorithm assignment
 * move-to-front    in   out
-------------    ---  ---
 A B C D E F      C    2
 C A B D E F      A    1
 A C B D E F      A    0
 A C B D E F      A    0
 A C B D E F      B    2
 B A C D E F      C    2
 C B A D E F      C    0
 C B A D E F      C    0
 C B A D E F      A    2
 A C B D E F      C    1
 C A B D E F      C    0
 C A B D E F      F    5
 F C A B D E
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    private static int[] asciiIndex = new int[R];
    private static char[] ascii = new char[R];

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // initialize with extended ascii characters
        for (int i = 0; i < R; i++) {
            asciiIndex[i] = i;
            ascii[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char input = (char) (BinaryStdIn.readByte() & 0xff);
            int index = asciiIndex[input];
            BinaryStdOut.write(index, 8);
            if (index != 0)
                moveToFront(index, input, asciiIndex);
        }

        BinaryStdOut.flush();
        BinaryStdOut.close();

    }

    // re-arrange the ascii array of characters by moving the
    // char c at index to front
    private static void moveToFront(int index, char c, int[] asciiIndexParam) {

        // move the char to front and shift char before one
        // position right
        // update the index of characters till the index to +1
        for (int i = 0; i < index; i++) {
            asciiIndexParam[ascii[i]] += 1;
        }
        // shift all characters before the index character
        // to 1  position right
        System.arraycopy(ascii, 0, ascii, 1, index);
        ascii[0] = c;
        // update the index to first -> 0
        asciiIndexParam[c] = 0;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // initialize with extended ascii characters
        for (int i = 0; i < R; i++) {
            asciiIndex[i] = i;
            ascii[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readByte();
            char input = ascii[index];
            BinaryStdOut.write(input);
            if (index != 0)
                moveToFront(index, input, asciiIndex);
        }

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        boolean toTransform = (args[0].equals("-"));
        if (toTransform) encode();
        else decode();

    }
}
