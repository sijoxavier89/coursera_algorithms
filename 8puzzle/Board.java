/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 1-May-2020
 *  Description: This class create Board for 8Puzzle Assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

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
        board = new int[len];
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
            if((i+1) % size == 0) {
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
            if(board[i] != 0) {
                if (i + 1 != board[i]) {
                    numOutOfPlace++;
                }
            }
        }
        return numOutOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int manhattanDistance = 0;
         int rowdist = 0;
         int coldist = 0;
         int val = 0;
        for(int i = 0; i < len; i++)
        {
            if(board[i] != 0) {
                val = i+1;
                coldist = Math.abs(getColIndex(val) - getColIndex(board[i]));
                rowdist = Math.abs(getRowIndex(val) - getRowIndex(board[i]));
                manhattanDistance += coldist + rowdist;
            }
        }

        return  manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        // checking i < len - 1 as the last
        // index is 0 for the goal board
        for(int i=0; i< len - 1; i++)
        {
            if(i+1 != board[i])
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
        int indexOfZero = getIndexZero(board);

        // get posible index positions
        Iterable<Integer> positions = getPosiblePositions(indexOfZero);

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

    // Get the 0 based index of tile 0
    private int getIndexZero(int[] b)
    {
       for(int i=0;i< len; i++)
       {
           if(b[i] == 0)
               return i;
       }
       return -1;
    }
    private int[][] getTwoDArray(int[] arr)
    {
        int[][] twodarr = new int[size][size];
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                twodarr[i][j] = arr[i * size + j];
            }
        }

        return  twodarr;
    }
    // return array of swap positions
    private Iterable<Integer> getPosiblePositions(int zeroTileIndex)
    {
       Stack<Integer> pos = new Stack<Integer>();
       int zeroTilePos =  zeroTileIndex + 1;
       int d = zeroTilePos / size;
       int r = zeroTilePos % size;

       // not the last or first column
       if(r != 0 && r != 1)
       {
          pos.push(zeroTilePos-1);
           pos.push(zeroTilePos+1);
       }
       else if(r == 1)// first column
       {
           pos.push(zeroTilePos+1);
       }else{
           pos.push(zeroTilePos-1);
       }

       // first row
       if((d == 1 && r == 0)|| (d == 0 && r > 0))
       {
           pos.push(zeroTilePos+size);
       }else if((d == size - 1 && r >0) ||(d == size && r == 0)) // last row
       {
           pos.push(zeroTilePos-size);
       }else{
           pos.push(zeroTilePos+size);
           pos.push(zeroTilePos-size);
       }

        return pos;
    }
    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        // index of blank tile
        int indexOfZero = getIndexZero(board);
        int row = getRowIndex(indexOfZero);

        int tile1Index= 0;
        int tile2Index = 0;

        int i = -1;
        if(row == 1)
        {
            i = size;

        }else if(row == size)
        {
             i = (row - 1)*size;
        }else{
            i = 0;
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

    // get 1 based index of 2d table row
    private int getRowIndex(int index)
    {
        int d = (index + 1 )/ size ;
        int r = (index + 1) % size;

        int row = -1;

        if(d > 0 && r > 0)
        {
            row = d-1;
        }else if(d > 0 && r == 0)
        {
            row = d;
        }else{
            row = 0;
        }

        return row;
    }

    // return 1 based index of two-d column
    private int getColIndex(int index)
    {
        int r = (index + 1) % size;

        int col = (r % size == 0)? size:r;

        return col;
    }
    // unit testing (not graded)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // print solution to standard output
            StdOut.println("Board\n");
            StdOut.println(initial);

         StdOut.println("Dimension\n");
        StdOut.println(initial.dimension());

        StdOut.println("Hamming\n");
        StdOut.println(initial.hamming());

        StdOut.println("Manhattan\n");
        StdOut.println(initial.manhattan());

        StdOut.println("neigbours\n");
        for(Board nb:initial.neighbors())
        {
            StdOut.println(nb);
        }

        StdOut.println("Twin\n");
        StdOut.println(initial.twin());

        StdOut.println("Is Goal\n");
        StdOut.println(initial.isGoal());

    }

}