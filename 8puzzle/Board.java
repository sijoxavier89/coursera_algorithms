/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 1-May-2020
 *  Description: This class create Board for 8Puzzle Assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {

    int[] board = null;
    int len = 0;
    int size = 0;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        size = tiles.length;
        len = size*size;
        int index = 0;
        for(int i = 0; i<size; i++)
        {
            for(int j=0; j<size;j++)
            {
                board[index] = tiles[i][j];
                index ++;
            }
        }
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        for (int i = 0; i < len; i++) {

            s.append(String.format("%2d ", board[i]));
            if(i % size == 0) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // board dimension n
    public int dimension()
    {
        int dimension = size;
        return dimension;
    }

    // number of tiles out of place
    public int hamming()
    {
        int numOutOfPlace = 0;
        for(int i=0; i< len; i++)
        {
            if(i != board[i])
            {
                numOutOfPlace++;
            }
        }
        return numOutOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int manhattanDistance = 0;

        for(int i = 0; i < len; i++)
        {
            manhattanDistance += Math.abs((i+1) - board[i]);
        }

        return  manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        for(int i=0; i< len; i++)
        {
            if(i != board[i])
            {
                return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;

        for(int i = 0; i < len; i++)
        {
            if(that.board[i] != this.board[i])
            {
                return false;
            }

        }

        return  true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {

        // index of blank tile
        int indexOfZero = Arrays.asList(board).indexOf(0);

        // get posible index positions
        int[] positions = getPosiblePositions(indexOfZero);

        Stack<Board> neigbours = new Stack<Board>();
        // swap zero tile with the nearest tile
        int[] boardCopy1;
        for(int p:positions)
        {
            boardCopy1 = board.clone();

            int temp = boardCopy1[p-1];
            boardCopy1[p-1] = boardCopy1[indexOfZero];
            boardCopy1[indexOfZero] = temp;
            neigbours.push(new Board(getTwoDArray(boardCopy1)));
        }
        return neigbours;
    }

    private int[][] getTwoDArray(int[] arr)
    {
        int[][] twodarr = new int[size][size];
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                twodarr[j][i] = arr[i * size + j];
            }
        }

        return  twodarr;
    }
    // return array of swap positions
    private int[] getPosiblePositions(int zeroTileIndex)
    {
       int[] pos = null;
       int zeroTilePos =  zeroTileIndex + 1;
       int d = zeroTilePos / size;
       int r = zeroTilePos % size;
       int i = 0;

       // not the last column
       if(r > 0)
       {
           pos[i] = i+1;
           i++;
       }

       // not the first column
       if(r > 1)
       {
           pos[i] = i+1;
           i++;
       }

       // not the last row
       if(d < size && r > 0)
       {
           pos[i] = i+size;
           i++;
       }

       // not the first row
        if(d > 1)
        {
            pos[i] = i - size;
            i++;
        }
        return pos;
    }
    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        // index of blank tile
        int indexOfZero = Arrays.asList(board).indexOf(0);
        int d = indexOfZero + 1 / size ;
        int r = indexOfZero + 1 % size;
        int row = (r == 0)? d: d+1;

        int tile1Index= 0;
        int tile2Index = 0;

        int i = -1;
        if(row == 1)
        {
            i = row*size;

        }else
        {
             i = (row - 1)*size;
        }

        tile1Index = i;
        tile2Index = i+1;
        // swap tiles
        int[] boardCopy = board.clone();
        int temp = boardCopy[tile1Index];
        boardCopy[tile1Index] = boardCopy[tile2Index];
        boardCopy[tile2Index] = temp;

        return   new Board(getTwoDArray(boardCopy));
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {
    }

}