package coderetreat

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

/**
 * 
 * @author Piyush Purang
 */

class GameOfLifeTest extends FunSuite with ShouldMatchers {
  test("test statetransition"){
    val state = Alive
    StateTransition(state)(Vector(Dead, Dead, Alive)) should be(Dead)
    StateTransition(state)(Vector(Alive, Alive, Alive, Alive)) should be(Dead)
    StateTransition(state)(Vector(Alive, Alive, Dead, Dead)) should be(Alive)
    StateTransition(state)(Vector(Alive, Alive, Alive, Dead)) should be(Alive)

    val dead = Dead
    StateTransition(dead)(Vector(Alive, Alive, Alive, Dead)) should be(Alive)
    StateTransition(dead)(Vector(Alive, Alive, Alive, Alive)) should be(Dead)
    StateTransition(dead)(Vector(Alive, Alive, Dead, Dead)) should be(Dead)
    StateTransition(dead)(Vector(Dead, Dead, Dead, Dead)) should be(Dead)
  }

}