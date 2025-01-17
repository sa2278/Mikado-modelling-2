import java.awt.geom.Point2D;

public class RayPaths {
    Point2D.Double startPoint;
    Point2D.Double endPoint;
    Boolean isDrawn;

    public RayPaths(Point2D.Double startPoint, Point2D.Double endPoint, Boolean isDrawn){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.isDrawn = isDrawn;
    }

    public Point2D.Double getStartPoint() {
        return startPoint;
    }

    public Point2D.Double getEndPoint() {
        return endPoint;
    }

    public void setDrawn(Boolean drawn) {
        isDrawn = drawn;
    }

    public Boolean getDrawn() {
        return isDrawn;
    }

}