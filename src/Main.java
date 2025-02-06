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
        final int[] qNum = {0};
        final int[] eNum = {0};

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_Q){
                model.update(stepSize, Boolean.FALSE);
                frame.repaint();
                qNum[0] += 1;
                System.out.println(qNum[0]);
            }
            if (e.getKeyCode() == KeyEvent.VK_E){
                model.update(stepSize, Boolean.TRUE);
                frame.repaint();
                eNum[0] += 1;
                System.out.println(eNum[0]);
            }
            if (e.getKeyCode() == KeyEvent.VK_A){
                int loopCount = 1000;
                for(int i = 0; i < loopCount; i++){
                    model.update(stepSize, Boolean.FALSE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.repaint();
                    System.out.println(i + " " + model.entropies.get(0) + "\n");

                }
            }

            if (e.getKeyCode() == KeyEvent.VK_D){
                int loopCount = 1000;
                for(int i = 0; i < loopCount; i++){
                    model.update(stepSize, Boolean.TRUE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.repaint();

                }
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
                    fileWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }



            }



            }

        });
    }


}