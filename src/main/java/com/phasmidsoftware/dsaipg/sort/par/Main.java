package com.phasmidsoftware.dsaipg.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {

        int[] arraySizes = {200000, 400000, 800000, 1600000}; // Different array sizes to test
        int[] threadCounts = {2, 4, 8, 16, 32, 64}; // Array of different thread counts to test

        processArgs(args);

        for (int size : arraySizes) {
            System.out.println("\nArray Size: " + size);
            for (int numThreads : threadCounts) {
                ForkJoinPool pool = new ForkJoinPool(numThreads);
                System.out.println("\nDegree of parallelism: " + pool.getParallelism());

                Random random = new Random();
                int[] array = new int[size];
                ArrayList<Long> timeList = new ArrayList<>();


                for (int j = 0; j < 10; j++) {
                    ParSort.cutoff = 10000 * (j + 1); // Adjust the cutoff value based on iterations
                    long time;

                    // Track the time for sorting
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 10; t++) {
                        // Fill the array with random values
                        for (int i = 0; i < array.length; i++) {
                            array[i] = random.nextInt(10000000);
                        }
                        ParSort.sort(array, 0, array.length); // Sorting the array
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    timeList.add(time);

                    // Print time for each cutoff
                    System.out.println("cutoff: " + (ParSort.cutoff) + "\t\t10 times Time: " + time + "ms");
                }

                // Save the results into a CSV file
                try {
                    FileOutputStream fis = new FileOutputStream("src/main/java/com/phasmidsoftware/dsaipg/sort/par/parallelSortResults_" + size + "_" + numThreads + "threads.csv");
                    OutputStreamWriter isr = new OutputStreamWriter(fis);
                    BufferedWriter bw = new BufferedWriter(isr);
                    int j = 0;
                    for (long i : timeList) {
                        String content = (double) 10000 * (j + 1) + "," + (double) i / 10 + "\n";
                        j++;
                        bw.write(content);
                        bw.flush();
                    }
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}