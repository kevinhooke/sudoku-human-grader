package kh.sudokugrader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kh.sudokugrader.exception.SolutionGridNotInitializedException;

/**
 * Sudoku grader. Uses human solving techniques to determine the difficulty of a puzzle.
 * 
 * @author kevinhooke
 *
 */

//TODO: this needs to be refactored to a main app and support classes, it's grown too large

public class SudokuGraderApp {

    //private static Logger LOG = Logger.getLogger("SudokuGraderApp");
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static final Set<Integer> allowedValues = new HashSet<>(
            Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));

    // example grid to solve
    // 1
//    private int[][] startingSudokuGrid = { { 0, 0, 0, 8, 1, 0, 6, 7, 0 }, { 0, 0, 7, 4, 9, 0, 2, 0, 8 },
//            { 0, 6, 0, 0, 5, 0, 1, 0, 4 }, { 1, 0, 0, 0, 0, 3, 9, 0, 0 }, { 4, 0, 0, 0, 8, 0, 0, 0, 7 },
//            { 0, 0, 6, 9, 0, 0, 0, 0, 3 }, { 9, 0, 2, 0, 3, 0, 0, 6, 0 }, { 6, 0, 1, 0, 7, 4, 3, 0, 0 },
//            { 0, 3, 4, 0, 6, 9, 0, 0, 0 } };


     private int[][] startingSudokuGrid = {
     { 5, 0, 8, 4, 0, 0, 7, 0, 0 },
     { 0, 0, 0, 0, 0, 0, 8, 1, 9 },
     { 1, 0, 3, 0, 0, 6, 4, 0, 0 },
     { 8, 0, 0, 9, 1, 0, 0, 0, 3 },
     { 0, 0, 9, 0, 6, 0, 2, 0, 0 },
     { 6, 0, 0, 0, 8, 3, 0, 0, 4 },
     { 0, 0, 5, 6, 0, 0, 1, 0, 7 },
     { 9, 4, 6, 0, 0, 0, 0, 0, 0 },
     { 0, 0, 1, 0, 0, 9, 6, 0, 2 }
     };

    // list (rows) of list of list of integers
    // eg { {1, 2, 3}, {1, 2, 3}, {1, 2, 3}, ... },
    // { {1, 2, 3}, {1, 2, 3}, {1, 2, 3}, ... },
    // ... }
    private List<List<List<Integer>>> solutionGrid = new ArrayList<>();

    
    private PuzzleDifficulty difficulty = new PuzzleDifficulty();
    private PuzzlePrinter printer = new PuzzlePrinter();

    //tracks pairs already identified in a row
    private Map<Integer, List<List<Integer>>> identifiedPairsInRow = new HashMap<>();
    
    /**
     * Default constructor.
     */
    public SudokuGraderApp() {
    }

    public static void main(String[] args) {

        SudokuGraderApp app = new SudokuGraderApp();
         
        app.populateSolutionGridWithStartingPosition();

        long startTime = System.currentTimeMillis();
        app.gradePuzzle();
        long endTime = System.currentTimeMillis();
        app.printSolutionGridWithBorders();
        LOGGER.info("Complete!");
        LOGGER.info("Elapsed time: " + (endTime - startTime));
    }

    void printSolutionGridWithBorders() {
        int rowIndex = 0;
        int colIndex = 0;
        StringBuilder sb = new StringBuilder(120);
        sb.append("\n+-------+-------+-------+\n");
        for (List<List<Integer>> row : this.solutionGrid) {
            colIndex = 0;
            sb.append("| ");
            for (List<Integer> currentRow : row) {
                for (Integer value : currentRow) {
                    sb.append(value + " ");
                    if (colIndex == 2 || colIndex == 5) {
                        sb.append("| ");
                    }
                    colIndex++;
                }
            }
            if (rowIndex == 2 || rowIndex == 5) {
                sb.append("|");
                sb.append("\n+-------+-------+-------+\n");
            } else {
                sb.append("|\n");
            }
            rowIndex++;
            //System.out.println();
        }
        sb.append("+-------+-------+-------+\n");
        LOGGER.info(sb.toString());
    }

    public void populateSolutionGridWithStartingPosition() {
        for (int row = 0; row < 9; row++) {
            List<List<Integer>> currentRow = new ArrayList<>();

            for (int col = 0; col < 9; col++) {
                List<Integer> currentCellPossibleNumberList = new ArrayList<>();
                int value = startingSudokuGrid[row][col];
                // if we have starting number for cell, add it to solution grid,
                // otherwise add an empty list for now - we'll come back and
                // populate each empty list with possible numbers when we start
                // solving
                if (value > 0) {
                    currentCellPossibleNumberList.add(value);
                    this.difficulty.incrementInitialGivens();
                }
                currentRow.add(currentCellPossibleNumberList);
            }
            solutionGrid.add(currentRow);
        }
    }

    /**
     * Grades the puzzle complexity by counting each of the techniques needed to solve the
     * puzzle. Loops through squares first, inserting candidate values
     * into each empty cell.
     * 
     * Counts:
     * - naked singles
     * - hidden singles
     * - TODO
     */
    public PuzzleDifficulty gradePuzzle() {
        
        //check starting grid was initialized
        if(this.solutionGrid == null || this.solutionGrid.size() == 0) {
            throw new SolutionGridNotInitializedException();
        }
        
        int passesThroughGridCount = 0;

        // pass 1 - loop through squares and populate empty cells with lists of all candidate values
        this.populateCandidateValues();

        this.printer.printGridWithBorders(startingSudokuGrid);

        // pass 2 - loop through individual cells and apply human solving techniques to determine complexity
        // continues until no more solutions are found
        // repeat these steps twice to see if the earlier steps find any additional solutions after
        // later approaches have run
        //TODO: this should really use a boolean check like the inner loops
        //Note: some easy puzzles can be solved with only 1 or 2 passes, some take 3 or more
        //TODO: revisit number of passes for harder puzzles
        int unsolvedCells = 0;
        for(int outerSolverLoop = 0; outerSolverLoop < 20; outerSolverLoop++) {
            boolean solvedValuesOnAtLeastOnePass = true;
            
            while (solvedValuesOnAtLeastOnePass) {
                boolean replacedOnLastIteration = false;
                for (int col = 0; col < 9; col++) {
                    for (int row = 0; row < 9; row++) {
                        
                        //TODO need to update difficulty here
                        //TODO: 2nd pass through hard puzzle example gets blank cells on row 4
                        boolean solvedValuesThisPass = this.findNakedSinglesInCandidates(row, col);
                        if (solvedValuesThisPass) {
                            replacedOnLastIteration = solvedValuesThisPass;
                        }
                    }
                    if (!replacedOnLastIteration) {
                        solvedValuesOnAtLeastOnePass = false;
                    }
                }
                passesThroughGridCount++;
                this.printer.printSolutionGrid(this.solutionGrid);
            }
            
            //did we find a solution? if not try next approach
            unsolvedCells = this.checkForCompleteSolution();
            if(unsolvedCells > 0) {
                solvedValuesOnAtLeastOnePass = true;
                
                while (solvedValuesOnAtLeastOnePass) {
                    boolean replacedOnLastIteration = false;
                    for (int col = 0; col < 9; col++) {
                        for (int row = 0; row < 9; row++) {
                            
                            boolean solvedValuesThisPass = this.findHiddenSinglesInCandidates(row, col);
                            if (solvedValuesThisPass) {
                                replacedOnLastIteration = solvedValuesThisPass;
                            }
                        }
                        if (!replacedOnLastIteration) {
                            solvedValuesOnAtLeastOnePass = false;
                        }
                    }
                    passesThroughGridCount++;
                    this.printer.printSolutionGrid(this.solutionGrid);
                }
                
            }
            
            //
            //TODO next approach - add hidden pairs: this is in progress
            //
            //did we find a solution? if not try next approach
            unsolvedCells = this.checkForCompleteSolution();
            if(unsolvedCells > 0) {
                solvedValuesOnAtLeastOnePass = true;
                
                while (solvedValuesOnAtLeastOnePass) {
                    boolean replacedOnLastIteration = false;
                    //TODO: try findPairsInCandidateRows first, add columns and squares later
                    //TODO: findPairsInCandidateCols
                    //TODO: findPairsInCandidateSquares
                    //TODO test first row only
                    for (int row = 0; row < 3; row++) {
                    //for (int row = 0; row < 9; row++) {
                        boolean solvedValuesThisPass = this.findPairsInCandidateRows(row, 2);
                        if (solvedValuesThisPass) {
                            replacedOnLastIteration = solvedValuesThisPass;
                        }
                    }
                    if (!replacedOnLastIteration) {
                        solvedValuesOnAtLeastOnePass = false;
                    }

                    passesThroughGridCount++;
                    this.printer.printSolutionGrid(this.solutionGrid);
                }
                
            }
        }
        
        if(unsolvedCells > 0) {
            this.difficulty.setPuzzleSolved(false);
            LOGGER.info("Puzzle solved: NO");
        }
        else {
            
            //TODO: this needs to call the dlx solver to actually check it's a valid solution
            
            this.difficulty.setPuzzleSolved(true);
            LOGGER.info("Puzzle solved: Yes");
        }
        
        //TODO: this needs to be moved to a return from this method
        
        LOGGER.info("Initial givens: " + this.difficulty.getInitialGivens());
        LOGGER.info("Passes through grid: " + passesThroughGridCount);
        LOGGER.info("Naked singles found: " + this.difficulty.getNakedSingleCount());
        LOGGER.info("Hidden singles found: " + this.difficulty.getHiddenSingleCount());
        LOGGER.info("Naked pairs found: " + this.difficulty.getNakedPairsCount());
        LOGGER.info("Hidden pairs found: " + "not implemented yet");
        
        return difficulty;
    }

    /**
     * Finds pairs in a given row, and removes anywhere in the same row where those
     * pair values exist in the same row.
     *  
     * @param row the row to search for pairs
     * @param numberOfCandidates 2 = pairs, 3 = triples, etc
     * @return true if a pair found and values removed as candates elsewhere in the row
     */
    boolean findPairsInCandidateRows(int row, int numberOfCandidates) {
        boolean result = false;
        
        //get row
        List<List<Integer>> values = this.getValuesInRow(row);
        //get locations of pairs
        // for example:
        // [1,2] : [0, 1] // the pair [1,2] exists in list 0 and list 1
        // [2,3] : [0]
        // [4,1] : [1]
        // [4,2] : [1]
        Map<List<Integer>, List<Integer>> locationOfPairs = this.findListsContainingPairs(values);
        result = this.removeCandidatesWherePairExistsTwice(row, locationOfPairs);
        return result;
    }

    private boolean removeCandidatesWherePairExistsTwice(int row, Map<List<Integer>, List<Integer>> locationOfPairs) {
        boolean result = false;
        System.out.println("Checking row: " + row);
        //get candidates in current row
        List<List<Integer>> rowCandidates = this.getValuesInRow(row);
        
        //find any list where it occurs in 2 locations
        for(Map.Entry<List<Integer>, List<Integer>> entry : locationOfPairs.entrySet()) {
            System.out.println("pair [" + entry.getKey().toString() + " locations [" + entry.getValue().toString());
            
            //does this pair exist in 2 cells where the pair is the only 2 candidates in that cell
            //how many cells contain just 2 values?
            //TODO this can probably be simplified to use Streams
            int containsOnlyThisPair = 0;
            List<Integer> cellsContainingOnlyThisPair = new ArrayList<>();
            for(Integer index : entry.getValue()) {
                System.out.println("    column: " + index + " : candidates: " + rowCandidates.get(index).size());
                //TODO test bug with specific puzzle with pairs here
                if(rowCandidates.get(index).containsAll(Arrays.asList(6,7))) {
                    System.out.println("Found 6,7");
                }
                if(rowCandidates.get(index).size() == 2) {
                    containsOnlyThisPair++;
                    cellsContainingOnlyThisPair.add(index);
                }
            }
            if(containsOnlyThisPair == 2) {
                System.out.println("    Match - cells containing only this pair: " + cellsContainingOnlyThisPair);                
                
                result = this.removePairFromCandidatesInRowWherePairExists(row, entry.getKey());

                //add new pair to tracked list for this row
                List<List<Integer>> knownPairsInRow = this.identifiedPairsInRow.get(row);
                if(knownPairsInRow == null) {
                    knownPairsInRow = new ArrayList<>();
                }
                knownPairsInRow.add(entry.getKey());
                this.identifiedPairsInRow.put(row, knownPairsInRow);

                this.difficulty.setNakedPairsCount(this.difficulty.getNakedPairsCount() +1);
            }
            else {
                System.out.println("    No match");
            }
 
        }
        
        return result;
    }

    //TODO unit test
    boolean doesCellContainPreviouslyIndetifiedPair(int row, List<Integer> valuesInCell) {
        boolean valueIsInAKnownPair = false;
        
        List<List<Integer>> knownPairsInRow = this.identifiedPairsInRow.get(row);
        if(knownPairsInRow != null && !knownPairsInRow.isEmpty()) {
            for(Integer value : valuesInCell) {
                for(List<Integer> knownPair : knownPairsInRow) {
                    valueIsInAKnownPair = knownPair.contains(value);
                    if(valueIsInAKnownPair) {
                        break;
                    }
                }
                if(valueIsInAKnownPair) {
                    break;
                }
            }
        }
        return valueIsInAKnownPair;
    }

    /**
     * For each value in a List of List of ints, find index of the list where the int exists.
     * 
     * For example, given [ [1,2,3], [3,4,5], [3,6,7] ] 
     * 1 exists in list 0
     * 2 exists in list 0
     * 3 exists in 0, 1, 2
     * 4 exists in 1
     * etc
     * 
     */
    Map<Integer, List<Integer>> findIndexesOfListWhereEachIntExists(List<List<Integer>> list){
        
        Map<Integer, List<Integer>> results = new HashMap<>();
        
        //get list of all values
        List<Integer> values = list.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        
        //for each of the values, get list index within list where it exists
        values.forEach(
                
                value -> {
                    List<Integer> listsContainingValue = findIndexesOfListsWhereValueExists(value, list);
                    if(results.get(value) == null) {
                        results.put(value, listsContainingValue);
                    }
                }
        );
        
        return results;
    }

    /**
     * Given a list of list of ints, find all the indexes of the lists where the passed value exists
     */
    public List<Integer> findIndexesOfListsWhereValueExists(int value, List<List<Integer>> list){
        
        List<List<Integer>> listsContainingInt = list.stream()
                .filter(e -> e.contains(Integer.valueOf(value)))
                .collect(Collectors.toList());
        
        List<Integer> listIndexes = new ArrayList<>();
        
        //get indexes where each list exists
        for(List<Integer> listContainingValue : listsContainingInt) {
            listIndexes.add(list.indexOf(listContainingValue));
        }
        return listIndexes;
    }

    
   /**
    * Finds combinations of pairs of values in a list. Note that [1,2] and [2,1] are considered
    * the same and we're only looking for combinations, not permutations (not all possible combinations
    * where order is relevant).
    * 
    * 
    * 
    * e.g. given [1,2,3] returns:
    * [1,2], [1,3], [2,3]
    * @return
    */
   //TODO: refactor to use Streams
   public List<List<Integer>> findCombinationsOfPairsInList(List<Integer> values){
       List<List<Integer>> result = new ArrayList<>();
       
           for(int startingInt = 0; startingInt < values.size(); startingInt++) {

               for(int secondValue = startingInt + 1 ; secondValue < values.size(); secondValue++) {
                   List<Integer> tempOneComboList = new ArrayList<>();
                   tempOneComboList.add(values.get(startingInt));
                   tempOneComboList.add(values.get(secondValue));
                   result.add(tempOneComboList);
               }
               
           }
       
       return result;
   }
   
   /**
    * Given a list of list of ints, find combinations in each of the lists.
    * 
    * Given: [ [1,2,3], [4,5,6], [7,8,9] ]
    * Returns:
    * [ [[1,2], [1,3], [2,3]], [[4,5], [4,6], [5,6]], [[7,8], [7,9], [8,9]] ]
    * @param list of list of ints
    * @return list of list of lists
    */
   List<List<List<Integer>>> findCombinationsOfPairsInListOfLists(List<List<Integer>> values){
       List<List<List<Integer>>> result = new ArrayList<>();
       
       values.forEach(listOfInts -> {
           List<List<Integer>> combosInList = this.findCombinationsOfPairsInList(listOfInts);
           result.add(combosInList);
       });
       
       return result;
   }
   
    /**
     * Given a list of list of ints, find all the indexes of the lists that contains the passed list.
     * 
     */
    List<Integer> findIndexesOfListsThatContainList(List<Integer> listToFind, List<List<Integer>> list){
        
        List<Integer> listIndexes =
                IntStream.range(0, list.size()).boxed()
                        .filter(i -> list.get(i).containsAll(listToFind))
                        .collect(Collectors.toList());
        
        return listIndexes;
    }
    
    /**
     * Find indexes of lists that contain a list.
     * 
     * Given a list of list of ints like: [ [1,2,3], [4,1,2], [7,8,9] ]
     * finds the indexes of where each pair of values exists in each of the other lists.
     * 
     * For example:
     * Pair [1,2] exists in list indexes 0 and 1, returned as a list [0, 1]
     * Pair [2,3] only exists in [0]
     * Similar for all other pairs, then only exist in one of the lists.
     *  
     * @param values
     * @return map of pairs with a list of the indexes of the lists where that pair exists,
     * for example:
     * [1,2] : [0, 1] // the pair [1,2] exists in list 0 and list 1
     * [2,3] : [0]
     * [4,1] : [1]
     * [4,2] : [1]
     * etc
     */
    Map<List<Integer>, List<Integer>> findListsContainingPairs(List<List<Integer>> values){
        Map<List<Integer>, List<Integer>> result = new HashMap<>();
        
        
        // Given: [ [1,2,3], [4,5,6], [7,8,9] ]
        List<List<List<Integer>>> listOfCombinations = this.findCombinationsOfPairsInListOfLists(values);
        //Returns: [
        //           [[1,2], [1,3], [2,3]], 
        //           [[4,5], [4,6], [5,6]],
        //           [[7,8], [7,9], [8,9]]
        //         ]

        
        listOfCombinations.stream()
            .forEach(listOfPairs -> listOfPairs.forEach(pairInList -> {
                
                List<Integer> listsContaining = findIndexesOfListsThatContainList(pairInList, values);
                List<Integer> listsContainingSoFar = result.get(pairInList);
                if(listsContainingSoFar == null) {
                    //add new entry for this list
                    result.put(pairInList, listsContaining);                    
                    //TODO continue here
                }
                else {
                    //this pair is already in the list, add the next set of indexes were it exists
                    //add new indexes that we've not already found
                    for(Integer nextIndex : listsContaining) {
                        if(!listsContainingSoFar.contains(nextIndex)){
                            listsContainingSoFar.add(nextIndex);
                            result.put(pairInList, listsContainingSoFar);
                        }
                    }
                }
            }));
        
        
        return result;
    }
    
    /**
     * Populates the grid with all possible candidate values. This must be called prior
     * to starting the solver.
     */
    void populateCandidateValues() {
        for (int rowSquare = 0; rowSquare < 3; rowSquare++) {
            for (int colSquare = 0; colSquare < 3; colSquare++) {
                Set<Integer> singleValuesInSquare = this.getSingleValuesInSquare(rowSquare, colSquare);
                //this.printValuesSet(singleValuesInSquare);

                Set<Integer> candidateValues = this.getCandidateValues(singleValuesInSquare);
                //System.out.print("Candidate values: ");
                //this.printValuesSet(candidateValues);
                // insert candidate values into every blank cell in this square
                this.updateValuesInSquare(rowSquare, colSquare, new ArrayList<Integer>(candidateValues));
            }
        }
    }

    /**
     * 
     * @param row
     * @param col
     * @return
     */
    boolean findHiddenSinglesInCandidates(int row, int col) {
        boolean valuesReplaced = false;
        int startingUnsolvedCells = this.checkForCompleteSolution();
        
        //find hidden singles in square
        Set<Integer> hiddenSinglesInSquare = this.findHiddenSinglesSquareByRowCol(row, col);
        //remove other candidates in cell for each of the hidden singles in a square
        boolean valuesRemovedInSquare = false;
        if(hiddenSinglesInSquare.size() > 0)
        {
            valuesRemovedInSquare = this.removeOtherCandidatesInSquareWhereHiddenSinglesExist(row, col, hiddenSinglesInSquare);
        }
        
        // find hidden singles in row
        Set<Integer> hiddenSinglesInRow = this.findHiddenSinglesInRow(row);
        //remove other candidates in row - this leaves a single candidate in cell which could uncover other cells to be solved
        boolean valuesRemovedInCell = false;
        if(hiddenSinglesInRow.size() > 0) {
            valuesRemovedInCell = this.removeOtherCandidatesInCellWhereHiddenSingleExists(row, hiddenSinglesInRow);
        }
        
        // get hidden singles in col
        Set<Integer> hiddenSinglesInCol = this.findHiddenSinglesInColumn(col);
        boolean valuesRemovedInCol = false;
        //remove other candidates in col
        if(hiddenSinglesInCol.size() > 0) {
            valuesRemovedInCol = this.removeOtherCandidatesInColWhereHiddenSinglesExist(row, col, hiddenSinglesInCol);
        }
        
        if(valuesRemovedInSquare || valuesRemovedInCell || valuesRemovedInCol) {
            valuesReplaced = true;
        }
        
        int endUnsolvedCells = this.checkForCompleteSolution();
        int solvedCells = startingUnsolvedCells - endUnsolvedCells;
        if(solvedCells > 0) {
            this.difficulty.setHiddenSingleCount(this.difficulty.getHiddenSingleCount() + solvedCells);
        }
        return valuesReplaced;
    }

    //TODO test this
    boolean removeOtherCandidatesInColWhereHiddenSinglesExist(int row, int col, Set<Integer> hiddenSinglesInCol) {
        boolean valuesRemoved = false;
        
        List<List<Integer>> valuesInCol = this.getValuesInCol(col);

        for (Integer value : hiddenSinglesInCol) {
            int compareColumn = 0;
            for(List<Integer> valuesInCell : valuesInCol) {
                //if this cell contains this candidate and there's other values in the cell, delete all other values and keep just this value
                if(valuesInCell.contains(value) && valuesInCell.size() > 1) {
                    valuesInCell.clear();
                    valuesInCell.add(value);
                    if(valuesInCell.size() == 0) {
                        throw new InvalidCandidateRemovalException();
                    }
                    valuesInCol.set(compareColumn, valuesInCell);
                    this.setColumnInSolutionGrid(col, valuesInCol);
                    valuesRemoved = true;
                }
                compareColumn++;
            }
        }
        return valuesRemoved;
    }

    //TODO
    boolean findLockedCandidates(int row, int col) {
        boolean result = false;
                
        return result;
    }
    
    /**
     * Sets new values in a column in the solution grid.
     * 
     * @param col the column to set the new values into
     * @param valuesInCol new values for the specified column
     */
    void setColumnInSolutionGrid(int col, List<List<Integer>> newValuesInCol) {

        for(int currentRow = 0; currentRow < 9; currentRow++) {
            List<List<Integer>> currentRowValues = this.solutionGrid.get(currentRow);
            
            //replace column values with new values
            currentRowValues.set(col, newValuesInCol.get(currentRow));
        }
    }

    //TODO test this
    boolean removeOtherCandidatesInSquareWhereHiddenSinglesExist(int row, int col,
            Set<Integer> hiddenSinglesInSquare) {
        boolean valuesRemoved = false;
        
        int squareRow = this.getSquareRowFromRow(row);
        int squareCol = this.getSquareColFromCol(col);
        
        // iterate 3 rows for square
        for (int rowOffset = squareRow * 3; rowOffset < (squareRow * 3) + 3; rowOffset++) {
            
            //TODO test is this fixed: IndexOutOfBoundsException here when passed 3? (was missing getSquareFrom...())
            
            List<List<Integer>> currentRow = getValuesInRow(rowOffset);

            // iterate 3 cells for current row
            for (int cellOffset = squareCol * 3; cellOffset < (squareCol * 3) + 3; cellOffset++) {
                List<Integer> cellContent = currentRow.get(cellOffset);
                
                //remove other candidates in cell if this cell contains a hidden single
                //if any of the values in this cell are in the hiddenSinglesSet, set the only
                //candidate in this cell to the hidden single and remove all the other values
                // - the first identified candidate value in the hidden singles list has to be
                //the only hidden single for this cell so we can break after finding the first one
                for(Integer candidateValue : cellContent) {
                    //only remove the other candidates if there's more than 2 candidates in the cell
                    if(hiddenSinglesInSquare.size() > 1 && hiddenSinglesInSquare.contains(candidateValue)) {
                        cellContent.clear();
                        cellContent = Arrays.asList(candidateValue);
                        if(cellContent.size() == 0) {
                            throw new InvalidCandidateRemovalException();
                        }
                        currentRow.set(cellOffset, cellContent);
                        valuesRemoved = true;
                        break;
                    }
                }
                        
             
            }
        }

        
        return valuesRemoved;
    }

    /**
     * For each identified hidden single in this row, find the cell where each exists and
     * remove the other candidates in that same cell.
     * 
     * @param hiddenSinglesInRow
     */
    boolean removeOtherCandidatesInCellWhereHiddenSingleExists(int row, Set<Integer> hiddenSinglesInRow) {
        boolean valuesRemoved = false;
        
        List<List<Integer>> valuesInRow = this.getValuesInRow(row);

        for (Integer valueToKeep : hiddenSinglesInRow) {
            int compareColumn = 0;
            for(List<Integer> valuesInCell : valuesInRow) {
                //if this cell contains this candidate, delete all other values and keep just this value
                if(valuesInCell.contains(valueToKeep)) {
                    valuesInCell.clear();
                    valuesInCell.add(valueToKeep);
                    if(valuesInCell.size() == 0) {
                        throw new InvalidCandidateRemovalException();
                    }
                    valuesInRow.set(compareColumn, valuesInCell);
                    this.solutionGrid.set(row, valuesInRow);
                    valuesRemoved = true;
                }
                compareColumn++;
            }
        }
        return valuesRemoved;
    }
    
    /**
     * For each identified naked pair in this row, find the cells in the row where the
     * any value in the pair exists with other candidates and remove the pair values.
     * 
     * @param pairInRow
     */
    boolean removePairFromCandidatesInRowWherePairExists(int row, List<Integer> pairInRow) {
        boolean valuesRemoved = false;
        
        List<List<Integer>> valuesInRow = this.getValuesInRow(row);

            int compareColumn = 0;
            for(List<Integer> valuesInCell : valuesInRow) {
                //if cell contains only the pair, skip this cell
                if(valuesInCell.containsAll(pairInRow) && valuesInCell.size() == pairInRow.size()) {
                    //skip
                }
                //if this cell contains the candidate with other values, remove the pair and keep the other candidates
                //if(valuesInCell.containsAll(pairInRow) && valuesInCell.size() > pairInRow.size()) {
                else{
                    
                    //does the current cell contain only a previously identified pair? if so we can't remove these
                    //values so we'll skip this cell
                    if(this.doesCellContainPreviouslyIndetifiedPair(row, valuesInCell)){
                        LOGGER.debug("Currrent cell contains identified pair, cannot remove values: [" + valuesInCell + "]");
                    }
                    else {
                        LOGGER.debug("Removing pair [" + pairInRow + "] from row " + row + "in cell [" + valuesInCell + "]");
                        boolean removed = valuesInCell.removeAll(pairInRow);
                        if(removed) {
                            valuesRemoved = true;
                        }
                        if(valuesInCell.size() == 0) {
                            throw new InvalidCandidateRemovalException();
                        }
                        valuesInRow.set(compareColumn, valuesInCell);
                        this.solutionGrid.set(row, valuesInRow);
                    }
                }
                compareColumn++;
            }
        return valuesRemoved;
    }
    
    /**
     * 
     * @param col
     * @return
     */
    Set<Integer> findHiddenSinglesInColumn(int col) {
        int startingUnsolvedCells = this.checkForCompleteSolution();
        Set<Integer> hiddenSingles = new HashSet<Integer>();
        List<List<Integer>> valuesInCol = this.getValuesInCol(col);
        for (int row = 0; row < 9; row++){
            
            List<Integer> valuesInRow = valuesInCol.get(row);
            
            //if this cell only contains a single value then skip because this could be a naked single and we'll check it
            //using the naked singles approach
            if(valuesInRow.size() > 1 ) {
                //for each of the candidate values in a cell, check if they exist in any of the other
                //cells - if they don't then this is a naked single
                for(Integer currentValueInCol : valuesInRow) {
                  //does this current value exist in any of the other rows in this column?
                    //exclude the current row
                    int rowToCompare = 0;
                    List<Integer> candidatesInAllRowsInCol = new ArrayList<>();
                    for(List<Integer> candidatesInRow : valuesInCol) {
                        
                        //if we're not comparing the same cell and if this cell contains more than a single value 
                        //(which would be a naked single)
                        if(rowToCompare != row) {
                            candidatesInAllRowsInCol.addAll(candidatesInRow);
                        }
                        rowToCompare++;
                    }
                    //if current value does not exist in any of the other cells in this column, save it
                    if (!candidatesInAllRowsInCol.contains(currentValueInCol)) {
                        hiddenSingles.add(currentValueInCol);
                    }
    
                }
            }
        }
        int endUnsolvedCells = this.checkForCompleteSolution();
        int solvedCells = startingUnsolvedCells - endUnsolvedCells;
        if(solvedCells > 0) {
            this.difficulty.setHiddenSingleCount(this.difficulty.getHiddenSingleCount() + solvedCells);
        }
        return hiddenSingles;
    }

    /**
     * Finds hidden singles in current row. A hidden single is a single candidate
     * value that only exists in a single cell and nowhere else in that
     * current square/row/col.
     * 
     * @param row
     * @return a list of found naked values in the candidates for this row
     */
    Set<Integer> findHiddenSinglesInRow(int row) {
        int startingUnsolvedCells = this.checkForCompleteSolution();
        Set<Integer> hiddenSingles = new HashSet<Integer>();
        List<List<Integer>> valuesInRow = this.getValuesInRow(row);
        for (int col = 0; col < 9; col++) {
            List<Integer> valuesInCol = valuesInRow.get(col);
            
            //if this cell only contains a single value then skip because this could be a naked single and we'll check it
            //using the nakend singles approach
            if(valuesInCol.size() > 1 ) {
                //for each of the candidate values in a cell, check if they exist in any of the other
                //cells - if they don't then this is a naked single
                for(Integer currentValueInCol : valuesInCol) {
                  //does this current value exist in any of the other columns?
                    //exclude the current column
                    int colToCompare = 0;
                    
                    //TODO: this loop is the same for checking each candidate and can be moved outside this loop
                    List<Integer> candidatesInAllColsInrow = new ArrayList<>();
                    for(List<Integer> candidatesInCol : valuesInRow) {
                        
                        //if we're not comparing the same cell and if this cell contains more than a single value 
                        //(which would be a naked single)
                        if(colToCompare != col) {
                            candidatesInAllColsInrow.addAll(candidatesInCol);
                        }
                        colToCompare++;
                    }
                    //if current value does not exist in any of the other cells in this row, save it
                    if (!candidatesInAllColsInrow.contains(currentValueInCol)) {
                        hiddenSingles.add(currentValueInCol);
                    }
    
                }
            }
        }
        int endUnsolvedCells = this.checkForCompleteSolution();
        int solvedCells = startingUnsolvedCells - endUnsolvedCells;
        if(solvedCells > 0) {
            this.difficulty.setHiddenSingleCount(this.difficulty.getHiddenSingleCount() + solvedCells);
        }
        return hiddenSingles;
    }

    // TODO ** HERE ** finish this
    Set<Integer> findHiddenSinglesSquareByRowCol(int row, int col) {

        int squareRow = this.getSquareRowFromRow(row);
        int squareCol = this.getSquareColFromCol(col);
        
        Set<Integer> hiddenSingleValuesInSquare = this.getHiddenSingleValuesInSquare(squareRow, squareCol);
        
        return hiddenSingleValuesInSquare;
    }

    /**
     * Each cell must have 1 final candidate value selected for the solution. If
     * there are any cells with > 1 candidate then the puzzle was not solved with the
     * attempted solving techniques.
     * 
     * @return
     */
    private int checkForCompleteSolution() {
        boolean solutionFound = true;
        int numberOfUnsolvedCells = 0;
        
        for (List<List<Integer>> row : this.solutionGrid) {
            for (List<Integer> currentCell : row) {

                if (currentCell.size() > 1) {
                    solutionFound = false;
                    numberOfUnsolvedCells++;
                }

            }
        }

        LOGGER.trace("Unsolved cells: " + numberOfUnsolvedCells);

        
        return numberOfUnsolvedCells;
    }

    /**
     * Finds any 'naked single' values in possible candidates. If any are found they
     * are selected as a solution for that cell.
     * 
     * //TODO: it's possible this can be refactored to find pairs and other combinations too
     * 
     * @param row
     * @param col
     * @return true if any naked singles were found
     */
    private boolean findNakedSinglesInCandidates(int row, int col) {
        boolean valuesReplaced = false;
        int startingUnsolvedCells = this.checkForCompleteSolution();
        
        Set<Integer> singleValuesInRow = this.findSingleValuesInRow(row);
        Set<Integer> singleValuesInCol = this.findSingleValuesInColumn(col);
        Set<Integer> singleValuesInSquare = this.findSingleValuesInSquareByRowCol(row, col);
        List<Integer> valuesInCell = this.getValueInCell(row, col);
        
        // replace candidates in this cell if they appear as a naked single in the same row, column or square,
        // but only if this cell isn't a naked single itself
        if (valuesInCell.size() > 1) {
            if(valuesInCell.size() == singleValuesInRow.size() && valuesInCell.containsAll(singleValuesInRow)) {
                throw new InvalidCandidateRemovalException();
            }
            boolean valuesReplacedInRow = valuesInCell.removeAll(singleValuesInRow);
            if(valuesInCell.size() == 0) {
                //TODO test removing this - did this used to work even though it's making invalid removals?
                //throw new InvalidCandidateRemovalException();
            }
            
            if(valuesInCell.size() == singleValuesInCol.size() && valuesInCell.containsAll(singleValuesInCol)) {
                throw new InvalidCandidateRemovalException();
            }
            boolean valuesReplacedInCol = valuesInCell.removeAll(singleValuesInCol);
            if(valuesInCell.size() == 0) {
                //TODO test removing this - did this used to work even though it's making invalid removals?
                //throw new InvalidCandidateRemovalException();
            }
            
            if(valuesInCell.size() == singleValuesInSquare.size() && valuesInCell.containsAll(singleValuesInSquare)) {
                //TODO test removing this - did this used to work even though it's making invalid removals?
                //throw new InvalidCandidateRemovalException();
            }
            //TODO: bug at some point row1 col7 is empty here after this next removal for the hard pairs test
            boolean valuesReplacedInSquare = valuesInCell.removeAll(singleValuesInSquare);
            if(valuesInCell.size() == 0) {
                //TODO test removing this - did this used to work even though it's making invalid removals?
                //throw new InvalidCandidateRemovalException();
            }

            valuesReplaced = valuesReplacedInRow || valuesReplacedInCol || valuesReplacedInSquare;
            if (valuesReplaced) {
                List<List<Integer>> valuesInRow = this.getValuesInRow(row);
                valuesInRow.set(col, valuesInCell);
                this.solutionGrid.set(row, valuesInRow);
            }
        }
        
        int endUnsolvedCells = this.checkForCompleteSolution();
        this.difficulty.setNakedSingleCount(this.difficulty.getNakedSingleCount() 
                + (startingUnsolvedCells - endUnsolvedCells));
        return valuesReplaced;
    }

    /**
     * Retrieves set of single values in a column.
     * 
     * @param col
     *            column index to retrieve
     * @return Set of single values in the specified column.
     */
    Set<Integer> findSingleValuesInColumn(int col) {
        Set<Integer> singleValues = new HashSet<Integer>();
        for (int row = 0; row < 9; row++) {
            List<List<Integer>> valuesInRow = getValuesInRow(row);
            List<Integer> valuesInCell = valuesInRow.get(col);
            if (valuesInCell.size() == 1) {
                singleValues.addAll(valuesInCell);
            }
        }
        return singleValues;
    }

    /**
     * Retrieves set of single values in a row.
     * 
     * @param row
     * @return
     */
    Set<Integer> findSingleValuesInRow(int row) {
        Set<Integer> singleValues = new HashSet<Integer>();
        List<List<Integer>> valuesInRow = getValuesInRow(row);
        for (int col = 0; col < 9; col++) {
            List<Integer> valuesInCol = valuesInRow.get(col);
            if (valuesInCol.size() == 1) {
                singleValues.addAll(valuesInCol);
            }
        }
        return singleValues;
    }

    /**
     * Retrieves set of current values in a square, by iterating 3 rows from the
     * starting row, and 3 columns from the starting column.
     * 
     * Squares are referenced: first row: {0,0}, {0,1}, {0,2} second row: {1,0},
     * {1,1}, {1,2} etc
     * 
     * @param row
     * @param col
     * @return
     */
    Set<Integer> getSingleValuesInSquare(int row, int col) {
        Set<Integer> values = new HashSet<>();

        // iterate 3 rows for square
        for (int rowOffset = row * 3; rowOffset < (row * 3) + 3; rowOffset++) {
            List<List<Integer>> currentRow = getValuesInRow(rowOffset);

            // iterate 3 cells for current row
            for (int cellOffset = col * 3; cellOffset < (col * 3) + 3; cellOffset++) {
                List<Integer> cellContent = currentRow.get(cellOffset);
                // only collect cells where we have a single (final) value,
                // not a list of possible values
                if (cellContent.size() == 1) {
                    values.add(cellContent.get(0));
                }
            }
        }

        return values;
    }

    
    /**
     * Finds hidden singles in a square.
     * 
     * @param row
     * @param col
     * @return candidate values that are hidden singles in this square
     */
    Set<Integer> getHiddenSingleValuesInSquare(int row, int col) {
        Set<Integer> result = new HashSet<>();
        Map<Integer, Integer> candidateValueCounts = new HashMap<>();
        
        // iterate 3 rows for square
        for (int rowOffset = row * 3; rowOffset < (row * 3) + 3; rowOffset++) {
            List<List<Integer>> currentRow = getValuesInRow(rowOffset);

            // iterate 3 cells for current row
            for (int cellOffset = col * 3; cellOffset < (col * 3) + 3; cellOffset++) {
                List<Integer> cellContent = currentRow.get(cellOffset);
                
                //collect and hidden value:
                // - where it only appears once in the square
                // - and occurs in a cell with more than 1 other candidate in the same cell
                //map of 1 through 9, value is count of occurrences
                if(cellContent.size() > 1) {
                    for(Integer value : cellContent) {
                        //get current count in map for this value, and increment count by 1
                        Integer countForValue = candidateValueCounts.get(value);
                        if(countForValue == null) {
                            candidateValueCounts.put(value, Integer.valueOf(1));
                        }
                        else {
                            countForValue++;
                            candidateValueCounts.put(value, countForValue);
                        }
                            
                    }
                }
            }
        }

        //check for any candidate values where they occur only once and copy these to the results
        for(Entry<Integer, Integer> item : candidateValueCounts.entrySet()) {
            if(item.getValue() == 1) {
                //TODO check this
                result.add(item.getKey());
            }
        }
        
        return result;
    }
    
    
    /**
     * Updates empty values in a square. Checks for single values in the same
     * column first, then the same row, and removes them from the guessed
     * values.
     * 
     * @param squareRow
     * @param squareCol
     * @param missingValuesInSquare
     * @return
     */
    boolean updateValuesInSquare(int squareRow, int squareCol, List<Integer> missingValuesInSquare) {

        boolean replacedValuesOnThisPass = false;

        // iterate 3 rows for square
        for (int row = squareRow * 3; row < (squareRow * 3) + 3; row++) {
            List<List<Integer>> currentRow = getValuesInRow(row);

            // get single values in same row

            Set<Integer> singleValuesInSameRow = this.findSingleValuesInRow(row);

            // iterate 3 columns for current row of this square
            for (int col = squareCol * 3; col < (squareCol * 3) + 3; col++) {

                List<Integer> cellContent = currentRow.get(col);

                // if the current cell is empty, replace it with the possible
                // list of guesses
                if (cellContent.size() == 0) {

                    Set<Integer> guessesForThisCell = new HashSet<>(missingValuesInSquare);

                    // remove single values for the same row
                    guessesForThisCell.removeAll(singleValuesInSameRow);

                    // remove single values in same column
                    Set<Integer> singleValuesInSameColumn = this.findSingleValuesInColumn(col);
                    guessesForThisCell.removeAll(singleValuesInSameColumn);

                    currentRow.set(col, new ArrayList<>(guessesForThisCell));

                    replacedValuesOnThisPass = true;
                }
            }
        }
        return replacedValuesOnThisPass;
    }

    private Set<Integer> findSingleValuesInSquareByRowCol(int row, int col) {
        //TODO test: are these working as expected?
        int squareRow = this.getSquareRowFromRow(row);
        int squareCol = this.getSquareColFromCol(col);
        Set<Integer> singleValuesInSquare = this.getSingleValuesInSquare(squareRow, squareCol);
        return singleValuesInSquare;
    }

    int getSquareColFromCol(int col) {
        int squareCol = col / 3;
        return squareCol;
    }

    int getSquareRowFromRow(int row) {
        int rowSquare = row / 3;
        return rowSquare;
    }

    List<Integer> getValueInCell(int row, int col) {
        List<List<Integer>> valuesInRow = this.getValuesInRow(row);
        return valuesInRow.get(col);
    }

    /**
     * Retrieves list of values for a given row
     * 
     * @param row
     * @return
     */
    List<List<Integer>> getValuesInRow(int row) {
        List<List<Integer>> currentRow = this.solutionGrid.get(row);
        return currentRow;
    }

    
    List<List<Integer>> getValuesInCol(int col){
        
        List<List<Integer>> columnValues = new ArrayList<>();
        
        for(List<List<Integer>> currentRow : this.solutionGrid) {
            List<Integer> valuesInCell = currentRow.get(col);
            
            //add values to results
            columnValues.add(valuesInCell);
        }
        
        
        return columnValues;
        
    }
    
    
    Set<Integer> getValuesInRowAsSet(int row) {
        Set<Integer> values = new HashSet<>();

        List<List<Integer>> currentRow = getValuesInRow(row);

        for (int col = 0; col < 8; col++) {

            List<Integer> valuesForCell = currentRow.get(col);
            values.addAll(valuesForCell);
        }

        return values;
    }

    /**
     * Retrieves a set of unique values for the specified column. Iterates
     * through all rows to retrieve each value for that column from each row.
     * 
     * @param col
     * @return
     */
    Set<Integer> getValuesInColumnAsSet(int col) {
        Set<Integer> values = new HashSet<>();

        for (int row = 0; row < 8; row++) {
            List<List<Integer>> currentRow = getValuesInRow(row);
            List<Integer> valuesForCell = currentRow.get(col);
            values.addAll(valuesForCell);
        }

        return values;
    }

    Set<Integer> getCandidateValues(Set<Integer> currentValues) {

        Set<Integer> missingValues = new HashSet<>(SudokuGraderApp.allowedValues);
        missingValues.removeAll(currentValues);
        return missingValues;
    }
    
    /**
     * Sets a rows in the solution grid, to support unit tests/
     * @param row
     * @param newValues
     */
    void setRowInSolutionGrid(int row, List<List<Integer>> newValues) {
        this.solutionGrid.set(row, newValues);
    }
    
    public int[][] getSudokuGrid() {
        return startingSudokuGrid;
    }

    public void setSudokuGrid(int[][] sudokuGrid) {
        this.startingSudokuGrid = sudokuGrid;
    }

    public void setSudokuGridWithSolutionShorthand(List<String> solutionShorthand) {
        int[][] startingGrid = new int[9][9];
        int rowIndex = 0;
        int colIndex = 0;
        for(String row : solutionShorthand) {
            for(String cellInRow : row.split("")) {
                if(cellInRow.equals(".")) {
                    startingGrid[rowIndex][colIndex] = 0;
                }
                else {
                    int value = Integer.parseInt(cellInRow);
                    startingGrid[rowIndex][colIndex] = value;
                }
                colIndex++;
            }
            rowIndex++;
            colIndex = 0;
        }
        this.setSudokuGrid(startingGrid);
    }

    public List<List<List<Integer>>> getSolutionGrid() {
        return solutionGrid;
    }

    public void setSolutionGrid(List<List<List<Integer>>> solutionGrid) {
        this.solutionGrid = solutionGrid;
    }

}