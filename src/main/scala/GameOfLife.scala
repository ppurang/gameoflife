package game.of.life

import java.lang.AssertionError
import java.util.Random

object `package` {
  type Neighbours = Seq[LifeCell]
  type NeighbourhoodState = Seq[State]
  type Rows = Int
  type Cols = Int

  case class Clock(time: Long) {
    def tik() = {
      Clock(time + 1)
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

  val Directions: Vector[Direction] = Vector(North, NorthEast, East, SouthEast, South, SouthWest, West, NorthWest)

  /*
  1,1 nw    1,2 n    1,3 ne
  2,1 w     2,2      2,3 e
  3,1 sw    3,2 s    3,3 se
  */
  sealed trait Direction extends ((Int, Int) => (Int, Int))

  case object North extends Direction {
    override def apply(row: Int, col: Int) = (row - 1, col)
  }

  case object NorthEast extends Direction {
    override def apply(row: Int, col: Int) = (row - 1, col + 1)
  }

  case object East extends Direction {
    override def apply(row: Int, col: Int) = (row, col + 1)
  }

  case object SouthEast extends Direction {
    override def apply(row: Int, col: Int) = (row + 1, col + 1)
  }

  case object South extends Direction {
    override def apply(row: Int, col: Int) = (row + 1, col)
  }

  case object SouthWest extends Direction {
    override def apply(row: Int, col: Int) = (row + 1, col - 1)
  }

  case object West extends Direction {
    override def apply(row: Int, col: Int) = (row, col - 1)
  }

  case object NorthWest extends Direction {
    override def apply(row: Int, col: Int) = (row - 1, col - 1)
  }
}

case class LifeCell(row: Int,
                    col: Int,
                    state: State,
                    clock: Clock)

object StateTransition extends (State => NeighbourhoodState => State) {
  def apply(state: State) = (n: NeighbourhoodState) => state match {
    case Alive => n.filter(_ == Alive).size match {
      case x: Int if !(x == 2 || x == 3) => Dead
      case _ => state
    }
    case Dead => n.filter(_ == Alive).size match {
      case x: Int if x == 3 => Alive
      case _ => state
    }
  }
}

object TimedLifeCycle extends (LifeCell => (Clock, Neighbours) => LifeCell) {
  def apply(cell: LifeCell) = {
    (clock: Clock, neighbours: Neighbours) => {
      if (cell.clock.time == clock.time) {
        println("Dude you are too late")
        cell
      } else if (cell.clock.time < clock.time) {
        LifeCell(cell.row, cell.col, StateTransition(cell.state)(neighbours.map(_.state)), clock)
      } else {
        throw new AssertionError("Can't yet travel back in time!")
      }
    }
  }
}

trait Colony {
  val clock: Clock
  def mutate: Colony
}

trait MapRowsColsColony {
  val rows: Rows
  val cols: Cols

  val cells: Map[(Rows, Cols), LifeCell]

  def cell(row: Int, col: Int) = {
    cells(row, col)
  }

  def neighbours(cell: LifeCell): Seq[LifeCell] = {
    for (i <- neighbourhoodCoordinates(cell.row, cell.col);
         n <- cells.get(i._1, i._2))
    yield n
  }

  def neighbourhoodCoordinates(x: Int, y: Int) = {
    for (d <- Directions) yield d(x, y)
  }

  override def toString = (for (i <- 1 to rows) yield "\n" +
          (for (j <- 1 to cols; cell <- cells.get(i, j)) yield cell.state).mkString("| ", " | ", " |")).mkString
}

object PrototypeColony {
  private case class PrototypeColony(rows: Rows, cols: Cols, clock: Clock, cells: Map[(Rows, Cols), LifeCell])
          extends Colony
          with MapRowsColsColony {
    require(cells.size == rows * cols,
      "%d number of cells isn't equal to expected number of cells %d".format(cells.size, rows * cols))

    def mutate = {
      val future = clock.tik()
      val futureCitizens = collection.mutable.HashMap[(Int, Int), LifeCell]()
      for (((row, col), cell) <- cells) {
        futureCitizens.put((row, col), TimedLifeCycle(cell)(future, neighbours(cell)))
      }
      PrototypeColony(rows, cols, future, futureCitizens.toMap)
    }
  }


  def apply(rows: Rows, cols: Cols, dice: Dice): Colony with MapRowsColsColony = {
    require(rows > 0, "rows need to be greater than 0 and not " + rows)
    require(cols > 0, "cols need to be greater than 0 and not " + cols)
    val mutableCells = collection.mutable.HashMap[(Int, Int), LifeCell]()
    for (row <- 1 to rows;
         col <- 1 to cols) {
      mutableCells.put((row, col), LifeCell(row, col, dice(), BigBang))
    }
    new PrototypeColony(rows, cols, BigBang, mutableCells.toMap)
  }
}

object PrimordialSoup extends ((Rows, Cols, Dice) => Colony) {
  def apply(rows: Rows, cols: Cols, dice: Dice = DefaultDice) = PrototypeColony(rows, cols, dice)
}

object GameOfLife {
  def main(args: Array[String]) {
    require(args.length == 3, "Require 3 commandline inputs rows, cols and eras")
    val rows = args(0).toInt
    val cols = args(1).toInt
    val eras = args(2).toInt
    var colony: Colony = PrimordialSoup(rows, cols)
    (1 to eras).foreach(
      _ => {
        println(colony.clock)
        println(colony)
        colony = colony.mutate
      }
    )
  }
}