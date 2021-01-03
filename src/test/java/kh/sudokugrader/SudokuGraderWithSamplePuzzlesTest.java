package kh.sudokugrader;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class SudokuGraderWithSamplePuzzlesTest {

    /**
     * Easy rated puzzle from https://www.websudoku.com
     */
    private int[][] sudokuGrid_easy1 = {
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
    
    /**
     * Easy puzzle from http://www.websudoku.com/?level=1&set_id=7605607374
     */
    private int[][] sudokuGrid_easy2 = {
        {0,8,9,0,0,0,2,0,0}, 
        {7,0,5,3,9,0,4,6,0},
        {0,0,0,8,0,0,6,5,9},
        
        {0,0,2,0,0,9,6,0,0},
        {6,0,0,5,4,1,0,0,2},
        {0,0,3,2,0,0,9,0,0},
        
        {9,3,0,0,0,8,0,0,0},
        {0,6,4,0,5,7,8,0,3},
        {0,0,1,0,0,0,7,4,0},
        
    };
    
    /**
     * Medium rated puzzle from https://www.websudoku.com
     */
    private int[][] sudokuGrid_medium1 = {
        {5,6,8,9,0,0,0,0,0},
        {0,0,0,0,0,0,4,0,0},
        {0,4,3,0,7,5,8,0,0},
        {0,8,9,0,4,2,0,7,0},
        {0,0,0,0,9,0,0,0,0},
        {0,7,0,8,6,0,2,9,0},
        {0,0,4,7,2,0,9,3,0},
        {0,0,6,0,0,0,0,0,0},
        {0,0,0,0,0,8,1,4,5}
    };
    
    /**
     * Hard rated puzzle from https://www.websudoku.com
     */
    private int[][] sudokuGrid_hard1 = {    
    {0,9,0,0,0,1,0,7,8},
    {0,3,0,0,0,0,0,0,9},
    {0,5,0,6,0,0,4,0,0},
    {0,0,0,2,0,7,0,0,0},
    {0,8,3,0,0,0,7,9,0},
    {0,0,0,5,0,8,0,0,0},
    {0,0,5,0,0,6,0,2,0},
    {2,0,0,0,0,0,0,4,0},
    {8,1,0,7,0,0,0,3,0}
    };
    
    private int[][] sudokuGrid_hardFromReddit1 = {  
    {0,0,3,0,0,5,0,0,9},
    {9,0,0,2,0,0,0,6,0},
    {0,8,0,0,4,0,7,0,0},
    {0,0,7,0,0,0,0,0,2},
    {0,2,0,0,0,0,1,0,0},
    {6,0,0,0,0,0,0,7,0},
    {0,4,0,0,8,0,6,0,0},
    {0,0,0,1,0,0,0,5,0},
    {0,0,9,0,0,7,0,0,3}
    };
    
    
    /**
     * From The Guardian, Easy 07/13/20 
     * https://www.theguardian.com/lifeandstyle/2020/jul/13/sudoku-4883-easy
     */
    private int[][] sudokuGrid_easy3 = {  
            {0,0,1,0,2,0,0,0,4},
            {0,6,0,0,0,0,0,0,0},
            {8,4,0,1,0,0,3,0,0},
            {0,0,0,0,3,0,0,9,0},
            {0,8,5,9,0,7,2,6,0},
            {0,1,0,0,8,0,0,0,0},
            {0,0,8,0,0,4,0,5,1},
            {0,0,0,0,0,0,0,4,0},
            {2,0,0,0,7,0,6,0,0}
            };
    
    
    /**
     * Example puzzle with hidden singles.
     * 
     * TODO: To solve, this also requires locked candidates to eliminate candidates.
     * e.g. col 7 has 2 9s in square 8 that eliminate 9 in the same column in row 3
     * 
     * From http://hodoku.sourceforge.net/en/tech_singles.php#h1
     */
    private int[][] sudokuGrid_easy4 = {
            {0,2,8,0,0,7,0,0,0},
            {0,1,6,0,8,3,0,7,0},
            {0,0,0,0,2,0,8,5,1},
            {1,3,7,2,9,0,0,0,0},
            {0,0,0,7,3,0,0,0,0},
            {0,0,0,0,4,6,3,0,7},
            {2,9,0,0,7,0,0,0,0},
            {0,0,0,8,6,0,1,4,0},
            {0,0,0,3,0,0,7,0,0}           
    };

    /**
     * Solveable with hidden singles only
     * 
     * From https://github.com/SudokuMonster/SukakuExplainer/wiki/SE121---Some-interesting-Sudokus
     */
    private int[][] sudokuGrid_easy5 = {
            {0,0,0,0,0,0,3,0,1},
            {8,6,0,0,0,0,0,0,0},
            {0,0,0,2,0,0,0,0,0},
            {0,0,0,0,4,0,7,6,0},
            {0,0,1,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,8,0},
            {0,7,0,0,0,0,6,4,0},
            {5,0,0,1,0,3,0,0,0},
            {0,0,0,5,0,0,0,0,0},          
    };
    
    /**
     * This easy puzzle can be solved with only naked singles, confirming therefore
     * difficult = easy.
     * 
     * Puzzle solved: Yes
     * Initial givens: 35
     * Passes through grid: 7
     * Naked singles found: 42
     * Hidden singles found: 0
     * 
     * TODO: how many outer loop parses does this puzzle need to find a solution?
     * TODO: should
     */
    @Test
    public void testEasy1(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_easy1);
        app.populateSolutionGridWithStartingPosition();
        PuzzleDifficulty diffculty = app.gradePuzzle();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
        assertTrue(diffculty.isPuzzleSolved());
    }
    

    /**
     * This easy puzzle can be solved with only naked singles, confirming therefore
     * difficult = easy.
     * 
     * Puzzle solved: Yes
     * Initial givens: 36
     * Passes through grid: 5
     * Naked singles found: 36
     * Hidden singles found: 0
     * 
     * TODO: how many outer loop parses does this puzzle need to find a solution?
     */
    @Test
    public void testEasy2(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_easy2);
        app.populateSolutionGridWithStartingPosition();
        PuzzleDifficulty diffculty = app.gradePuzzle();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
        assertTrue(diffculty.isPuzzleSolved());
    }
    
    /**
     * Easy puzzle from The Guardian.
     * 
     * Techniques to solve:
     * - naked singles
     * - hidden singles
     * 
     * Requires 3 outer loop iterations to solve (other easy puzzles so far only needed 2.
     * 
     * Puzzle solved: Yes
     * Initial givens: 26
     * Passes through grid: 12
     * Naked singles found: 31
     * Hidden singles found: 23
     */
    @Test
    public void testEasy3(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_easy3);
        app.populateSolutionGridWithStartingPosition();
        PuzzleDifficulty diffculty = app.gradePuzzle();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
        assertTrue(diffculty.isPuzzleSolved());
    }
    
    /**
     * TODO: can't currently be solved with naked or hidden singles
     */
    @Test
    public void testEasy4(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_easy4);
        app.populateSolutionGridWithStartingPosition();
        PuzzleDifficulty diffculty = app.gradePuzzle();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
        assertTrue(diffculty.isPuzzleSolved());
    }
    
    /**
     * Puzzle solved: Yes
     * Initial givens: 17
     * Passes through grid: 31
     * Naked singles found: 12
     * Hidden singles found: 52
     */
    @Test
    public void testEasy5(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_easy5);
        app.populateSolutionGridWithStartingPosition();
        PuzzleDifficulty diffculty = app.gradePuzzle();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
        assertTrue(diffculty.isPuzzleSolved());
    }
    
    
    /**
     * Medium difficulty example from https://www.websudoku.com
     * 
     * This puzzle can be solved with naked and hidden singles, therefore
     * by this grading approach this is really a simple puzzle.
     * 
     * Puzzle solved: Yes
     * Initial givens: 31
     * Passes through grid: 10
     * Naked singles found: 21
     * Hidden singles found: 27
     */
    @Test
    public void testMedium1(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_medium1);
        app.populateSolutionGridWithStartingPosition();
        PuzzleDifficulty diffculty = app.gradePuzzle();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
        assertTrue(diffculty.isPuzzleSolved());
    }
    
    /**
     * Hard puzzle from ???
     * 
     * Can be solved with only naked and hidden singles. Is this really a hard
     * puzzle? Should be easy/medium on my scale?
     * 
     * Puzzle solved: Yes
     * Initial givens: 26
     * Passes through grid: 11
     * Naked singles found: 15
     * Hidden singles found: 37
     */
    @Test
    public void testHard1(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_hard1);
        app.populateSolutionGridWithStartingPosition();
        PuzzleDifficulty diffculty = app.gradePuzzle();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
        assertTrue(diffculty.isPuzzleSolved());
    }
        
    @Test
    //TODO: bug - candidates from some cells are being completely removed with this puzzle
    public void testHardFromReddit(){
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_hardFromReddit1);
        app.populateSolutionGridWithStartingPosition();
        PuzzleDifficulty diffculty = app.gradePuzzle();
        app.printSolutionGridWithBorders();
        
        //TODO need asserts
        assertTrue(diffculty.isPuzzleSolved());
    }
    
    
}
