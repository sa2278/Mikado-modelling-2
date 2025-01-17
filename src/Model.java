import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class Model extends JPanel{
    public static final int RAYS_NUM = 100000;
    public static final Color POINT_COLOR = Color.RED;
    public static final Color LINE_COLOR_DRAWN = Color.BLACK;
    public static final Color LINE_COLOR_UNDRAWN = Color.GRAY;
    public static final Color OBJECT_COLOR = Color.RED;
    public static final int PARTICLE_NUM = 2;
    private  JFrame frame;
    double particleRadius = 75;
    public boolean isPainted = Boolean.FALSE;
    public ArrayList<RayPaths> rays = new ArrayList<>();
    public ArrayList<Particle> particle = new ArrayList<>();
    public ArrayList<Point2D.Double> intersections = new ArrayList<>();

    private Ellipse2D outerEdge;
    private double radius = 0;

    public ArrayList<Double> entropies = new ArrayList<>();
    public ArrayList<Double> distances = new ArrayList<>();

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        double padding = 10;
        radius = (double) Math.min(this.getWidth(), this.getHeight()) / 2 - padding * 2;

        outerEdge = new Ellipse2D.Double(padding, padding, radius * 2, radius * 2);
        g2.setStroke(new BasicStroke(4));
        g2.draw(outerEdge);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(POINT_COLOR);


        if(isPainted == Boolean.FALSE){
            double currentSect = 0;
            // g2.setStroke(new BasicStroke(4));

            // the new objects must not exist outside of the defined area, so the object diameter is subtracted

            double r = (radius - particleRadius - 4);

            double xObject1 = r * Math.cos(0) + radius + padding;
            double yObject1 = r * Math.sin(0) + radius + padding;
            particle.add(new Particle(new Point2D.Double(xObject1,yObject1), particleRadius));


            double xObject2 = r * Math.cos(Math.PI) + radius + padding;
            particle.add(new Particle(new Point2D.Double(xObject2,yObject1), particleRadius));

            System.out.println(xObject1 + " , " + yObject1 + " and " + xObject2 + " , " + yObject1);
            // g2.setStroke(new BasicStroke(1));


            for (int i = 0; i < RAYS_NUM; i++) {
                Random rand = new Random();
                double angle = Math.random() * 2 * Math.PI;
                r = radius * Math.sqrt(Math.random());
                double x = r * Math.cos(angle) + radius + padding;
                double y = r * Math.sin(angle) + radius + padding;

                double angle2 = Math.random() * 2 * Math.PI ;
                Point2D.Double start = new Point2D.Double(x + 1000 * Math.cos(angle2), y + 1000 * Math.sin(angle2));
                Point2D.Double end = new Point2D.Double((x - 1000 * Math.cos(angle2)), (y - 1000* Math.sin(angle2)));
                Line2D temp = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
                RayPaths currentRay = (new RayPaths(start, end, Boolean.FALSE));
                for (int j = 0; j < PARTICLE_NUM; j++){
                    if(Math.abs(temp.ptLineDist(particle.get(j).getX(), particle.get(j).getY())) < particleRadius){
                        currentRay.setDrawn(Boolean.FALSE);
                        break;
                    }
                    currentRay.setDrawn(Boolean.TRUE);
                }
                rays.add(currentRay);

            }
            // this is limited to 1000 for processing purposes
            for (int k = 0; k < 1000; k++) {
                RayPaths ray = rays.get(k);
                Line2D temp = new Line2D.Double(ray.startPoint.getX(), ray.getStartPoint().getY(), ray.getEndPoint().getX(), ray.getEndPoint().getY());
                if (ray.isDrawn == Boolean.TRUE){
                    g2.setColor(LINE_COLOR_DRAWN);
                    g2.draw(temp);
                }
                else{
                    g2.setColor(LINE_COLOR_UNDRAWN);
                    g2.draw(temp);
                }
            }

            Double distance = particle.get(0).distance(particle.get(1));
            Double entropy = calculateEntropy();
            distances.add(distance);
            entropies.add(entropy);

            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.BLACK);
            g2.draw(outerEdge);
            g2.setColor(OBJECT_COLOR);
            g2.setStroke(new BasicStroke(2));
            for(int ob = 0; ob < PARTICLE_NUM; ob++){
                g2.draw(objectsToDraw.get(ob));
            }
            g2.setStroke(new BasicStroke(1));

            isPainted = Boolean.TRUE;

        }
        else{
            rays.clear();
            for (int i = 0; i < RAYS_NUM; i++) {
                Random rand = new Random();
                double angle = Math.random() * 2 * Math.PI ;
                double r = radius * Math.sqrt(Math.random());
                double x = r * Math.cos(angle) + radius + padding;
                double y = r * Math.sin(angle) + radius + padding;

                double angle2 = Math.random() * 2 * Math.PI ;
                Point2D.Double start = new Point2D.Double(x + 1000 * Math.cos(angle2), y + 1000 * Math.sin(angle2));
                Point2D.Double end = new Point2D.Double((x - 1000 * Math.cos(angle2)), (y - 1000* Math.sin(angle2)));
                Line2D temp = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
                RayPaths currentRay = (new RayPaths(start, end, Boolean.FALSE));
                for (int j = 0; j < PARTICLE_NUM; j++){
                    if(Math.abs(temp.ptLineDist(particle.get(j).getX(), particle.get(j).getY())) < particleRadius){
                        currentRay.setDrawn(Boolean.FALSE);
                        break;
                    }
                    currentRay.setDrawn(Boolean.TRUE);
                }
                rays.add(currentRay);

            }

            for (int k = 0; k < 1000; k++) {
                RayPaths ray = rays.get(k);
                Line2D temp = new Line2D.Double(ray.startPoint.getX(), ray.getStartPoint().getY(), ray.getEndPoint().getX(), ray.getEndPoint().getY());
                if (ray.isDrawn == Boolean.TRUE){
                    g2.setColor(LINE_COLOR_DRAWN);
                    g2.draw(temp);
                }
                else{
                    g2.setColor(LINE_COLOR_UNDRAWN);
                    g2.draw(temp);
                }
            }
            Double distance = particle.get(0).distance(particle.get(1));
            Double entropy = calculateEntropy();
            distances.add(distance);
            entropies.add(entropy);

            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.BLACK);
            g2.draw(outerEdge);
            g2.setColor(OBJECT_COLOR);
            g2.setStroke(new BasicStroke(2));
            for(int ob = 0; ob < PARTICLE_NUM; ob++){
                g2.draw(objectsToDraw.get(ob));
            }
            g2.setStroke(new BasicStroke(1));
            System.out.println("after painting updated " + rays.size());

        }
        // TODO remove
        g2.setClip(new Ellipse2D.Double(padding, padding, radius * 2, radius * 2));
        System.out.println("after painting " + rays.size());

    }

    public Double calculateEntropy(){
        int nLegal = 0;
        for(RayPaths ray : rays){
            if(ray.isDrawn){
                nLegal += 1;
            }
        }
        return Math.log(nLegal);
    }

    public void update(int batchSize, Boolean random){
        for( int iter = 0; iter < batchSize; iter++){
            // update selects a random ray from the rays and flips update if it is not intersecting
            //updateObject();


            double index = getRandomNumber(0, RAYS_NUM - 1);
            System.out.println("in update" + rays.size());

            RayPaths currentRay = rays.get((int)index);
            Line2D temp = new Line2D.Double(currentRay.startPoint.getX(), currentRay.startPoint.getY(), currentRay.endPoint.getX(), currentRay.endPoint.getY());
            // check if there is any collision

            if(currentRay.getDrawn() == Boolean.TRUE){
                currentRay.setDrawn(Boolean.FALSE);
            }
            else{
                for (int j = 0; j < PARTICLE_NUM; j++){
                    if(Math.abs(temp.ptLineDist(particle.get(j).getX(), particle.get(j).getY())) < particleRadius){
                        currentRay.setDrawn(Boolean.FALSE);
                        break;
                    }
                    currentRay.setDrawn(Boolean.TRUE);
                }

            }
            for (int j = 0; j < PARTICLE_NUM; j++){
                for(RayPaths checkRays : rays){
                    Line2D checkLine = new Line2D.Double(checkRays.startPoint.getX(), checkRays.startPoint.getY(), checkRays.endPoint.getX(), checkRays.endPoint.getY());
                    if(Math.abs(checkLine.ptLineDist(particle.get(j).getX(), particle.get(j).getY())) < particleRadius){
                        checkRays.setDrawn(Boolean.FALSE);
                    }
                }
            }


        }
    }

    private double getRandomNumber(double min, double max) {
        return (double) ((Math.random() * (max - min)) + min);
    }
}
