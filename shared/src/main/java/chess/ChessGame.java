package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currentTeamColor;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentTeamColor;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTeamColor = team;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
//        ChessPiece currentPiece = this.board.getPiece(move.getStartPosition());
//        Collection<ChessMove> validMoves = currentPiece.pieceMoves(this.board, move.getStartPosition());
//        if (!validMoves.contains(move.getEndPosition())) {
//
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
        TeamColor opposingTeamColor = TeamColor.WHITE;

        if (teamColor == TeamColor.WHITE) {
            opposingTeamColor = TeamColor.BLACK;
        }

        ChessPosition kingPosition = this.board.getKingPosition(teamColor);
        Collection<ChessMove> opposingTeamMoves = this.board.getTeamMoves(opposingTeamColor);

        for (var entry : opposingTeamMoves) {
//            System.out.println("DEBUG 89: " + entry.getEndPosition().toString() + " " + kingPosition.toString() + " " + (Objects.equals(entry.getEndPosition().toString(), kingPosition.toString())));
            if (Objects.equals(entry.getEndPosition(), kingPosition)) {
//                System.out.println("DEBUG: kingpos in oppmoves");
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
        TeamColor opposingTeamColor = TeamColor.WHITE;

        if (teamColor == TeamColor.WHITE) {
            opposingTeamColor = TeamColor.BLACK;
        }

        ChessPosition kingPosition = this.board.getKingPosition(teamColor);
        Collection<ChessMove> kingMoves = this.board.getPiece(kingPosition).pieceMoves(this.board, kingPosition);
        System.out.println("DEBUG 116: kingmoves " + kingMoves.toString());
        int kingMoveSize = kingMoves.size();
        Collection<ChessMove> opposingTeamMoves = this.board.getTeamMoves(opposingTeamColor);

        for (var entry : opposingTeamMoves) {
            for (var kingEntry : kingMoves) {
                if (Objects.equals(entry.getEndPosition(), kingEntry.getEndPosition())) {
                    kingMoves.remove(kingEntry);
                }
            }
        }

        System.out.println("DEBUG 128: kingmoves " + kingMoves.toString());
        return (kingMoveSize > 0) && (kingMoves.size() == 0);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
        return (!this.isInCheck(teamColor)) && (this.board.getTeamMoves(teamColor).size() == 0);
    }


    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
//        throw new RuntimeException("Not implemented");
    }
}
