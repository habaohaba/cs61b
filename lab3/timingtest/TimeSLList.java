package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opcounts = new AList<>();
        // construct the n list
        for (int i = 1000; i <= 128000; i *= 2) {
            ns.addLast(i);
            opcounts.addLast(10000);
        }
        // calculate the processing time
        double time_consume;
        for (int i = 0; i < ns.size(); i++) {
            SLList<Integer> test = new SLList<>();
            // add items to test sllist
            for (int j = 1; j <= ns.get(i); j++) {
                test.addLast(1);
            }
            Stopwatch sw = new Stopwatch();
            for (int j = opcounts.get(i); j > 0; j--) {
                test.getLast();
            }
            time_consume = sw.elapsedTime();
            times.addLast(time_consume);
        }
        printTimingTable(ns, times, opcounts);
    }
}
