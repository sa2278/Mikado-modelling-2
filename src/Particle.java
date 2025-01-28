import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class Particle {
    private Point2D.Double centre;
    private double vx, vy;
    private Double radius;
    private Random rand = new Random();
    private Double horizVX = 0.5;

    public Particle(Point2D.Double centre, Double radius){
        this.centre = centre;
        this.radius = radius;
        this.vx = rand.nextDouble() * 2 - 1; // Random velocity x
        this.vy = rand.nextDouble() * 2 - 1; // Random velocity y
    }

    public void move(Double circleRadius, Point2D.Double regionCentre){
        centre.x += vx;
        centre.y += vy;
        // on the colision with the border, there is a minor random offset of between -0.1 to 0.1 to simulate a random bounce
        if (centre.distance(regionCentre)  > circleRadius - radius - 4){
            double angle = Math.atan2(centre.getX() - regionCentre.getX(), centre.getY() - regionCentre.getY());
            vx = -vx + ((rand.nextDouble() * 2 - 1) / 10);
            vy = -vy + ((rand.nextDouble() * 2 - 1) / 10);
        }
    }

    public void moveHorizontal(Double circleRadius, Point2D.Double regionCentre){
        centre.x += horizVX;
        // on the colision with the border, there is a minor random offset of between -0.1 to 0.1 to simulate a random bounce
        if (centre.distance(regionCentre)  > circleRadius - radius - 4){
            horizVX = -horizVX;
        }
    }

    public Double getX(){
        return centre.getX();
    }

    public Double getY(){
        return centre.getY();
    }

    public Ellipse2D.Double particleAsCircle(){
        return new Ellipse2D.Double(centre.getX() - radius, centre.getY() - radius, radius * 2, radius * 2);
    }

    public Double distance(Particle p2){
        return centre.distance(p2.centre);
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }
}