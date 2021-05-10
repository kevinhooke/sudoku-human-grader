package kh.sudokugrader;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    
/*
 * This puzzle was generated with 60 removed candidates. Human solver/grader can't currently
 * solve this one. The DLX Solver can solve it ok.
+-------+-------+-------+
| . . . | 9 . . | 4 . . | 
| . . 1 | 7 . . | 8 2 . | 
| . . . | . . 8 | 3 . . | 
+-------+-------+-------+
| . . 8 | . . . | . . . | 
| . . . | . . . | . . 8 | 
| . . . | 8 . 4 | . . . | 
+-------+-------+-------+
| . . 6 | . . . | 5 . . | 
| 5 . 2 | 4 . . | . . . | 
| . . 4 | . 5 7 | 9 . . | 
+-------+-------+-------+
 *     
 */
    
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
        //TODO complete this
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
	public void testFindHiddenSinglesInCol_0() {
        Set<Integer> expectedValues = new HashSet<>();
        expectedValues.add(1);
        
        SudokuGraderApp app = new SudokuGraderApp();
        app.setSudokuGrid(this.sudokuGrid_easy3);
        app.populateSolutionGridWithStartingPosition();
        app.populateCandidateValues();
        app.printSolutionGrid();
        Set<Integer> values = app.findHiddenSinglesInColumn(0);
        
        assertTrue(values.size() == 1);
        assertTrue(values.containsAll(expectedValues));
	}

	   @Test
	    public void testFindHiddenSinglesInCol_1() {
	        Set<Integer> expectedValues = new HashSet<>();
	        expectedValues.add(2);
	        
	        SudokuGraderApp app = new SudokuGraderApp();
	        app.setSudokuGrid(this.sudokuGrid_easy3);
	        app.populateSolutionGridWithStartingPosition();
	        app.populateCandidateValues();
	        app.printSolutionGrid();
	        Set<Integer> values = app.findHiddenSinglesInColumn(1);
	        
	        assertTrue(values.size() == 1);
	        assertTrue(values.containsAll(expectedValues));
	    }

       @Test
       public void testFindHiddenSinglesInCol_2() {
           SudokuGraderApp app = new SudokuGraderApp();
           app.setSudokuGrid(this.sudokuGrid_easy3);
           app.populateSolutionGridWithStartingPosition();
           app.populateCandidateValues();
           app.printSolutionGrid();
           Set<Integer> values = app.findHiddenSinglesInColumn(2);
           
           assertTrue(values.size() == 0);
       }
	   
    @Test
    public void testFindHiddenSinglesInRow_1() {

        // TODO
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
	   
	   @Test
	   public void testGetValuesInCol_0() {
	       List<String> givenSolutionsShorthand = new ArrayList<>();
           givenSolutionsShorthand.add("1........");
           givenSolutionsShorthand.add("2........");
           givenSolutionsShorthand.add("3........");
           givenSolutionsShorthand.add("4........");
           givenSolutionsShorthand.add("5........");
           givenSolutionsShorthand.add("6........");
           givenSolutionsShorthand.add("7........");
           givenSolutionsShorthand.add("8........");
           givenSolutionsShorthand.add("9........");
           
           SudokuGraderApp app = new SudokuGraderApp();
           app.setSudokuGridWithSolutionShorthand(givenSolutionsShorthand);
           app.populateSolutionGridWithStartingPosition();
           List<List<Integer>> columnValues = app.getValuesInCol(0);
           assertEquals(9, columnValues.size());
           assertEquals(1, columnValues.get(0).size());
           assertEquals(Integer.valueOf(1), columnValues.get(0).get(0));
           assertEquals(1, columnValues.get(8).size());
           assertEquals(Integer.valueOf(9), columnValues.get(8).get(0));

	   }
	   
	   @Test
	   public void testSetColumnInSolutionGrid_col1() {
	       List<String> givenSolutionsShorthand = new ArrayList<>();
           givenSolutionsShorthand.add(".........");
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
           app.populateSolutionGridWithStartingPosition();
           
           List<List<Integer>> columnValues = app.getValuesInCol(0);
           assertEquals(9, columnValues.size());
           //all cells are empty
           assertEquals(0, columnValues.get(0).size());
           assertEquals(0, columnValues.get(1).size());
           assertEquals(0, columnValues.get(2).size());
           assertEquals(0, columnValues.get(3).size());
           assertEquals(0, columnValues.get(4).size());
           assertEquals(0, columnValues.get(5).size());
           assertEquals(0, columnValues.get(6).size());
           assertEquals(0, columnValues.get(7).size());
           assertEquals(0, columnValues.get(8).size());
           
           //set col 1
           List<List<Integer>> valuesInCol = new ArrayList<>();
           List<Integer> row1Values = new ArrayList<>();
           row1Values.add(Integer.valueOf(1));
           List<Integer> row2Values = new ArrayList<>();
           row2Values.add(Integer.valueOf(2));
           row2Values.add(Integer.valueOf(3));
           List<Integer> row3Values = new ArrayList<>();
           List<Integer> row4Values = new ArrayList<>();
           List<Integer> row5Values = new ArrayList<>();
           List<Integer> row6Values = new ArrayList<>();
           List<Integer> row7Values = new ArrayList<>();
           List<Integer> row8Values = new ArrayList<>();
           List<Integer> row9Values = new ArrayList<>();
           
           valuesInCol.add(row1Values);
           valuesInCol.add(row2Values);
           valuesInCol.add(row3Values);
           valuesInCol.add(row4Values);
           valuesInCol.add(row5Values);
           valuesInCol.add(row6Values);
           valuesInCol.add(row7Values);
           valuesInCol.add(row8Values);
           valuesInCol.add(row9Values);
           app.setColumnInSolutionGrid(0, valuesInCol);
           
           //check results
           List<List<Integer>> newColumnValues = app.getValuesInCol(0);
           assertEquals(9, newColumnValues.size());
           //row 1 in this column has 1 value
           assertEquals(1, newColumnValues.get(0).size());
           //check value is 1
           assertEquals(Integer.valueOf(1), newColumnValues.get(0).get(0));
           
           //row 2 in this column has 2 values
           assertEquals(2, newColumnValues.get(1).size());
           //check 1st value is 2
           assertEquals(Integer.valueOf(2), newColumnValues.get(1).get(0));
           //check 2nd value is 3
           assertEquals(Integer.valueOf(3), newColumnValues.get(1).get(1));

           //all other rows are empty
           assertEquals(0, newColumnValues.get(2).size());
           assertEquals(0, newColumnValues.get(3).size());
           assertEquals(0, newColumnValues.get(4).size());
           assertEquals(0, newColumnValues.get(5).size());
           assertEquals(0, newColumnValues.get(6).size());
           assertEquals(0, newColumnValues.get(7).size());
           assertEquals(0, newColumnValues.get(8).size());
	   }
	   
	   //TODO findHiddenSingles in col with values:
	   // [[4, 6, 7, 8, 9], [3], [2], [3, 8, 9], [3, 6, 7, 8, 9], [3, 6, 7, 9], [8, 9], [1], [5]]
	   // 4 is a hidden single here and is not currently being found

	  /**
       * Puzzle with hidden pairs example on line 1:
       * https://www.sudokuwiki.org/sudoku.htm?bd=000000000904607000076804100309701080008000300050308702007502610000403208000000000
       * 
       */
	   @Test
	   public void testFindIndexesOfListWhereEachIntExists() {
	       List<String> givenSolutionsShorthand = new ArrayList<>();
           givenSolutionsShorthand.add(".........");
           givenSolutionsShorthand.add("9.46.7...");
           givenSolutionsShorthand.add(".768.41..");
           givenSolutionsShorthand.add("3.97.1.8.");
           givenSolutionsShorthand.add("..8...3..");
           givenSolutionsShorthand.add(".5.3.87.2");
           givenSolutionsShorthand.add("..75.261.");
           givenSolutionsShorthand.add("...4.32.8");
           givenSolutionsShorthand.add(".........");
           
           SudokuGraderApp app = new SudokuGraderApp();
           app.setSudokuGridWithSolutionShorthand(givenSolutionsShorthand);
           app.populateSolutionGridWithStartingPosition();
           app.populateCandidateValues();
           app.printSolutionGrid();
           Map<Integer, List<Integer>> result = app.findIndexesOfListWhereEachIntExists(app.getValuesInRow(0));
	       
           //test locations for 1
           assertEquals(5, result.get(Integer.valueOf(1)).size());
           assertEquals(Arrays.asList(0,1,2,3,4), result.get(Integer.valueOf(1)));
           
           //test locations for 2
           assertEquals(6, result.get(Integer.valueOf(2)).size());
           assertEquals(Arrays.asList(0,1,2,3,4,7), result.get(Integer.valueOf(2)));
           
           //test locations for 3
           assertEquals(5, result.get(Integer.valueOf(3)).size());
           assertEquals(Arrays.asList(1,2,4,7,8), result.get(Integer.valueOf(3)));
	   }
}
