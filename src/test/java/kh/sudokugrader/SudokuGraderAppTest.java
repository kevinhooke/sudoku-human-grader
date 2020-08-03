package kh.sudokugrader;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class SudokuGraderAppTest {

	private SudokuGraderApp app = new SudokuGraderApp();
	
	private int[][] sudokuGrid = {
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
	
    private int[][] expectedResult1 = {
            {1,2,3,4,5,6,7,8,9},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
        };
	
	public SudokuGraderAppTest() {
		app.setSudokuGrid(this.sudokuGrid);
	}
	
	@Test
	public void testGetSingleValuesInColumn_0(){
	    this.app.populateSolutionGridWithStartingPosition();
	    Set<Integer> values = this.app.findSingleValuesInColumn(0);
	    Set<Integer> expectedValues = new HashSet<>();
	    expectedValues.add(1);
	    expectedValues.add(4);
	    expectedValues.add(9);
	    expectedValues.add(6);
	    assertTrue(values.containsAll(expectedValues));
	}

   @Test
    public void testGetSingleValuesInColumn_8(){
        
    }

	@Test
	public void testSquareColFromCol_0(){
		int result = this.app.getSquareColFromCol(0);
		assertTrue(result == 0);
	}

	@Test
	public void testSquareColFromCol_1(){
		int result = this.app.getSquareColFromCol(1);
		assertTrue(result == 0);
	}
	
	@Test
	public void testSquareColFromCol_2(){
		int result = this.app.getSquareColFromCol(2);
		assertTrue(result == 0);
	}

	@Test
	public void testSquareColFromCol_3(){
		int result = this.app.getSquareColFromCol(3);
		assertTrue(result == 1);
	}
	
	@Test
	public void testSquareRowFromRow_0(){
		int result = this.app.getSquareRowFromRow(0);
		assertTrue(result == 0);
	}

	@Test
	public void testSquareRowFromRow_1(){
		int result = this.app.getSquareRowFromRow(1);
		assertTrue(result == 0);
	}
	
	@Test
	public void testSquareRowFromRow_2(){
		int result = this.app.getSquareRowFromRow(2);
		assertTrue(result == 0);
	}

	@Test
	public void testSquareRowFromRow_3(){
		int result = this.app.getSquareRowFromRow(3);
		assertTrue(result == 1);
	}
	
	@Test
	public void testGetValuesInSquare00() {
		Set<Integer> expectedValues = new HashSet<>();
		expectedValues.add(6);
		expectedValues.add(7);
		this.app.populateSolutionGridWithStartingPosition();
		Set<Integer> values = this.app.getSingleValuesInSquare(0, 0);
		assertTrue(values.containsAll(expectedValues));
	}

	@Test
	public void testGetValuesInSquare01() {
		Set<Integer> expectedValues = new HashSet<>();
		expectedValues.add(1);
		expectedValues.add(4);
		expectedValues.add(6);
		this.app.populateSolutionGridWithStartingPosition();
		Set<Integer> values = this.app.getSingleValuesInSquare(1, 0);
		assertTrue(values.containsAll(expectedValues));
	}

	@Test
	public void testGetValuesInSquare10() {
		Set<Integer> expectedValues = new HashSet<>();
		expectedValues.add(1);
		expectedValues.add(4);
		expectedValues.add(5);
		expectedValues.add(8);
		expectedValues.add(9);
		this.app.populateSolutionGridWithStartingPosition();
		Set<Integer> values = this.app.getSingleValuesInSquare(0, 1);
		assertTrue(values.containsAll(expectedValues));
	}
	
	@Test
	public void testGetValuesInCol0(){
		Set<Integer> expectedValues = new HashSet<>();
		expectedValues.add(1);
		expectedValues.add(4);
		expectedValues.add(6);
		expectedValues.add(9);
		this.app.populateSolutionGridWithStartingPosition();
		Set<Integer> values = this.app.getValuesInColumnAsSet(0);
		
		assertTrue(values.containsAll(expectedValues));
		assertFalse(values.contains(2));
		assertFalse(values.contains(3));
		assertFalse(values.contains(5));
		assertFalse(values.contains(7));
		assertFalse(values.contains(8));

	}

	@Test
	public void testGetValuesInCol8(){
		Set<Integer> expectedValues = new HashSet<>();
		expectedValues.add(3);
		expectedValues.add(4);
		expectedValues.add(7);
		expectedValues.add(8);
		this.app.populateSolutionGridWithStartingPosition();
		Set<Integer> values = this.app.getValuesInColumnAsSet(8);
		
		assertTrue(values.containsAll(expectedValues));
		assertFalse(values.contains(1));
		assertFalse(values.contains(2));
		assertFalse(values.contains(5));
		assertFalse(values.contains(6));
		assertFalse(values.contains(9));

	}

	@Test
	public void testGetValuesInRow0(){
		Set<Integer> expectedValues = new HashSet<>();
		expectedValues.add(1);
		expectedValues.add(6);
		expectedValues.add(7);
		expectedValues.add(8);
		this.app.populateSolutionGridWithStartingPosition();
		Set<Integer> values = this.app.getValuesInRowAsSet(0);
		
		assertTrue(values.containsAll(expectedValues));
		assertFalse(values.contains(2));
		assertFalse(values.contains(3));
		assertFalse(values.contains(4));
		assertFalse(values.contains(5));
		assertFalse(values.contains(9));

	}

	@Test
	public void testGetValuesInRow8(){
		Set<Integer> expectedValues = new HashSet<>();
		expectedValues.add(3);
		expectedValues.add(4);
		expectedValues.add(6);
		expectedValues.add(9);
		this.app.populateSolutionGridWithStartingPosition();
		Set<Integer> values = this.app.getValuesInRowAsSet(8);
		
		assertTrue(values.containsAll(expectedValues));
		assertFalse(values.contains(1));
		assertFalse(values.contains(2));
		assertFalse(values.contains(5));
		assertFalse(values.contains(7));
		assertFalse(values.contains(8));

	}

	@Test
    public void testFindHiddenSinglesInRow_0(){
	    
	    //TODO
        Set<Integer> expectedValues = new HashSet<>();
        expectedValues.add(4);
        
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid);
        app.populateSolutionGridWithStartingPosition();
        app.populateCandidateValues();
        app.printSolutionGrid();
        Set<Integer> values = app.findHiddenSinglesInRow(0);
        
        assertTrue(values.size() == 1);
        assertTrue(values.containsAll(expectedValues));
	}
	
	   @Test
	    public void testFindHiddenSinglesInRow_1(){
	        
	        //TODO
	        Set<Integer> expectedValues = new HashSet<>();
	        expectedValues.add(1);
	        
	        SudokuGraderApp app = new SudokuGraderApp();
	        app.setSudokuGrid(this.sudokuGrid);
	        app.populateSolutionGridWithStartingPosition();
	        app.populateCandidateValues();
	        app.printSolutionGrid();
	        Set<Integer> values = app.findHiddenSinglesInRow(1);
	        
	        assertTrue(values.size() == 1);
            assertTrue(values.containsAll(expectedValues));

	   }
	   
	    /**
	     * Test for issue with blank cells seems to start with a row like this:
	     * 
	     * [[7], [5], [2, 8], [6], [8, 9], [9], [4], [1], [3]]
	     * Set<Integer> hiddenSinglesInRow = this.findHiddenSinglesInRow(row);
	     * 
	     * Before fix: returns wrong result: 2, 9
	     * Expected result: 2
	     */
	    @Test
	    public void testfindHiddenSinglesInRow_wrongResult() {
	        SudokuGraderApp app = new SudokuGraderApp();
	        app.populateSolutionGridWithStartingPosition();
	        
	        List<List<Integer>> testRow = new ArrayList<>();
	        testRow.add(Arrays.asList(7));
	        testRow.add(Arrays.asList(5));
	        testRow.add(Arrays.asList(2, 8));
	        testRow.add(Arrays.asList(6));
	        testRow.add(Arrays.asList(8, 9));
	        testRow.add(Arrays.asList(9));
	        testRow.add(Arrays.asList(4));
	        testRow.add(Arrays.asList(1));
	        testRow.add(Arrays.asList(3));
	        app.setRowInSolutionGrid(2, testRow);
	        Set<Integer> hiddenSinglesInRow = app.findHiddenSinglesInRow(2);
	        
	        assertTrue(hiddenSinglesInRow.size() == 1);
	        assertTrue(hiddenSinglesInRow.contains(2));
	    }

	   @Test
	   public void testsetSudokuGridWithSolutionShorthand1() {
	       List<String> givenSolutionsShorthand = new ArrayList<>();
	        givenSolutionsShorthand.add("123456789");
	        givenSolutionsShorthand.add(".........");
	        givenSolutionsShorthand.add(".........");
	        givenSolutionsShorthand.add(".........");
	        givenSolutionsShorthand.add(".........");
	        givenSolutionsShorthand.add(".........");
	        givenSolutionsShorthand.add(".........");
	        givenSolutionsShorthand.add(".........");
	        givenSolutionsShorthand.add(".........");
	        
	        SudokuGraderApp app = new SudokuGraderApp();
	        app.setSudokuGridWithSolutionShorthand(givenSolutionsShorthand);
	        int[][] solutionGrid = app.getSudokuGrid();
	        assertArrayEquals(expectedResult1, solutionGrid);
	        
	        
	   }
}
