
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class Graph {
    private int V;
    private int E;
    String[] tab = new String[2];
    LinkedList<Integer>[] adj;

    /**
     * Creation d'un graphe vide avec V sommets sans aretes
     * param V le nombre de nombres
     *
     * @throws java.lang.IllegalArgumentException
     *          si V < 0
     */
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Le nombre de sommets ne peut pas être négatif.");
        this.V = V;
        this.E = 0;
        adj = (LinkedList<Integer>[]) new LinkedList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new LinkedList<Integer>();
        }
    }

    /**
     * Initialisation d'un graphe suivant un fichier au format pfg
     *
     * @throws URISyntaxException
     * @throws IOException
     * @throws java.lang.IndexOutOfBoundsException
     *
     * @throws java.lang.IllegalArgumentException
     *
     */
    public Graph(String s) throws URISyntaxException, IOException {
        //pour faciliter la localisation du fichier
        URL path = ClassLoader.getSystemResource(s);

        int num_ligne = 0;

        LinkedList<String> arete = new LinkedList<String>();

        File f = null;
        if (path != null) {

            f = new File(path.toURI());

        }

        InputStream flux = new FileInputStream(f);
        InputStreamReader lecture = new InputStreamReader(flux);
        BufferedReader buff = new BufferedReader(lecture);
        String ligne;
        while ((ligne = buff.readLine()) != null) {
            num_ligne += 1;
            if (num_ligne == 1) {
                this.V = Integer.parseInt(ligne);
                adj = (LinkedList<Integer>[]) new LinkedList[V];
                for (int v = 0; v < this.V; v++) {
                    adj[v] = new LinkedList<Integer>();
                }
            }
            else
            {
                arete.add(ligne);
                tab = ligne.split(" ");
                this.addEdge(Integer.parseInt(tab[0]) - 1, Integer.parseInt(tab[1]) - 1);
                if(num_ligne%1000==1) System.out.println(num_ligne);
            }
        }
        buff.close();
        System.out.println(num_ligne);
        //creation des listes d'adjacences

        }


        //remplissages des listes d'ajdacences
        /*for (int i = 0; i < arete.size(); i++) {



        }*/



    /**
     * Retourne le nombre de sommet du graphe
     *
     * @return le nombre de sommet du graphe
     */
    public int V() {
        return V;
    }

    /**
     * Retourne le nombre d'arretes du graphe
     *
     * @return le nombre d'arretes du graphe
     */
    public int E() {
        return E;
    }

    /**
     * Valide la creation d'un sommet
     *
     * @throws IndexOutOfBoundsException
     */
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    /**
     * ajoute une arete au graphe.
     *
     * @param v un sommet
     * @param w un autre sommet
     * @throws java.lang.IndexOutOfBoundsException
     *
     */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }


    /**
     * Retourne les sommets adjacents au sommet <tt>v</tt>
     *
     * @param v le sommet
     * @return les sommets adjacents au sommet <tt>v</tt> comme un Iterable
     * @throws java.lang.IndexOutOfBoundsException
     *          unless 0 <= v < V
     */
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }


    /**
     * Retourne l'exentricite du sommet <tt>v</tt> ainsi qu'un sommet ( le premier ) pour
     * lequel cette distance est atteinte sous forme de tableau
     *
     * @param v le sommet
     * @return un tableau 0=>sommet, 1=>exentricite pour un sommet <tt>v</tt>
     */

    public int[] eccentricity(int v) {
        int[] R = new int[2];
        validateVertex(v);
        BreadthFirstPaths bfs;
        int max = 0, pos = -1;
        for (int i = 0; i < this.V; i++) {
            bfs = new BreadthFirstPaths(this, i);
            int b = bfs.distTo(v);
            if (max < b) {
                max = b;
                pos = i;
            }
            //System.out.println("distance entre "+i+" et "+v+" : "+bfs.distTo(v));
        }
        //System.out.println("exentricite de " + v + " : " + max + " atteint pour " + pos);
        R[0] = pos;
        R[1] = max;
        return R;
    }

    /**
     * Retourne le diametre du graphe
     *
     * @param v le sommet
     * @return un entier ( le diametre du graphe)
     */
    public int diametre() {
        int max = 0;
        for (int i = 0; i < this.V; i++) {
            int e = this.eccentricity(i)[1];
            if (max < e) max = e;
        }
        return max;
    }

    /**
     * Retourne une estimation du diametre du graphe par l'heuristique de HABIB
     *
     * @return un entier (heuristique de Habib pour le diametre du graphe)
     */
    public int Habib() {
        int k, balise;
        int x4 = 0;
        int x1 = 0;
        balise = 0;
        int x2 = this.eccentricity(x1)[0];
        System.out.println("exentricite 1");
        int x3 = this.eccentricity(x2)[0];
        BreadthFirstPaths bfs = new BreadthFirstPaths(this, x2);
        if (bfs.distTo(x3) % 2 == 0) k = (int) (bfs.distTo(x3) / 2);
        else k = (int) ((bfs.distTo(x3) + 1) / 2);
        for (int x : bfs.pathTo(x3)) {
            if (balise <= k) {
                balise++;
                x4 = x;
            }
        }
        int x5 = this.eccentricity(x4)[0];
        int l = this.eccentricity(x5)[1];
        return l; // Affiche le résultat.
    }

    /**
     * Retourne un affichage du graphe.
     *
     * @return un affichage du graphe sous forme de String
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < this.V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }


}
