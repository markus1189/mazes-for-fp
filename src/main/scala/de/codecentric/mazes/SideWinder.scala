package de.codecentric.mazes

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object SideWinder {
  def on(grid: Grid): Unit = {
    grid.eachRow { row =>
      val run = ArrayBuffer[Cell]()

      row.foreach { cell =>
        run += cell

        val atEasternBoundary = cell.east.isEmpty
        val atNorthernBoundary = cell.north.isEmpty

        val shouldCloseOut = atEasternBoundary || (!atNorthernBoundary && Random.nextBoolean())

        if (shouldCloseOut) {
          val member = run(scala.util.Random.nextInt(run.size))
          member.north.foreach(member.link(_))
          run.clear()
        } else {
          cell.east.foreach(cell.link(_))
        }
      }
    }
  }
}
