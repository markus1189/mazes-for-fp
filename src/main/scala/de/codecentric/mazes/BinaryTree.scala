package de.codecentric.mazes

import scala.util.Random

object BinaryTree {
  def on(grid: Grid): Unit = {
    grid.eachCell { cell =>
      val neighbors = (cell.north ++ cell.east).toList
      if (neighbors.nonEmpty) {
        val index = Random.nextInt(neighbors.size)
        val neighbor = neighbors.lift(index)
        neighbor.foreach(cell.link(_))
      }
    }
  }
}
