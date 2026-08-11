package structures;

public class DisjointSet {
    private final int[] parent;
    private final int[] rank;

    public DisjointSet(int size) {
        this.parent = new int[size];
        this.rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    public int find(int value) {
        if (parent[value] != value) {
            parent[value] = find(parent[value]);
        }
        return parent[value];
    }

    public void union(int first, int second) {
        int rootFirst = find(first);
        int rootSecond = find(second);
        if (rootFirst == rootSecond) {
            return;
        }
        if (rank[rootFirst] < rank[rootSecond]) {
            parent[rootFirst] = rootSecond;
        } else if (rank[rootFirst] > rank[rootSecond]) {
            parent[rootSecond] = rootFirst;
        } else {
            parent[rootSecond] = rootFirst;
            rank[rootFirst]++;
        }
    }
}
