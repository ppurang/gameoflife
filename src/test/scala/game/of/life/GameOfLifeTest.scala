package game.of.life

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

/**
 * 
 * @author Piyush Purang
 */

class GameOfLifeTest extends FunSuite with ShouldMatchers {
  test("alive transitions") {
    val state = Alive
    StateTransition(state)(Vector(Dead, Dead)) should be(Dead)
    StateTransition(state)(Vector(Alive, Alive)) should be(Alive)
    StateTransition(state)(Vector(Alive, Alive, Alive)) should be(Alive)
  }

  test("dead transitions") {
    val state = Dead
    StateTransition(state)(Vector(Dead, Dead)) should be(Dead)
    StateTransition(state)(Vector(Alive, Alive, Dead, Dead, Alive)) should be(Alive)
  }

}