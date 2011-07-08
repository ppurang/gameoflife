package game.of.life

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

/**
 * 
 * @author Piyush Purang
 */

class GameOfLifeSpec extends FunSuite with ShouldMatchers {

  test("given a state and a seq of neighbouring state we should be able to decide on the next state") {
    val alive =  StateTransition(Alive)
    alive(Vector(Alive, Alive, Alive)) should be(Alive)
    alive(Vector(Alive, Alive)) should be(Alive)
    alive(Vector(Alive)) should be(Dead)
    alive(Vector(Alive, Alive, Alive, Alive)) should be(Dead)

    val dead =  StateTransition(Dead)
    dead(Vector(Alive, Alive, Alive)) should be(Alive)
    dead(Vector(Alive, Alive)) should be(Dead)
    dead(Vector(Alive)) should be(Dead)
    dead(Vector(Alive, Alive, Alive, Alive)) should be(Dead)
  }

  test("directions always returns 8 coordinates") {
    val colony: MapRowsColsColony = PrototypeColony(3, 3, GaussianDice)
    val ofCoordinates: Vector[(Int, Int)] = PrototypeColony(3, 3, GaussianDice).neighbourhoodCoordinates(1, 1)
    ofCoordinates.size should be (Directions.size)
  }

  test("colony gets all the nearest neighbours") {
    val colony: MapRowsColsColony = PrototypeColony(3, 3, GaussianDice)
    val cell = colony.cell(2,2)
    val validNeighbours = colony.neighbours(cell)
    validNeighbours.size should be (Directions.size)
  }

  test("colony gets only valid nearest neighbours") {
    val colony: MapRowsColsColony = PrototypeColony(3, 3, GaussianDice)
    val cell = colony.cell(1,1)
    val validNeighbours = colony.neighbours(cell)
    validNeighbours.size should be (3)
  }

  test("test new colony") {
    val colony: Colony = PrimordialSoup(10, 10, GaussianDice)
    println(colony)
    println(colony.mutate)
    0 should be (0)
  }
}