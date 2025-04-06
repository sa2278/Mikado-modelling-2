import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.*;

public class Model extends JPanel{
    public static final int RAYS_NUM = 1000000;
    public static final Color POINT_COLOR = Color.RED;
    public static final Color LINE_COLOR_DRAWN = Color.BLACK;
    public static final Color LINE_COLOR_UNDRAWN = Color.GRAY;
    public static final Color OBJECT_COLOR = Color.RED;
    public static final int PARTICLE_NUM = 2;
    private  JFrame frame;
    // double particleRadius = 75;
    double particleRadius = 75;
    public boolean isPainted = Boolean.FALSE;
    public ArrayList<RayPaths> rays = new ArrayList<>();
    public ArrayList<Particle> particles = new ArrayList<>();
    public ArrayList<Point2D.Double> intersections = new ArrayList<>();

    private Ellipse2D outerEdge;
    public double radius = 0;

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

            // in this case the diameter of each circle is 125
            double xObject1 = outerEdge.getCenterX() + particleRadius + 1;
            double yObject1 = outerEdge.getCenterY();
            particles.add(new Particle(new Point2D.Double(xObject1,yObject1), particleRadius));


            double xObject2 = outerEdge.getCenterX() - particleRadius - 1;
            particles.add(new Particle(new Point2D.Double(xObject2,yObject1), particleRadius));

            System.out.println(xObject1 + " , " + yObject1 + " and " + xObject2 + " , " + yObject1);
            // g2.setStroke(new BasicStroke(1));


            for (int i = 0; i < RAYS_NUM; i++) {
                double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
                r = radius * Math.sqrt(ThreadLocalRandom.current().nextDouble());
                double x = r * Math.cos(angle) + radius + padding;
                double y = r * Math.sin(angle) + radius + padding;

                double angle2 = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI ;
                Point2D.Double start = new Point2D.Double(x + 1000 * Math.cos(angle2), y + 1000 * Math.sin(angle2));
                Point2D.Double end = new Point2D.Double((x - 1000 * Math.cos(angle2)), (y - 1000* Math.sin(angle2)));
                Line2D temp = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
                RayPaths currentRay = (new RayPaths(start, end, Boolean.FALSE));
                for (int j = 0; j < PARTICLE_NUM; j++){
                    if(Math.abs(temp.ptLineDist(particles.get(j).getX(), particles.get(j).getY())) < particleRadius){
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

            Double distance = particles.get(0).distance(particles.get(1));
            Double entropy = calculateEntropy(rays);
            distances.add(distance);
            entropies.add(entropy);

            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.BLACK);
            g2.draw(outerEdge);
            g2.setColor(OBJECT_COLOR);
            g2.setStroke(new BasicStroke(2));
            for(int ob = 0; ob < PARTICLE_NUM; ob++){
                g2.draw(particles.get(ob).particleAsCircle());
            }
            g2.setStroke(new BasicStroke(1));
            isPainted = Boolean.TRUE;

        }
        else{
            rays.clear();
            for (int i = 0; i < RAYS_NUM; i++) {
                double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI ;
                double r = radius * Math.sqrt(ThreadLocalRandom.current().nextDouble());
                double x = r * Math.cos(angle) + radius + padding;
                double y = r * Math.sin(angle) + radius + padding;

                double angle2 = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI ;
                Point2D.Double start = new Point2D.Double(x + 1000 * Math.cos(angle2), y + 1000 * Math.sin(angle2));
                Point2D.Double end = new Point2D.Double((x - 1000 * Math.cos(angle2)), (y - 1000* Math.sin(angle2)));
                Line2D temp = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
                RayPaths currentRay = (new RayPaths(start, end, Boolean.FALSE));
                for (int j = 0; j < PARTICLE_NUM; j++){
                    if(Math.abs(temp.ptLineDist(particles.get(j).getX(), particles.get(j).getY())) < particleRadius){
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

            Double distance = particles.get(0).distance(particles.get(1));
            Double entropy = calculateEntropy(rays);
            distances.add(distance);
            entropies.add(entropy);

            System.out.println(distances.size());

            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.BLACK);
            g2.draw(outerEdge);
            g2.setColor(OBJECT_COLOR);
            g2.setStroke(new BasicStroke(2));
            for(int ob = 0; ob < PARTICLE_NUM; ob++){
                g2.draw(particles.get(ob).particleAsCircle());
            }
            g2.setStroke(new BasicStroke(1));
            // System.out.println("after painting updated " + rays.size());

        }
        // TODO remove
        g2.setClip(new Ellipse2D.Double(padding, padding, radius * 2, radius * 2));
        //  System.out.println("after painting " + rays.size());

    }

    public Double calculateEntropy(ArrayList<RayPaths> rayPaths){
        double nLegal = 0;
        for(RayPaths ray : rayPaths){
            if(ray.isDrawn){
                nLegal += 1;
            }
        }
        return nLegal / RAYS_NUM;
    }

    public ArrayList<Double> calculateGradients(ArrayList<Double> entropies, ArrayList<Double> distances){
        ArrayList<Double> gradients =  new ArrayList<>();
        for(int i = 0; i < entropies.size() - 1; i++){
            Double ds = entropies.get(i + 1) - entropies.get(i);
            Double dx = distances.get(i + 1) - distances.get(i);
            Double midpoint = distances.get(i) + (dx / 2);
            Double gradient = ds / dx;
            if (gradient < 0){
                gradients.add(gradient);
            }

        }
        return gradients;
    }

    public double getParticleRadius() {
        return particleRadius;
    }

    public void update(int batchSize, Boolean random){
        Double regionRadius = (double) Math.min(this.getWidth(), this.getHeight()) / 2 - 10 * 2;
        for( int iter = 0; iter < batchSize; iter++){
            // update selects a random ray from the rays and flips update if it is not intersecting
            double centreX = outerEdge.getCenterX();
            double centreY = outerEdge.getCenterY();
            Point2D.Double regionCentre =  new Point2D.Double(centreX, centreY);
            if(random){
                for(Particle particle : particles){
                    particle.move(regionRadius, regionCentre);
                }
            }
            else{
                Boolean direction = Boolean.TRUE;
                for(Particle particle : particles){
                    particle.moveHorizontal(regionRadius, regionCentre, direction);
                    direction = !(direction);
                }

            }



            double index = getRandomNumber(0, RAYS_NUM - 1);
            // For line debug System.out.println("in update" + rays.size());

            RayPaths currentRay = rays.get((int)index);
            Line2D temp = new Line2D.Double(currentRay.startPoint.getX(), currentRay.startPoint.getY(), currentRay.endPoint.getX(), currentRay.endPoint.getY());
            // check if there is any collision

            if(currentRay.getDrawn() == Boolean.TRUE){
                currentRay.setDrawn(Boolean.FALSE);
            }
            else{
                for (int j = 0; j < PARTICLE_NUM; j++){
                    if(Math.abs(temp.ptLineDist(particles.get(j).getX(), particles.get(j).getY())) < particleRadius){
                        currentRay.setDrawn(Boolean.FALSE);
                        break;
                    }
                    currentRay.setDrawn(Boolean.TRUE);
                }

            }
            for (int j = 0; j < PARTICLE_NUM; j++){
                for(RayPaths checkRays : rays){
                    Line2D checkLine = new Line2D.Double(checkRays.startPoint.getX(), checkRays.startPoint.getY(), checkRays.endPoint.getX(), checkRays.endPoint.getY());
                    if(Math.abs(checkLine.ptLineDist(particles.get(j).getX(), particles.get(j).getY())) < particleRadius){
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
