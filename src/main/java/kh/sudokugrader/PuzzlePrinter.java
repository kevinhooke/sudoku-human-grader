package kh.sudokugrader;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PuzzlePrinter {

    private static final Logger LOGGER = LogManager.getLogger();
    
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
