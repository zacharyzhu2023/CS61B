/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.List;
import java.util.Random;

import static loa.Piece.*;

/** An automated Player.
 *  @author Zachary Zhu
 */

class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (depth == 0 || board.gameOver()) {
            return heuristic(board);
        }
        int bestScore;
        if (sense == -1) {
            bestScore = Integer.MAX_VALUE;
        } else {
            bestScore = Integer.MIN_VALUE;
        }
        List<Move> potentialMoves = board.legalMoves();
        for (Move mv : potentialMoves) {
            board.makeMove(mv);
            int score = findMove(board,
                    depth - 1, false, -1 * sense, alpha, beta);
            if ((score >= bestScore && sense == 1)
                    || (score <= bestScore && sense == -1)) {
                bestScore = score;
                if (saveMove) {
                    _foundMove = mv;
                }
            }
            if (sense == 1) {
                alpha = Math.max(score, alpha);
            } else {
                beta = Math.min(score, beta);
            }
            if (alpha >= beta) {
                board.retract();
                break;
            }
            board.retract();
        }
        return bestScore;
    }

    /**
     * Returns an integer evaluation of the board for testing purposes.
     * @param board for the board to be evaluated.
     * @return an integer evaluation of the board
     */
    public static int mockHeuristic(Board board) {
        /**
         * Use this for testing purposes: generate a random integer
         * so as to not worry about the calculation method for the heuristic.
         * Instead, focus on making sure alpha/beta pruning works
         * Generates random INTEGER from -10 to 10
         */
        if (board.gameOver() && board.winner() == WP) {
            return INFTY;
        } else if (board.gameOver() && board.winner() == BP) {
            return -1 * INFTY;
        }
        return new Random().nextInt(20) - 10;
    }

    /**
     * Method for evaluating the effectiveness of the new move.
     * @param board referring to state of board
     * @return a number that refers to how beneficial the new state of board is.
     */
    public static int heuristic(Board board) {
        /** This heuristic should evaluate the efficacy of each potential move.
         * Must return -INF if winning move for BLACK, +INF
         * if winning move for WHITE.
         * Characteristics to consider:
         * 1. Difference in number of pieces--(white - black)
         *      (more is better?)
         * 2. Dif. in number of regions formed by # of pieces
         *      (# white regions - # black regions) --> (fewer is better)
         * 3. Number of possible moves
         *      (more for white/fewer for black is better for white)
         */
        int heuristic = 0;
        if (board.winner() == BP) {
            return -1 * INFTY;
        } else if (board.winner() == WP) {
            return INFTY;
        } else {
            List<Integer> whiteRegions = board.getRegionSizes(WP);
            List<Integer> blackRegions = board.getRegionSizes(BP);

            int totalWhitePieces = 0, totalBlackPieces = 0;
            for (int i = 0; i < whiteRegions.size(); i += 1) {
                totalWhitePieces += whiteRegions.get(i);
            }
            for (int i = 0; i < blackRegions.size(); i += 1) {
                totalBlackPieces += blackRegions.get(i);
            }

            heuristic += totalWhitePieces - totalBlackPieces;

            heuristic += blackRegions.size() - whiteRegions.size();

            if (board.turn() == WP) {
                heuristic += board.legalMoves().size();
            } else if (board.turn() == BP) {
                heuristic -= board.legalMoves().size();
            }
            return heuristic;
        }
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 3;
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
