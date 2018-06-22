import java.util.Comparator;


/**
 * Created by ahinazar on 18/04/2017.
 */

public class PointCompareX implements Comparator<Point> {

    public int compare(Point i, Point j) {
        if (i.getX() > j.getX()) return 1;
        return -1;
    }
}

