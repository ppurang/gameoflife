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

  test("given a lifecell we should be able to deduce its next stage") {
    val clock = BigBang
    val perfectCell = LifeCell(Alive, Vector(LifeCell(Alive, Vector(), clock),LifeCell(Alive, Vector(), clock),LifeCell(Alive, Vector(), clock)), clock)
    TimedLifeCycle(perfectCell)(clock tik).state should be (Alive)
    val overcrowdedCell = LifeCell(Alive, Vector(LifeCell(Alive, Vector(), clock),LifeCell(Alive, Vector(), clock),LifeCell(Alive, Vector(), clock),LifeCell(Alive, Vector(), clock)), clock)
    TimedLifeCycle(overcrowdedCell)(clock.tik).state should be (Dead)
    val deadCell = LifeCell(Alive, Vector(LifeCell(Alive, Vector(), clock),LifeCell(Alive, Vector(), clock),LifeCell(Alive, Vector(), clock)), clock)
    TimedLifeCycle(deadCell)(clock.tik).state should be (Alive)
  }

  test("directions always returns 8 coordinates") {
    val ofCoordinates: Vector[(Int, Int)] = NewColony(3, 3).neighbourhoodCoordinates(1, 1)
    ofCoordinates.size should be (Directions.size)
  }

  test("colony gets all the nearest neighbours") {
    val colony: NewColony = NewColony(3, 3)
    val cell = colony.cell(2,2)
    val validNeighbours = colony.neighbours(cell).filter(
      _ match {
        case Some(x) => true
        case _ => false
      }
    )
    validNeighbours.size should be (Directions.size)
  }

  test("colony gets only valid nearest neighbours") {
    val colony: NewColony = NewColony(3, 3)
    val cell = colony.cell(1,1)
    val validNeighbours = colony.neighbours(cell).filter(
      _ match {
        case Some(x) => true
        case _ => false
      }
    )
    validNeighbours.size should be (3)
  }


  test("test new colony") {
    val colony: ModelColony = PrimordialSoup(10, 10, GaussianDice)
    println(colony)
    println(colony.mutate)
    0 should be (0)
  }



}