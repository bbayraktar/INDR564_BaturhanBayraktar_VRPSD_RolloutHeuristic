import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

public class Rollout_Main {

    static long beg;
    static long end;
    static Random rnd;
    static int nid_g;
    static int ins_num;
    static double Dbar = 3;
    static ArrayList<Instance> instances;

    static Dictionary node_num = new Hashtable();
    static Dictionary fill_rate = new Hashtable();

    static Dictionary low_node = new Hashtable();
    static Dictionary upp_node = new Hashtable();

    public static void main(String[] args) {
        rnd = new Random(0);
        Data.DataRead();
        for (int s=0;s<12;s++){
            int a = (int) node_num.get(s);
            double b = (double) fill_rate.get(s);
            Instance_Generation(a, b);
            for (int i=0;i<instances.size();i++){
                System.out.println();
                System.out.println("Set: " + s + " - Instance: " + i);
                beg = System.currentTimeMillis();
                Solve_Model.Solve(instances.get(i));
                end = System.currentTimeMillis();
                System.out.println(end-beg);
            }
        }
    }



    public static void Instance_Generation(int node_num, double fill) {
        ins_num = 10;
        instances = new ArrayList<>();

        for (int i = 0; i < ins_num; i++) {
            nid_g = 1;
            Instance instance = new Instance(node_num, fill);
            instances.add(instance);
        }
    }


}
