package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentTeamColor == chessGame.currentTeamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTeamColor);
    }

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.currentTeamColor = TeamColor.WHITE;
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
        Collection<ChessMove> possibleMoves = this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        return pareInvalidMoves(possibleMoves, this.getBoard().getPiece(startPosition).getTeamColor());
//        throw new RuntimeException("Not implemented");
    }

    public Collection<ChessMove> validTeamMoves(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = this.board.getTeamMoves(teamColor);
        return pareInvalidMoves(possibleMoves, teamColor);
    }

    public Collection<ChessMove> pareInvalidMoves(Collection<ChessMove> possibleMoves, TeamColor  teamColor) {
        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessBoard oldBoard = this.getBoard().Copy();

        for (var move : possibleMoves) {
            this.setBoard(this.getBoard().moveCopy(move));
            if (!this.isInCheck(teamColor)) {
                validMoves.add(move);
            }

            this.setBoard(oldBoard);
        }

        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    //must account for moves that would result in check
    public void makeMove(ChessMove move) throws InvalidMoveException {
//        throw new RuntimeException("Not implemented");

        ChessPiece movedPiece = this.getBoard().getPiece(move.getStartPosition());

        if (movedPiece == null) throw new InvalidMoveException("There is no piece at this location.");
        if (movedPiece.getTeamColor() != this.currentTeamColor) throw new InvalidMoveException("It is " + this.currentTeamColor.toString() + "'s turn.");

        Collection<ChessMove> validMoves = this.pareInvalidMoves(movedPiece.pieceMoves(this.getBoard(), move.getStartPosition()), movedPiece.getTeamColor());

        if (!validMoves.contains(move)) throw new InvalidMoveException("This move is not valid.");

//        System.out.println("DEBUG 104: \n" + this.getBoard().toString());
        this.setBoard(this.getBoard().moveCopy(move));

        if (move.getPromotionPiece() != null) {
            movedPiece.promotePawn(move.getPromotionPiece(), move.getEndPosition());
        }
//        System.out.println("DEBUG 109: \n" + this.getBoard().toString());

        if (this.currentTeamColor == TeamColor.WHITE) {
            this.setTeamTurn(TeamColor.BLACK);
        } else {
            this.setTeamTurn(TeamColor.WHITE);
        }
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
        return (this.isInCheck(teamColor) && this.validTeamMoves(teamColor).isEmpty());
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
        return (!this.isInCheck(teamColor)) && (this.validTeamMoves(teamColor).isEmpty());
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
