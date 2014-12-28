
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Stack;


public class BreadthFirstPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;

    /**
     * Calcule le plus court chemin entre le sommet d'origine s
     * aet les autres sommet du graphe  G
     *
     * @param G le graphe
     * @param s le sommet d'origine
     */
    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        bfs(G, s);
    }


    private void bfs(Graph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }


    /**
     * @param v le sommet
     * @return true si il y a un chemin vers le sommet v et false  sinon
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * @param v le sommet
     * @return  le nombre de sommet du plus court chemin     vers v
     */
    public int distTo(int v) {
        return distTo[v];
    }

    /**
     * @param v le sommet
     * @return la liste des sommets du plus court chemin comme un Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }
}