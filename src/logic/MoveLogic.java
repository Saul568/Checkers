package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Game;

/**
 * Determines the validity of moves in a checkers game.
 */
public class MoveLogic {

  /**
   * Checks if the move is legal in the current game context.
   */
  public static boolean isValidMove(Game game, int startIndex, int endIndex) {
    return game != null && isValidMove(
      game.getBoard(),
      game.isP1Turn(),
      startIndex,
      endIndex,
      game.getSkipIndex()
    );
  }

  /**
   * Validates the move based on checkers' rules and the state of the board.
   */
  public static boolean isValidMove(
    Board board,
    boolean isP1Turn,
    int startIndex,
    int endIndex,
    int skipIndex
  ) {
    if (board == null || startIndex == endIndex) {
      return false;
    }
    if (Board.isValidIndex(skipIndex) && skipIndex != startIndex) {
      return false;
    }
    return validateIDs(board, isP1Turn, startIndex, endIndex) &&
           validateDistance(board, isP1Turn, startIndex, endIndex);
  }

  /**
   * Ensures that the start and end IDs match the current player and that skips are legal.
   */
  private static boolean validateIDs(Board board, boolean isP1Turn, int startIndex, int endIndex) {
    if (board.get(endIndex) != Board.EMPTY) {
      return false;
    }
    int id = board.get(startIndex);
    boolean isPlayerOneChecker = isP1Turn && (id == Board.BLACK_CHECKER || id == Board.BLACK_KING);
    boolean isPlayerTwoChecker = !isP1Turn && (id == Board.WHITE_CHECKER || id == Board.WHITE_KING);
    if (!isPlayerOneChecker && !isPlayerTwoChecker) {
      return false;
    }

    Point middle = Board.middle(startIndex, endIndex);
    int midID = board.get(Board.toIndex(middle));
    boolean isMiddleOpponent = midID != Board.INVALID &&
                               ((isP1Turn && (midID == Board.WHITE_CHECKER || midID == Board.WHITE_KING)) ||
                                (!isP1Turn && (midID == Board.BLACK_CHECKER || midID == Board.BLACK_KING)));
    return !isMiddleOpponent;
  }

  /**
   * Confirms that the move is diagonal, within proper range, and adheres to the direction rules.
   */
  private static boolean validateDistance(Board board, boolean isP1Turn, int startIndex, int endIndex) {
    Point start = Board.toPoint(startIndex);
    Point end = Board.toPoint(endIndex);
    int dx = end.x - start.x;
    int dy = end.y - start.y;
    if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) > 2) {
      return false;
    }
    
    int id = board.get(startIndex);
    if ((id == Board.WHITE_CHECKER && dy > 0) || (id == Board.BLACK_CHECKER && dy < 0)) {
      return false;
    }

    if (Math.abs(dx) == 1 && hasAvailableSkips(board, isP1Turn)) {
      return false;
    }
    return true;
  }

  /**
   * Checks if there are skips available for any of the player's checkers.
   */
  private static boolean hasAvailableSkips(Board board, boolean isP1Turn) {
    List<Point> checkers = isP1Turn ? board.find(Board.BLACK_CHECKER) : board.find(Board.WHITE_CHECKER);
    checkers.addAll(isP1Turn ? board.find(Board.BLACK_KING) : board.find(Board.WHITE_KING));
    for (Point p : checkers) {
      if (!MoveGenerator.getSkips(board, Board.toIndex(p)).isEmpty()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if a checker is in a position where it cannot be skipped.
   */
  public static boolean isSafe(Board board, Point checker) {
    if (board == null || checker == null) {
      return true;
    }
    int index = Board.toIndex(checker);
    if (board.get(index) == Board.EMPTY) {
      return true;
    }
    return !canBeSkipped(board, checker);
  }

  /**
   * Evaluates if a checker can be skipped by an opponent.
   */
  private static boolean canBeSkipped(Board board, Point checker) {
    int id = board.get(Board.toIndex(checker));
    boolean isBlack = (id == Board.BLACK_CHECKER || id == Board.BLACK_KING);
    List<Point> adjacentPoints = new ArrayList<>();
    MoveGenerator.addPoints(adjacentPoints, checker, Board.BLACK_KING, 1);
    for (Point p : adjacentPoints) {
      if (isOpponentChecker(board, p, isBlack) && isValidSkipPosition(board, checker, p)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if a point contains an opponent's checker.
   */
  private static boolean isOpponentChecker(Board board, Point p, boolean isBlack) {
    int tid = board.get(Board.toIndex(p));
    return (isBlack && (tid == Board.WHITE_CHECKER || tid == Board.WHITE_KING)) ||
           (!isBlack && (tid == Board.BLACK_CHECKER || tid == Board.BLACK_KING));
  }

  /**
   * Validates if a skip from the given point is legal.
   */
  private static boolean isValidSkipPosition(Board board, Point checker, Point opponentChecker) {
    int dx = (checker.x - opponentChecker.x) * 2;
    int dy = (checker.y - opponentChecker.y) * 2;
    boolean isOpponentKing = (board.get(Board.toIndex(opponentChecker)) == Board.BLACK_KING ||
                              board.get(Board.toIndex(opponentChecker)) == Board.WHITE_KING);
    if (!isOpponentKing && (isOpponentChecker(board, opponentChecker, true) ^ (dy < 0))) {
      return false;
    }
    int endIndex = Board.toIndex(new Point(opponentChecker.x + dx, opponentChecker.y + dy));
    return MoveGenerator.isValidSkip(board, Board.toIndex(opponentChecker), endIndex);
  }
}
