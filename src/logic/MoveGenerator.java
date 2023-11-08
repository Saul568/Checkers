package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.Board;

/**
 * Generates possible moves or skips for checkers on a board.
 */
public class MoveGenerator {

  /**
   * Finds move end-points from a start point on the board.
   */
  public static List<Point> getMoves(Board board, Point start) {
    return getMoves(board, Board.toIndex(start));
  }

  /**
   * Finds move end-points from a start index on the board.
   */
  public static List<Point> getMoves(Board board, int startIndex) {
    List<Point> endPoints = new ArrayList<>();
    if (board == null || !Board.isValidIndex(startIndex)) {
      return endPoints;
    }

    int id = board.get(startIndex);
    Point p = Board.toPoint(startIndex);
    addPoints(endPoints, p, id, 1);

    // Filter out points that are not empty
    endPoints.removeIf(end -> board.get(end.x, end.y) != Board.EMPTY);

    return endPoints;
  }

  /**
   * Finds skip end-points from a start point on the board.
   */
  public static List<Point> getSkips(Board board, Point start) {
    return getSkips(board, Board.toIndex(start));
  }

  /**
   * Finds skip end-points from a start index on the board.
   */
  public static List<Point> getSkips(Board board, int startIndex) {
    List<Point> endPoints = new ArrayList<>();
    if (board == null || !Board.isValidIndex(startIndex)) {
      return endPoints;
    }

    int id = board.get(startIndex);
    Point p = Board.toPoint(startIndex);
    addPoints(endPoints, p, id, 2);

    // Remove invalid skip points
    endPoints.removeIf(end -> !isValidSkip(board, startIndex, Board.toIndex(end)));

    return endPoints;
  }

  /**
   * Validates a potential skip move.
   */
  public static boolean isValidSkip(Board board, int startIndex, int endIndex) {
    if (board == null || board.get(endIndex) != Board.EMPTY) {
      return false;
    }

    int id = board.get(startIndex);
    int midID = board.get(Board.toIndex(Board.middle(startIndex, endIndex)));

    // Check for validity of the skip based on checker and king IDs
    return !(id == Board.INVALID || id == Board.EMPTY || midID == Board.INVALID || midID == Board.EMPTY) &&
           (midID == Board.BLACK_CHECKER || midID == Board.BLACK_KING) != 
           (id == Board.WHITE_CHECKER || id == Board.WHITE_KING);
  }

  /**
   * Adds potential points for moves or skips to a list.
   */
  public static void addPoints(List<Point> points, Point p, int id, int delta) {
    boolean isKing = (id == Board.BLACK_KING || id == Board.WHITE_KING);
    if (isKing || id == Board.BLACK_CHECKER) {
      points.add(new Point(p.x + delta, p.y + delta));
      points.add(new Point(p.x - delta, p.y + delta));
    }
    if (isKing || id == Board.WHITE_CHECKER) {
      points.add(new Point(p.x + delta, p.y - delta));
      points.add(new Point(p.x - delta, p.y - delta));
    }
  }
}
