import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Naloga10 {

    static class Node{
        int element;
        Node parent;
        Node left;
        Node right;

        Node(int e, Node parent){
            element = e;
            this.parent = parent;
        }

        Node(int e,Node parent, Node left, Node right){
            element = e;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }

    static int maxOnRange(int start, int end, int[] arr){
            if(end >= arr.length) return -1;
            else if(start < 0) return -1;
            else {
                int maxIndex = -1;
                int max = 0;
                for (int i = start; i <= end; i++) {
                    if (arr[i] > max) {
                        maxIndex = i;
                        max = arr[i];
                    }
                }
                return maxIndex;
            }
    }

    static void constructTree(int start, int end, int[] arr, Node parentNode, Tree t, boolean left){
        int h = maxOnRange(start,end,arr);
        if(h != -1){
            Node tmp = t.addNode(arr[h],parentNode,left);
            constructTree(start,h-1,arr,tmp,t,true);
            constructTree(h+1,end,arr,tmp,t,false);
        }
    }

    static class Tree{
        Node root;

        Tree(){
            root = null;
        }

        Tree(Node n){
            root = n;
        }

        Node addNode(int element, Node parent, boolean left){
            if(parent == null){
                root = new Node(element,null);
                return root;
            }
            else {
                if (left) {
                    Node tmp = new Node(element, parent);
                    parent.left = tmp;
                    return tmp;
                } else {
                    Node tmp = new Node(element, parent);
                    parent.right = tmp;
                    return tmp;
                }
            }
        }

        int height(Node root){
            if(root == null) return 0;
            else{
                int leftHeight = height(root.left);
                int rightHeight = height(root.right);
                if(leftHeight > rightHeight) return (leftHeight+1);
                else return  (rightHeight+1);
            }
        }

        void printUtil(Node root, int h, StringBuilder sb){
            if(root == null) return;
            if(h == 0) sb.append(root.element+",");
            else if(h > 0){
                printUtil(root.left,h-1,sb);
                printUtil(root.right,h-1,sb);
            }
        }

        StringBuilder printSubtree(){
            StringBuilder sb = new StringBuilder();
            int h = height(root);
            for(int i = 0; i < h; i++){
                printUtil(root,i,sb);
            }
            return sb;
        }
    }


    public static void main(String[] args) throws Exception{
        long start = System.nanoTime();
        File input = new File(args[0]);
        PrintWriter output = new PrintWriter(args[1]);
        Scanner scan = new Scanner(input);
        String line = scan.nextLine();
        String[] numsS = line.split(",");
        int nums[] = new int[numsS.length];
        for(int i = 0; i < numsS.length; i++){
            nums[i] = Integer.parseInt(numsS[i]);
        }
        Tree drevo = new Tree();
        constructTree(0,nums.length-1,nums,null,drevo,true);
        StringBuilder sb = drevo.printSubtree();
        sb.deleteCharAt(sb.length()-1);
        output.print(sb.toString());
        output.close();
        System.out.print(((double)System.nanoTime()-(double)start)/1_000_000_000);
    }
}
