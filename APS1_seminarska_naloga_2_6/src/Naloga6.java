import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Naloga6 {

    static class Vertex{
        public ArrayList<String> neighbours;
        public boolean colorSet;
        public boolean color;

        Vertex(){
            neighbours = new ArrayList<>();
            colorSet = false;
        }
    }

    static boolean colorGraph(HashMap<String,Vertex> group, String start){
        Vertex v = group.get(start);
        for(int i = 0; i < v.neighbours.size(); i++){
            Vertex w = group.get(v.neighbours.get(i));
            if(!w.colorSet){
                w.colorSet = true;
                w.color = !v.color;
                if(!colorGraph(group,v.neighbours.get(i))){//Ce neko poddrevo ni dvodelno, potem tudi naddrevo ne more biti
                    return false;
                }
            }
            else if(v.color == w.color)//Ce sta dve sosednji vozlisci iste barve potem drevo ni dvodelno - ne moramo ga pobarvati z dvema barvama
                return false;
        }
        return true;
    }

    static int numberOfTrues(HashMap<String,Vertex> group){
        int count = 0;
        for(Vertex tmp : group.values()){
            if(tmp.colorSet && tmp.color) count++;
        }
        return count;
    }

    static int numberOfFalses(HashMap<String,Vertex> group){
        int count = 0;
        for(Vertex tmp : group.values()){
            if(tmp.colorSet &&!tmp.color) count++;
        }
        return count;
    }

    static StringBuilder getTrues(HashMap<String,Vertex> group){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,Vertex>tmp : group.entrySet()){
            if(tmp.getValue().colorSet && tmp.getValue().color) sb.append(tmp.getKey()+"\n");
        }
        return sb;
    }

    static StringBuilder getFalses(HashMap<String,Vertex> group){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,Vertex>tmp : group.entrySet()){
            if(tmp.getValue().colorSet && !tmp.getValue().color) sb.append(tmp.getKey()+"\n");
        }
        return sb;
    }

    public static void main(String[] args) throws Exception{
        long start = System.nanoTime();
        File input = new File(args[0]);
        PrintWriter output = new PrintWriter(args[1]);
        Scanner scan = new Scanner(input);
        String line = scan.nextLine();
        int connections = Integer.parseInt(line);
        HashMap<String,Vertex> group = new HashMap<>();
        boolean valid = true;
        String first = new String();
        for(int i = 0; i < connections; i++){//Grajenje grafa
            line = scan.nextLine();
            String[] tmp = line.split("-");
            //System.out.println(tmp[0]+"-"+tmp[1]);
            if(valid){//Zapomnimo si prvega
                first = tmp[0];
                valid = false;
            }
            if(!group.containsKey(tmp[0])){//Ce prva vrednost se ne obstaja kot vozlisce jo dodamo
                Vertex v = new Vertex();
                v.neighbours.add(tmp[1]);
                group.put(tmp[0],v);
            }
            else{//Ce ze obstaja pa le dodamo soseda
                group.get(tmp[0]).neighbours.add(tmp[1]);
            }
            if(!group.containsKey(tmp[1])){//Isto samo da gledamo drugo vrednost
                Vertex v = new Vertex();
                v.neighbours.add(tmp[0]);
                group.put(tmp[1],v);
            }
            else{
                group.get(tmp[1]).neighbours.add(tmp[0]);
            }
        }
        Vertex firstVertex = group.get(first);
        firstVertex.colorSet = true;
        firstVertex.color = true;
        if(colorGraph(group,first)){//Ce graf lahko pobarvamo z dvema barvama lahko razdelimo na voznike in vozila
            int trues = numberOfTrues(group);//stevilo pobarvanih s prvo barvo
            int falses = numberOfFalses(group);//stevilo pobarvanih z drugo barvo
            if(trues == falses){//Vecje stevilo predstavlja voznike, ce ne ni mozne enolicne resitve
                output.println(-1);
            }
            else if(trues > falses){
                output.print(getTrues(group).toString());
            }
            else if(trues < falses){
                output.print(getFalses(group).toString());
            }
        }
        else{//Grafa ne moremo pobarvati z dvema barvama, tako da resitev ni mozna
            output.println(-1);
        }
        output.close();
        System.out.print(((double)System.nanoTime()-(double)start)/1_000_000_000);
    }
}
