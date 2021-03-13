/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    //private Board board;
    private MinPQ<SearchNode> minPq = new MinPQ<SearchNode>();
    private int moves;
    private boolean isTwinSolvable = false;
    private SearchNode finalNode;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
       // this.board = initial;
        SearchNode searchNode = new SearchNode(initial,0,null);
        minPq.insert(searchNode);
        runSolver(initial);

    }

    private void runSolver(Board initial)
    {
        while (!solveBoard(initial)||!solveTwin(initial));
    }
    private boolean solveBoard(Board board)
    {
        return solve(board);
    }

    private  boolean solveTwin(Board board)
    {
        isTwinSolvable = solve(board.twin());
        return true;
    }
    private boolean solve(Board board)
    {
        SearchNode dqNode = minPq.delMin();
        while(!dqNode.board.isGoal())
        {
            Iterable<Board> neighbours = dqNode.board.neighbors();
            moves++;
            for(Board b: neighbours)
            {
                if(!b.equals(dqNode.board)) {
                    minPq.insert(new SearchNode(b, moves, dqNode));
                }
            }
            dqNode = minPq.delMin();
        }
        finalNode = dqNode;
        return true;
    }
    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return !isTwinSolvable;
    }

    // min number of moves to solve initial board
    public int moves()
    {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution()
    {
        Stack<Board> solution = new Stack<Board>();
        SearchNode current = finalNode;
        while(current != null)
        {
            solution.push(current.board);
            current = current.previous;
        }

        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        public SearchNode(Board board, int moves, SearchNode previous)
        {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
        SearchNode previous;
        Board board;
        // number moves made to reach board
        int moves;
        public int manhattanPriority() {
            int manhattanFunc = board.manhattan() + moves;
            return manhattanFunc;
        }
        public int compareTo(SearchNode o) {
            if(this.manhattanPriority() > o.manhattanPriority())
                return 1;
            else if(this.manhattanPriority() < o.manhattanPriority())
                return -1;
            else return 0;
        }
    }
    // test client (see below)
    // public static void main(String[] args)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }


}

