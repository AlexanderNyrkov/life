package org.shurik.life;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame f = new JFrame("Life");
        World world = new World();
        world.setPreferredSize(new Dimension(world.getWidth(), world.getHeight()));
        f.getContentPane().add(world); // control bar to which we add the game
        f.pack(); // sets the size of the container, which is needed to display all of the components
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // the action which will take place on the closure frame (exit application)
        f.setVisible(true);
    }
}
