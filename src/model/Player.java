/* Name: Player
 * Author: Saul Lara
 * Description: This class represents a player of the system.
 */

package model;

public abstract class Player {

  public abstract boolean isHuman();

  public abstract void updateGame(Game game);

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[isHuman=" + isHuman() + "]";
  }
}
