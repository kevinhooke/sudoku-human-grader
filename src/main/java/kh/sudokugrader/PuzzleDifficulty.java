package kh.sudokugrader;

/**
 * Metrics for a solved puzzle used to derive the difficuly of a puzzle.
 * 
 * @author kevinhooke
 *
 */
public class PuzzleDifficulty {
    
    public enum DifficultyRating {
        EASY,
        MEDIUM,
        HARD
    }
    
    private boolean puzzleSolved;
    
    private int initialGivens;
    
    //simple techniques count
    private int nakedSingleCount;
    private int hiddenSingleCount;
    
    //medium techniques count
    private int nakedPairsCount;
    private int hiddenPairsCount;
    
    //hard techniques count
    private int nakedTriplesCount;
    private int hiddenTriplesCount;
    private int xWingCount;
    
    /**
     * Returns graded difficulty of the puzzle.
     * @return DifficultyRating
     */
    public DifficultyRating getDifficulty() {
        DifficultyRating rating = null;
        
        if(puzzleSolved && (nakedSingleCount > 0 || hiddenSingleCount > 0 ) && (nakedPairsCount == 0 && hiddenPairsCount == 0)) {
            rating = DifficultyRating.EASY;
        }
        else if(puzzleSolved && (nakedPairsCount > 0 || hiddenPairsCount > 0)){
            rating = DifficultyRating.MEDIUM;
        }
        else{
            rating = DifficultyRating.HARD;
        }
        
        return rating;
    }
    
    public int incrementNakedSingleCount() {
        this.nakedSingleCount++;
        return this.nakedSingleCount;
    }
    
    public int incrementHiddenSingleCount() {
        this.hiddenSingleCount++;
        return this.hiddenSingleCount;
    }
    
    public int getNakedSingleCount() {
        return nakedSingleCount;
    }
    public void setNakedSingleCount(int nakedSingleCount) {
        this.nakedSingleCount = nakedSingleCount;
    }
    
    /**
     * 
     * @return number of hidden singles found in the puzzle
     */
    public int getHiddenSingleCount() {
        return hiddenSingleCount;
    }
    public void setHiddenSingleCount(int hiddenSingleCount) {
        this.hiddenSingleCount = hiddenSingleCount;
    }
    
    /**
     * 
     * @return number of naked pairs found in the puzzle
     */
    public int getNakedPairsCount() {
        return nakedPairsCount;
    }
    public void setNakedPairsCount(int nakedPairsCount) {
        this.nakedPairsCount = nakedPairsCount;
    }
    
    /**
     * 
     * @return number of hidden pairs found in the puzzle
     */
    public int getHiddenPairsCount() {
        return hiddenPairsCount;
    }
    public void setHiddenPairsCount(int hiddenPairsCount) {
        this.hiddenPairsCount = hiddenPairsCount;
    }
    
    /**
     * 
     * @return number of naked triples found in the puzzle
     */
    public int getNakedTriplesCount() {
        return nakedTriplesCount;
    }
    public void setNakedTriplesCount(int nakedTriplesCount) {
        this.nakedTriplesCount = nakedTriplesCount;
    }
    
    /**
     * 
     * @return nmber of hidden triples found in the puzzle
     */
    public int getHiddenTriplesCount() {
        return hiddenTriplesCount;
    }
    public void setHiddenTriplesCount(int hiddenTriplesCount) {
        this.hiddenTriplesCount = hiddenTriplesCount;
    }
    
    
    public int getxWingCount() {
        return xWingCount;
    }
    public void setxWingCount(int xWingCount) {
        this.xWingCount = xWingCount;
    }

    /**
     * 
     * @return true if he puzzle was solved using the human solver approaches
     */
    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    public void setPuzzleSolved(boolean puzzleSolved) {
        this.puzzleSolved = puzzleSolved;
    }

    /**
     * 
     * @return number of givens in the initial puzzle
     */
    public int getInitialGivens() {
        return initialGivens;
    }

    public void setInitialGivens(int initialGivens) {
        this.initialGivens = initialGivens;
    }
    
    public void incrementInitialGivens() {
        this.initialGivens++;
    }
    
}
