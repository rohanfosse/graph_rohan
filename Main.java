import java.io.IOException;
import java.net.URISyntaxException;

public class Main {


    public static void main(String[] args) throws IOException, URISyntaxException {
        String Methode = args[0];
        String in = new String(args[1]);
        Graph G = new Graph(in);
        if (Methode.equals("e")) System.out.println(G.diametre());
        else if (Methode.equals("h")) System.out.println(G.Habib());


    }
}
