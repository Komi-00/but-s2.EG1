package tp_graphe;

import java.util.List;

import fr.ulille.but.sae2_02.graphes.CalculAffectation;
import fr.ulille.but.sae2_02.graphes.GrapheNonOrienteValue;

public class ExempleGraphe {

    public static void main(String[] args) {
        // Créer tableau 2 dimensions couts
        double[][] couts = new double[][]{
                {1, 2, 3}, // Chaque tutorés ou tuteurs avec les poids
                {1, 0, 4},
                {10, 6, 8}
        };

        List<String> tutores = List.of("Renan", "Michel", "Manuel"); // Faire une liste des tutorés
        List<String> tuteurs = List.of("Florian", "Kilian", "Benjamin"); // Faire une liste des tuteurs

        GrapheNonOrienteValue<String> graphe = new GrapheNonOrienteValue<>();

        for (String tutore : tutores) {
            graphe.ajouterSommet(tutore);
        }

        for (String tuteur : tuteurs) {
            graphe.ajouterSommet(tuteur);
        }

        for (int i = 0; i < tutores.size(); i++) {
            for (int j = 0; j < tuteurs.size(); j++) {
                graphe.ajouterArete(tutores.get(i), tuteurs.get(j), couts[i][j]);
            }
        }

        CalculAffectation<String> calcul = new CalculAffectation<>(graphe, tutores, tuteurs);

        System.out.println(calcul.getAffectation());
    }

}