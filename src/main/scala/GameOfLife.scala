package game.of.life

object `package` {
  type NeighbourhoodState  =  Seq[State]
  type StatePredicate = (State => Boolean)

  val FilterNeighbours  = (p: StatePredicate) => (x : NeighbourhoodState) => x.filter(p).size
  //Ouch move the declaration of FilterNeighbours above below the following two to encounter a NPE
  //not that nice.
  val AliveNeighbours = FilterNeighbours(_ == Alive)
  val DeadNeighbours = FilterNeighbours(_ == Dead)
}

sealed trait State
case object Alive extends State
case object Dead extends State

object StateTransition extends  (State => NeighbourhoodState => State) {
  def apply(present: State) =  (ns : NeighbourhoodState) =>  present match {
    case Alive => AliveNeighbours(ns) match {
      case 2 | 3 => Alive
      case _ => Dead
    }
    case _ => AliveNeighbours(ns) match {
      case 3 => Alive
      case _ => Dead
    }
  }
}