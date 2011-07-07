package game.of.life

import java.lang.AssertionError
import java.util.Random

object `package` {
  type Neighbours = Seq[LifeCell]
  type NewNeighbours = Seq[NewLifeCell]
  type NeighbourhoodState = Seq[State]
  type Rows = Int
  type Cols = Int
  case class Clock(time: Long) {
    def tik() = {
      Clock(time+1)
    }
  }

  type Dice = () => State
  private val random = new Random(System.nanoTime());

  implicit val DefaultDice = () => {
    random.nextDouble() match {
      case x if x > 0.5 => Alive
      case _ => Dead
    }

  }

  val GaussianDice = () => {
    random.nextGaussian() match {
      case x if x > 0.5 => Alive
      case _ => Dead
    }
  }

  sealed trait State
  case object Dead extends State {
    override def toString = " "
  }
  case object Alive extends State {
    override def toString = "*"
  }

  object BigBang extends Clock(0)
  val Directions : Vector[Direction]= Vector(North, NorthEast, East, SouthEast, South, SouthWest, West, NorthWest)
}

object NewStateTransition extends (State => NeighbourhoodState => State)   {
  def apply(state: State) = (n: NeighbourhoodState) => state match {
    case Alive => n.filter(_ == Alive).size match {
      case x: Int if !(x == 2 || x == 3) => Dead
      case _ => state
    }
    case Dead =>  n.filter(_ == Alive).size match {
      case x: Int if x == 3 => Alive
      case _ => state
    }
  }
}

object NewTimedLifeCycle extends (NewLifeCell => (Clock, NewNeighbours) => NewLifeCell)   {
  def apply(cell: NewLifeCell) =  {
    (clock: Clock, neighbours: NewNeighbours) =>  {
      if (cell.clock.time == clock.time) {
        println("Dude you are too late")
        cell
      } else if (cell.clock.time < clock.time) {
        NewLifeCell(cell.row, cell.col, StateTransition(cell.state)(neighbours.map(_.state)), clock)
      } else {
        throw new AssertionError("Can't yet travel back in time!")
      }
    }
  }
}

trait ModelColony {
  val clock: Clock
  def mutate: ModelColony
}
object PrimordialSoup extends ((Rows, Cols, Dice) => ModelColony ){
  private case class SomeColony(rows: Int, cols: Int, clock: Clock, cells: Map[(Rows,Cols), NewLifeCell])  extends ModelColony {
    require(cells.size == rows*cols, "%d number of cells isn't equal to expected number of cells %d".format(cells.size, rows*cols))
    def mutate =  {
      val future = clock.tik()
      val futureCitizens = collection.mutable.HashMap[(Int, Int), NewLifeCell]()
      for (((row, col), cell)  <- cells) {
        futureCitizens.put((row, col), NewTimedLifeCycle(cell)(future, neighbours(cell)))
      }
      SomeColony(rows, cols, future, futureCitizens.toMap)
    }
    def cell(row: Int, col: Int) = {
        cells(row, col)
    }
    def neighbours(cell: NewLifeCell): Seq[NewLifeCell] = {
      for (i <- neighbourhoodCoordinates(cell.row, cell.col);
           n <- cells.get(i._1, i._2))
        yield n
    }
    def neighbourhoodCoordinates(x: Int, y : Int) = {
      for (d <- Directions) yield d.apply(x, y)
    }

    override def toString = (for (i <- 1 to rows) yield "\n" + (for(j <- 1 to cols; cell <- cells.get(i, j)) yield cell.state).mkString("| ", " | ", " |")).mkString
  }
  //def apply(rows: Rows, cols: Cols) : ModelColony = apply(rows, cols)

  def apply(rows: Rows, cols: Cols, dice: Dice = DefaultDice)  : ModelColony = {
    require(rows > 0, "rows need to be greater than 0 and not " + rows)
    require(cols > 0, "cols need to be greater than 0 and not " + cols)
    val mutableCells = collection.mutable.HashMap[(Int, Int), NewLifeCell]()
    for (row <- 1 to rows;
         col <- 1 to cols) {
       mutableCells.put((row, col), NewLifeCell(row, col, dice(), BigBang))
    }
    new SomeColony(rows, cols, BigBang, mutableCells.toMap)
  }
}
/*
1,1 nw    1,2 n    1,3 ne
2,1 w     2,2      2,3 e
3,1 sw    3,2 s    3,3 se
*/
sealed trait Direction extends ((Int, Int) => (Int, Int))

case object North extends Direction {
  override def apply(row: Int, col: Int) = (row-1, col)
}
case object NorthEast extends Direction {
  override def apply(row: Int, col: Int) = (row-1, col+1)
}
case object East extends Direction {
  override def apply(row: Int, col: Int) = (row, col+1)
}
case object SouthEast extends Direction {
  override def apply(row: Int, col: Int) = (row+1, col+1)
}
case object South extends Direction {
  override def apply(row: Int, col: Int) = (row+1, col)
}
case object SouthWest extends Direction {
  override def apply(row: Int, col: Int) = (row+1, col-1)
}
case object West extends Direction {
  override def apply(row: Int, col: Int) = (row, col-1)
}
case object NorthWest extends Direction {
  override def apply(row: Int, col: Int) = (row-1, col-1)
}


case class NewColony(rows: Int, cols: Int, clock: Clock = BigBang) {
  require(rows > 0, "rows need to be greater than 0 and not " + rows)
  require(cols > 0, "cols need to be greater than 0 and not " + cols)
  private var mutableCells = collection.mutable.HashMap[(Int, Int), NewLifeCell]()

  lazy val cells = mutableCells.toMap


  def mutate = NewColony(rows, cols, clock)

  for (row <- 1 to rows;
       col <- 1 to cols) {
     mutableCells.put((row, col), NewLifeCell(row, col, Dead, clock))
  }

  require(mutableCells.size == rows*cols, "%d number of cells isn't equal to expected number of cells %d".format(mutableCells.size, rows*cols))

  def cell(row: Int, col: Int) = {
      mutableCells(row, col)
  }
  def neighbours(cell: NewLifeCell) = {
    for (i <- neighbourhoodCoordinates(cell.row, cell.col))
      yield mutableCells.get(i._1, i._2)
  }

  def neighbourhoodCoordinates(x: Int, y : Int) = {
    for (d <- Directions) yield d.apply(x, y)
  }


/*
    val north: Option[LifeCell] = cells.get((cell.x - 1, cell.y))
    val northeast: Option[LifeCell] = cells.get((cell.x - 1, cell.y + 1))
    val east: Option[LifeCell] = cells.get((cell.x, cell.y + 1))
    val southeast: Option[LifeCell] = cells.get((cell.x+1, cell.y + 1))
    val south: Option[LifeCell] = cells.get((cell.x+1, cell.y))
    val southwest: Option[LifeCell] = cells.get((cell.x+1, cell.y-1))
    val west: Option[LifeCell] = cells.get((cell.x, cell.y-1))
    val northwest: Option[LifeCell] = cells.get((cell.x-1, cell.y-1))
*/
}


object StateTransition extends (State => NeighbourhoodState => State)   {
  def apply(state: State) = (n: NeighbourhoodState) => state match {
    case Alive => n.filter(_ == Alive).size match {
      case x: Int if !(x == 2 || x == 3) => Dead
      case _ => state
    }
    case Dead =>  n.filter(_ == Alive).size match {
      case x: Int if x == 3 => Alive
      case _ => state
    }
  }
}

/*object LifeCycle extends (LifeCell =>  LifeCell)   {
  def apply(cell: LifeCell) = LifeCell(StateTransition(cell.state)(cell.neighbours.map(_.state)), cell.neighbours)
}*/


object TimedLifeCycle extends (LifeCell => Clock => LifeCell)   {
  def apply(cell: LifeCell) =  {
    (clock: Clock) =>  {
      if (cell.clock.time == clock.time) {
        println("Dude you are too late")
        cell
      } else if (cell.clock.time < clock.time) {
        LifeCell(StateTransition(cell.state)(cell.neighbours.map(_.state)), cell.neighbours.map(TimedLifeCycle(_)(clock)), clock)
      } else {
        throw new AssertionError("Can't yet travel back in time!")
      }
    }
  }
}

case class Neighbour(direction: Direction, cell: Option[LifeCell])

case class LifeCell(
   state: State,
   neighbours : Seq[LifeCell],
   clock: Clock
) {
  require(neighbours.forall(_.clock == clock))
}
case class NewLifeCell(
   row: Int = 0,
   col: Int = 0,
   state: State,
   clock: Clock
)
/*
case class LifeCell(
   initState: State,
   north: Neighbour = Neighbour(North, None),
   northEast: Neighbour = Neighbour(NorthEast, None),
   east: Neighbour = Neighbour(East, None),
   southEast: Neighbour = Neighbour(SouthEast, None),
   south: Neighbour = Neighbour(South, None),
   southWest: Neighbour = Neighbour(SouthWest, None),
   west: Neighbour = Neighbour(West, None),
   northWest: Neighbour = Neighbour(NorthWest, None)
) {
  val state = initState
  lazy val neighbours = Vector(north, northEast, east, southEast, south, southWest, west, northWest)
}
*/


class GameOfLife {
  class Cell(row: Int, col: Int, state: State, clock: Clock) {
    require(state != null)
    require(row > 0)
    require(col > 0)
  }



  case class Colony(rows: Int, cols: Int) {
    require(rows > 0, "rows need to be greater than 0 and not " + rows)
    require(cols > 0, "cols need to be greater than 0 and not " + cols)

    var clock: Clock = BigBang

    val cells: Vector[Cell] = Vector()

    def drawColony() =  {
      val cell00 = new Cell(0, 0, Dead, clock)
      val cell01 = new Cell(0, 0, Dead, clock)
    }

    def tik() {
      clock.tik()
/*
      cells.foreach(
        _.tik()
      )
*/
    }
  }
}

object GameOfLife {
  def main(args: Array[String]) {
    require(args.length == 3, "Require 3 commandline inputs rows, cols and eras")
    val rows = args(0).toInt
    val cols = args(1).toInt
    val eras = args(2).toInt
    var colony: ModelColony = PrimordialSoup(rows, cols)
    (1 to eras).foreach(
      _ => {
        println(colony.clock)
        println(colony)
        colony = colony.mutate
      }
    )
  }
}





