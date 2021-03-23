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
    private final MinPQ<SearchNode> minPq = new MinPQ<SearchNode>();
    private final MinPQ<SearchNode> minPqTwin = new MinPQ<SearchNode>();
    private SearchNode finalNode;
    private SearchNode finalNodeTwin;
    private final boolean isSolveble;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null)
        {
            throw new IllegalArgumentException();
        }

        SearchNode searchNode = new SearchNode(initial,0,null);
        minPq.insert(searchNode);

        // Twin
        SearchNode searchNodeTwin = new SearchNode(initial.twin(),0,null);
        minPqTwin.insert(searchNodeTwin);
        // Run
        runSolver();
        isSolveble = !finalNodeTwin.board.isGoal();
    }

    private void runSolver()
    {
        solve();
    }

    private boolean solve()
    {
        SearchNode dqNode = minPq.delMin();
        SearchNode dqNodeTwin = minPqTwin.delMin();
        while(!dqNode.board.isGoal() && !dqNodeTwin.board.isGoal())
        {

            Iterable<Board> neighbours = dqNode.board.neighbors();

            for(Board b: neighbours)
            {
                if(dqNode.previous != null ) {
                    if (!b.equals(dqNode.previous.board)) {
                        minPq.insert(new SearchNode(b, dqNode.movesNum + 1, dqNode));

                    }
                }else{
                    minPq.insert(new SearchNode(b, dqNode.movesNum + 1, dqNode));
                }
            }

            //Twin
            Iterable<Board> neighboursTwin = dqNodeTwin.board.neighbors();
            for(Board b: neighboursTwin)
            {
                if(dqNodeTwin.previous != null) {
                    if (!b.equals(dqNodeTwin.previous.board)) {
                        minPqTwin.insert(new SearchNode(b, dqNodeTwin.movesNum + 1, dqNodeTwin));
                    }
                }else{
                    minPqTwin.insert(new SearchNode(b, dqNodeTwin.movesNum + 1, dqNodeTwin));
                }
            }


            dqNode = minPq.delMin();
            dqNodeTwin = minPqTwin.delMin();
        }
        finalNode = dqNode;
        finalNodeTwin = dqNodeTwin;
        return true;
    }
    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return isSolveble;
    }

    // min number of moves to solve initial board
    public int moves()
    {
        if (isSolvable())
        return finalNode.movesNum;
        else
            return -1;
    }


    public Iterable<Board> solution()
    {
        if (isSolvable()) {
            Stack<Board> solution = new Stack<Board>();
            SearchNode current = finalNode;
            while (current != null) {
                solution.push(current.board);
                current = current.previous;
            }

            return solution;
        }else {
            return null;
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        public SearchNode(Board board, int moves, SearchNode previous)
        {
            this.board = board;
            this.movesNum = moves;
            this.previous = previous;
            this.manhattanDistance = manhattanDistance();
            this.priority = manhattanPriorityFunc();
        }
        SearchNode previous;
        Board board;
        private final int priority;
        private final int manhattanDistance;
        // number moves made to reach board
         int movesNum;
        private int manhattanPriorityFunc() {
            int manhattanFunc = board.manhattan() + movesNum;
            return manhattanFunc;
        }

        private int manhattanDistance()
        {
            return board.manhattan();
        }
        private int manhattanPriority()
        {
            return this.priority;
        }
        public int compareTo(SearchNode o) {
            if(this.manhattanPriority() > o.manhattanPriority())
                return 1;
            else if(this.manhattanPriority() < o.manhattanPriority())
                return -1;
            else {
                if(this.manhattanDistance > o.manhattanDistance)
                {
                    return 1;
                }else if(this.manhattanDistance < o.manhattanDistance)
                {
                    return -1;
                }
                return 0;
            }
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

