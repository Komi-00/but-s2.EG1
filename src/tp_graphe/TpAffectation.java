package tp_graphe;

import java.util.List;

import fr.ulille.but.sae2_02.graphes.CalculAffectation;
import fr.ulille.but.sae2_02.graphes.GrapheNonOrienteValue;

public class TpAffectation {
    public static void main(String[] args) {
        // Instancier un Graphe Non Oriente Value
        GrapheNonOrienteValue<String> g = new GrapheNonOrienteValue<String>();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        g.ajouterSommet("D");
        g.ajouterSommet("W");
        g.ajouterSommet("X");
        g.ajouterSommet("Y");
        g.ajouterSommet("Z");

        g.ajouterArete("A", "W", 13);
        g.ajouterArete("A", "X", 4);
        g.ajouterArete("A", "Y", 7);
        g.ajouterArete("A", "Z", 6);

        g.ajouterArete("B", "W", 1);
        g.ajouterArete("B", "X", 11);
        g.ajouterArete("B", "Y", 5);
        g.ajouterArete("B", "Z", 4);

        g.ajouterArete("C", "W", 6);
        g.ajouterArete("C", "X", 7);
        g.ajouterArete("C", "Y", 2);
        g.ajouterArete("C", "Z", 8);

        g.ajouterArete("D", "W", 1);
        g.ajouterArete("D", "X", 3);
        g.ajouterArete("D", "Y", 5);
        g.ajouterArete("D", "Z", 9);
        System.out.println(g);

        CalculAffectation<String> calculAffect = new CalculAffectation<String>(g, List.of("A", "B", "C", "D"),
                List.of("W", "X", "Y", "Z"));
        System.out.println(calculAffect.getAffectation());
        System.out.println(calculAffect.getCout());
    }
}