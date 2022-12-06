# Analyse

## Caractéristiques et consignes

- Généralités
  - Tutorat pour les étudiants de 1er année en difficulté
  - Tutorat réalisé par des étudiants de 2 ou 3 ème année
  - Plateforme supervisé par des enseignants
- Candidatures dans ressource(s)
  - Les enseignants interviennent dans 1 à n ressources
  - Étudiant de 1er année s'inscrit dans 1 à n ressources pour bénéficier du tutorat
  - Étudiant de 2ème ou 3ème année candidate dans 1 ressource
  - Nombre de places par ressource limité
- Validation des candidatures
  - Chaque candidature peut être acceptée/rejetée par un prof de la ressource concernée selon son niveau
  - Chaque inscription doit être validée par un enseignant de la ressource concernée uniquement
  - Inscription peut être refusée si niveau candidat correct
  - Enseignants doivent pouvoir choisir des critères de filtrage automatique
    - Critères génériques et paramétriques
    - Ex: Filtrage moyenne supérieure/inférieure à x
    - Ex: Filtrage nb absences
    - ...
    - Doivent être applicables aux tuteurs et étudiants en difficulté
- Affectation
  - Il n'y aura pas forcément de place pour tout le monde et c'est normal, tout dépend du [calcul de l'affectation](#calculs-pour-affectation)
  - Affectation automatique
  - Aussi possibilité d'affectation manuelle d'un étudiant à un tuteur
  - Si nb de tuteurs trop faible, les tuteurs de 3ème année peuvent encadrer plusieurs étudiants de 1er année
- Plateforme doit gérer les aléas
  - Inscriptions tardives (étudiant/tuteur)
  - Désistements (étudiant/tuteur)
  - ...
  - L'algo d'affectation doit être relancée sans modifier les affectations déjà réalisées
- Autres
  - Données fournies dans un fichier

### "Users stories"

- Candidatures
  - (prof) Intervient dans 1 à n ressources
  - (prof) Approuver une candidature
  - (prof) Approuver des candidatures selon filtrage automatique
  - (prof) Désapprouver une candidature
  - (prof) Désapprouver des candidatures selon filtrage automatique
  - (prof) Changer le statut de la candidature seulement si c'est un prof de la ressource concernée
  - (élève) Candidater dans plusieurs ressources
  - (tuteur) Candidater dans une ressource
- Affectation
  - (prof) Affecter manuellement un étudiant à un tuteur
  - (plateforme) Lancer l'algorithme d'affectation
  - (élève) Être affecté à 1 tuteur
  - (tuteur 2eme) Être affecté à 1 élève
  - (tuteur 3eme) Être affecté à 1...n élèves (TODO: n combien ??)
  - (plateforme) Ne pas changer le résultat des précédents affectations si aléas
  - (ressource) Possède un nombre de places limité

### Notes

On peut créer des students groups dependant de la ressource et calculer les affectations sur ce student group qui contient les élèves et tuteurs qui candidatent dans cette ressource.

Ou alors on crée une classe candidature qui contient un étudiant et une ressource et ainsi un élève peut candidater dans plusieurs ressources

Avantages et inconvénients avec student group: le filtrage peut s'appliquer sur une seule ressource, de plus le graphe est simplifié mais on devra lancer l'algo d'affectation autant de fois qu'il y a de ressources

On pourra utiliser les set pour éviter les doublons (candidatures potentiellement en double)

## Calculs pour affectation

### Consignes

- +♾ si étudiant rejeté (ou on le met pas ?)
- tuteur de troisième année prioritaire
- tuteur avec meilleure moyenne privilégié
- élève avec résultat faible privilégié
- critères de motivation peuvent être pris en compte
- ...

### Réflexion

- On remet toutes les valeurs numériques dans une échelle entre 0 et 1 pour ne pas avoir à gérer leur échelle originale avec les coeffs.

- Si étudiant rejeté => ne pas mettre car un étudiant non - rejeté de 3ème année pourrait prendre sa place. De plus, la librairie fournie ne supporte pas les valeurs tel que Double.POSITIVE_INFINITY (ce qui est logique)

## A faire ou à réfléchir

- Faire un schéma pour la décision de l'affectation.