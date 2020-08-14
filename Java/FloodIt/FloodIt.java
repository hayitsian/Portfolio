import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a single square of the game area
class Cell {

  /*
   * TEMPLATE
   * FIELDS:
   * ... this.CELL_SIDE ... -- int
   * ... this.x ... -- int
   * ... this.y ... -- int
   * ... this.color ... -- Color
   * ... this.flooded ... -- boolean
   * ... this.left ... -- Cell
   * ... this.right ... -- Cell
   * ... this.top ... -- Cell
   * ... this.bottom ... -- Cell
   * 
   * METHODS
   * ... this.drawCell() ... -- WorldImage
   * ... this.placeCell(WorldScene) ... -- WorldScene
   * ... this.flood() ... -- void
   * ... this.updateLeft(Cell) ... -- void
   * ... this.updateRight(Cell) ... -- void
   * ... this.updateTop(Cell) ... -- void
   * ... this.updateBottom(Cell) ... -- void
   * 
   * METHODS FOR FIELDS
   * 
   */

  // Constant representing the side length of this Cell
  static int CELL_SIDE = 20;
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // Constructor
  Cell(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = false;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }

  // Draws this Cell as a Square
  WorldImage drawCell() {
    return new RectangleImage(CELL_SIDE, CELL_SIDE, OutlineMode.SOLID, this.color);
  }

  // Places the image of this Cell in the given WorldScene
  WorldScene placeCell(WorldScene bg) {
    bg.placeImageXY(this.drawCell(), this.x + (CELL_SIDE / 2), this.y + (CELL_SIDE / 2));
    return bg;
  }

  // Updates the right Cell of this Cell to the given Cell
  void updateRight(Cell that) {
    this.right = that;
  }

  // Updates the bottom Cell of this Cell to the given Cell
  void updateBottom(Cell that) {
    this.bottom = that;
  }

  // Updates the left Cell of this Cell to the given Cell
  void updateLeft(Cell that) {
    this.left = that;
  }

  // Updates the top Cell of this Cell to the given Cell
  void updateTop(Cell that) {
    this.top = that;
  }

  // Updates the color of this Cell to the given Color
  void updateColor(Color that) {
    this.color = that;
  }

  // Checks to see if the given Posn is within the boundaries of this Cell
  boolean withinPos(Posn pos) {
    return pos.x > this.x && pos.y > this.y && pos.x < (this.x + Cell.CELL_SIDE)
        && pos.y < (this.y + Cell.CELL_SIDE);
  }

  // Determines if this Cell is adjacent to a flooded Cell
  boolean adjacentToFlooded() {
    return this.checkLeft() || this.checkRight() || this.checkTop() || this.checkBottom();
  }

  // Checks to see if this cells left cell is flooded
  boolean checkLeft() {
    if (this.left == null) {
      return false;
    }
    return this.left.flooded;
  }

  // Checks to see if this cells right cell is flooded
  boolean checkRight() {
    if (this.right == null) {
      return false;
    }
    return this.right.flooded;
  }

  // Checks to see if this cells top cell is flooded
  boolean checkTop() {
    if (this.top == null) {
      return false;
    }
    return this.top.flooded;
  }

  // Checks to see if this cells bottom cell is flooded
  boolean checkBottom() {
    if (this.bottom == null) {
      return false;
    }
    return this.bottom.flooded;
  }

  // Update this Cell to make it flooded
  void flood() {
    this.flooded = true;
  }
}

class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<Cell> board = new ArrayList<Cell>(this.boardSize * this.boardSize);
  // Defines an int constant
  static int BOARD_SIZE = 5;
  // Defines dynamic int that represents the board size
  int boardSize = BOARD_SIZE;
  // Random object to generate list of cell colors
  Random randy;
  // Background the list of cells is placed on
  WorldScene bg;
  // index keeping track of what diagonals have been flooded
  int diag = 0;

  // Default constructor
  FloodItWorld() {
    this(new Random());
  }

  // Convenience Constructor for testing (input Board)
  FloodItWorld(ArrayList<Cell> board, int boardSize) {
    this.boardSize = boardSize;
    this.randy = new Random();
    bg = new WorldScene(FloodItWorld.BOARD_SIZE * Cell.CELL_SIDE,
        FloodItWorld.BOARD_SIZE * Cell.CELL_SIDE);
    this.board = board;
  }

  // Convenience Constructor for testing (input Random)
  FloodItWorld(Random randy) {
    this.randy = randy;
    bg = new WorldScene(FloodItWorld.BOARD_SIZE * Cell.CELL_SIDE,
        FloodItWorld.BOARD_SIZE * Cell.CELL_SIDE);
    this.generateCells();
    this.updateCells();
  }

  // Convenience Constructor for testing (input Board Size)
  FloodItWorld(int boardSize) {
    this.boardSize = boardSize;
    this.randy = new Random();
    bg = new WorldScene(this.boardSize * Cell.CELL_SIDE, this.boardSize * Cell.CELL_SIDE);
    this.generateCells();
    this.updateCells();
  }

  // Convenience Constructor for testing (input Board Size and Random)
  FloodItWorld(int boardSize, Random randy) {
    this.boardSize = boardSize;
    this.randy = randy;
    bg = new WorldScene(this.boardSize * Cell.CELL_SIDE, this.boardSize * Cell.CELL_SIDE);
    this.generateCells();
    this.updateCells();
  }

  // Draws the world scene for each cell
  public WorldScene makeScene() {
    WorldScene scene = this.bg;
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        Cell other = board.get((i * this.boardSize) + j);
        scene = other.placeCell(scene);
      }
    }
    boolean gameOver = true;
    Color end = board.get(0).color;
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        if (!board.get((i * this.boardSize) + j).color.equals(end)) {
          gameOver = false;
        }
      }
    }
    if (gameOver) {
      scene = new WorldScene(this.boardSize * Cell.CELL_SIDE, this.boardSize * Cell.CELL_SIDE);
      scene.placeImageXY(new TextImage("You Win!", 18, Color.BLACK),
          this.boardSize * Cell.CELL_SIDE / 2, this.boardSize * Cell.CELL_SIDE / 2);
    }
    return scene;

  }

  // Cascades the cells by diagonals and increments the diagonal each tick
  // to create the cascading effect
  public void onTick() {
    int numSquares = diag + 1;
    int row = diag;
    int start = 0;
    if (diag > (this.boardSize - 1)) {
      numSquares = (2 * this.boardSize) - 1 - diag;
      row = this.boardSize - 1;
      start = diag - (this.boardSize - 1);
    }
    for (int j = 0; j < numSquares; j++) {
      Cell other = board.get((row * this.boardSize) + start - (j * (this.boardSize - 1)));
      Cell top = board.get(0);
      if (other.flooded) {
        other.updateColor(top.color);
      }
    }
    if (diag == (2 * this.boardSize) - 1) {
      diag = 0;
    } else {
      diag += 1;
    }
  }

  // Changes the color of the top left cell and updates any cells
  // that are adjacent to flooded cells AND have the same color as the top left
  // cell
  public void onMouseClicked(Posn pos) {
    this.diag = 0;
    for (int r = 0; r < this.boardSize * this.boardSize; r++) {
      for (int i = 0; i < this.boardSize; i++) {
        for (int j = 0; j < this.boardSize; j++) {
          Cell other = board.get((i * this.boardSize) + j);
          Cell top = board.get(0);
          if (other.adjacentToFlooded() && other.color.equals(top.color)) {
            other.flood();
          }
        }
      }
    }
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        Cell other = board.get((i * this.boardSize) + j);
        Cell top = board.get(0);
        if (other.withinPos(pos)) {
          top.updateColor(other.color);
          break;
        }
      }
    }

  }

  // Resets the board with a new board if the user presses the "r" key
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.randy = new Random();
      this.generateCells();
      this.updateCells();
    }
  }

  // Generates the initially randomly colored ArrayList of Cells
  void generateCells() {
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        Cell other = new Cell(i * Cell.CELL_SIDE, j * Cell.CELL_SIDE, this.generateColor());
        if (i == 0 && j == 0) {
          other.flooded = true;
        }
        board.add((i * this.boardSize) + j, other);
      }
    }
  }

  // Updates the list of Cells adjacent cells based on their position in the list
  void updateCells() {
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        Cell other = board.get((i * this.boardSize) + j);
        if (i > 0) {
          other.updateTop(board.get(((i - 1) * this.boardSize) + j));
        }
        if (i < this.boardSize - 1) {
          other.updateBottom(board.get(((i + 1) * this.boardSize) + j));
        }
        if (j < this.boardSize - 1) {
          other.updateRight(board.get((i * this.boardSize) + j + 1));
        }
        if (j > 0) {
          other.updateLeft(board.get((i * this.boardSize) + j - 1));
        }
      }
    }
  }

  // Generates a random color
  Color generateColor() {
    int index = randy.nextInt(6);
    switch (index) {
      case 0:
        return Color.RED;
      case 1:
        return Color.GREEN;
      case 2:
        return Color.YELLOW;
      case 3:
        return Color.BLUE;
      case 4:
        return Color.MAGENTA;
      case 5:
        return Color.ORANGE;
      default:
        return Color.BLACK;
    }
  }
}

class ExamplesFloodIt {

  WorldScene bg = new WorldScene(60, 60);

  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;

  Cell c1 = new Cell(0, 0, Color.BLUE);
  Cell c2 = new Cell(20, 0, Color.MAGENTA);
  Cell c3 = new Cell(0, 20, Color.MAGENTA);
  Cell c4 = new Cell(20, 20, Color.GREEN);
  Cell c5 = new Cell(20, 40, Color.MAGENTA);
  Cell c6 = new Cell(40, 20, Color.YELLOW);
  Cell c7 = new Cell(40, 0, Color.ORANGE);
  Cell c8 = new Cell(0, 40, Color.BLUE);
  Cell c9 = new Cell(40, 40, Color.MAGENTA);

  WorldImage w1 = new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE);
  WorldImage w2 = new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA);
  WorldImage w3 = new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA);
  WorldImage w4 = new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN);

  WorldScene ws1 = new WorldScene(60, 60);
  WorldScene ws2 = new WorldScene(60, 60);
  WorldScene ws3 = new WorldScene(60, 60);
  WorldScene ws4 = new WorldScene(60, 60);
  WorldScene ws = new WorldScene(60, 60);

  ArrayList<Cell> boardBackup = new ArrayList<Cell>(
      Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9));

  Random randy = new Random(16);

  FloodItWorld flood = new FloodItWorld(3, randy);

  ArrayList<Cell> flood2Board;

  FloodItWorld flood2;

  FloodItWorld flood3 = new FloodItWorld(boardBackup, 3);

  FloodItWorld flood6 = new FloodItWorld(boardBackup, 3);

  FloodItWorld flood4 = new FloodItWorld(4);

  void initTestConditions() {
    // for testing methods in Cell class that return void
    cell1 = new Cell(0, 0, Color.BLUE);
    cell2 = new Cell(20, 0, Color.MAGENTA);
    cell3 = new Cell(0, 20, Color.MAGENTA);
    cell4 = new Cell(20, 20, Color.GREEN);

    c1.flood();

    ws1.placeImageXY(w1, c1.x + (Cell.CELL_SIDE / 2), c1.y + (Cell.CELL_SIDE / 2));
    ws2.placeImageXY(w1, c1.x + (Cell.CELL_SIDE / 2), c1.y + (Cell.CELL_SIDE / 2));
    ws2.placeImageXY(w2, c2.x + (Cell.CELL_SIDE / 2), c2.y + (Cell.CELL_SIDE / 2));
    ws3.placeImageXY(w1, c1.x + (Cell.CELL_SIDE / 2), c1.y + (Cell.CELL_SIDE / 2));
    ws3.placeImageXY(w2, c2.x + (Cell.CELL_SIDE / 2), c2.y + (Cell.CELL_SIDE / 2));
    ws3.placeImageXY(w3, c3.x + (Cell.CELL_SIDE / 2), c3.y + (Cell.CELL_SIDE / 2));
    ws4.placeImageXY(w1, c1.x + (Cell.CELL_SIDE / 2), c1.y + (Cell.CELL_SIDE / 2));
    ws4.placeImageXY(w2, c2.x + (Cell.CELL_SIDE / 2), c2.y + (Cell.CELL_SIDE / 2));
    ws4.placeImageXY(w3, c3.x + (Cell.CELL_SIDE / 2), c3.y + (Cell.CELL_SIDE / 2));
    ws4.placeImageXY(w4, c4.x + (Cell.CELL_SIDE / 2), c4.y + (Cell.CELL_SIDE / 2));

    ws.placeImageXY(w1, c1.x + (Cell.CELL_SIDE / 2), c1.y + (Cell.CELL_SIDE / 2));
    ws.placeImageXY(w2, c2.x + (Cell.CELL_SIDE / 2), c2.y + (Cell.CELL_SIDE / 2));
    ws.placeImageXY(w3, c3.x + (Cell.CELL_SIDE / 2), c3.y + (Cell.CELL_SIDE / 2));
    ws.placeImageXY(w4, c4.x + (Cell.CELL_SIDE / 2), c4.y + (Cell.CELL_SIDE / 2));
    ws.placeImageXY(c5.drawCell(), c5.x + (Cell.CELL_SIDE / 2), c5.y + (Cell.CELL_SIDE / 2));
    ws.placeImageXY(c6.drawCell(), c6.x + (Cell.CELL_SIDE / 2), c6.y + (Cell.CELL_SIDE / 2));
    ws.placeImageXY(c7.drawCell(), c7.x + (Cell.CELL_SIDE / 2), c7.y + (Cell.CELL_SIDE / 2));
    ws.placeImageXY(c8.drawCell(), c8.x + (Cell.CELL_SIDE / 2), c8.y + (Cell.CELL_SIDE / 2));
    ws.placeImageXY(c9.drawCell(), c9.x + (Cell.CELL_SIDE / 2), c9.y + (Cell.CELL_SIDE / 2));

    c1.updateBottom(c2);
    c4.updateBottom(c5);
    c4.updateTop(c2);
    c1.updateTop(c2);
    c4.updateLeft(c3);
    c1.updateLeft(c2);
    c4.updateRight(c6);
    c1.updateRight(c2);
    c2.updateLeft(c1);
    c9.flood();
    c5.flood();
    c7.flood();
    c8.flood();
    c8.updateRight(c9);
    c9.updateLeft(c8);
    c8.updateTop(c5);
    c5.updateBottom(c8);

    Random randy = new Random(16);
    flood2 = new FloodItWorld(randy);
    flood2Board = flood2.board;
  }

  void testBigBang(Tester t) {
    flood4.bigBang(flood4.boardSize * Cell.CELL_SIDE, flood4.boardSize * Cell.CELL_SIDE, 0.2);
  }

  boolean testDrawCell(Tester t) {
    return t.checkExpect(c1.drawCell(), w1) && t.checkExpect(c2.drawCell(), w2)
        && t.checkExpect(c3.drawCell(), w3) && t.checkExpect(c4.drawCell(), w4);
  }

  boolean testPlaceCell(Tester t) {
    this.initTestConditions();
    return t.checkExpect(c1.placeCell(bg), ws1) && t.checkExpect(c2.placeCell(ws1), ws2)
        && t.checkExpect(c3.placeCell(ws2), ws3) && t.checkExpect(c4.placeCell(ws3), ws4);
  }

  boolean testUpdateBottom(Tester t) {
    this.initTestConditions();

    return t.checkExpect(c1.bottom, c2) && t.checkExpect(c4.bottom, c5);
  }

  boolean testUpdateTop(Tester t) {
    this.initTestConditions();

    return t.checkExpect(c1.top, c2) && t.checkExpect(c4.top, c2);
  }

  boolean testUpdateLeft(Tester t) {
    this.initTestConditions();

    return t.checkExpect(c1.left, c2) && t.checkExpect(c4.left, c3);
  }

  boolean testUpdateRight(Tester t) {
    this.initTestConditions();

    return t.checkExpect(c1.right, c2) && t.checkExpect(c4.right, c6);
  }

  boolean testMakeScene(Tester t) {
    this.initTestConditions();
    return t.checkExpect(flood.makeScene(), ws);
  }

  boolean testGenerateCells(Tester t) {
    this.initTestConditions();
    return t.checkExpect(flood.board.size(), 9) && t.checkExpect(flood.board.get(0).left, null)
        && t.checkExpect(flood.board.get(4).color, Color.GREEN);
  }

  boolean testGenerateColor(Tester t) {
    this.initTestConditions();
    return t.checkExpect(flood2.generateColor(), Color.YELLOW)
        && t.checkExpect(flood2.generateColor(), Color.RED)
        && t.checkExpect(flood2.generateColor(), Color.MAGENTA);
  }

  boolean testUpdateCells(Tester t) {
    this.initTestConditions();
    flood3.updateCells();
    return t.checkExpect(flood3.board.get(0), c1) && t.checkExpect(flood3.board.get(0).top, c2)
        && t.checkExpect(flood3.board.get(4).bottom, c8);
  }

  boolean testUpdateColor(Tester t) {
    this.initTestConditions();
    cell4.updateColor(Color.BLUE);
    cell1.updateColor(Color.RED);
    return t.checkExpect(cell4.color, Color.BLUE) && t.checkExpect(cell1.color, Color.RED);
  }

  boolean testWithinPos(Tester t) {
    this.initTestConditions();
    return t.checkExpect(cell1.withinPos(new Posn(5, 5)), true)
        && t.checkExpect(cell1.withinPos(new Posn(50, 50)), false);
  }

  boolean testAdjacentToFlooded(Tester t) {
    this.initTestConditions();
    return t.checkExpect(c1.adjacentToFlooded(), false)
        && t.checkExpect(c9.adjacentToFlooded(), true)
        && t.checkExpect(c2.adjacentToFlooded(), true);
  }

  boolean testFlood(Tester t) {
    this.initTestConditions();
    cell2.flood();
    cell4.flood();
    return t.checkExpect(cell2.flooded, true) && t.checkExpect(cell4.flooded, true)
        && t.checkExpect(cell3.flooded, false);
  }

  boolean testCheckLeft(Tester t) {
    this.initTestConditions();
    return t.checkExpect(c1.checkLeft(), false) && t.checkExpect(c2.checkLeft(), true)
        && t.checkExpect(c3.checkLeft(), false);
  }

  boolean testCheckRight(Tester t) {
    this.initTestConditions();
    return t.checkExpect(c1.checkRight(), false) && t.checkExpect(c8.checkRight(), true)
        && t.checkExpect(c3.checkRight(), false);
  }

  boolean testCheckTop(Tester t) {
    this.initTestConditions();
    return t.checkExpect(c1.checkTop(), false) && t.checkExpect(c8.checkTop(), true)
        && t.checkExpect(c3.checkTop(), false);
  }

  boolean testCheckBottom(Tester t) {
    this.initTestConditions();
    return t.checkExpect(c1.checkBottom(), false) && t.checkExpect(c5.checkBottom(), true)
        && t.checkExpect(c3.checkBottom(), false);
  }

  boolean testOnKeyEvent(Tester t) {
    this.initTestConditions();
    flood2.onKeyEvent("r");
    flood.onKeyEvent("s");
    return t.checkExpect(flood2.board, flood2Board);
  }

  boolean testOnTick(Tester t) {
    this.initTestConditions();
    flood3.onTick();
    return t.checkExpect(flood3.board, this.boardBackup);
  }

  boolean testOnMouseClicked(Tester t) {
    this.initTestConditions();
    flood3.onMouseClicked(new Posn(1000, 1000));
    flood6.onMouseClicked(new Posn(50, 50));
    return t.checkExpect(flood3.board.get(0), c1) && t.checkExpect(flood6.board.get(2), c3);
  }
}