package org.shurik.life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by alexander on 27.01.17.
 */
public class World extends JPanel implements ActionListener {
    private final Timer mainTimer; // a timer that update program
    private final Image img; // world image
    private final int width; // the width of the world
    private final int height; // the height of the world
    private final int cellWidth; // cell width
    private final int cellHeight; // cell height
    private int x0; // left point of the world
    private int y0; // the upper point of the world
    private Random random; // random

    public World() {
        this.img = new ImageIcon(getClass().getResource("/Game.png")).getImage(); // world object with the image
        this.width = img.getWidth(null); // the width of the world becomes equal to the width of the image
        this.height = img.getHeight(null); // the height of the world becomes equal to the height of the image
        this.x0 = 0; // left coordinate point of the X axis is the left part of the world - 0
        this.y0 = 0; // coordinate of the top point in the Y-axis is equal to the top of the world - 0
        this.mainTimer = new Timer(150, this); // timer starts every 3 seconds
        this.cellHeight = 30; // the height of the cell is equal to 30
        this.cellWidth = 30; // the height of the cell is equal to 30
        this.random = new Random(); // creates a random object
        this.mainTimer.start(); // start timer
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
        life(); // starts life in the world
    }

    /**
     * matrix creates and adds the initial cells
     * @return matrix
     */
    public int[][] matrix() {
        int[][] arr = new int[30][30]; // creates a matrix of 30x30

        /*
        adds a cell in the middle and around
         */
        arr[arr.length / 2][arr.length / 2] = 1;
        arr[arr.length / 2][arr.length / 2 - 1] = 1;
        arr[arr.length / 2][arr.length / 2 - 2] = 1;

        arr[arr.length / 2 - 1][arr.length / 2] = 1;
        arr[arr.length / 2 - 1][arr.length / 2 - 1] = 1;
        arr[arr.length / 2 - 1][arr.length / 2 - 2] = 1;

        arr[arr.length / 2 - 2][arr.length / 2] = 1;
        arr[arr.length / 2 - 2][arr.length / 2 - 1] = 1;
        arr[arr.length / 2 - 2][arr.length / 2 - 2] = 1;

        return arr;
    }

    private int[][] cell = matrix();

    /**
     * draws the world and cells
     * @param g class object that allows you to work on graphics
     */
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
        for (int i = 0; i < cell.length; i++) {
            for (int j = 0; j < cell[i].length; j++) {

                /*
                column number becomes equal to X and Y is the line number
                 */
                if (cell[i][j] != 0) {
                    if (j > 1) {
                        x0 = cellWidth * j + j;
                    } else if (j == 1) {
                        x0 = cellWidth + j;
                    } else {
                        x0 = 0;
                    }

                    if (i > 1) {
                        y0 = cellHeight * i + i;
                    } else if (i == 1) {
                        y0 = cellHeight + i;
                    } else {
                        y0 = 0;
                    }

                    if (cell[i][j] == 1) {
                        g.setColor(Color.green); // healthy cell
                    } else if (cell[i][j] == 2) {
                        g.setColor(Color.red); // infected cell
                    } else if (cell[i][j] == 3) {
                        g.setColor(Color.cyan); // immune cell
                    }
                    g.fillRect(x0, y0, cellWidth, cellHeight); // draws a filled square
                }
            }
        }
    }

    /**
     * cell life
     */
    private void life() {
        for (int i = 0; i < cell.length; i++) {
            for (int j = 0; j < cell[i].length; j++) {
                int limitExceeded = 0;
                if (i > 0 && i < cell.length-1 && j > 0 && j < cell.length-1) {
                    limitExceeded = 1; // checks whether neighbors are outside the current cell
                }

                Boolean somebodyOnBottom = cell[i + limitExceeded][j] != 0;
                Boolean somebodyOnUp = cell[i - limitExceeded][j] != 0;
                Boolean somebodyOnRight = cell[i][j + limitExceeded] != 0;
                Boolean somebodyOnLeft = cell[i][j - limitExceeded] != 0;
                Boolean somebodyOnBottomRight = cell[i + limitExceeded][j + limitExceeded] != 0;
                Boolean somebodyOnBottomLeft = cell[i + limitExceeded][j - limitExceeded] != 0;
                Boolean somebodyOnUpLeft = cell[i - limitExceeded][j - limitExceeded] != 0;
                Boolean somebodyOnUpRight = cell[i - limitExceeded][j + limitExceeded] != 0;

                if (i >= cell.length || i <= 0 || j >= cell.length || j <= 0) { // if the cell is trying to get out of the borders of the world, it dies
                    cell[i][j] = 0;
                }

                if (cell[i][j] != 0) {
                    int near = 0; // the number of cells around the current

                    /*
                    check with how many cells bordering the current
                     */
                    if (somebodyOnBottom) {
                        near++;
                    }
                    if (somebodyOnUp) {
                        near++;
                    }
                    if (somebodyOnRight) {
                        near++;
                    }
                    if (somebodyOnLeft) {
                        near++;
                    }
                    if (somebodyOnBottomRight) {
                        near++;
                    }
                    if (somebodyOnBottomLeft) {
                        near++;
                    }
                    if (somebodyOnUpLeft) {
                        near++;
                    }
                    if (somebodyOnUpRight) {
                        near++;
                    }

                    if (near > 4 || near < 2) {
                        cell[i][j] = 0;
                        continue;
                    } // If the current cell near more than 4 or less than 2 other cells, it dies of overcrowding or loneliness

                    int chance = random.nextInt(100); // born cell state
                    int nextGen; // the next generation that will be born 1 - healthy, 2 - infected, 3 - with immunity
                    if (cell[i][j] == 3) { // if the current cell immunity
                        if (chance < 75) {
                            nextGen = 1; // cell immunity is a 75% chance of having a healthy
                        } else {
                            nextGen = 3; // and a 25% chance with immunity
                        }
                        int immunityFuture = random.nextInt(100); // state of a cell with immunity on the next move
                        if (immunityFuture < 10) {
                            cell[i][j] = 0; // It has a 10% chance of dying from an accident or from old age
                        }
                    } else if (cell[i][j] == 2) { // if the current cell is infected
                        if (chance < 75) {
                            nextGen = 2; // infected cell has a 75% chance of having an infected
                        } else {
                            nextGen = 3; // and 25% of the immune
                        }
                        int futureInfected = random.nextInt(100); // the condition of the infected cell to the next turn
                        if (futureInfected < 75) {
                            cell[i][j] = 0; // 75% of cases, die
                        } else {
                            cell[i][j] = 3; // 25% and recovering gains immunity to disease
                        }
                    } else { // if the current cell is healthy
                        nextGen = 1; // healthy cell gives birth to a healthy 100% cases
                        int futureHealthy = random.nextInt(100); // state healthy cells on the next move
                        if (futureHealthy < 15) {
                            cell[i][j] = 2; // It has a chance of 15% become infected
                        } else if (futureHealthy > 90) {
                            cell[i][j] = 0; // 10% chance of dying from an accident or from old age
                        }
                    }

                    int chanceOfBirth = random.nextInt(100); // chance of cell birth on the current course

                    if (chanceOfBirth < 70) { // chance of birth is 70%
                        if (!somebodyOnBottom) {
                            cell[i + limitExceeded][j] = nextGen; // if there's space for a cell of birth below the new cell is born there
                        } else if (!somebodyOnUp) {
                            cell[i - limitExceeded][j] = nextGen; // if there's space for the birth of cells on top of the new cell is born there
                        } else if (!somebodyOnRight) {
                            cell[i][j + limitExceeded] = nextGen; // if there's space to the right of the cells of the birth a new cell is born there
                        } else if (!somebodyOnLeft) {
                            cell[i][j - limitExceeded] = nextGen; // if there's space for a cell birth left the new cell is born there
                        } else if (!somebodyOnBottomRight) {
                            cell[i + limitExceeded][j + limitExceeded] = nextGen; // if there's space to the bottom right cell of birth the new cell is born there
                        } else if (!somebodyOnUpLeft) {
                            cell[i - limitExceeded][j - limitExceeded] = nextGen; // if there's space for a cell of birth at the top left is the new cell is born there
                        } else if (!somebodyOnBottomLeft) {
                            cell[i + limitExceeded][j - limitExceeded] = nextGen; // if there's space for a cell birth on the bottom left is the new cell is born there
                        } else if (!somebodyOnUpRight) {
                            cell[i - limitExceeded][j + limitExceeded] = nextGen; // if there is an empty seat for the top right cell birth the new cell is born there
                        }
                    }
                }
            }
        }
    }
}
