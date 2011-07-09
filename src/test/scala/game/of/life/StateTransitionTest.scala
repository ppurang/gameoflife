package game.of.life

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

/**
 * 
 * @author Piyush Purang
 */

class StateTransitionTest extends FunSuite with ShouldMatchers {
  test("test alive transitions")  {
    StateTransition(Alive)(Vector(Dead, Dead, Dead)) should be(Dead)
    StateTransition(Alive)(Vector(Alive, Alive, Dead)) should be(Alive)
    StateTransition(Alive)(Vector(Alive, Alive, Alive)) should be(Alive)
  }
  test("test dead transitions") {
    StateTransition(Dead)(Vector(Dead, Dead, Dead)) should be(Dead)
    StateTransition(Dead)(Vector(Alive, Alive, Alive)) should be(Alive)
    StateTransition(Dead)(Vector(Alive, Alive, Dead)) should be(Dead)
  }

}