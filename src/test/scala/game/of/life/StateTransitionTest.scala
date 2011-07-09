package game.of.life

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import game.of.life._

/**
 * 
 * @author Piyush Purang
 */

class StateTransitionTest extends FunSuite with ShouldMatchers {
  test("test alive transitions") {
    val state = Alive
    StateTransition(state)(Vector(Dead, Dead, Dead)) should be (Dead)
    StateTransition(state)(Vector(Alive, Alive, Dead)) should be (Alive)
    StateTransition(state)(Vector(Alive, Alive, Alive, Dead, Dead)) should be (Alive)
    StateTransition(state)(Vector(Alive, Alive, Alive, Alive, Dead)) should be (Dead)
  }

  test("test dead transitions") {
    val state = Dead
    StateTransition(state)(Vector(Dead, Dead, Dead)) should be (Dead)
    StateTransition(state)(Vector(Alive, Alive, Dead)) should be (Dead)
    StateTransition(state)(Vector(Alive, Alive, Alive, Dead, Dead)) should be (Alive)
    StateTransition(state)(Vector(Alive, Alive, Alive, Alive, Dead)) should be (Dead)
  }

}