public class Data extends Rollout_Main {

    public static void DataRead(){
        DataCust();
        Bounds();
        FillRate();
    }

    public static void DataCust(){
        for (int i=0;i<12;i++){
            if (i/3==0){
                node_num.put(i,6);
            }
            else if (i/3==1){
                node_num.put(i,7);
            }
            else if (i/3==2){
                node_num.put(i,8);
            }
            else {
                node_num.put(i,9);
            }
        }
    }

    public static void Bounds(){
        low_node.put(0,1);
        upp_node.put(0,3);
        low_node.put(1,2);
        upp_node.put(1,4);
        low_node.put(2,3);
        upp_node.put(2,5);
    }

    public static void FillRate(){
        for (int i=0;i<12;i++){
            if (i%3==0){
                fill_rate.put(i,0.75);
            }
            else if (i%3==1){
                fill_rate.put(i,1.25);
            }
            else {
                fill_rate.put(i,1.75);
            }
        }
    }

}
