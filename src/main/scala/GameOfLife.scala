package game.of.life


object `package` {
  type NeighbourhoodState = Seq[State]


  sealed trait State
  case object Alive extends State
  case object Dead extends State
  val AliveTransition = (ns: NeighbourhoodState) => (ns.filter(_ == Alive).size match {
      case x if x == 2 || x == 3 => Alive
      case _ => Dead
    })

}

object StateTransition extends (State => NeighbourhoodState => State) {

  def apply(present: State) = present match {
    case Alive => AliveTransition
    case Dead => (ns: NeighbourhoodState) => (ns.filter(_ == Alive).size match {
      case 3 => Alive
      case _ => Dead
    })
  }
}

class GameOfLife {

}