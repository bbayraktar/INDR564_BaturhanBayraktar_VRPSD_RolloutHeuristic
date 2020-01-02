import java.util.ArrayList;

public class Entity extends Rollout_Main {
    static class Node {
        int nid;
        int demand;
        int type;
        double[] prob;
        double lat;
        double lon;

        public Node() {
            lat = rnd.nextDouble();
            lon = rnd.nextDouble();
            nid = nid_g++;
            type = rnd.nextInt(low_node.size());
            demand = DemandD(type);
        }

        public Node(String S) {
            lat = 0;
            lon = 0;
            nid = 0;
            demand = 0;
            type = -1;
        }

        static int DemandD(int type) {
            int demand = 0;
            demand=rnd.nextInt((int)upp_node.get(type)-(int)low_node.get(type))+(int)low_node.get(type);
            return demand;
        }

        static Node NodeCloner(Node n) {
            Node nc = new Node();
            nc.demand = n.demand;
            nc.lat = n.lat;
            nc.lon = n.lon;
            nc.nid = n.nid;
            return nc;
        }
    }
}
