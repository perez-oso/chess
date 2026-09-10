package chess;

import java.util.*;


/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    Map<ChessPosition, ChessPiece> activePieces;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(activePieces, that.activePieces);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(activePieces);
    }

    public ChessBoard() {
        activePieces = new HashMap<ChessPosition, ChessPiece>() {};
        return;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */

    public void addPiece(ChessPosition position, ChessPiece piece) {
//        throw new RuntimeException("Not implemented");
        this.activePieces.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
//        throw new RuntimeException("Not implemented");
        return this.activePieces.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
//        throw new RuntimeException("Not implemented");
        activePieces.clear();

        PlacePieces(ChessGame.TeamColor.BLACK);
        PlacePieces(ChessGame.TeamColor.WHITE);
    }

    public void PlacePieces(ChessGame.TeamColor color) {
        int row = 8;

        if (color == ChessGame.TeamColor.WHITE) {
            row = 1;
        }

        ChessPiece currentPiece;
        ChessPosition currentPosition;

        currentPiece = new ChessPiece(color, ChessPiece.PieceType.ROOK);
        currentPosition = new ChessPosition(row, 1);
        activePieces.put(currentPosition, currentPiece);
        currentPiece = new ChessPiece(color, ChessPiece.PieceType.ROOK);
        currentPosition = new ChessPosition(row, 8);
        activePieces.put(currentPosition, currentPiece);

        currentPiece = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        currentPosition = new ChessPosition(row, 2);
        activePieces.put(currentPosition, currentPiece);
        currentPiece = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        currentPosition = new ChessPosition(row, 7);
        activePieces.put(currentPosition, currentPiece);

        currentPiece = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        currentPosition = new ChessPosition(row, 3);
        activePieces.put(currentPosition, currentPiece);
        currentPiece = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        currentPosition = new ChessPosition(row, 6);
        activePieces.put(currentPosition, currentPiece);

        currentPiece = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
        currentPosition = new ChessPosition(row, 4);
        activePieces.put(currentPosition, currentPiece);

        currentPiece = new ChessPiece(color, ChessPiece.PieceType.KING);
        currentPosition = new ChessPosition(row, 5);
        activePieces.put(currentPosition, currentPiece);

        row = 7;

        if (color == ChessGame.TeamColor.WHITE) {
            row = 2;
        }

        for (int col = 1; col <= 8; col++) {
            currentPiece = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            currentPosition = new ChessPosition(row, col);
            activePieces.put(currentPosition, currentPiece);
        }
    }
}
