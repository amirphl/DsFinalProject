public class Edge {
    private int src;
    private int dest;
    private double rank;

    public Edge(int src, int dest, double rank) {
        this.src = src;
        this.dest = dest;
        this.rank = rank;
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }
}
