import java.util.*;

class Node {
    int val;
    boolean visited;

    public Node(int val){
        this.val = val;
        visited = false;
    }

    @Override
    public String toString() {
        return val+"";
    }
}
