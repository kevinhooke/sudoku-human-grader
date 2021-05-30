package kh.sudokugrader;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PuzzlePrinter {

    private static final Logger LOGGER = LogManager.getLogger();
    
    void printSolutionGrid(List<List<List<Integer>>> grid) {
        LOGGER.trace("Current candidates: ");
        StringBuilder sb = new StringBuilder(120);
        sb.append("\n");
        for (List<List<Integer>> row : grid) {
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
    
    /**
     * Prints the puzzle grid with borders around each square.
     * 
     */
    public void printGridWithBorders(int[][] grid) {
        StringBuilder sb = new StringBuilder(120);
        sb.append("\n+-------+-------+-------+\n");
        for (int row = 0; row < 9; row++) {
            sb.append("| ");
            for (int col = 0; col < 9; col++) {
                int value = grid[row][col];
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

    
    public void printValuesSet(Set<Integer> squareValues) {
        for (Integer i : squareValues) {
            System.out.print(i.toString() + ", ");
        }
        System.out.println();
        System.out.println();
    }

    
}
