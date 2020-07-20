package kh.sudokugrader;

public class PuzzleDifficulty {
    
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
    public int getHiddenSingleCount() {
        return hiddenSingleCount;
    }
    public void setHiddenSingleCount(int hiddenSingleCount) {
        this.hiddenSingleCount = hiddenSingleCount;
    }
    public int getNakedPairsCount() {
        return nakedPairsCount;
    }
    public void setNakedPairsCount(int nakedPairsCount) {
        this.nakedPairsCount = nakedPairsCount;
    }
    public int getHiddenPairsCount() {
        return hiddenPairsCount;
    }
    public void setHiddenPairsCount(int hiddenPairsCount) {
        this.hiddenPairsCount = hiddenPairsCount;
    }
    public int getNakedTriplesCount() {
        return nakedTriplesCount;
    }
    public void setNakedTriplesCount(int nakedTriplesCount) {
        this.nakedTriplesCount = nakedTriplesCount;
    }
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

    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    public void setPuzzleSolved(boolean puzzleSolved) {
        this.puzzleSolved = puzzleSolved;
    }

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
