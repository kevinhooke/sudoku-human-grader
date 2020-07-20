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
     * This easy puzzle can be solved with only naked singles, confirming therefore
     * difficult = easy.
     * 
     * Puzzle solved: Yes
     * Initial givens: 35
     * Passes through grid: 7
     * Naked singles found: 42
     * Hidden singles found: 0
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
     * THis east puzzle from The Guardian does not currently solve.
     * TODO: looks like I didn't apply the patterns across columns yet, which is interesting as that means
     * apply techniques across rows only has been sufficient so far.
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
     * Medium difficulty example from https://www.websudoku.com
     * 
     * This puzzle cannot be solved with only naked/hidden singles.
     * 
     * This puzzle can be solved with naked and hidden singles, therefore
     * by this grading approach this is really a simple puzzle.
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
    
    @Test
    //TODO: bug - candidates from some cells are being completely removed with this puzzle
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
