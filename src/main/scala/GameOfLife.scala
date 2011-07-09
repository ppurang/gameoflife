package game.of.life



/**
 *
 * @author Piyush Purang
 */

object `package` {

  sealed trait State

  case object Alive extends State

  case object Dead extends State

  type NeighbourhoodState = Seq[State]
  type StatePredicate = (State => Boolean)

  val AlivePredicate = (x:State) => x == Alive
  val DeadPredicate = (x:State) => x == Dead

  val FilteredNeighbours =  (p:(State => Boolean)) => ((ns:NeighbourhoodState) => ns.filter(p).size)

}

object StateTransition extends (State => NeighbourhoodState => State) {
  def apply(present: State) = (ns:NeighbourhoodState) => present match {
    case Alive => FilteredNeighbours(_ == Alive)(ns) match  {
      case 2 | 3 => Alive
      case _ => Dead
    }
    case Dead =>  FilteredNeighbours(AlivePredicate)(ns) match  {
      case 3 => Alive
      case _ => Dead
    }
  }
}

class GameOfLife {

}