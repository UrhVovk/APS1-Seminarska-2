import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Naloga7 {

    static HashSet<ArrayList<Integer>> validPaths; //globalni hashset ki bo hranil razlicne poti
    static HashMap<Integer,GraphNode> nodes; //predstavlja vse mozne povezave - nas graf

    static class Path{
        public double cost;
        public Integer otherSide;

        Path(double cost, Integer otherSide){
            this.cost = cost;
            this.otherSide = otherSide;
        }
    }

    static class GraphNode{
        public ArrayList<Path> connections = new ArrayList<>();
    }

    static class Travel{
        public double currentCost;
        public ArrayList<Integer> visitedNodes;

        Travel(Integer startNode){
            currentCost = 0;
            visitedNodes = new ArrayList<>();
            visitedNodes.add(startNode);
        }

        Travel(double currentCost, ArrayList vn){
            this.currentCost = currentCost;
            visitedNodes = (ArrayList<Integer>)vn.clone();
        }
    }

    static int availableTrips(Integer start,Travel tmp, double maxCost){
        if(start == tmp.visitedNodes.get(tmp.visitedNodes.size()-1) && tmp.visitedNodes.size() > 1){
            Collections.sort(tmp.visitedNodes);
            if(!validPaths.contains(tmp.visitedNodes)){//Preveri ce ista pot ze obstaja
                validPaths.add(tmp.visitedNodes);
                return 1;
            }
            else return 0;
        }
        else{
            Integer lastNode = tmp.visitedNodes.get(tmp.visitedNodes.size()-1);
            ArrayList<Path> connections = nodes.get(lastNode).connections;
            int availableRoutes = 0;
            for(int i = 0; i < connections.size(); i++){
                if(tmp.currentCost + connections.get(i).cost <= maxCost && (connections.get(i).otherSide == start || tmp.visitedNodes.indexOf(connections.get(i).otherSide) == -1)) {
                    //Preverimo ce si sploh lahko privoscimo obisk dolocenega mesta in ce smo ga slucajno ze obiskali
                    Travel newTmp = new Travel(tmp.currentCost,tmp.visitedNodes);
                    newTmp.visitedNodes.add(connections.get(i).otherSide);//dodaj novo obiskano mesto
                    newTmp.currentCost += connections.get(i).cost;//povecaj ceno
                    availableRoutes += availableTrips(start,newTmp,maxCost);
                }
            }
            return availableRoutes;
        }
    }

    public static void main(String[] args) throws Exception{
        long start = System.nanoTime();
        File input = new File(args[0]);
        PrintWriter output = new PrintWriter(args[1]);
        Scanner scan = new Scanner(input);
        String line = scan.nextLine();
        int stPovezav = Integer.parseInt(line);
        nodes = new HashMap<>();
        for(int i = 0; i < stPovezav; i++){
            line = scan.nextLine();
            String[] buffer = line.split(",");
            Integer side1 = Integer.parseInt(buffer[0]);
            Integer side2 = Integer.parseInt(buffer[1]);
            double cost = Double.parseDouble(buffer[2]);
            if(nodes.containsKey(side1)){//Preglej ce je element ze v hashmapu
                GraphNode tmp = nodes.get(side1);
                tmp.connections.add(new Path(cost,side2));
            }
            else {
                GraphNode tmp = new GraphNode();
                tmp.connections.add(new Path(cost,side2));
                nodes.put(side1,tmp);
            }
            if(nodes.containsKey(side2)){
                GraphNode tmp = nodes.get(side2);
                tmp.connections.add(new Path(cost,side1));
            }
            else {
                GraphNode tmp = new GraphNode();
                tmp.connections.add(new Path(cost,side1));
                nodes.put(side2,tmp);
            }
        }
        line = scan.nextLine();
        String[] buffer = line.split(",");
        Integer startNode = Integer.parseInt(buffer[0]);
        double maxCost = Double.parseDouble(buffer[1]);
        //Queue<Travel> queue = new LinkedList<>();
        //int validTravels = 0;
        validPaths = new HashSet<>();
        /*queue.add(new Travel(startNode));
        while(!queue.isEmpty()){
            Travel tmp = queue.remove();
            Integer lastNode = tmp.visitedNodes.get(tmp.visitedNodes.size()-1);
            ArrayList<Path> connections = nodes.get(lastNode).connections;
            if(lastNode == startNode && tmp.visitedNodes.size() > 1){//Preveri ce je pot sklenjena
                Collections.sort(tmp.visitedNodes);
                if(!validPaths.contains(tmp.visitedNodes)){//Preveri ce ista pot ze obstaja
                    validPaths.add(tmp.visitedNodes);
                    validTravels++;
                }
            }
            else{
                for(int i = 0; i < connections.size(); i++){
                    if(tmp.currentCost + connections.get(i).cost <= maxCost &&                  //poglej ce je skupna cena trenutno gledane povezave manjsa od maksimuma
                    (connections.get(i).otherSide == startNode || tmp.visitedNodes.indexOf(connections.get(i).otherSide) == -1)) {    //in mesto se ni bilo obiskano
                        Travel newTmp = new Travel(tmp.currentCost,tmp.visitedNodes);
                        newTmp.visitedNodes.add(connections.get(i).otherSide);//dodaj novo obiskano mesto
                        newTmp.currentCost += connections.get(i).cost;//povecaj ceno
                        queue.add(newTmp);
                    }
                }
            }
        }
        output.println(validTravels);*/
        output.println(availableTrips(startNode,new Travel(startNode),maxCost));
        output.close();
        System.out.print(((double)System.nanoTime()-(double)start)/1_000_000_000);
    }
}
