import javafx.util.Pair;

import java.io.*;

public class GraphGenerator {

    private String graph_type;
    private String path;

    GraphGenerator(String graph_type, String path) {
        this.graph_type = graph_type;
        this.path = path;
    }

    Graph getGraph() throws Exception {
        switch (graph_type) {
            case "Matrix":
                return createGraphInMatrixFormat();
            case "LinkedList":
                return createGraphInLinkedListFormat();
        }
        return null;
    }

    private Graph createGraphInMatrixFormat() throws IOException {
        Pair t = number_of_vertex_edges();
        int[][] matrix = new int[(int) t.getValue()][2];
        int[] startPoints = new int[(int) t.getKey() + 1];
        int[] degree = new int[(int) t.getKey() + 1];
        startPoints[0] = -1;
        degree[0] = -1;

        Edge edges[] = new Edge[(int) t.getValue() / 2];
        int E = 0;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
        String line;
        int counter = 0;
        int deg = 0;
        int edge_counter = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String split[] = line.split(",");
            int v1 = Integer.valueOf(split[0]);
            int v2 = Integer.valueOf(split[1]);

            matrix[E][0] = v1;
            matrix[E][1] = v2;

            try {
                if (matrix[E][0] != matrix[E - 1][0]) {
                    startPoints[v1 - 1] = counter;
                    degree[v1 - 1] = deg;
                    counter = E;
                    deg = 0;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }

            deg++;
            E++;

            boolean isAdded = false;
            if (startPoints[v1] != 0 || v1 == 1) {
                int v1_loc = startPoints[v1];
                while (matrix.length > v1_loc && matrix[v1_loc][0] == v1)
                    if (matrix[v1_loc++][1] == v2) {
                        isAdded = true;
                        break;
                    }
            }

            if (isAdded)
                continue;

            if (startPoints[v2] != 0) {
                int v2_loc = startPoints[v2];
                while (matrix.length > v2_loc && matrix[v2_loc][0] == v2)
                    if (matrix[v2_loc++][1] == v1) {
                        isAdded = true;
                        break;
                    }
            }

            if (isAdded)
                continue;
            edges[edge_counter++] = new Edge(v1, v2, 0);
        }

        bufferedReader.close();
        startPoints[startPoints.length - 1] = counter;
        degree[degree.length - 1] = deg;
        return new Graph((int) t.getKey() + 1, E / 2, edges, matrix, startPoints, degree);
    }

    private Graph createGraphInLinkedListFormat() throws IOException {
        Pair t = number_of_vertex_edges();
        GNode list[] = new GNode[((int) t.getKey()) + 1];
        int[] degree = new int[(int) t.getKey() + 1];
        degree[0] = -1;

        for (int i = 0; i < list.length; i++) {
            GNode gNode = new GNode(i);
            list[i] = gNode;
        }

        Edge edges[] = new Edge[(int) t.getValue() / 2];
        int E = 0;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
        String line;
        GNode temp;
        int deg = 0;
        int preNode = 1;
        while ((line = bufferedReader.readLine()) != null) {
            String split[] = line.split(",");
            int v1 = Integer.valueOf(split[0]);
            int v2 = Integer.valueOf(split[1]);

            boolean isAdded = false;
            GNode test = list[v1];
            while ((test = test.next) != null)
                if (test.vertex == v2)
                    isAdded = true;

            if (!isAdded) {
                edges[E++] = new Edge(v1, v2, 0);
                GNode node = new GNode(v2);
                temp = list[v1].next;
                list[v1].next = node;
                node.next = temp;

                node = new GNode(v1);
                temp = list[v2].next;
                list[v2].next = node;
                node.next = temp;
            }

            if (preNode != v1) {
                degree[preNode] = deg;
                deg = 0;
            }

            deg++;
            preNode = v1;
        }
        bufferedReader.close();
        degree[degree.length - 1] = deg;

        return new Graph((int) t.getKey() + 1, E, edges, list, degree);
    }

    private Pair number_of_vertex_edges() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
        String line;
        String lastLine = null;
        int vertex;
        int edge = 0;
        while ((line = bufferedReader.readLine()) != null) {
            edge++;
            lastLine = line;
        }


        vertex = Integer.valueOf(lastLine.split(",")[0]);
        bufferedReader.close();
        Pair<Integer, Integer> pair = new Pair<>(vertex, edge);
        return pair;
    }
}
