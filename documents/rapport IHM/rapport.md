---
title: Rapport d'IHM. SAE 2.01-2.02
authors:
  - Renan Declercq
  - Florian Etrillard
  - Felix Pereira
---

# Rapport d'IHM. SAE 2.01-2.02

!!! abstract Groupe E-G1
    Renan Declercq
    Florian Etrillard
    Felix Pereira

!!! abstract Repo Gitlab
    - <https://gitlab.univ-lille.fr/sae2.01-2.02/2022/E-G1>  
    - Signature du commit de rendu : `79108972e4d1fe708f4c643b884acd9737844df6`

!!! tip Vidéo de présentation du projet
    <https://youtu.be/cIlK_sN6A8Q>

## 1. Table des matières

<!-- @import "[TOC]" {cmd="toc" depthFrom=2 depthTo=6 orderedList=false} -->

<!-- code_chunk_output -->

- [1. Table des matières](#1-table-des-matières)
- [2. Captures d'écran de l'application finale](#2-captures-décran-de-lapplication-finale)
- [3. Utilisation de l'application / Scénario](#3-utilisation-de-lapplication--scénario)
- [4. Justification de nos choix de conception](#4-justification-de-nos-choix-de-conception)
  - [4.1. La fenêtre globale](#41-la-fenêtre-globale)
  - [4.2. Etudiants](#42-etudiants)
    - [4.2.1. `ListView` d'étudiants](#421-listview-détudiants)
    - [4.2.2. Popup `StudentInfo`](#422-popup-studentinfo)
  - [4.3. Paramètres](#43-paramètres)
  - [4.4. Affectations](#44-affectations)
- [5. Contribution des membres du groupe](#5-contribution-des-membres-du-groupe)
  - [5.1. Renan Declercq](#51-renan-declercq)
  - [5.2. Florian Etrillard](#52-florian-etrillard)
  - [5.3. Felix Pereira](#53-felix-pereira)
  - [5.4. Bilan des contributions](#54-bilan-des-contributions)
    - [5.4.1. Comment le groupe a réussi à exploiter au mieux les compétences de chacun ?](#541-comment-le-groupe-a-réussi-à-exploiter-au-mieux-les-compétences-de-chacun-)
    - [5.4.2. Participation active de chaque membre dans l'écriture du code. Commits réguliers et contribution équivalente de chaque membre](#542-participation-active-de-chaque-membre-dans-lécriture-du-code-commits-réguliers-et-contribution-équivalente-de-chaque-membre)

<!-- /code_chunk_output -->

## 2. Captures d'écran de l'application finale

@import "./images/appli1.png"
@import "./images/appli2.png"
@import "./images/appli7.png"
@import "./images/appli3.png"
@import "./images/appli4.png"
@import "./images/appli5.png"
@import "./images/appli6.png"

## 3. Utilisation de l'application / Scénario

Vous trouverez des fichiers JSON déjà préparés dans le dossier `res`, vous n'avez qu'à les importer :

- Importer étudiants : `res/students.json`
- Importer règles d'approbation : `res/IHM/IHM_filters.json`
  
!!! info
    L'application ne prend en compte qu'une seule matière. Afin de construire notre scénario, nous avons fait en sorte de ne prendre en compte que les étudiants inscrits dans la matière IHM dans notre fichier `students.json` que nous avions déjà utilisée dans notre scénario pour la partie POO.

Nous avons aussi des fichiers de sauvegarde :

- Paramètres : `/res/config_javafx.json` 

!!! Note Sauvegarde des affectations
    Nous aurions aimé implémenter la sauvegarde des affectations, mais nous n'avons malheureusement pas eu le temps

## 4. Justification de nos choix de conception

### 4.1. La fenêtre globale

Nous avons utilisé un `TabPane` comprenant des onglets pour changer de menu de façon intuitive.

Nous avons choisi de segmenter l'application en 3 parties :

- `Etudiants`
- `Affectations`
- `Paramètres`

Au lancement de l'application, l'utilisateur est automatiquement dans le menu `Etudiants` car les premières actions demandées à l'utilisateur sont situés dans ce menu.

### 4.2. Etudiants

Fenêtre `Etudiant` pré importation des étudiants
@import "./images/Etudiants-pre.png"

Dans le menu `Etudiants` nous retrouvons :

- Un bouton d'import des étudiants
- Un bouton d'import des règles d'approbation pour filtrer les étudiants pré-affectation
- La visualisation des tuteurs importés via une `listView`
- La visualisation des tutorés importés via une `listView`
- Une zone de texte de recherche de tuteurs
- Une zone de texte de recherche de tutorés
- Un bouton pour forcer l'affectation entre deux étudiants
- Un bouton pour interdire l'affectation entre deux étudiants
- Un bouton pour annuler l'affectation forcée ou interdite entre deux étudiants
- Boutons de tri des étudiants tuteurs et tutorés (et son menu déroulant de critères de tri)
  
  - Prénom
  - Nom
  - Moyenne générale
  - Moyenne de la ressource
  - Nombre d'absences

Premièrement l'enseignant est amené à importer ses étudiants et à importer des règles d'approbation s'il le souhaite (pour éviter d'avoir à les approuver à la main avant le calcul d'affectation)

Fenêtre Etudiant post importation des étudiants
@import "./images/EtudiantsWindow.png"

!!! info Bouton de tri
    Il faut sélectionner son menu pour mettre automatiquement en ordre croissant du critère sélectionné.
    Vous pouvez appuyer sur le bouton `Trier` pour changer entre l'ordre croissant et décroissant du critère sélectionné.
    @import "./images/Tri-tuteurs.png"


!!! Info Boutons
    Nous avons fait en sorte d'activer/désactiver les boutons lorsqu'une action est réalisable ou non. Cela se caractérise par une diminution d'opacité lorsque le bouton est inutilisable. De ce fait, un contrôle explicite des boutons est intégré, l'utilisateur sait alors lorsqu'il peut appuyer sur un bouton ou non.
    @import "./images/UseButton.png"
    De plus, les boutons sont organisés de façon stratégique, nous avons suivi la loi de proximité pour montrer des fonctionnalités proches concernant des boutons proches. Comme ci-dessous avec les fonctionnalités de forçage/d'interdiction d'affectation puis du bouton permettant d'annuler une affectation modifiée de la sorte.
    Pour finir, nous avons pris en compte la loi de Fitts dans l'élaboration de notre interface, ci-dessus par exemple, pour effectuer une affectation forcée ou interdite, il faut sélectionner un étudiant tuteur à droite et un étudiant tutoré à gauche, les boutons (suffisamment larges) se situent pile entre les deux listes, ainsi lorsque l'utilisateur effectue des affectations manuelles, il peut garder sa souris dans une zone limitée et éviter les gros aller-retour de souris.

!!! tip Etudiants non approuvés
    L'approbation ne rentre en compte que dans le calcul d'affectation, un étudiant non approuvé peut très bien se retrouver dans une affectation forcée !
    @import "./images/forceNotApprove.png"
    @import "./images/forceNotApprove2.png"


!!! Info Importation de fichiers
    Nos importations de fichiers se font via un `FileChooser`, nous avons une fenêtre d'erreur qui s'ouvre lorsque le fichier sélectionné n'a pas le bon format (soit si le fichier n'est pas un JSON, soit si le JSON ne contient pas les informations `(Key:Value)` correspondante à nos fonctions d'importation). Cela permet un contrôle sur les actions de l'utilisateur pour éviter des erreurs a posteriori sur le fichier importé. De plus, le message d'erreur permet une connaissance au diagnostic de l'erreur à l'utilisateur, c'est indispensable pour qu'il corrige son erreur.
    @import "./images/FileChooser.png"
    @import "./images/ErrorWindow.png"

#### 4.2.1. `ListView` d'étudiants

Les listes des étudiants sont composés d'un élément customisé nommé `StudentCard`

@import "./images/StudentCard.png"

Cet élément est composé de :

- Un bouton d'approbation
- Un bouton de désapprobation
- Un label prénom
- Un label nom
- Un label moyenne générale
- Un label moyenne de la ressource
- Un label nombre d'absences
- Un bouton permettant d'accéder à toutes les informations de l'étudiant (`StudentInfo`)

!!! tip Icônes
    Nous avons utilisé beaucoup d'icônes pour représenter des boutons ou des états, car nous trouvons que les icônes facilitent la compréhension à condition qu'elles soient cohérentes avec les standards. De plus, les icônes ajoutent un côté esthétique au design de l'application et nous permettent de diminuer le texte pour rester minimaliste.
    Nous avons une certaine homogénéité concernant nos boutons, le style graphique des icônes utilisées est le même et les couleurs sont cohérentes avec les significations et avec le design global de l'application.


!!! Info Informations supplémentaire
    Lorsque l'on sélectionne un étudiant nous avons une icône qui s'affiche sur tous les étudiants qui ont une affectation forcée ou interdite avec cet étudiant.
    @import "./images/IconForcedForbidden.png"
    Ici Edmee McCay est sélectionnée, elle a une affectation forcée avec Leone Showte et une affectation interdite avec Dafnee McNay.
    On constate aussi que l'étudiant tutoré sélectionné est Neo Harp et qu'il n'a pas d'affectation forcée ou interdite avec Edmee McCay.
    Si l'on ajoute une affectation interdite entre Neo Harp et Edmee McCay voici ce que cela donne :
    @import "./images/IconForcedForbidden2.png"

!!! tip Tooltips
    Des tooltips ont été implémenté à divers endroits pour informer l'utilisateur lorsqu'il laisse le curseur sur un élément, par exemple dans nos StudentCards :
    - Idéal si un nom d'étudiant est trop long
     @import "./images/tooltip1.png"
    - Pour avoir la correspondance de chaque valeur, par exemple si l'on fige son curseur sur la valeur `10.98 ` ci-dessous
    @import "./images/tooltip2.png"
    - Ou encore pour connaître la fonctionnalité d'un bouton, par exemple si l'on met son curseur sur la 1ère icône ci-dessous
    @import "./images/tooltip3.png"

#### 4.2.2. Popup `StudentInfo`

Cette fenêtre est exclusivement informative, elle donne quelques informations sur l'étudiant.

@import "./images/StudentInfo.png"

### 4.3. Paramètres

Une fois que l'enseignant a effectué toutes les actions qu'il souhaitait dans le menu `Etudiants`, il peut se diriger vers le menu `Paramètres` s'il le souhaite.

!!! Info Paramètres
    Des paramètres sont déjà pris en compte par défaut, ils sont suffisamment cohérents pour que l'enseignant ne s'en préoccupe pas, néanmoins il a tout de même la possibilité de les changer assez facilement et intuitivement

@import "./images/parametres.png"  

Les paramètres servent à changer l'importance des critères pris en compte dans le calcul d'affectation. Les critères pris en compte et modifiable sont :

- Moyenne générale
- Moyenne de la ressource
- Année du tuteur
- Motivation du tuteur
- Motivation du tutoré

Nous avons décidé de mettre des `sliders` pour sélectionner le coefficient de chaque critère. Chaque `slider` va de 0 à 10 et l'utilisateur peut sélectionner les valeurs de 0.5 en 0.5.
Les `sliders` positionnés les uns en dessous des autres permettent à l'utilisateur de bien visualiser la différence d'importance entre chaque critère. Ainsi, nous pensons que les `sliders` sont la solution la plus visuelle et la plus rapide pour l'utilisateur.

!!! Info Réinitialisation et sauvegarde
    Un fichier de sauvegarde est déjà préconfiguré par nos soins dans `/res/startup_config.json`. Ce fichier sera chargé automatiquement pour définir les coefficients originaux de chaque critère pendant le calcul d'affectations (si non définis par l'utilisateur) et les ressources disponibles (même si dans le cadre de l'application, la seule réellement utilisée est `IHM`).  
    L'utilisateur peut changer l'importance des critères sans appuyer sur le bouton sauvegarder, ses changements seront pris en compte s'il lance une affectation par la suite, cependant ils ne seront pas sauvegardés dans le fichier `/res/coefficients_config.json`, c'est-à-dire qu'à la prochaine ouverture de l'application, ses changements concernant l'importance des critères ne seront pas pris en compte s'il relance une affectation.
    Le bouton réinitialisation permet de revenir à la configuration telle qu'elle a été sauvegarder pour la dernière fois dans le fichier `/res/coefficients_config.json`.

### 4.4. Affectations

Maintenant que les parties `Etudiants` et `Paramètres` ont été réglé, l'utilisateur peut se diriger vers l'onglet `Affectations`. Le menu `Affectations` permet d'accéder à la partie centrale de l'application, c'est-à-dire le calcul d'affectation.

Dans le menu Affectations nous retrouvons :

- Deux toggle buttons `Calculées et forcées` / `Interdites` permettant le basculement d'affichage d'une liste à l'autre
- Une `listView` permettant la visualisation sur les affectations calculées et forcées
- Une `listView` permettant la visualisation sur les affectations interdites
- Une zone de texte de recherche d'étudiants
- Un menu de tri
- Un bouton pou réinitialiser les affectations calculées
- Un bouton pour lancer le calcul d'affectation

@import "./images/AssignmentsEmpty.png"

Lorsque l'utilisateur arrive sur cette fenêtre pour la première fois il n'y a encore aucune affectation effectuée. Il peut tout de même jeter un oeil aux affectations forcées et interdites avant de lancer l'affectation. Pour cela nous avons un `toggle bouton` pour visualiser les affectations forcées et calculées et un autre pour les affectations forcées. Lorsque l'un de ces boutons est pressé, l'autre est automatiquement relâché. Cela permet de ne visualiser qu'une seule liste à la fois.

L'utilisateur peut ensuite lancer le calcul d'affectation en appuyant sur le bouton dédié.

Une fois les affectations obtenues, l'enseignant peut faire une recherche par prénom ou nom

@import "./images/rechercheNom.png"

Ou alors il peut les trier en sélectionnant l'un des critères de tri

@import "./images/triAffect.png"

Nous avons aussi implémenté un code couleur concernant le contrôle des affectations, l'enseignant peut visualiser directement les affectations qui semblent problématique.

@import "./images/couleurAffec.png"

!!! tip Code couleur des affectations
    On peut en effet constater 3 couleurs différentes qui englobent les affectations :
    **Vert** : Un score d'affectation faible
    **Orange** : Un score d'affectation moyen
    **Rouge** : Un score d'affectation fort

    Le score minimum est utilisé lorsqu'un étudiant tuteur est affecté à plusieurs étudiants en difficultés.

!!! tip Affectation interdite post lancement d'affectation
    Si certaines affectations calculées ne lui plaisent pas, l'utilisateur peut tout à fait les ajoutées en affectation interdites, mais il faudra relancer un calcul d'affectation pour séparer les deux étudiants.
    @import "./images/iconeInterdite.png"
    @import "./images/affectationsInterdiction.png"

!!! tip Désistement d'un étudiant
    L'utilisateur peut aussi retirer un étudiant d'une affectation pour cause de désistement par exemple en appuyant sur l'icône ci-dessous
    @import "./images/desistement.png"
    @import "./images/DesistementMessage.png"

## 5. Contribution des membres du groupe

### 5.1. Renan Declercq

| Partie                  | Détails                                                                                                                                                                                           |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Maquette basse fidélité | Participation à la réalisation de la maquette basse fidélité et des séquences d'interactions. J'ai surtout travaillé sur le menu `Affectations`                                                   |
| Vue `Etudiants`         | Participation à la réalisation du menu `Etudiants` sur SceneBuilder notamment sur certains designs (icônes) et sur la fenêtre Information de l'étudiant + implémentation `StudentInfoController`  |
| Vue `Affectations`      | Participation à la réalisation du menu `Affectations` sur SceneBuilder notamment sur la scène globale + participation à l'implémentation de `AssignmentsViewController`                           |
| Vue `Paramètres`        | Réalisation du menu `Paramètres` sur SceneBuilder et implémentation de `SettingsViewController`                                                                                                   |
| Fenêtre globale         | Réalisation d'une partie de la fenêtre globale et insertion des FXML                                                                                                                              |
| Autre                   | Création de la classe `CustomAlert`, quelques légères modifications dans notre code de POO nécessaire pour la partie IHM, réalisation de la vidéo de présentation du projet, rédaction du rapport |

### 5.2. Florian Etrillard

| Partie                  | Détails                                                                                                                                                                                                                                          |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Maquette basse fidélité | Réalisation de la page étudiants, apport d'idées sur le design et l'expérience utilisateur                                                                                                                                                       |
| Vue `Etudiants`         | Réalisation des cellules personnalisées et de la `"card"` pour représenter les étudiants, mise en place de la logique de recherche/tri, affichage des étudiants dans les `listview` et gestion des boutons d'interdiction/forçage d'affectations |
| Vue `Affectations`      | Même chose que précédemment, en rajoutant la création d'un nouveau composant personnalisé pour représenter les affectations                                                                                                                      |
| Autre                   | Améliorations d'UI/UX, refactorisation/amélioration du code, diverses configurations du projet (export `jar`...), participation à la rédaction du rapport                                                                                        |

### 5.3. Felix Pereira

| Partie                  | Détails                                                                                                                                                                                                                                                      |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Maquette basse fidélité | Participation à la réalisation de la maquette basse fidélité et des séquences d'interactions. J'ai surtout travaillé sur les séquences d'interactions possibles avant d'établir la maquette et sur le menu `Paramètres` lors de l'élaboration de la maquette |

### 5.4. Bilan des contributions

#### 5.4.1. Comment le groupe a réussi à exploiter au mieux les compétences de chacun ?

Nous avons séparé les tâches en fonction de leur difficulté et en fonction de la rapidité de chacun pour que tout le monde puisse réfléchir, avancer à son rythme, dans le but d'ancrer correctement les notions apprises, et que le projet soit le plus enrichissant possible.

#### 5.4.2. Participation active de chaque membre dans l'écriture du code. Commits réguliers et contribution équivalente de chaque membre

Pour les raisons expliquées ci-dessus (notamment que chacun n'a pas besoin du même temps pour apprendre), ainsi qu'une différence d'investissement entre les membres du groupe, il y a quelques écarts de contribution entre certains membres. Cependant, nous avons veillés à faire des commits réguliers (plus de `300`) et ce que chacun puisse comprendre les notions utilisées dans le projet, afin de progresser.
