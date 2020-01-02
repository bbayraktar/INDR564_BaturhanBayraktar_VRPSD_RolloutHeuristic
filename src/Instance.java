import java.util.ArrayList;

public class Instance extends Entity {

    Node[] nodes;
    double[][] dist;
    int node_num;
    ArrayList<Node> BS;
    int Q;

    public Instance(int node_num, double fill) {
        this.node_num = node_num;
        nodes = new Node[this.node_num];
        nodes[0] = new Node("source");
        Q = (int) Math.round(Dbar * (node_num) / (fill + 1));
        for (int i = 1; i < this.node_num; i++) {
            nodes[i] = new Node();
            nodes[i].prob = new double[Q+1];
            double pr = 1.0 / (double)((int) upp_node.get(nodes[i].type) - (int) low_node.get(nodes[i].type) + 1);
            for (int j = 0; j < nodes[i].prob.length; j++) {
                if (j >= (int) low_node.get(nodes[i].type) && j <= (int) upp_node.get(nodes[i].type))
                    nodes[i].prob[j] = pr;
                else
                    nodes[i].prob[j] = 0;
            }

        }
        dist = Distance_Matrix();
        BS = Base_Sequence();
    }

    public double[][] Distance_Matrix() {
        double[][] dist = new double[node_num][node_num];
        for (int i = 0; i < node_num; i++) {
            for (int j = 0; j < node_num; j++) {
                Entity.Node c1 = nodes[i];
                Entity.Node c2 = nodes[j];
                dist[i][j] = Math.sqrt(Math.pow(c1.lat - c2.lat, 2) + Math.pow(c1.lon - c2.lon, 2));
            }
        }
        return dist;
    }

    public ArrayList<Node> Base_Sequence() {
        ArrayList<Node> order = new ArrayList<>();
        ArrayList<Integer> cover = new ArrayList<>();
        order.add(nodes[0]);
        cover.add(0);
        int last = 0;
        for (int i = 0; i < nodes.length - 1; i++) {
            double min = 9;
            int mn = -1;
            for (int j = 0; j < nodes.length; j++) {
                if (dist[last][j] < min && last != j && !cover.contains(j)) {
                    min = dist[last][j];
                    mn = j;
                }
            }
            order.add(nodes[mn]);
            cover.add(mn);
            last = mn;
        }
        return order;
    }

}
