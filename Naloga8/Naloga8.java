import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Naloga8 {

    static int dimension;
    static int[][] heights;

    static class Node{
        public int min;
        public int max;
        public int dimension;
        public Node[] children;

        Node(){
            children = new Node[4];
        }

        Node(int min, int max, int dimension){
            this.min = min;
            this.max = max;
            this.dimension = dimension;
            children = new Node[4];
        }

        int childrenMin(){
            int currMin = children[0].min;
            for(int i = 1; i < 4; i++){
                if(children[i].min < currMin) currMin = children[i].min;
            }
            return currMin;
        }

        int childrenMax(){
            int currMax = children[0].max;
            for(int i = 1; i < 4; i++){
                if(children[i].max > currMax) currMax = children[i].max;
            }
            return currMax;
        }
    }

    static class Tree{
        Node root;

        Tree(){
            root = buildTree(0,0,dimension-1,dimension-1,dimension);
        }

        Node buildTree(int startX, int startY, int endX, int endY, int dim){
            if(startX == endX && startY == endY){//Ce smo na eni sami tocki naredimo nov list in ga vrnemo
                return new Node(heights[startX][startY], heights[startX][startY],dim);
            }
            else{//drugace pa razbijamo na manjse kvardate, dokler ne pridemo do tock
                Node tmp = new Node();
                tmp.children[0] = buildTree(startX,startY,startX + (dim/2) - 1,startY + (dim/2) - 1,dim/2);
                tmp.children[1] = buildTree(startX + dim/2, startY, endX, startY + (dim/2) - 1, dim/2);
                tmp.children[2] = buildTree(startX, startY + dim/2, startX + (dim/2) - 1, endY, dim/2);
                tmp.children[3] = buildTree(startX + dim/2,startY + dim/2,endX,endY,dim/2);
                tmp.min = tmp.childrenMin();
                tmp.max = tmp.childrenMax();
                tmp.dimension = dim;
                return tmp;
            }
        }

        int[] sunkenPointsUtil(Node n, int waterHeight){
            if(waterHeight < n.min){//Ce je visina voda nizja od minimuma, potem ni nic potopljeno in smo gledali le eno vozlisce
                int[] tmp = new int[2];
                tmp[0] = 0;
                tmp[1] = 1;
                return tmp;
            }
            else if(waterHeight >= n.max){//Ce je visina vode vec od maksimuma potem je vse potopljeno in smo gledali le eno vozlisce
                int[] tmp = new int[2];
                tmp[0] = n.dimension * n.dimension;
                tmp[1] = 1;
                return tmp;
            }
            else{//Ce je delno potopljeno gremo gledati za vsako poddrevo
                int childTmp[] = new int[2];
                int tmp[] = new int[2];
                for(int i = 0; i < 4; i++){
                    childTmp = sunkenPointsUtil(n.children[i],waterHeight);
                    tmp[0] += childTmp[0];
                    tmp[1] += childTmp[1];
                }
                tmp[1] += 1;
                return tmp;
            }
        }

        int[] sunkenPoints(int waterHeight){
            return sunkenPointsUtil(root,waterHeight);
        }
    }

    public static void main(String[] args) throws Exception{
        long start = System.nanoTime();
        File input = new File(args[0]);
        PrintWriter output = new PrintWriter(args[1]);
        Scanner scan = new Scanner(input);
        String line = scan.nextLine();
        dimension = Integer.parseInt(line);
        heights = new int[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            line = scan.nextLine();
            String[] buffer = line.split(",");
            for(int j = 0; j < dimension; j++){
                heights[j][i] = Integer.parseInt(buffer[j]);
            }
        }
        line = scan.nextLine();
        int n = Integer.parseInt(line);
        int[] checkHeights = new int[n];
        for(int i = 0; i < n; i++){
            line = scan.nextLine();
            checkHeights[i] = Integer.parseInt(line);
        }

        Tree h = new Tree();

        for(int i = 0; i < n; i++){
            int[] tmp = h.sunkenPoints(checkHeights[i]);
            output.println(tmp[0] + "," + tmp[1]);
        }
        output.close();
        System.out.print(((double)System.nanoTime()-(double)start)/1_000_000_000);
    }
}
