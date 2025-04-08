import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
// import java.util.Timer;

import javax.swing.*;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the step size");
        //int stepSize = Integer.parseInt(System.console().readLine());
        int stepSize = 1;
        JFrame frame = new JFrame();
        JLayeredPane layeredPane = new JLayeredPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setMinimumSize(new Dimension( 1000, 1000));
        frame.setResizable(Boolean.FALSE);
        Model model = new Model();
        frame.add(model);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_Q){
                model.update(stepSize, Boolean.FALSE);
                frame.repaint();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                model.update(stepSize, Boolean.FALSE);
                frame.repaint();
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT){
                model.update(stepSize, Boolean.FALSE);
                frame.repaint();
            }

            if (e.getKeyCode() == KeyEvent.VK_A){
                int loopCount = 308;
                int[] iter = {0};
                Timer timer = new Timer(100, new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        if(iter[0] < loopCount){
                            model.update(stepSize, Boolean.FALSE);
                            frame.repaint();
                            iter[0]++;
                        }
                        else {
                            ((Timer) e.getSource()).stop();
                            ArrayList<Double> entropy = new ArrayList<>(model.entropies);
                            ArrayList<Double> dists = new ArrayList<>(model.distances);
                            ArrayList<Double> gradients = new ArrayList<>(model.calculateGradients(entropy, dists));
                            try{
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
                                FileWriter fileWriter = getFileWriter(dists, entropy, gradients, model);
                                fileWriter.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }

                });
                timer.start();





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
                ArrayList<Double> gradients = new ArrayList<>(model.calculateGradients(entropy, dists));
                try{
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
                    FileWriter fileWriter = getFileWriter(dists, entropy, gradients, model);
                    fileWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            }
        });
    }

    private static FileWriter getFileWriter(ArrayList<Double> dists, ArrayList<Double> entropy, ArrayList<Double> grad, Model model) throws IOException {

        File file = new File("entropy_vs_distance.csv");
        int ind = 1;
        while(file.createNewFile() == Boolean.FALSE){
            file = new File("entropy_vs_distance" + ind + ".csv");
            ind += 1;
        }
        FileWriter fileWriter = new FileWriter(file.getCanonicalPath());
        fileWriter.append("Distance,Entropy");
        fileWriter.append("\n");
        for(int i = 0; i < dists.size(); i ++){
            fileWriter.append(String.valueOf(dists.get(i)));
            fileWriter.append(",");
            fileWriter.append(String.valueOf(entropy.get(i)));
            fileWriter.append("\n");
        }
        return fileWriter;
    }


}