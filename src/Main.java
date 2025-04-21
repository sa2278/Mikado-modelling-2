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
        int stepSize = 2;
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
                int loopCount = 153;
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
                            try{
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
                                FileWriter fileWriter = getFileWriter(dists, entropy);
                                fileWriter.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }

                });
                timer.start();





            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                ArrayList<Double> entropy = new ArrayList<>(model.entropies);
                ArrayList<Double> dists = new ArrayList<>(model.distances);

                try{
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
                    FileWriter fileWriter = getFileWriter(dists, entropy);
                    fileWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            }
        });
    }

    private static FileWriter getFileWriter(ArrayList<Double> dists, ArrayList<Double> entropy) throws IOException {

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