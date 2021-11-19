package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opcounts = new AList<>();
        // construct the n list
        for(int i = 1000; i <= 128000; i *=2){
            ns.addLast(i);
        }
        //calculate the processing time
        for(int i = 0; i < ns.size(); i ++){
            AList<Integer> test = new AList<>();
            int count_addlast=0;
            double time_cosume = 0;
            for(int j = 1; j <= ns.get(i); j ++){
                Stopwatch sw = new Stopwatch();
                test.addLast(1);
                time_cosume = time_cosume + sw.elapsedTime();
                count_addlast ++;
            }
            times.addLast(time_cosume);
            opcounts.addLast(count_addlast);
        }
        printTimingTable(ns, times, opcounts);
    }
}
