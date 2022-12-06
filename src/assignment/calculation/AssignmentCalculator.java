package assignment.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.ulille.but.sae2_02.graphes.Arete;
import fr.ulille.but.sae2_02.graphes.CalculAffectation;
import fr.ulille.but.sae2_02.graphes.GrapheNonOrienteValue;

public class AssignmentCalculator<E1 extends IAssignable, E2 extends IAssignable> {
    public static final int MAX_LEFT_ASSIGNABLE_PER_RIGHT_ASSIGNABLE = 2;
    public static final int MAX_RIGHT_ASSIGNABLE_PER_LEFT_ASSIGNABLE = 1;

    private final Set<E1> leftAssignables;
    private final Set<E2> rightAssignables;
    private final Set<Assignment<E1, E2>> existingAssignments;
    private final String ressourceName;

    /**
     * Create a new assignment calculator for the given ressource.
     *
     * @param leftAssignables     the assignables on the left side of the assignment
     * @param rightAssignables    the assignables on the right side of the assignment
     * @param existingAssignments the existing assignments
     * @param ressourceName       the name of the ressource
     */
    public AssignmentCalculator(Set<E1> leftAssignables, Set<E2> rightAssignables, Set<Assignment<E1, E2>> existingAssignments,
                                String ressourceName) {
        this.leftAssignables = leftAssignables;
        this.rightAssignables = rightAssignables;
        this.existingAssignments = existingAssignments;
        this.ressourceName = ressourceName;
    }

    /**
     * Compute the final list of assignments according to leftAssignables, rightAssignables and existingAssignments.
     *
     * @return the final list of assignments
     */
    public List<Assignment<E1, E2>> computeAssignments() {
        UniqueAssignableWrapper.reset();

        List<UniqueAssignableWrapper> leftList = this.computeDuplicatedAssignables(this.leftAssignables,
                MAX_LEFT_ASSIGNABLE_PER_RIGHT_ASSIGNABLE);
        List<UniqueAssignableWrapper> rightList = this.computeDuplicatedAssignables(this.rightAssignables,
                MAX_RIGHT_ASSIGNABLE_PER_LEFT_ASSIGNABLE);

        leftList = completeWithFictive(leftList, rightList.size());
        rightList = completeWithFictive(rightList, leftList.size());

        GrapheNonOrienteValue<UniqueAssignableWrapper> graph = this.getAssignmentGraph(leftList, rightList);

        return this.calculAffectationToAssignments(new CalculAffectation<>(graph, leftList, rightList), graph);
    }

    /**
     * Convert the result of the assignment computation into a list of assignments.
     *
     * @param calculAffectation the assignment computation
     * @param graph             the assignment graph
     * @return the list of assignments
     */
    private List<Assignment<E1, E2>> calculAffectationToAssignments(CalculAffectation<UniqueAssignableWrapper> calculAffectation,
                                                                    GrapheNonOrienteValue<UniqueAssignableWrapper> graph) {
        List<Assignment<E1, E2>> assignments = new ArrayList<>();

        for (Arete<UniqueAssignableWrapper> edge : calculAffectation.getAffectation()) {
            Assignment<E1, E2> newAssignment = this.edgeToAssignment(edge, graph);
            if (newAssignment != null) {
                assignments.add(newAssignment);
            }
        }
        return assignments;
    }

    /**
     * Convert assignment graph edge to an assignment by using {@link #getAssignmentResult(E1, E2, double)}. Return null if one of the assignable is fictive.
     *
     * @param edge  the edge to convert
     * @param graph the assignment graph
     * @return the assignment or {@code null} if one of the assignable is fictive
     */
    private Assignment<E1, E2> edgeToAssignment(Arete<UniqueAssignableWrapper> edge, GrapheNonOrienteValue<UniqueAssignableWrapper> graph) {

        IAssignable leftAssignable = edge.getExtremite1().getAssignable();
        IAssignable rightAssignable = edge.getExtremite2().getAssignable();

        if (!leftAssignable.isFictive() && !rightAssignable.isFictive()) {
            return this.getAssignmentResult((E1) leftAssignable, (E2) rightAssignable,
                    graph.getPoids(edge.getExtremite1(), edge.getExtremite2()));
        }
        return null;
    }

    /**
     * Create a new assignment between the given left and right assignables or return the existing one if it already exists. If one of the assignable is full, it
     * returns null.
     *
     * @param leftAssignable  the assignable on the left side of the assignment
     * @param rightAssignable the assignable on the right side of the assignment
     * @param score           the score computed for the two assignables
     * @return the new assignment or existing assignment, oe {@code null} if one of the assignable is full
     */
    private Assignment<E1, E2> getAssignmentResult(E1 leftAssignable, E2 rightAssignable, double score) {
        if (this.isLeftAssignableFull(leftAssignable) || this.isRightAssignableFull(rightAssignable)) {
            return null;
        }
        Assignment<E1, E2> existingAssignment = this.getExistingAssignment(leftAssignable, rightAssignable);
        if (existingAssignment != null) {
            return existingAssignment;
        }

        return new Assignment<>(leftAssignable, rightAssignable, AssignmentType.COMPUTED, score);
    }

    /**
     * Test if the given left assignable is full.
     *
     * @param leftAssignable the assignable to test
     * @return true if the assignable is full
     */
    private boolean isLeftAssignableFull(E1 leftAssignable) {
        int count = 0;
        for (Assignment<E1, E2> assignment : this.existingAssignments) {
            if (assignment.containsLeft(leftAssignable) && assignment.isEffective()) {
                count += 1;
            }
        }
        return count >= AssignmentCalculator.MAX_LEFT_ASSIGNABLE_PER_RIGHT_ASSIGNABLE;
    }

    /**
     * Test if the given right assignable is full.
     *
     * @param rightAssignable the assignable to test
     * @return true if the assignable is full
     */
    private boolean isRightAssignableFull(E2 rightAssignable) {
        int count = 0;
        for (Assignment<E1, E2> assignment : this.existingAssignments) {
            if (assignment.containsRight(rightAssignable) && assignment.isEffective()) {
                count += 1;
            }
        }
        return count >= AssignmentCalculator.MAX_RIGHT_ASSIGNABLE_PER_LEFT_ASSIGNABLE;
    }

    /**
     * Create a list of assignables by duplicating each assignable by the number of assignables that it can take (only if it accepts multiples assignments). Each
     * assignable is wrapped inside a {@link UniqueAssignableWrapper} to allow "duplicates".
     *
     * @param assignables                the non duplicated list of assignables
     * @param maxAssignablePerAssignable the number of assignables that an assignable can be assigned to
     * @return the list of duplicated assignables wrapped inside a {@link UniqueAssignableWrapper}
     */
    private List<UniqueAssignableWrapper> computeDuplicatedAssignables(Set<? extends IAssignable> assignables,
                                                                       int maxAssignablePerAssignable) {
        List<UniqueAssignableWrapper> assignablesList = new ArrayList<>();

        for (IAssignable assignable : assignables) {
            if (assignable.isApproved(this.ressourceName)) {
                assignablesList.add(new UniqueAssignableWrapper(assignable));
            }
            for (int i = 1; i < maxAssignablePerAssignable; i++) {
                if (assignable.canHaveMultiple() && assignable.isApproved(this.ressourceName)) {
                    assignablesList.add(new UniqueAssignableWrapper(assignable));
                }
            }
        }
        return assignablesList;
    }

    /**
     * Complete the duplicated assignables list with fictive assignables according to the opposite list size.
     *
     * @return the list of duplicated assignables completed with fictive assignables if needed
     */
    private static List<UniqueAssignableWrapper> completeWithFictive(List<UniqueAssignableWrapper> assignablesList, int oppositeListSize) {
        List<UniqueAssignableWrapper> assignables = new ArrayList<>(assignablesList);
        for (int i = assignables.size(); i < oppositeListSize; i++) {
            assignables.add(new UniqueAssignableWrapper(new FictiveAssignable()));
        }
        return assignables;
    }

    /**
     * Get the assignments graph by using duplicated list of left assignables and right assignables. It uses
     * {@link #getAssignablesScore(UniqueAssignableWrapper, UniqueAssignableWrapper)} to compute score between two vertices.
     *
     * @param leftAssignables  the duplicated list of left assignables
     * @param rightAssignables the duplicated list of right assignables
     * @return the assignments graph
     */
    private GrapheNonOrienteValue<UniqueAssignableWrapper> getAssignmentGraph(List<UniqueAssignableWrapper> leftAssignables,
                                                                              List<UniqueAssignableWrapper> rightAssignables) {
        GrapheNonOrienteValue<UniqueAssignableWrapper> graph = new GrapheNonOrienteValue<>();

        for (UniqueAssignableWrapper assignable : leftAssignables) {
            graph.ajouterSommet(assignable);
        }

        for (UniqueAssignableWrapper assignable : rightAssignables) {
            graph.ajouterSommet(assignable);
        }

        for (UniqueAssignableWrapper leftAssignable : leftAssignables) {
            for (UniqueAssignableWrapper rightAssignable : rightAssignables) {
                double score = this.getAssignablesScore(leftAssignable, rightAssignable);
                graph.ajouterArete(leftAssignable, rightAssignable, score);
            }
        }
        return graph;
    }

    /**
     * Get the existing assignment between the given left and right assignables. Return null if it does not exist.
     *
     * @param leftAssignable  the assignable on the left side of the assignment
     * @param rightAssignable the assignable on the right side of the assignment
     * @return the existing assignment or {@code null} if it does not exist
     */
    private Assignment<E1, E2> getExistingAssignment(IAssignable leftAssignable, IAssignable rightAssignable) {
        for (Assignment<E1, E2> assignment : this.existingAssignments) {
            if (assignment.contains(leftAssignable, rightAssignable)) {
                return assignment;
            }
        }
        return null;
    }

    /**
     * Get the score calculation type for the given assignables depending on this cases:
     * <ol>
     * <li>One of the assignables is fictive, the score calculation needs to use the worst score possible</li>
     * <li>There is not assignment with the two assignables, the score calculation needs to compute the score</li>
     * <li>There is already an effective assignment between the two assignables, the score calculation needs to use the best score possible to fix the
     * assignment</li>
     * <li>There is already an assignment that is forbidden, the score calculation needs to compute the wort score possible</li>
     * </ol>
     *
     * @param leftAssignable  the assignable on the left side of the assignment
     * @param rightAssignable the assignable on the right side of the assignment
     * @return {@link AssignmentType#FORBIDDEN} in case number 1 and 4, {@link AssignmentType#FORCED} in case number 2, {@link AssignmentType#COMPUTED} in case
     * number 3
     */
    private AssignmentType getScoreCalculationType(IAssignable leftAssignable, IAssignable rightAssignable) {
        if (leftAssignable.isFictive() || rightAssignable.isFictive())
            return AssignmentType.FORBIDDEN;

        Assignment<E1, E2> assignment = this.getExistingAssignment(leftAssignable, rightAssignable);
        if (assignment == null) {
            return AssignmentType.COMPUTED;
        } else if (assignment.isEffective()) {
            return AssignmentType.FORCED;
        }

        return AssignmentType.FORBIDDEN;
    }

    /**
     * Get the score of two assignables and use the left assignable duplications counter to add a score malus.
     *
     * @param leftAssignable  the assignable on the left side of the assignment
     * @param rightAssignable the assignable on the right side of the assignment
     * @return the score of the two assignables
     */
    private double getAssignablesScore(UniqueAssignableWrapper leftAssignable, UniqueAssignableWrapper rightAssignable) {

        AssignmentType scoreCalculationType = this.getScoreCalculationType(leftAssignable.getAssignable(), rightAssignable.getAssignable());

        ScoreCalculator sc = new ScoreCalculator(leftAssignable, rightAssignable, this.ressourceName);

        return sc.computeOverallScore(scoreCalculationType, leftAssignable.getNumberOfDuplications() - 1);
    }

}
