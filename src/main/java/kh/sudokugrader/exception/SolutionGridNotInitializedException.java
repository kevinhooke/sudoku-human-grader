package kh.sudokugrader.exception;

public class SolutionGridNotInitializedException extends RuntimeException {
    public SolutionGridNotInitializedException() {
        super("Did you forget to call populateSolutionGridWithStartingPosition() ?");
    }
}
