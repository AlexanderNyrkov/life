package org.shurik.life;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Шурик on 10.01.2017.
 */
public class Main {
    public static void main(String[] args) {
        JFrame f = new JFrame("Life");
        World world = new World(); // create an object of the game
        world.setPreferredSize(new Dimension(world.getWidth(), world.getHeight())); // set the size of the game
        f.getContentPane().add(world); // control bar to which we add the game
        f.pack(); // sets the size of the container, which is needed to display all of the components
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // the action which will take place on the closure frame (exit application)
        f.setVisible(true); // make frame visible
    }
}
