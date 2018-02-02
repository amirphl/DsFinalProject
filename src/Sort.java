
public class Sort {


    static void mergeSort(Edge[] edges, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(edges, l, m);
            mergeSort(edges, m + 1, r);
            merge(edges, l, m, r);
        }
    }

    static void merge(Edge arr[], int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        Edge L[] = new Edge[n1];
        Edge R[] = new Edge[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];


        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i].getRank() >= R[j].getRank()) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }


    static void bubbleSort(Edge[] edges, int law, int high) {
        for (int i = law; i <= high - 1; i++)
            for (int j = law; j <= high - i - 1; j++)
                if (edges[j].getRank() < edges[j + 1].getRank()) {
                    // swap temp and arr[i]
                    Edge temp = edges[j];
                    edges[j] = edges[j + 1];
                    edges[j + 1] = temp;
                }
    }

    static void optimumSort(Edge[] edges, int low, int high, String optimum_sort_type, int N) {
        if (low < high) {
            if (high - low <= N)
                switch (optimum_sort_type) {
                    case "Insertion":
                        insertionSort(edges, low, high);
                        return;
                    case "Bubble":
                        bubbleSort(edges, low, high);
                        return;
                }


            int i = low, j = high;
            // Get the pivot element from the middle of the list
            Edge pivot = edges[low + (high - low) / 2];

            // Divide into two lists
            while (i <= j) {
                // If the current value from the left list is larger than the pivot
                // element then get the next element from the left list
                while (edges[i].getRank() > pivot.getRank()) {
                    i++;
                }
                // If the current value from the right list is smaller than the pivot
                // element then get the next element from the right list
                while (edges[j].getRank() < pivot.getRank()) {
                    j--;
                }

                // If we have found a value in the left list which is smaller than
                // the pivot element and if we have found a value in the right list
                // which is larger than the pivot element then we exchange the
                // values.
                // As we are done we can increase i and j
                if (i <= j) {
                    Edge t = edges[i];
                    edges[i] = edges[j];
                    edges[j] = t;
                    i++;
                    j--;
                }
            }

            if (j - low < high - i) {
                if (low < j)
                    optimumSort(edges, low, j, optimum_sort_type, N);
                if (i < high)
                    optimumSort(edges, i, high, optimum_sort_type, N);
            } else {
                if (i < high)
                    optimumSort(edges, i, high, optimum_sort_type, N);
                if (low < j)
                    optimumSort(edges, low, j, optimum_sort_type, N);
            }
        }
    }

    static void insertionSort(Edge[] edges, int low, int high) {
        for (int i = low + 1; i <= high; ++i) {
            Edge key = edges[i];
            int j = i - 1;
            while (j >= 0 && edges[j].getRank() < key.getRank()) {
                edges[j + 1] = edges[j];
                j = j - 1;
            }
            edges[j + 1] = key;
        }
    }

//    static void quickSort(Edge[] arr, int low, int high) {
//
//        if (low < high) {
//            int i = low, j = high;
//            // Get the pivot element from the middle of the list
//            Edge pivot = arr[low + (high - low) / 2];
//
//            // Divide into two lists
//            while (i <= j) {
//                // If the current value from the left list is larger than the pivot
//                // element then get the next element from the left list
//                while (arr[i].getRank() > pivot.getRank()) {
//                    i++;
//                }
//                // If the current value from the right list is smaller than the pivot
//                // element then get the next element from the right list
//                while (arr[j].getRank() < pivot.getRank()) {
//                    j--;
//                }
//
//                // If we have found a value in the left list which is smaller than
//                // the pivot element and if we have found a value in the right list
//                // which is larger than the pivot element then we exchange the
//                // values.
//                // As we are done we can increase i and j
//                if (i <= j) {
//                    Edge t = arr[i];
//                    arr[i] = arr[j];
//                    arr[j] = t;
//                    i++;
//                    j--;
//                }
//            }
//
//            final int i1 = i;
//            final int j1 = j;
//            if (j - low < high - i) {
//                if (low < j)
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            quickSort1(arr, low, j1);
//                        }
//                    }).start();
//                if (i < high)
//                    quickSort1(arr, i, high);
//            } else {
//                if (i < high)
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            quickSort1(arr, i1, high);
//                        }
//                    }).start();
//                if (low < j)
//                    quickSort1(arr, low, j);
//            }
//        }
//
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    static void quickSort(Edge[] arr, int low, int high) {

        if (low < high) {
            int i = low, j = high;
            // Get the pivot element from the middle of the list
            Edge pivot = arr[low + (high - low) / 2];

            // Divide into two lists
            while (i <= j) {
                // If the current value from the left list is larger than the pivot
                // element then get the next element from the left list
                while (arr[i].getRank() > pivot.getRank()) {
                    i++;
                }
                // If the current value from the right list is smaller than the pivot
                // element then get the next element from the right list
                while (arr[j].getRank() < pivot.getRank()) {
                    j--;
                }

                // If we have found a value in the left list which is smaller than
                // the pivot element and if we have found a value in the right list
                // which is larger than the pivot element then we exchange the
                // values.
                // As we are done we can increase i and j
                if (i <= j) {
                    Edge t = arr[i];
                    arr[i] = arr[j];
                    arr[j] = t;
                    i++;
                    j--;
                }
            }

            if (j - low < high - i) {
                if (low < j)
                    quickSort(arr, low, j);
                if (i < high)
                    quickSort(arr, i, high);
            } else {
                if (i < high)
                    quickSort(arr, i, high);
                if (low < j)
                    quickSort(arr, low, j);
            }
        }
    }


    public static void main(String args[]) {
        Edge edges[] = new Edge[30];
        edges[0] = new Edge(0, 0, 5);
        edges[1] = new Edge(0, 0, 100);
        edges[2] = new Edge(0, 0, 53);
        edges[3] = new Edge(0, 0, 56);
        edges[4] = new Edge(0, 0, 89);
        edges[5] = new Edge(0, 0, 76);
        edges[6] = new Edge(0, 0, 4);
        edges[7] = new Edge(0, 0, 220);
        edges[8] = new Edge(0, 0, -1);
        edges[9] = new Edge(0, 0, -10);
        edges[10] = new Edge(0, 0, 6);
        edges[11] = new Edge(0, 0, 4);
        edges[12] = new Edge(0, 0, 3);
        edges[13] = new Edge(0, 0, -8);
        edges[14] = new Edge(0, 0, -5);
        edges[15] = new Edge(0, 0, 68);
        edges[16] = new Edge(0, 0, 75);
        edges[17] = new Edge(0, 0, -9);
        edges[18] = new Edge(0, 0, 562);
        edges[19] = new Edge(0, 0, 251);
        edges[20] = new Edge(0, 0, 87);
        edges[21] = new Edge(0, 0, -89);
        edges[22] = new Edge(0, 0, 25);
        edges[23] = new Edge(0, 0, 26);
        edges[24] = new Edge(0, 0, 27);
        edges[25] = new Edge(0, 0, 5);
        edges[26] = new Edge(0, 0, 89);
        edges[27] = new Edge(0, 0, -8);
        edges[28] = new Edge(0, 0, -110);
        edges[29] = new Edge(0, 0, -80);
        mergeSort(edges, 0, 29);
        for (Edge edge : edges)
            System.out.println((edge.getRank()));
    }
}

