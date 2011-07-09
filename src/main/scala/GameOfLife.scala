package coderetreat

/**
 *
 * @author Piyush Purang
 */

object `coderetreat` {
  type Neighbours = Seq[LifeCell]
  type NeighbouringState = Seq[State]
  //type Addition = (Int, Int) => Int

}


import coderetreat._


sealed trait State

case object Alive extends State

case object Dead extends State

case class LifeCell(state: State)


object StateTransition extends (State => NeighbouringState  => State) {

  def apply(present: State) = (n: NeighbouringState) => {
    present match {
      case Alive => n.filter(_ != Dead).size match {
        case x if x == 2 || x == 3  => Alive
        case _ => Dead
      }

      case Dead =>  n.filter(_ != Dead).size match {
        case x if x == 3 => Alive
        case _ => Dead
      }

    }
  }

}

object GameOfLife {


}