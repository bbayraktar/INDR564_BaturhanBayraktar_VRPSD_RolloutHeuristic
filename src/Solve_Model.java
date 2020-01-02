import java.util.ArrayList;

public class Solve_Model extends Entity {
    static Instance ins;
    static State next;

    static class Act {
        int action;
        double expected;

        public Act() {
            action = -1;
            expected = 999999999;
        }
    }

    static class State {
        int l;
        int q_l;
        int[] j_l;

        public State() {
            l = 0;
            q_l = ins.Q;
            j_l = new int[ins.node_num];
            j_l[0] = 0;
            for (int i = 1; i < j_l.length; i++) {
                j_l[i] = -1;
            }
        }

        public State StateCloner() {
            State clone = new State();
            clone.l = l;
            clone.q_l = q_l;
            for (int i = 0; i < clone.j_l.length; i++) {
                clone.j_l[i] = j_l[i];
            }
            return clone;
        }
    }

    static class Solution {
        ArrayList<Node> bSeq;
        ArrayList<Node> route;
        ArrayList<State> states;
        ArrayList<Integer> actions;

        public Solution() {
            bSeq = ins.BS;
            route = new ArrayList<>();
            states = new ArrayList<>();
            actions = new ArrayList<>();
        }

        public void visualize() {
            System.out.println("Route: ");
            System.out.print("0 - ");
            for (int i = 0; i < route.size(); i++) {
                System.out.print(route.get(i).nid + " - ");
            }
            System.out.println();
            System.out.println("Actions: ");
            for (int i = 0; i < actions.size(); i++) {
                System.out.print(actions.get(i) + " - ");
            }
        }
    }


    public static void Solve(Instance inst) {
        ins = inst;
        Solution sol = new Solution();
        next = new State();
        while (contains(next.j_l)) {
            Act[] act = new Act[ins.node_num];
            for (int i = 0; i < act.length; i++) {
                act[i] = new Act();
            }
            for (int i = 0; i < ins.node_num; i++) {
                if (next.j_l[i] != 0) {
                    ArrayList<Node> arrayList = Cyclic(next, sol.bSeq, i);
                    arrayList.add(0, ins.nodes[next.l]);
                    act[i] = ExpL(arrayList, 0, next.q_l);
                }
            }
            double min = 99999;
            int inm = -1;
            for (int i = 0; i < act.length; i++) {
                if (act[i].expected < min) {
                    min = act[i].expected;
                    inm = i;
                }
            }

            State current = next.StateCloner();
            sol.states.add(current);
            sol.route.add(ins.nodes[inm]);
            if (act[inm].action == 0) {
                sol.actions.add(0);
                if (ins.nodes[inm].demand < next.q_l) {
                    next.l = inm;
                    if (next.j_l[inm] == -1)
                        next.q_l -= ins.nodes[inm].demand;
                    else {
                        next.q_l -= next.j_l[inm];
                    }
                    next.j_l[inm] = 0;
                } else {
                    next.l = 0;
                    next.j_l[inm] = ins.nodes[inm].demand - next.q_l;
                    next.q_l = ins.Q;
                }
            } else {
                sol.actions.add(1);
                next.l = inm;
                if (next.j_l[inm] == -1)
                    next.q_l = ins.Q - ins.nodes[inm].demand;
                else {
                    next.q_l = ins.Q - next.j_l[inm];
                }
                next.j_l[inm] = 0;
            }
        }
        sol.states.add(next);
        sol.visualize();
        System.out.println();
    }

    public static ArrayList<Node> Cyclic(State s, ArrayList<Node> bSeq, int l) {
        ArrayList<Node> cy_route = new ArrayList<>();
        int p = -1;
        for (int i = 0; i < ins.node_num; i++) {
            if (bSeq.get(i).nid == l) {
                p = i;
                break;
            }
        }
        if (p == -1)
            System.out.println();
        for (int i = p; i < ins.node_num; i++) {
            if (s.j_l[bSeq.get(i).nid] != 0)
                cy_route.add(bSeq.get(i));
        }
        for (int i = 0; i < p; i++) {
            if (s.j_l[bSeq.get(i).nid] != 0)
                cy_route.add(bSeq.get(i));
        }
        return cy_route;
    }

    public static Act ExpL(ArrayList<Node> route, int l, int q_l) {       // l = vehicle's location l=0,...,n, and q_l = vehicle's capacity after serving l
        Act act = new Act();                                            //e_l = expected length of tau_l
        if (l == route.size() - 1) {
            act.expected = ins.dist[route.get(l).nid][0];
            act.action = 0;
        } else if (route.get(l).nid == 0) {
            act.expected = ExpL0(route, l, ins.Q);
            act.action = 0;
        } else {
            double e_l0 = ExpL0(route, l, q_l);
            double e_l1 = ExpL1(route, l, q_l);
            if (e_l0 < e_l1) {
                act.expected = e_l0;
                act.action = 0;
            } else {
                act.expected = e_l1;
                act.action = 1;
            }
        }
        return act;
    }

    public static double ExpL0(ArrayList<Node> route, int l, int q_l) {
        double e_l0 = 0;
        e_l0 += ins.dist[route.get(l).nid][route.get(l + 1).nid];
        if (next.j_l[route.get(l + 1).nid] != -1) {
            e_l0 += ExpL(route, l + 1, (q_l - next.j_l[route.get(l + 1).nid])).expected;
        } else {
            for (int j = 0; j <= q_l; j++) {
                if (ins.nodes[route.get(l + 1).nid].prob[j] > 0) {
                    e_l0 += ExpL(route, l + 1, (q_l - j)).expected * ins.nodes[route.get(l + 1).nid].prob[j];
                }
            }
            int a=Math.max(0,q_l+1);
            for (int j = a; j <= ((int) upp_node.get(route.get(l + 1).type)); j++) {
                if (ins.nodes[route.get(l + 1).nid].prob[j] > 0) {
                    e_l0 += (2 * ins.dist[0][route.get(l + 1).nid] + ExpL(route, l + 1, (ins.Q + a - j)).expected) * ins.nodes[route.get(l + 1).nid].prob[j];
                }
            }

        }

        return e_l0;
    }

    public static double ExpL1(ArrayList<Node> route, int l, int q_l) {
        double e_l1 = 0;

        e_l1 += ins.dist[route.get(l).nid][0];
        e_l1 += ins.dist[0][route.get(l + 1).nid];
        if (next.j_l[route.get(l + 1).nid] != -1) {
            e_l1 += ExpL(route, l + 1, (ins.Q - next.j_l[route.get(l + 1).nid])).expected;
        } else {
            for (int j = 0; j <= (int) upp_node.get(route.get(l + 1).type); j++) {
                e_l1 += ExpL(route, l + 1, (ins.Q - j)).expected * ins.nodes[route.get(l + 1).nid].prob[j];
            }
        }

        return e_l1;
    }

    public static boolean contains(int[] arr) {
        for (int i : arr) {
            if (i == -1) {
                return true;
            }
        }
        return false;
    }
}
