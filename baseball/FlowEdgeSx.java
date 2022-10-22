/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: October 14, 2022
 *  Description: Edge representing a flow
 **************************************************************************** */

class FlowEdgeSx {

    private final int fromVrtx;
    private final int toVrtx;
    private final double capacity;
    private double flow;


    public FlowEdgeSx(int from, int to, double capacity) {
        this.fromVrtx = from;
        this.toVrtx = to;
        this.capacity = capacity;
    }

    public int from() {
        return fromVrtx;
    }

    public int to() {
        return toVrtx;
    }

    public double capacity() {
        return capacity;
    }

    public int other(int vertex) {
        if (vertex == fromVrtx)
            return toVrtx;
        else
            return fromVrtx;
    }
    
    public double residualCapcityTo(int vertex) {
        if (vertex == fromVrtx) // edge to vertex
            return flow;
        else return capacity - flow;

    }

    public void addResidualFlowTo(int vertex) {
        if (vertex == fromVrtx) {
            flow -= flow;
        }
        else {
            flow += flow;
        }
    }

    public static void main(String[] args) {

    }
}
