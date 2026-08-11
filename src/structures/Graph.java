package structures;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final double[][] matrix;
    private final List<String> labels;

    public Graph(int size) {
        this.matrix = new double[size][size];
        this.labels = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            labels.add("L" + i);
        }
    }

    public void setLabel(int index, String label) {
        labels.set(index, label);
    }

    public String getLabel(int index) {
        return labels.get(index);
    }

    public void addEdge(int from, int to, double weight) {
        matrix[from][to] = weight;
        matrix[to][from] = weight;
    }

    public double weight(int from, int to) {
        return matrix[from][to];
    }

    public int size() {
        return matrix.length;
    }
}
