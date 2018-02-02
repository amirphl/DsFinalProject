
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {

    private final int V;
    private int E;
    private Edge[] edges;
    private int[][] matrix = null;
    private int[] startPoints = null;
    private int[] degree = null;
    private GNode[] list = null;
    private Sort sort = new Sort();
    private int lastSrc;
    private int lastDst;

    Graph(int v, int e, Edge[] edges, int[][] matrix, int[] startPoints, int degree[]) {
        V = v;
        E = e;
        this.edges = edges;
        this.matrix = matrix;
        this.startPoints = startPoints;
        this.degree = degree;
    }

    Graph(int v, int e, Edge[] edges, GNode[] list, int[] degree) {
        V = v;
        E = e;
        this.edges = edges;
        this.list = list;
        this.degree = degree;
    }

    void sortEdges(String sort_type, String optimum_sort_type, int N) {
        switch (sort_type) {
            case "Quick":
                sort.quickSort(edges, 0, E - 1);
                break;
            case "Merge":
                sort.mergeSort(edges, 0, E - 1);
                break;
            case "Bubble":
                sort.bubbleSort(edges, 0, E - 1);
                break;
            case "Insertion":
                sort.insertionSort(edges, 0, E - 1);
                break;
            case "Optimum":
                sort.optimumSort(edges, 0, E - 1, optimum_sort_type, N);
        }
    }

    void addScoreToEdges() {
        int min;
        for (int i = 0; i < E; i++) {
            min = Integer.min(degree(edges[i].getSrc()), degree(edges[i].getDest()));
            if (min == 1) {
                edges[i].setRank(Integer.MAX_VALUE);
                continue;
            }
            edges[i].setRank(((double) cycleWithLength3(edges[i].getSrc(), edges[i].getDest()) + 1) /
                    (min - 1));
        }
    }

    private int cycleWithLength3(int i, int j) {
        int result = 0;
        if (matrix != null) {
            int start = startPoints[j];
            while (start < matrix.length && matrix[start][0] == j) {
                if (matrix[start][1] == -1) {
                    start++;
                    continue;
                }
                int middle = startPoints[matrix[start][1]];
                while (middle < matrix.length && matrix[middle][0] == matrix[start][1]) {
                    if (matrix[middle][1] == i) {
                        result++;
                        break;
                    }
                    middle++;
                }
                start++;
            }
            return result;
        }


        GNode node = list[j];
        GNode temp;
        while ((node = node.next) != null) {
            temp = list[node.vertex];
            while ((temp = temp.next) != null)
                if (temp.vertex == i) {
                    result++;
                    break;
                }
        }
        return result;
    }

    int degree(int vertex) {
        return degree[vertex];
    }

    void deleteLeastCIJ() {
        if (E == 0)
            return;
        int src = edges[E - 1].getSrc();
        int dest = edges[E - 1].getDest();
        lastSrc = src;
        lastDst = dest;

        if (matrix != null) {
            int point = startPoints[src];
            while (point < matrix.length && matrix[point][0] == src) {
                if (matrix[point][1] == dest) {
                    matrix[point][1] = -1;
                    break;
                }
                point++;
            }

            point = startPoints[dest];
            while (point < matrix.length && matrix[point][0] == dest) {
                if (matrix[point][1] == src) {
                    matrix[point][1] = -1;
                    break;
                }
                point++;
            }

            degree[src] -= 1;
            degree[dest] -= 1;
            edges[E - 1] = null;
            E--;

            return;
        }

        GNode node = list[src];
        GNode pre = list[src];
        while ((node = node.next) != null) {
            if (node.vertex == dest) {
                pre.next = node.next;
                break;
            }
            pre = pre.next;
        }

        node = list[dest];
        pre = list[dest];
        while ((node = node.next) != null) {
            if (node.vertex == src) {
                pre.next = node.next;
                break;
            }
            pre = pre.next;
        }

        degree[src] -= 1;
        degree[dest] -= 1;
        edges[E - 1] = null;
        E--;
    }

    boolean isConnected() {
        if (matrix != null)
            return bfs_adjacenecy_matrix();
        return bfs_list();
    }

    private boolean bfs_adjacenecy_matrix() {
//        boolean visited[] = new boolean[V];
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(1);
//        while (!queue.isEmpty()) {
//            int v = queue.poll();
//            visited[v] = true;
//
//            int point = startPoints[v];
//            while (point < matrix.length && matrix[point][0] == v) {
//                if (matrix[point][1] != -1 && !visited[matrix[point][1]])
//                    queue.offer(matrix[point][1]);
//                point++;
//            }
//        }
//        return visited;
        boolean visited[] = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(lastSrc);
        if (matrix != null) {
            while (!queue.isEmpty()) {
                int v = queue.poll();
                visited[v] = true;
                int point = startPoints[v];
                while (point < matrix.length && matrix[point][0] == v) {
                    if (matrix[point][1] == lastDst)
                        return true;
                    if (matrix[point][1] != -1 && !visited[matrix[point][1]])
                        queue.offer(matrix[point][1]);
                    point++;
                }
            }
        }
        return false;
    }

    private boolean checkConnectivity(boolean array[]) {
        for (int i = 1; i < array.length; i++)
            if (!array[i])
                return false;
        return true;
    }

    private boolean bfs_list() {
//        boolean visited[] = new boolean[V];
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(1);
//        GNode temp;
//
//        while (!queue.isEmpty()) {
//            int v = queue.poll();
//            visited[v] = true;
//            temp = list[v];
//            while ((temp = temp.next) != null)
//                if (!visited[temp.vertex])
//                    queue.offer(temp.vertex);
//        }
//        return visited;

        boolean visited[] = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(lastSrc);
        GNode temp;
        while (!queue.isEmpty()) {
            int v = queue.poll();
            visited[v] = true;
            temp = list[v];
            while ((temp = temp.next) != null) {
                if (temp.vertex == lastDst)
                    return true;
                if (!visited[temp.vertex])
                    queue.offer(temp.vertex);
            }
        }

        return false;
    }

    void setEdgesCommunities(String sort_type, String optimum_sort_type, int N, String test_case) throws IOException {
        String path;
        boolean visited[] = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1);
        if (matrix != null) {
            while (!queue.isEmpty()) {
                int v = queue.poll();
                visited[v] = true;
                int point = startPoints[v];
                while (point < matrix.length && matrix[point][0] == v) {
                    if (matrix[point][1] != -1 && !visited[matrix[point][1]]) {
                        queue.offer(matrix[point][1]);
                    }
                    point++;
                }
            }
            path = test_case + "-Matrix-" + sort_type + "-" + optimum_sort_type + "-" + N + "-.txt";
        } else {
            GNode temp;
            while (!queue.isEmpty()) {
                int v = queue.poll();
                visited[v] = true;
                temp = list[v];
                while ((temp = temp.next) != null)
                    if (!visited[temp.vertex])
                        queue.offer(temp.vertex);
            }
            path = test_case + "-LinkedList-" + sort_type + "-" + optimum_sort_type + "-" + N + "-.txt";
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));

        for (int i = 1; i < visited.length; i++)
            if (!visited[i]) {
                bufferedWriter.write("#" + i + " : B");
                bufferedWriter.newLine();
            }

        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public int getE() {
        return E;
    }

    public int getV() {
        return V;
    }

    public GNode[] getList() {
        return list;
    }
}
