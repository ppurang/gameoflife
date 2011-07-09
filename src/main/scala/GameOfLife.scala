package game.of.life

object `package` {
  type NeighbourhoodState  =  Seq[State]
  val AliveNeighbours = FilterNeighbours(_ == Alive)
  val DeadNeighbours = FilterNeighbours(_ == Dead)
  type StatePredicate = (State => Boolean)

  val FilterNeighbours : StatePredicate  => (NeighbourhoodState  => Int) = (p: StatePredicate) => {
    (x : NeighbourhoodState) => x.filter(p).size
  }

}
sealed trait State
case object Alive extends State
case object Dead extends State



object StateTransition extends  (State => NeighbourhoodState => State) {
  def apply(present: State) =  (ns : NeighbourhoodState) =>  present match {
    case Alive => AliveNeighbours(ns) match {
      case x if x == 2 || x == 3 => Alive
      case _ => Dead
    }
    case _ => AliveNeighbours(ns) match {
      case x if x == 3 => Alive
      case _ => Dead
    }
  }
}