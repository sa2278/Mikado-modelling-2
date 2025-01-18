import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the step size");
        //int stepSize = Integer.parseInt(System.console().readLine());
        int stepSize = 10;
        JFrame frame = new JFrame();


        JLayeredPane layeredPane = new JLayeredPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setMinimumSize(new Dimension( 1000, 1000));
        Model model = new Model();
        frame.add(model);
        frame.setVisible(true);
        // TODO remove this
        // System.out.println("in main " + model.rays.size());
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    model.update(stepSize, Boolean.FALSE);
                    frame.repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_R){
                    //model.update(stepSize, Boolean.TRUE);
                    frame.repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    ArrayList<Double> entropy = new ArrayList<>(model.entropies);
                    ArrayList<Double> dists = new ArrayList<>(model.distances);
                    try{
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
                        FileWriter fileWriter = new FileWriter("Entropy_output" + formatter.format(new Date()) +".csv");
                        fileWriter.append("Distance,Log_Entropy");
                        fileWriter.append("\n");
                        for(int i = 0; i < dists.size(); i ++){
                            fileWriter.append(String.valueOf(dists.get(i)));
                            fileWriter.append(",");
                            fileWriter.append(String.valueOf(entropy.get(i)));
                            fileWriter.append("\n");


                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }


                }



            }

        });
    }


}