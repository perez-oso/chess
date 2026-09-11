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
                int inc = -1;
                if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    inc = 1;
                }

                testPosition = new ChessPosition(myPosition.getRow() + inc, myPosition.getColumn());
                if (board.getPiece(testPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } // if space in front is empty

                testPosition = new ChessPosition(myPosition.getRow() + inc, myPosition.getColumn() - inc);
                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType)); // add it to the list
                } // if left diagonal is empty or capturable

                testPosition = new ChessPosition(myPosition.getRow() + inc, myPosition.getColumn() + inc);
                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType)); // add it to the list
                } // if right diagonal is empty or capturable

                if (myPosition.getRow() % 5 == 2) { // if 2-square advance is permissible
                    testPosition = new ChessPosition(myPosition.getRow() + 2 * inc, myPosition.getColumn());

                    if (board.getPiece(testPosition) == null) {
                        possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                    }
                }

            case PieceType.ROOK:
                RookTests(board, myPosition, possibleMoves);

            case PieceType.KNIGHT:
                int row, col;

                int[][] offsets = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

                for (int i = 0; i < 8; i++) {
                    row = myPosition.getRow() + offsets[i][0];
                    col = myPosition.getColumn() + offsets[i][1];

                    if (0 < row && row <= 8 && 0 < col && col <= 8) {
                        testPosition = new ChessPosition(row, col);
                        if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                            possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType)); // add it to the list
                        }
                    }
                }
            case PieceType.BISHOP:
                BishopTests(board, myPosition, possibleMoves);

            case PieceType.KING:
                if (myPosition.getRow() < 8) {
                    testPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                        possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType)); // add it to the list
                    }
                }

                if (myPosition.getRow() > 0) {
                    testPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                        possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType)); // add it to the list
                    }
                }

                if (myPosition.getColumn() < 8) {
                    testPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                    if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                        possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType)); // add it to the list
                    }
                }

                if (myPosition.getColumn() > 0) {
                    testPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                    if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                        possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType)); // add it to the list
                    }
                }
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

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } else {
                    break;
                }
            }

            for (int i = myPosition.getRow() - 1; i > 0; i--) {
                testPosition = new ChessPosition(i, myPosition.getColumn());

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } else {
                    break;
                }
            }

            for (int i = myPosition.getColumn() + 1; i <= 8; i++) {
                testPosition = new ChessPosition(myPosition.getColumn(), i);

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } else {
                    break;
                }
            }

            for (int i = myPosition.getColumn() - 1; i > 0; i--) {
                testPosition = new ChessPosition(myPosition.getColumn(), i);

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } else {
                    break;
                }
            }
    }

        void BishopTests(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
            ChessPosition testPosition;

            for (int i = myPosition.getRow() + 1; i <= 8; i++) {
                testPosition = new ChessPosition(i, i);

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } else {
                    break;
                }
            }

            for (int i = myPosition.getRow() - 1; i > 0; i--) {
                testPosition = new ChessPosition(i, i);

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } else {
                    break;
                }
            }

            for (int i = 1; (myPosition.getRow() + 1 <= 8) && (myPosition.getColumn() - i > 0); i++) {
                testPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } else {
                    break;
                }
            }

            for (int i = 1; (myPosition.getRow() - 1 > 0) && (myPosition.getColumn() + i <= 8); i++) {
                testPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);

                if (board.getPiece(testPosition) == null || board.getPiece(testPosition).pieceType != this.pieceType) {
                    possibleMoves.add(new ChessMove(myPosition, testPosition, this.pieceType));
                } else {
                    break;
                }
            }
        }
}
