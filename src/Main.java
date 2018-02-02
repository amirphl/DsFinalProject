import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import java.io.IOException;

public class Main {

    static Logger logger;
    static int logger_counter = 1;
    static long logger_ref_time = 0;
    static long total_time = System.currentTimeMillis();

    public static void main(String args[]) throws IOException {
        logger = Logger.getLogger(Main.class);
        SimpleLayout layout = new SimpleLayout();
        FileAppender appender = null;
        GraphGenerator graphGenerator = null;
        boolean flag = false;
        try {
            if (args.length != 4)
                throw new ArrayIndexOutOfBoundsException();
            graphGenerator = new GraphGenerator(args[1], args[3]);
            appender = new FileAppender(layout, args[3] + "-" + args[1] + "-" + args[2] + ".txt", false);
            flag = true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        if (!flag)
            try {
                graphGenerator = new GraphGenerator(args[1], args[5]);
                appender = new FileAppender(layout, args[5] + "-" + args[1] + "-" + args[2] + "-" + args[3] + "-" + args[4] + ".txt", false);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                return;
            }

        logger.addAppender(appender);

        try {
            long time = System.currentTimeMillis();
            Graph graph = graphGenerator.getGraph();
            logger.info("Time for creating graph in " + args[1] + " format : " + (System.currentTimeMillis() - time) + " mili seconds.");
            if (args.length == 4)
                run(graph, args[2], "", -1, args[3]);
            else if (args.length == 6)
                run(graph, args[2], args[3], Integer.valueOf(args[4]), args[5]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void run(Graph graph, String sort_type, String optimum_sort_type, int N, String test_case) {
        long start_time = System.currentTimeMillis();
        logger_ref_time = System.currentTimeMillis();
        try {
            switch (sort_type) {
                case "Quick":
                    do {
                        graph.addScoreToEdges();
                        graph.sortEdges("Quick", "", -1);
                        graph.deleteLeastCIJ();
                        info();
                    } while (graph.isConnected());
                    break;
                case "Insertion":
                    do {
                        graph.addScoreToEdges();
                        graph.sortEdges("Insertion", "", -1);
                        graph.deleteLeastCIJ();
                        info();
                    } while (graph.isConnected());
                    break;
                case "Bubble":
                    do {
                        graph.addScoreToEdges();
                        graph.sortEdges("Bubble", "", -1);
                        graph.deleteLeastCIJ();
                        info();
                    } while (graph.isConnected());
                    break;
                case "Merge":
                    do {
                        System.out.println("YE");
                        graph.addScoreToEdges();
                        graph.sortEdges("Merge", "", -1);
                        graph.deleteLeastCIJ();
                        info();
                    } while (graph.isConnected());
                    break;
                case "Optimum":
                    do {
                        graph.addScoreToEdges();
                        graph.sortEdges("Optimum", optimum_sort_type, N);
                        graph.deleteLeastCIJ();
                        info();
                    } while (graph.isConnected());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        logger.info(--logger_counter + " Edges are being deleted with this case.");
        logger.info("Total time of executing algorithm : " + (System.currentTimeMillis() - start_time)  + " mili seconds.");
        logger.info("Total time of program life cycle : " + (System.currentTimeMillis() - total_time)  + " mili seconds.");
        try {
            graph.setEdgesCommunities(sort_type, optimum_sort_type, N, test_case);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not create community file.");
        }
    }


    static void info() {
        if (logger_counter % 100 == 0) {
            System.out.println(logger_counter + " Edges in " + (System.currentTimeMillis() - logger_ref_time) + " mili seconds.");
            logger_ref_time = System.currentTimeMillis();
        }
        logger_counter++;
    }
}
