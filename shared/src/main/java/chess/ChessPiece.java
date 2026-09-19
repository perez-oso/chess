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
        ChessGame.TeamColor thisColor = this.getTeamColor();

        switch (this.pieceType) {
            case PieceType.PAWN:
                int inc = -1;
                Collection<ChessMove> pawnMovesBeforePromotion = new ArrayList<ChessMove>() {};

                if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    inc = 1;
                }

                testPosition = new ChessPosition(myPosition.getRow() + inc, myPosition.getColumn());
                if (board.getPiece(testPosition) == null) {
                    pawnMovesBeforePromotion.add(new ChessMove(myPosition, testPosition, null));
                } // if space in front is empty

                testPosition = new ChessPosition(myPosition.getRow() + inc, myPosition.getColumn() - inc);
                if (board.getPiece(testPosition) != null && (board.getPiece(testPosition).getTeamColor() != thisColor)) {
                    pawnMovesBeforePromotion.add(new ChessMove(myPosition, testPosition, null)); // add it to the list
//                    System.out.println("DEBUG: " + this.getTeamColor() + " " + this.pieceType + " - " + board.getPiece(testPosition).getTeamColor() + " " + board.getPiece(testPosition).pieceType);
                } // if first diagonal is empty or capturable

                testPosition = new ChessPosition(myPosition.getRow() + inc, myPosition.getColumn() + inc);
                if (board.getPiece(testPosition) != null && (board.getPiece(testPosition).getTeamColor() != thisColor)) {
                    pawnMovesBeforePromotion.add(new ChessMove(myPosition, testPosition, null)); // add it to the list
//                    System.out.println("DEBUG: " + this.getTeamColor() + " " + this.pieceType + " - " + board.getPiece(testPosition).getTeamColor() + " " + board.getPiece(testPosition).pieceType);
                } // if second diagonal is empty or capturable

                if ((this.getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2) ||
                        (this.getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7)) { // if promotion is possible
                    for (ChessMove move : pawnMovesBeforePromotion) {
                        possibleMoves.add(new ChessMove(move.start, move.end, PieceType.QUEEN));
                        possibleMoves.add(new ChessMove(move.start, move.end, PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(move.start, move.end, PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(move.start, move.end, PieceType.ROOK));
                    }
                } else {
                    possibleMoves = pawnMovesBeforePromotion;
                }

                if ((this.getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7) ||
                        (this.getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2)) { // if 2-square advance is permissible
                    testPosition = new ChessPosition(myPosition.getRow() + 2 * inc, myPosition.getColumn());
                    ChessPosition oneAhead = new ChessPosition(myPosition.getRow() + inc, myPosition.getColumn());

                    if (board.getPiece(testPosition) == null && board.getPiece(oneAhead) == null) {
                        possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    }
                }
                break;
            case PieceType.ROOK:
                RookTests(board, myPosition, possibleMoves);
                break;
            case PieceType.KNIGHT:
                int row, col;

                int[][] xyKnight = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

                for (int i = 0; i < 8; i++) {
                    row = myPosition.getRow() + xyKnight[i][0];
                    col = myPosition.getColumn() + xyKnight[i][1];

                    if (0 < row && row <= 8 && 0 < col && col <= 8) {
                        testPosition = new ChessPosition(row, col);
                        if (board.getPiece(testPosition) == null || board.getPiece(testPosition).getTeamColor() != this.getTeamColor()) {
                            possibleMoves.add(new ChessMove(myPosition, testPosition, null)); // add it to the list
                        }
                    }
                }
                break;
            case PieceType.BISHOP:
                BishopTests(board, myPosition, possibleMoves);
                break;
            case PieceType.KING:
                int[][] xyKing = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                int x, y;

                for (int i = 0; i < 8; i++) {
                    x = myPosition.getRow() + xyKing[i][0];
                    y = myPosition.getColumn() + xyKing[i][1];

                    if (0 < x && x < 9 && 0 < y && y < 9) {
                        testPosition = new ChessPosition(x, y);
                        if (board.getPiece(testPosition) == null || board.getPiece(testPosition).getTeamColor() != this.getTeamColor()) {
                            possibleMoves.add(new ChessMove(myPosition, testPosition, null)); // add it to the list
                        }
                    }
                }

                break;
            case PieceType.QUEEN:
                RookTests(board, myPosition, possibleMoves);
                BishopTests(board, myPosition, possibleMoves);
        }

            return possibleMoves;
    }

        void RookTests(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
            ChessPosition testPosition;

            for (int i = myPosition.getRow() + 1; i <= 8; i++) {
                testPosition = new ChessPosition(i, myPosition.getColumn());

                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                } else if (board.getPiece(testPosition).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i = myPosition.getRow() - 1; i > 0; i--) {
                testPosition = new ChessPosition(i, myPosition.getColumn());

                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                } else if (board.getPiece(testPosition).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i = myPosition.getColumn() + 1; i <= 8; i++) {
                testPosition = new ChessPosition(myPosition.getRow(), i);

                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                } else if (board.getPiece(testPosition).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i = myPosition.getColumn() - 1; i > 0; i--) {
                testPosition = new ChessPosition(myPosition.getRow(), i);

                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                } else if (board.getPiece(testPosition).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    break;
                } else {
                    break;
                }
            }
    }

        void BishopTests(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
            ChessPosition testPosition;

            for (int i = myPosition.getRow() + 1, j = myPosition.getColumn() + 1; i <= 8 && j <= 8; i++, j++) {
                testPosition = new ChessPosition(i, j);

                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                } else if (board.getPiece(testPosition).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i = myPosition.getRow() - 1, j = myPosition.getColumn() - 1; i > 0 && j > 0; i--, j--) {
                    testPosition = new ChessPosition(i, j);

                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                } else if (board.getPiece(testPosition).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i = 1; (myPosition.getRow() + i <= 8) && (myPosition.getColumn() - i > 0); i++) {
                testPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);

                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                } else if (board.getPiece(testPosition).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    break;
                } else {
                    break;
                }
            }

            for (int i = 1; (myPosition.getRow() - i > 0) && (myPosition.getColumn() + i <= 8); i++) {
                testPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);

                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                } else if (board.getPiece(testPosition).getTeamColor() != this.getTeamColor()){
                    possibleMoves.add(new ChessMove(myPosition, testPosition, null));
                    break;
                } else {
                    break;
                }
            }
        }
}
