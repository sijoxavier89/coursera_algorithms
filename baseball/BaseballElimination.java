/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: October 14, 2022
 *  Description: Find the teams which are mathematically eliminated
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class BaseballElimination {
    private final int count;
    private int[] wins;
    private int[] loss;
    private int[] remain;
    private int[][] games;
    private HashMap<String, Integer> teams;
    private HashMap<Integer, String> teamsKey;
    private FordFulkerson fordFulkerson;
    private ArrayList<BaseTeam> teamsMap;

    public BaseballElimination(
            String filename)                    // create a baseball division from given filename in format specified below
    {
        In stream = new In(filename);
        count = stream.readInt(); // total number of teams
        wins = new int[count];
        loss = new int[count];
        remain = new int[count];
        games = new int[count][count];
        teams = new HashMap<>(count);
        teamsMap = new ArrayList<BaseTeam>();
        teamsKey = new HashMap<>(count);
        for (int i = 0; i < count; i++) {

            String str = stream.readString();
            teams.put(str, i);
            teamsMap.add(new BaseTeam(i, str));
            teamsKey.put(i, str);
            wins[i] = stream.readInt();
            loss[i] = stream.readInt();
            remain[i] = stream.readInt();

            for (int j = 0; j < count; j++) {

                games[i][j] = stream.readInt();


            }
        }
        // sort
        Collections.sort(teamsMap);
    }


    private void validate(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException();
    }

    private class BaseTeam implements Comparable<BaseTeam> {
        private int key;
        private String value;

        public BaseTeam(int team, String value) {
            this.key = team;
            this.value = value;
        }

        public int keyOf() {
            return this.key;
        }

        public String valueOf() {
            return this.value;
        }

        public int compareTo(BaseTeam that) {
            return wins[that.keyOf()] - wins[key];
        }
    }


    public int numberOfTeams()                        // number of teams
    {
        return count;
    }

    public Iterable<String> teams()                                // all teams
    {
        return teams.keySet();
    }

    public int wins(String team)                      // number of wins for given team
    {
        validate(team);
        return wins[teams.get(team)];
    }

    public int losses(String team)                    // number of losses for given team
    {
        validate(team);
        return loss[teams.get(team)];
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        validate(team);
        return remain[teams.get(team)];
    }

    public int against(String team1,
                       String team2)    // number of remaining games between team1 and team2
    {
        validate(team1);
        validate(team2);
        return games[teams.get(team1)][teams.get(team2)];
    }

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        validate(team);
        // check if it can be trivially eliminated
        int s = teams.get(team);
        HashSet<String> set = (HashSet<String>) trivialElimination(s);
        if (!set.isEmpty()) return true;

        FlowNetwork f = createFlowNetork(s);
        // test
        // StdOut.println(f.toString());
        fordFulkerson = new FordFulkerson(f, s, count * count + 1);

        // check other vertex of source edges are full
        for (FlowEdge e : f.adj(s)) {
            if (e.residualCapacityTo(e.other(s)) > 0)
                return true;
        }
        return false;
    }

    private Iterable<String> trivialElimination(int team) {
        HashSet<String> set = new HashSet<>();
        int possibleWin = wins[team] + remain[team];

        for (BaseTeam item : teamsMap) {
            if (wins[item.keyOf()] <= possibleWin) break;
            else {
                if (item.keyOf() != team)
                    set.add(item.valueOf());
            }

        }

        return set;
    }

    public Iterable<String> certificateOfElimination(
            String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        validate(team);
        int s = teams.get(team);
        HashSet<String> set = (HashSet<String>) trivialElimination(s);
        if (!set.isEmpty()) return set;

        FlowNetwork f = createFlowNetork(s);
        fordFulkerson = new FordFulkerson(f, s, count * count + 1);

        // check other vertex of source edges are full
        Queue<String> list = new Queue<>();
        // int vCount = f.V();
        for (int i : teamsKey.keySet()) {
            if (i != s && fordFulkerson.inCut(i)) {
                list.enqueue(teamsKey.get(i));
            }
        }
        return list.size() > 0 ? list : null;
    }

    private FlowNetwork createFlowNetork(int team) {
        int winPlusrem = wins[team] + remain[team];
        FlowNetwork fn = new FlowNetwork(count * count + 2);
        int t = count * count + 1;

        boolean[][] gamesAdded = new boolean[count][count];
        int gameNum = 1;
        // add edges from source vertex to games edges other than team s
        // then add games to winning team vertex with infinite capacity
        // then add sink edges with max winning capacity wi + r1 - w team
        HashSet<Integer> sinkEdge = new HashSet<Integer>();
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (i != team && j != team && !gamesAdded[i][j] && games[i][j] != 0) {
                    int gameVertex = count + gameNum; // i-j vertex
                    double capacity = games[i][j];

                    FlowEdge sourceEdge = new FlowEdge(team, gameVertex, capacity); // s -> i-j
                    fn.addEdge(sourceEdge);
                    gamesAdded[i][j] = true;
                    gamesAdded[j][i] = true;
                    gameNum++;
                    // add winning edges i-j -> i, i-j -> j
                    FlowEdge w1 = new FlowEdge(gameVertex, i, Double.POSITIVE_INFINITY);
                    fn.addEdge(w1);
                    FlowEdge w2 = new FlowEdge(gameVertex, j, Double.POSITIVE_INFINITY);
                    fn.addEdge(w2);

                    // add sink edges

                    if (!sinkEdge.contains(i)) {
                        FlowEdge se1 = new FlowEdge(i, t, (winPlusrem - wins[i] >= 0 ?
                                                           winPlusrem - wins[i] : 0)); // i -> t
                        fn.addEdge(se1);
                        sinkEdge.add(i);
                    }
                    if (!sinkEdge.contains(j)) {
                        FlowEdge se2 = new FlowEdge(j, t, (winPlusrem - wins[j] >= 0 ?
                                                           winPlusrem - wins[j] : 0)); // j -> t
                        fn.addEdge(se2);
                        sinkEdge.add(j);
                    }
                }
            }
        }
        return fn;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        int winOfR = 0;
        int possibleWinOfTeam;
        ArrayList<String> list = new ArrayList<>();
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                possibleWinOfTeam = division.wins(team) + division.remaining(team);
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                    // validate certificate
                    winOfR += division.wins(t);
                    list.add(t);
                }
                StdOut.println("}");
                int gOfR = 0;

                for (int i = 0; i < list.size() - 1; i++) {
                    for (int j = i + 1; j < list.size(); j++) {
                        gOfR += division.against(list.get(i), list.get(j));
                    }
                }
                double aOfR = (double) (winOfR + gOfR) / (double) (list.size());
                StdOut.println("a(R):" + aOfR);
                StdOut.println("possible wins of team:" + possibleWinOfTeam);
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }


    }
}
