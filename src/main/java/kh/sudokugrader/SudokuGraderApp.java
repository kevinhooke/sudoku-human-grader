package kh.sudokugrader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kh.sudokugrader.exception.SolutionGridNotInitializedException;

/**
 * Sudoku grader.
 * 
 * TODO
 * 
 * @author kevinhooke
 *
 */
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
    
    /**
     * Default constructor.
     */
    public SudokuGraderApp() {
    }

    public static void main(String[] args) {

        SudokuGraderApp app = new SudokuGraderApp();
        //app.printGridWithBorders();

        app.populateSolutionGridWithStartingPosition();
        //app.printSolutionGrid();
        app.printGridWithBorders();
        long startTime = System.currentTimeMillis();
        app.gradePuzzle();
        long endTime = System.currentTimeMillis();
        app.printSolutionGridWithBorders();
        LOGGER.info("Complete!");
        LOGGER.info("Elapsed time: " + (endTime - startTime));
    }

    private void printValuesSet(Set<Integer> squareValues) {
        for (Integer i : squareValues) {
            System.out.print(i.toString() + ", ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Prints the puzzle grid with borders around each square.
     * 
     */
    private void printGridWithBorders() {
        StringBuilder sb = new StringBuilder(120);
        sb.append("\n+-------+-------+-------+\n");
        for (int row = 0; row < 9; row++) {
            sb.append("| ");
            for (int col = 0; col < 9; col++) {
                int value = startingSudokuGrid[row][col];
                sb.append((value == 0 ? " " : value) + " ");
                if (col == 2 || col == 5) {
                    sb.append("| ");
                }
            }
            if (row == 2 || row == 5) {
                sb.append("|");
                sb.append("\n+-------+-------+-------+\n");
            }
            else {
                sb.append("|\n");
            }
        }

        sb.append("+-------+-------+-------+\n");
        LOGGER.info(sb.toString());
    }

    void printSolutionGrid() {
        LOGGER.trace("Current candidates: ");
        StringBuilder sb = new StringBuilder(120);
        sb.append("\n");
        for (List<List<Integer>> row : this.solutionGrid) {
            for (List<Integer> currentCell : row) {
                // full cell pad to accomodate 0..9
                // int paddingSize = (9 - currentCell.size()) * 3;

                // temp - reduced pad
                int paddingSize = (5 - currentCell.size()) * 3;

                sb.append("{ ");
                for (Integer i : currentCell) {
                    sb.append(i.toString() + ", ");
                }
                for (int paddingCount = 0; paddingCount < paddingSize; paddingCount++) {
                    sb.append(" ");
                }

                sb.append(" },");
            }
            sb.append("\n");
        }
        LOGGER.trace(sb.toString());
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

        this.printGridWithBorders();

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
                this.printSolutionGrid();
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
                    this.printSolutionGrid();
                }
                
            }
        }
        
        if(unsolvedCells > 0) {
            this.difficulty.setPuzzleSolved(false);
            LOGGER.info("Puzzle solved: NO");
        }
        else {
            this.difficulty.setPuzzleSolved(true);
            LOGGER.info("Puzzle solved: Yes");
        }
        
        //TODO: this needs to be moved to a return from this method
        
        LOGGER.info("Initial givens: " + this.difficulty.getInitialGivens());
        LOGGER.info("Passes through grid: " + passesThroughGridCount);
        LOGGER.info("Naked singles found: " + this.difficulty.getNakedSingleCount());
        LOGGER.info("Hidden singles found: " + this.difficulty.getHiddenSingleCount());
        
        return difficulty;
    }

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

    //TODO in progress
    boolean findHiddenSinglesInCandidates(int row, int col) {
        boolean valuesReplaced = false;
        int startingUnsolvedCells = this.checkForCompleteSolution();
        
        //find hidden singles in square
        Set<Integer> hiddenSinglesInSquare = this.findHiddenSinglesSquareByRowCol(row, col);
        //remove other candidates in cell for each of the hidden singles in a square
        boolean valuesRemovedInSquare = false;
        if(hiddenSinglesInSquare.size() > 0)
        {
            //TODO bug: this is not removing values anymore, was working
            valuesRemovedInSquare = this.removeOtherCandidatesInSquareWhereHiddenSinglesExist(row, col, hiddenSinglesInSquare);
        }
        
        // find hidden singles in row
        Set<Integer> hiddenSinglesInRow = this.findHiddenSinglesInRow(row);
        //remove other candidates in row - this leaves a single candidate in cell which could uncover other cells to be solved
        boolean valuesRemovedInCell = false;
        if(hiddenSinglesInRow.size() > 0) {
            valuesRemovedInCell = this.removeOtherCandidatesInCellWhereHiddenSingleExists(row, col, hiddenSinglesInRow);
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
                    valuesInCol.set(compareColumn, valuesInCell);
                    this.setColumnInSolutionGrid(col, valuesInCol);
                    valuesRemoved = true;
                }
                compareColumn++;
            }
        }
        return valuesRemoved;
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

    void removeOtherCandidatesInRowWhereHiddenSinglesExist() {
        // TODO implement this
        
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
     * For each identified hidden single in this row, find the cell where each exists and remove the other candidates
     * 
     * @param hiddenSinglesInRow
     */
    boolean removeOtherCandidatesInCellWhereHiddenSingleExists(int row, int col, Set<Integer> hiddenSinglesInRow) {
        boolean valuesRemoved = false;
        
        List<List<Integer>> valuesInRow = this.getValuesInRow(row);

        for (Integer value : hiddenSinglesInRow) {
            int compareColumn = 0;
            for(List<Integer> valuesInCell : valuesInRow) {
                //if this cell contains this candidate, delete all other values and keep just this value
                if(valuesInCell.contains(value)) {
                    valuesInCell.clear();
                    valuesInCell.add(value);
                    valuesInRow.set(compareColumn, valuesInCell);
                    this.solutionGrid.set(row, valuesInRow);
                    valuesRemoved = true;
                }
                compareColumn++;
            }
        }
        return valuesRemoved;
    }
    
    
    //TODO need to test this
    Set<Integer> findHiddenSinglesInColumn(int col) {
        int startingUnsolvedCells = this.checkForCompleteSolution();
        Set<Integer> hiddenSingles = new HashSet<Integer>();
        List<List<Integer>> valuesInCol = this.getValuesInCol(col);
        for (int row = 0; row < 9; row++){
            
            //TODO test this - was incorrectly using col instead of row
            List<Integer> valuesInRow = valuesInCol.get(row);
            
            //if this cell only contains a single value then skip because this could be a naked single and we'll check it
            //using the naked singles approach
            if(valuesInCol.size() > 1 ) {
                //for each of the candidate values in a cell, check if they exist in any of the other
                //cells - if they don't then this is a naked single
                for(Integer currentValueInCol : valuesInRow) {
                  //does this current value exist in any of the other columns?
                    //exclude the current column
                    int colToCompare = 0;
                    
                    //TODO: bug - I don't think this approach works, see occurence counting approach in getHiddenSingleValuesInSquare() instead
                    List<Integer> candidatesInAllColsInrow = new ArrayList<>();
                    for(List<Integer> candidatesInCol : valuesInCol) {
                        
                        //if we're not comparing the same cell and if this cell contains more than a single value 
                        //(which would be a naked single)
                        if(colToCompare != col) {
                            candidatesInAllColsInrow.addAll(candidatesInCol);
                        }
                        colToCompare++;
                    }
                    //if current value does not exist in any of the other cells in this column, save it
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
            boolean valuesReplacedInRow = valuesInCell.removeAll(singleValuesInRow);
            boolean valuesReplacedInCol = valuesInCell.removeAll(singleValuesInCol);
            boolean valuesReplacedInSquare = valuesInCell.removeAll(singleValuesInSquare);

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
                //TODO: fix this
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

}