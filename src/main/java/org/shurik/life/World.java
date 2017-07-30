package org.shurik.life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class World extends JPanel implements ActionListener {
    private final Timer mainTimer;
    private final Image img;
    private final int width;
    private final int height;
    private final int cellWidth;
    private final int cellHeight;
    private int x0;
    private int y0;
    private Random random;

    public World() {
        this.img = new ImageIcon(getClass().getResource("/Game.png")).getImage();
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
        this.x0 = 0;
        this.y0 = 0;
        this.mainTimer = new Timer(150, this);
        this.cellHeight = 30;
        this.cellWidth = 30;
        this.random = new Random();
        this.mainTimer.start();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
        life();
    }

    /**
     * matrix creates and adds the initial cells
     * @return matrix
     */
    public int[][] matrix() {
        int[][] arr = new int[30][30];

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
                        g.setColor(Color.green);
                    } else if (cell[i][j] == 2) {
                        g.setColor(Color.red);
                    } else if (cell[i][j] == 3) {
                        g.setColor(Color.cyan);
                    }
                    g.fillRect(x0, y0, cellWidth, cellHeight);
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
                    limitExceeded = 1;
                }

                Boolean somebodyOnBottom = cell[i + limitExceeded][j] != 0;
                Boolean somebodyOnUp = cell[i - limitExceeded][j] != 0;
                Boolean somebodyOnRight = cell[i][j + limitExceeded] != 0;
                Boolean somebodyOnLeft = cell[i][j - limitExceeded] != 0;
                Boolean somebodyOnBottomRight = cell[i + limitExceeded][j + limitExceeded] != 0;
                Boolean somebodyOnBottomLeft = cell[i + limitExceeded][j - limitExceeded] != 0;
                Boolean somebodyOnUpLeft = cell[i - limitExceeded][j - limitExceeded] != 0;
                Boolean somebodyOnUpRight = cell[i - limitExceeded][j + limitExceeded] != 0;

                if (i >= cell.length || i <= 0 || j >= cell.length || j <= 0) {
                    cell[i][j] = 0;
                }

                if (cell[i][j] != 0) {
                    int near = 0;

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
                    }

                    int chance = random.nextInt(100);
                    int nextGen;
                    if (cell[i][j] == 3) {
                        if (chance < 75) {
                            nextGen = 1;
                        } else {
                            nextGen = 3;
                        }
                        int immunityFuture = random.nextInt(100);
                        if (immunityFuture < 10) {
                            cell[i][j] = 0;
                        }
                    } else if (cell[i][j] == 2) {
                        if (chance < 75) {
                            nextGen = 2;
                        } else {
                            nextGen = 3;
                        }
                        int futureInfected = random.nextInt(100);
                        if (futureInfected < 75) {
                            cell[i][j] = 0; // 75% of cases, die
                        } else {
                            cell[i][j] = 3; // 25% and recovering gains immunity to disease
                        }
                    } else {
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
                            cell[i + limitExceeded][j] = nextGen;
                        } else if (!somebodyOnUp) {
                            cell[i - limitExceeded][j] = nextGen;
                        } else if (!somebodyOnRight) {
                            cell[i][j + limitExceeded] = nextGen;
                        } else if (!somebodyOnLeft) {
                            cell[i][j - limitExceeded] = nextGen;
                        } else if (!somebodyOnBottomRight) {
                            cell[i + limitExceeded][j + limitExceeded] = nextGen;
                        } else if (!somebodyOnUpLeft) {
                            cell[i - limitExceeded][j - limitExceeded] = nextGen;
                        } else if (!somebodyOnBottomLeft) {
                            cell[i + limitExceeded][j - limitExceeded] = nextGen;
                        } else if (!somebodyOnUpRight) {
                            cell[i - limitExceeded][j + limitExceeded] = nextGen;
                        }
                    }
                }
            }
        }
    }
}
