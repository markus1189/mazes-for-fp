package de.codecentric.mazes

import spire.math.Natural
import scala.util.Random
import cats.Show
import cats.syntax.show._

case class Cell(row: Natural,
                col: Natural,
                var north: Option[Cell] = None,
                var south: Option[Cell] = None,
                var east: Option[Cell] = None,
                var west: Option[Cell] = None) {
  private[this] var linksMap: Map[Cell, Boolean] = Map()

  def link(cell: Cell, bidi: Boolean = true): Cell = {
    linksMap += cell -> true
    if (bidi) cell.link(this, false)
    this
  }

  def unlink(cell: Cell, bidi: Boolean = true): Cell = {
    linksMap -= cell
    if (bidi) cell.unlink(this, false)
    this
  }

  def links: Set[Cell] = linksMap.keySet

  def isLinked(cell: Cell): Boolean = linksMap.contains(cell)

  def neighbors: Vector[Cell] = Vector(north, south, east, west).flatten
}

case class Grid(rows: Int, columns: Int) {
  val grid: Vector[Vector[Cell]] = prepareGrid
  configureCells()

  def prepareGrid: Vector[Vector[Cell]] = {
    Vector.tabulate(rows) { row =>
      Vector.tabulate(columns) { col =>
        Cell(Natural(row), Natural(col))
      }
    }
  }

  def configureCells(): Unit = {
    eachCell { cell =>
      val (row, col) = cell.row -> cell.col

      cell.north = if (row == Natural.zero) None else getCell(row - Natural.one, col)
      cell.south = getCell(row + Natural.one, col)
      cell.west = if (col == Natural.zero) None else getCell(row, col - Natural.one)
      cell.east = getCell(row, col + Natural.one)
    }
  }

  def getCell(row: Natural, col: Natural): Option[Cell] = {
    for {
      theRow <- grid.lift(row.toInt)
      cell <- theRow.lift(col.toInt)
    } yield cell
  }

  def eachRow(f: Vector[Cell] => Unit): Unit = {
    grid.foreach(f)
  }

  def eachCell(f: Cell => Unit): Unit = {
    eachRow(_.foreach(f))
  }

  def randomCell: Option[Cell] = {
    val row = Natural(Random.nextInt(rows.toInt))
    val col = Natural(Random.nextInt(columns.toInt))

    getCell(row, col)
  }
}

object Grid {
  implicit val gridShow: Show[Grid] = new Show[Grid] {
    override def show(grid: Grid): String = {
      var output = "+" + "---+" * grid.columns + "\n"

      grid.eachRow { row =>
        var top = "|"
        var bottom = "+"

        row.foreach { cell =>
          var body = "   "
          val eastBoundary = if (cell.east.exists(cell.isLinked(_))) " " else "|"
          top += body + eastBoundary

          val southBoundary = if (cell.south.exists(cell.isLinked(_))) "   " else "---"
          bottom += southBoundary + "+"
        }

        output += top + "\n"
        output += bottom + "\n"
      }

      output
    }
  }
}

object Main extends App {
  println(Grid(10,10).show)
  val g = Grid(4,4)
  BinaryTree.on(g)
  println(g.show)
}
