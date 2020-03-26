/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.List;

import static loa.Piece.*;

/** An automated Player.
 *  @author Zachary Zhu
 */

/**
 * Stuff to consider
 * 1. Some combinations of moves lead to the same board state.
 * 2. Alpha-beta pruning??
 * 3.
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

        // Working with a depth of one
        List<Move> potentialMoves = board.legalMoves();
        if (board.turn() == BP) {
            int minHeuristic = Integer.MAX_VALUE;
            Move minMove = null;
            for (int i = 0; i < potentialMoves.size(); i += 1) {
                board.makeMove(potentialMoves.get(i));
                int heuristicValue = heuristic(board);
                if (heuristicValue < minHeuristic) {
                    minMove = potentialMoves.get(i);
                    minHeuristic = heuristicValue;
                }
                board.retract();
            }
            if (minHeuristic != Integer.MIN_VALUE && saveMove) {
                _foundMove = minMove;
            }
            return minHeuristic;
        } else {
            int maxHeuristic = Integer.MIN_VALUE;
            Move maxMove = null;
            for (int i = 0; i < potentialMoves.size(); i += 1) {
                board.makeMove(potentialMoves.get(i));
                int heuristicValue = heuristic(board);
                if (heuristicValue > maxHeuristic) {
                    maxMove = potentialMoves.get(i);
                    maxHeuristic = heuristicValue;
                }
                board.retract();
            }
            if (maxHeuristic != Integer.MIN_VALUE && saveMove) {
                _foundMove = maxMove;
            }
            return maxHeuristic;
        }
        /** Original Code
         * // FIXME
        if (saveMove) {
            _foundMove = null; // FIXME
        }
        return 0; // FIXME **/
    }

    /**
     * Method for evaluating the effectiveness of the new move.
     * @param board referring to state of board
     * @return a number that refers to how beneficial the new state of board is.
     */
    public static int heuristic(Board board) {
        /** This heuristic should evaluate the efficacy of each potential move.
         * Must return -INF if winning move for BLACK, +INF if winning move for WHITE.
         * Characteristics to consider:
         * 1. Difference in number of pieces--(white - black)   (more is better?)
         * 2. Dif. in number of regions formed by # of pieces--(# white regions - # black regions)   (fewer is better)
         * 3. Number of possible moves--(more for white/fewer for black is better for white)
         */
        int heuristic = 0;
        if (board.winner() == BP) {
            return Integer.MIN_VALUE;
        } else if (board.winner() == WP) {
            return Integer.MAX_VALUE;
        } else {
            List<Integer> whiteRegions = board.getRegionSizes(WP);
            List<Integer> blackRegions = board.getRegionSizes(BP);

            // Getting total # of black/white pieces
            int totalWhitePieces = 0, totalBlackPieces = 0;
            for (int i = 0; i < whiteRegions.size(); i += 1) {
                totalWhitePieces += whiteRegions.get(i);
            }
            for (int i = 0; i < blackRegions.size(); i += 1) {
                totalBlackPieces += blackRegions.get(i);
            }
            // Adding first metric
            heuristic += totalWhitePieces - totalBlackPieces;
            // Adding second metric
            heuristic += blackRegions.size() - whiteRegions.size();
            // Adding third metric
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
        // FIXME -- Need to experiment with the depth
        return 1;
    }

    // FIXME: Other methods, variables here.

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
