package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, pieceType);
    }

    ChessGame.TeamColor color;
    ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
//        throw new RuntimeException("Not implemented");
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
//        throw new RuntimeException("Not implemented");
        return this.pieceType;
    }

    /*
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
//        throw new RuntimeException("Not implemented");
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>() {};

        ChessPosition testPosition;

        switch (this.pieceType) {
            case PieceType.PAWN:
                testPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    //                possibleMoves.add(new ChessMove(myPosition, testPosition, ??)) // add it to the list
                }
            case PieceType.ROOK:
            case PieceType.KNIGHT:
            case PieceType.BISHOP:
            case PieceType.KING:
            case PieceType.QUEEN:
                throw new RuntimeException("Not implemented");
        }

        return possibleMoves;
    }
}
