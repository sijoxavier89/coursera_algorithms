/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 1-May-2020
 *  Description: This class create Board for 8Puzzle Assignment
 **************************************************************************** */

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
        
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()

    // unit testing (not graded)
    public static void main(String[] args)

}