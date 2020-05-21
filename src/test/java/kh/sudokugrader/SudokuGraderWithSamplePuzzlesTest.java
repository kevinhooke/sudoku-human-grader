package kh.sudokugrader;

import org.junit.Test;

public class SudokuGraderWithSamplePuzzlesTest {

    
    private int[][] sudokuGrid1_easy = {
            {0,0,0,8,1,0,6,7,0},
            {0,0,7,4,9,0,2,0,8},
            {0,6,0,0,5,0,1,0,4},
            {1,0,0,0,0,3,9,0,0},
            {4,0,0,0,8,0,0,0,7},
            {0,0,6,9,0,0,0,0,3},
            {9,0,2,0,3,0,0,6,0},
            {6,0,1,0,7,4,3,0,0},
            {0,3,4,0,6,9,0,0,0}
    };
    
    private int[][] sudokuGrid2_easy = {
        {0,0,0,8,1,0,6,7,0}, 
        {0,0,7,4,9,0,2,0,8},
        {0,6,0,0,5,0,1,0,4},
        {1,0,0,0,0,3,9,0,0},
        {4,0,0,0,8,0,0,0,7},
        {0,0,6,9,0,0,0,0,3},
        {9,0,2,0,3,0,0,6,0},
        {6,0,1,0,7,4,3,0,0},
        {0,3,4,0,6,9,0,0,0}
    };
    
    @Test
    public void testEasy1(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid2_easy);
        app.populateSolutionGridWithStartingPosition();
        app.solve();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
    }
    

    /**
     * Result from DLX Solver:
+-------+-------+-------+
| 3 4 9 | 8 1 2 | 6 7 5 | 
| 5 1 7 | 4 9 6 | 2 3 8 | 
| 2 6 8 | 3 5 7 | 1 9 4 | 
+-------+-------+-------+
| 1 8 5 | 7 2 3 | 9 4 6 | 
| 4 9 3 | 6 8 1 | 5 2 7 | 
| 7 2 6 | 9 4 5 | 8 1 3 | 
+-------+-------+-------+
| 9 7 2 | 5 3 8 | 4 6 1 | 
| 6 5 1 | 2 7 4 | 3 8 9 | 
| 8 3 4 | 1 6 9 | 7 5 2 | 
+-------+-------+-------+
     */
    @Test
    public void testEasy2(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid1_easy);
        app.populateSolutionGridWithStartingPosition();
        app.solve();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
    }
    
}
