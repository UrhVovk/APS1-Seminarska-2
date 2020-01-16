import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Naloga9 {

    static class Point{
        public int id;
        public double x;
        public double y;
        HashMap<Integer, Double> distances;

        Point(int id, double x, double y){
            this.id = id;
            this.x = x;
            this.y = y;
            distances = new HashMap<>();
        }
    }

    static class Cluster{
        public ArrayList<Point> points;

        Cluster(){
            points = new ArrayList<>();
        }

        public StringBuilder returnCluster(){
            StringBuilder sb = new StringBuilder();
            Collections.sort(points, new Comparator<Point>() {
                @Override
                public int compare(Point o1, Point o2) {
                    if(o1.id > o2.id) return 1;
                    else if(o1.id < o2.id) return -1;
                    else return 0;
                }
            });
            for(int i = 0; i < points.size(); i++){
                sb.append(points.get(i).id);
                if(i != points.size()-1){
                    sb.append(",");
                }
            }
            return sb;
        }
    }

    static void combineNearestClusters(ArrayList<Cluster> clusters){ //Works
        double min = (Math.pow(clusters.get(0).points.get(0).x - clusters.get(1).points.get(0).x, 2)
                + Math.pow(clusters.get(0).points.get(0).y - clusters.get(1).points.get(0).y, 2));
        int cluster1 = 0;
        int cluster2 = 1;
        for(int i = 0; i < clusters.size() - 1; i++){
            for(int p = 0; p < clusters.get(i).points.size(); p++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    for(int pp = 0; pp < clusters.get(j).points.size(); pp++) {
                        double distance = (Math.pow(clusters.get(i).points.get(p).x - clusters.get(j).points.get(pp).x, 2)
                                + Math.pow(clusters.get(i).points.get(p).y - clusters.get(j).points.get(pp).y, 2));
                        if (distance < min) {
                            min = distance;
                            cluster1 = i;
                            cluster2 = j;
                        }
                    }
                }
            }
        }
        /*clusters.get(cluster1).points.addAll(clusters.get(cluster2).points); Pocasneje
        clusters.remove(cluster2);*/
        for(int i = 0; i < clusters.get(cluster2).points.size(); i++){
            clusters.get(cluster1).points.add(clusters.get(cluster2).points.get(i));
            clusters.get(cluster2).points.remove(i);
            i--;
        }
        if(clusters.get(cluster2).points.size() == 0){
            clusters.remove(cluster2);
        }
    }

    public static void main(String[] args) throws Exception{
        long start = System.nanoTime();
        File input = new File(args[0]);
        PrintWriter output = new PrintWriter(args[1]);
        Scanner scan = new Scanner(input);
        String line = scan.nextLine();
        int n = Integer.parseInt(line);
        ArrayList<Cluster> clusters = new ArrayList<>();
        for(int i = 1; i <= n; i++){
            line = scan.nextLine();
            String[] tmp = line.split(",");
            double currX = Double.parseDouble(tmp[0]);
            double currY = Double.parseDouble(tmp[1]);
            Point p = new Point(i,currX,currY);
            clusters.add(new Cluster());
            clusters.get(i-1).points.add(p);
            for(int j = 0; j < clusters.size()-1; j++){
                double distance = (Math.pow(clusters.get(i-1).points.get(0).x - clusters.get(j).points.get(0).x,2)
                                    + Math.pow(clusters.get(i-1).points.get(0).y - clusters.get(j).points.get(0).y,2));
                clusters.get(i-1).points.get(0).distances.put(j+1,distance);
                clusters.get(j).points.get(0).distances.put(i,distance);
            }
        }
        line = scan.nextLine();
        int k = Integer.parseInt(line);
        for(int i = 0; i < n-k; i++){t
            combineNearestClusters(clusters);
        }
        for(int i = 0; i < clusters.size(); i++){
            output.println(clusters.get(i).returnCluster().toString());
        }
        output.close();
        System.out.print(((double)System.nanoTime()-(double)start)/1_000_000_000);
    }
}
