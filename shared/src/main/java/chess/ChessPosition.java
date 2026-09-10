package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return r == that.r && c == that.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }

    int r, c;

    public ChessPosition(int row, int col) {
        this.r = row;
        this.c = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
//        throw new RuntimeException("Not implemented");
        return this.r;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
//        throw new RuntimeException("Not implemented");
        return this.c;
    }

    @Override
    public String toString() {
        return "(" + this.getRow() + ", " +  this.getColumn() + ")";
    }
}
