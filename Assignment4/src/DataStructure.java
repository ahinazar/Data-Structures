import java.util.Comparator;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class DataStructure implements DT {

    //fields

    //heads
    private Container headX;
    private Container headY;

    //tails
    private Container tailX;
    private Container tailY;

    //size of points at linkedlist
    private int size;

    //////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
    //constractor
    public DataStructure() {
        headX = null;
        headY = null;
        tailX = null;
        tailY = null;
        size = 0;
    }

    @Override
    //adds point to it's place at sorted linkedlist X and at sorted linkedlist Y
    public void addPoint(Point point) {
        Container toAddX = new Container(point);
        Container toAddY = new Container(point);
        PointCompareX compX = new PointCompareX();
        PointCompareY compY = new PointCompareY();
        if (size == 0) { // there are no Containers at all
            size++;
            headX = toAddX;
            headY = toAddY;
            tailX = toAddX;
            tailY = toAddY;
            headX.setConnectToOtherList(headY);
            headY.setConnectToOtherList(headX);
            tailX.setConnectToOtherList(tailY);
            tailY.setConnectToOtherList(tailX);
        } else if (size == 1) { // there is one point at linkedlist
            size++;
            if (compX.compare(point, headX.getData()) == 1) { // point is bigger than head point (at x axis) than we add it to it's tail
                tailX = toAddX;
                tailX.setPrev(headX);
                headX.setNext(tailX);
                if (compY.compare(point, headY.getData()) == 1) { // point is bigger than head point (at y axis) than we add it to it's tail
                    tailY = toAddY;
                    tailY.setPrev(headY);
                    headY.setNext(tailY);
                    tailX.setConnectToOtherList(tailY);
                    tailY.setConnectToOtherList(tailX);
                    headX.setConnectToOtherList(headY);
                    headY.setConnectToOtherList(headX);
                } else { // point is smaller than head point (at y axis) than we add it to it's head
                    headY = toAddY;
                    tailY.setPrev(headY);
                    headY.setNext(tailY);
                    headY.setConnectToOtherList(tailX);
                    tailX.setConnectToOtherList(headY);
                    tailY.setConnectToOtherList(headX);
                    headX.setConnectToOtherList(tailY);
                }
            } else { // point is smaller than head point (at x axis) than we add it to it's head
                headX = toAddX;
                headX.setNext(tailX);
                tailX.setPrev(headX);
                if (compY.compare(point, headY.getData()) == 1) { // point is bigger than head point (at y axis) than we add it to it's tail
                    tailY = toAddY;
                    tailY.setPrev(headY);
                    headY.setNext(tailY);
                    headY.setConnectToOtherList(tailX);
                    tailX.setConnectToOtherList(headY);
                    tailY.setConnectToOtherList(headX);
                    headX.setConnectToOtherList(tailY);
                } else { // point is smaller than head point (at y axis) than we add it to it's head
                    headY = toAddY;
                    tailY.setPrev(headY);
                    headY.setNext(tailY);
                    headY.setConnectToOtherList(headX);
                    headX.setConnectToOtherList(headY);
                    tailX.setConnectToOtherList(tailY);
                    tailY.setConnectToOtherList(tailX);
                }
            }
        } else { // there is more than one Container
            size++;
            boolean found = false;
            Container curr = headX;
            while (!found && curr != null) { // we didn't found where to place the point or we didn't get to linkedlist end
                if (compX.compare(point, curr.getData()) == 1) { // if point is bigger than current point, we will compare to it's next
                    curr = curr.getNext();
                } else if (curr == headX) { // we add point to head at x axis linkedlist
                    toAddX.setNext(headX);
                    headX.setPrev(toAddX);
                    headX = toAddX;
                } else { // we found where to spot the new point
                    found = true;
                    curr.getPrev().setNext(toAddX);
                    toAddX.setPrev(curr.getPrev());
                    toAddX.setNext(curr);
                    curr.setPrev(toAddX);
                }
            }
            if (curr == null) { //we didn't find where to place the new point, than it's bigger than all points at linked list, we will put it at tail
                tailX.setNext(toAddX);
                toAddX.setPrev(tailX);
                tailX = toAddX;
            }
            found = false;
            curr = headY;
            while (!found && curr != null) {// we didn't found where to place the point or we didn't get to linkedlist end
                if (compY.compare(point, curr.getData()) == 1) { // if point is bigger than current point, we will compare to it's next
                    curr = curr.getNext();
                } else if (curr == headY) { // we add point to head at x axis linkedlist
                    toAddY.setNext(headY);
                    headY.setPrev(toAddY);
                    headY = toAddY;
                } else { // we found where to spot the new point
                    found = true;
                    curr.getPrev().setNext(toAddY);
                    toAddY.setPrev(curr.getPrev());
                    toAddY.setNext(curr);
                    curr.setPrev(toAddY);
                }
            }
            if (curr == null) { //we didn't find where to place the new point, than it's bigger than all points at linked list, we will put it at tail
                tailY.setNext(toAddY);
                toAddY.setPrev(tailY);
                tailY = toAddY;
            }
            toAddX.setConnectToOtherList(toAddY);
            toAddY.setConnectToOtherList(toAddX);
        }
    }

    @Override
    //returns array contains all points in range min<z<max such that z is x or y according to axis
    public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
        Point[] acc = new Point[size]; // accumulator that will save all points at range
        int i = 0; //index
        Container curr;
        if (axis.booleanValue()) {
            curr = headX;
            while (curr != null && curr.getData().getX() < min) { //to delete all smaller than min points
                curr = curr.getNext();
            }
            while (curr != null && curr.getData().getX() <= max) { //to enter at acc all good condition points
                acc[i] = curr.getData();
                i++;
                curr = curr.getNext();
            }
            if (i == 0) { //there are no points that fullfill condition, we will return empty array
                return new Point[0];
            } else { // we initiate new array of points that will be the answer
                Point[] ans = new Point[i];
                for (int j = 0; j < i; j++) {
                    ans[j] = acc[j];
                }
                return ans;
            }
        } else {
            curr = headY;
            while (curr != null && curr.getData().getY() < min) { //to delete all smaller than min points
                curr = curr.getNext();
            }
            while (curr != null && curr.getData().getY() <= max) { //to enter at acc all good condition points
                acc[i] = curr.getData();
                i++;
                curr = curr.getNext();
            }
            if (i == 0) { //there are no points that fullfill condition, we will return empty array
                return new Point[0];
            } else { // we initiate new array of points that will be the answer
                Point[] ans = new Point[i];
                for (int j = 0; j < i; j++) {
                    ans[j] = acc[j];
                }
                return ans;
            }
        }
    }

    @Override
    //returns array contains all points in range min<z<max such that z is x or y according to opposite axis
    public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
        Point[] acc = new Point[size]; // accumulator that will save all points at range
        int i = 0; //index
        Container curr;
        if (!(axis.booleanValue())) {
            curr = headX;
            while (curr != null) {
                if (curr.getData().getY() >= min && curr.getData().getY() <= max) { // if point condition fits, we will add it to accumulator array
                    acc[i] = curr.getData();
                    i++;
                }
                curr = curr.getNext();
            }
        } else {
            curr = headY;
            while (curr != null) {
                if (curr.getData().getX() >= min && curr.getData().getX() <= max) { // if point condition fits, we will add it to accumulator array
                    acc[i] = curr.getData();
                    i++;
                }
                curr = curr.getNext();
            }
        }
        if (i == 0) {  //there are no points that fullfill condition, we will return empty array
            return new Point[0];
        } else { // we initiate new array of points that will be the answer
            Point[] ans = new Point[i];
            for (int j = 0; j < i; j++) {
                ans[j] = acc[j];
            }
            return ans;
        }
    }

    @Override
    //returns density by density formula
    public double getDensity() {
        if (size == 1) // there is only one point at linkedlist, so density = 0;
            return 0;
        return (double) size / ((double) (tailX.getData().getX() - headX.getData().getX()) * (double) (tailY.getData().getY() - headY.getData().getY())); // by density formula
    }

    @Override
    //delets all points that their value bigger than max or smaller than min
    public void narrowRange(int min, int max, Boolean axis) {
        if (axis) { //x axis
            Container compHeadX = headX;
            while (compHeadX != null && compHeadX.getData().getX() < min) { //to delete all points that < min
                if (size == 1) {
                    headX = null;
                    headY = null;
                    tailX = null;
                    tailY = null;
                    size--;
                    return;
                }
                changeConnect(compHeadX.getConnectToOtherList(), !axis); // to delete from the other axis
                changeHead(compHeadX, axis); // to set new Head
                compHeadX = compHeadX.getNext();
                size -= 1;
            }
            Container compTailX = tailX;
            while (compTailX != null && compTailX.getData().getX() > max) { //to delete all points that > max
                if (size == 1) {
                    headX = null;
                    headY = null;
                    tailX = null;
                    tailY = null;
                    size--;
                    return;
                }
                changeConnect(compTailX.getConnectToOtherList(), !axis); // to delete from the other axis
                changeTail(compTailX, axis); // to set new Tail
                compTailX = compTailX.getPrev();
                size -= 1;
            }
        } else {
            Container compHeadY = headY;
            while (compHeadY != null && compHeadY.getData().getY() < min) { //to delete all points that < min
                if (size == 1) {
                    headX = null;
                    headY = null;
                    tailX = null;
                    tailY = null;
                    size--;
                    return;
                }
                changeConnect(compHeadY.getConnectToOtherList(), !axis); // to delete from the other axis
                changeHead(compHeadY, axis); // to set new Head
                compHeadY = compHeadY.getNext();
                size -= 1;
            }
            Container compTailY = tailY;
            while (compTailY != null && compTailY.getData().getY() > max) { //to delete all points that > max
                if (size == 1) {
                    headX = null;
                    headY = null;
                    tailX = null;
                    tailY = null;
                    size--;
                    return;
                }
                changeConnect(compTailY.getConnectToOtherList(), !axis); // to delete from the other axis
                changeTail(compTailY, axis); // to set new Tail
                compTailY = compTailY.getPrev();
                size -= 1;
            }
        }
    }


    @Override
    //return the largest axis (y or x), if true, than x is bigger than y. else otherwise.
    public Boolean getLargestAxis() {
        return ((tailX.getData().getX() - headX.getData().getX()) > (tailY.getData().getY() - headY.getData().getY()));
    }

    @Override
    // return the median Container according to axis
    public Container getMedian(Boolean axis) {
        Container current;
        int index = size / 2;
        if (axis == true) {
            current = headX;
            for (int j = 0; j < index; j = j + 1) {
                current = current.getNext();
            }
            return current;
        } else {
            current = headY;
            for (int j = 0; j < index; j = j + 1) {
                current = current.getNext();
            }
            return current;
        }
    }

    @Override
    //need to return 2 most close points at axis according to the input's container, width and axis.
    public Point[] nearestPairInStrip(Container container, double width, Boolean axis) {
        int counter = 0; // to count how many containers at width
        Point[] pointsInWidth;
        if (axis.booleanValue()) {
            double min = container.getData().getX() - (width / 2);
            double max = container.getData().getX() + (width / 2);
            Container start = container;
            while (start.getPrev() != null && start.getPrev() != headX && (start.getPrev().getData().getX() >= min)) { // left count
                start = start.getPrev();
                counter++;
            }
            if (start.getPrev() == headX && start.getPrev().getData().getX() >= min) {
                start = start.getPrev();
                counter++;
            }
            if (start == null) { // we got to head's place
                start = headX;
                counter--;
            }
            counter++;
            Container end = container;
            while (end.getNext() != null && end.getNext() != tailX && (end.getNext().getData().getX() <= max)) { // right count;
                end = end.getNext();
                counter++;
            }
            if (end.getNext() == tailX && end.getNext().getData().getX() <= max) {
                end = end.getPrev();
                counter++;
            }
            if (end == null) { // we got to tail's place
                end = tailX;
                counter--;
            }
            counter++; // for the input container
            if (counter == 1) { // just the input is at width
                return new Point[0];
            }
            if ((size) >= (counter * ((Math.log(counter)) / (Math.log(2))))) { // now we choose which way is faster and than we initialize pointsInWidth array
                pointsInWidth = new Point[counter];
                for (int i = 0; i < counter; i++) {
                    pointsInWidth[i] = start.getData();
                    start = start.getNext();
                }
                PointCompareY comp = new PointCompareY();
                Arrays.sort(pointsInWidth, comp);
            } else {
                pointsInWidth = getPointsInRangeOppAxis(((int) min) + 1, (int) max, axis);
            }
            Point[] ans = new Point[2];
            double minLengthBetweenPoints = Double.MAX_VALUE;
            for (int g = 0; g < pointsInWidth.length; g++) { // we count 7 points ahead
                for (int j = 1; j <= 7; j++) {
                    if ((g + j) < pointsInWidth.length) {
                        if (pointDistance(pointsInWidth[g], pointsInWidth[g + j]) < minLengthBetweenPoints) {
                            minLengthBetweenPoints = pointDistance(pointsInWidth[g], pointsInWidth[g + j]);
                            ans[0] = pointsInWidth[g];
                            ans[1] = pointsInWidth[g + j];
                        }
                    }
                }
            }
            return ans;
        } else {
            double min = container.getData().getY() - (width / 2);
            double max = container.getData().getY() + (width / 2);
            Container start = container;
            while (start.getPrev() != null && start.getPrev() != headY && (start.getPrev().getData().getY() >= min)) { // left count
                start = start.getPrev();
                counter++;
            }
            if (start.getPrev() == headY && start.getPrev().getData().getY() >= min) {
                start = start.getPrev();
                counter++;
            }
            if (start == null) { // we got to head's place
                start = headY;
                counter--;
            }
            counter++;
            Container end = container;
            while (end.getNext() != null && end.getNext() != tailY && (end.getNext().getData().getY() <= max)) { // right count;
                end = end.getNext();
                counter++;
            }
            if (end.getNext() == tailY && end.getNext().getData().getY() <= max) {
                end = end.getPrev();
                counter++;
            }
            if (end == null) { // we got to tail's place
                end = tailY;
                counter--;
            }
            counter++; // for the input container
            if (counter == 1) {
                return new Point[0];
            }
            if ((size) >= (counter * ((Math.log(counter)) / (Math.log(2))))) { // now we choose which way is faster and than we initialize pointsInWidth array
                pointsInWidth = new Point[counter];
                for (int i = 0; i < counter; i++) {
                    pointsInWidth[i] = start.getData();
                    start = start.getNext();
                }
                PointCompareX comp = new PointCompareX();
                Arrays.sort(pointsInWidth, comp);
            } else {
                pointsInWidth = getPointsInRangeOppAxis(((int) min) + 1, (int) max, axis);
            }
            Point[] ans = new Point[2];
            double minLengthBetweenPoints = Double.MAX_VALUE;
            for (int g = 0; g < pointsInWidth.length; g++) {  // we count 7 points ahead
                for (int j = 1; j <= 7; j++) {
                    if ((g + j) < pointsInWidth.length) {
                        if (pointDistance(pointsInWidth[g], pointsInWidth[g + j]) < minLengthBetweenPoints) {
                            minLengthBetweenPoints = pointDistance(pointsInWidth[g], pointsInWidth[g + j]);
                            ans[0] = pointsInWidth[g];
                            ans[1] = pointsInWidth[g + j];
                        }
                    }
                }
            }
            return ans;
        }
    }

    @Override
    public Point[] nearestPair() {
        Point[] ans = new Point[2];
        if (size < 2)
            return new Point[2];
        if (size <= 4) {
            return nearest4Points();
        }
        Container before;
        Container after;
        Boolean axis = getLargestAxis();
        Container median = getMedian(axis);
        before = median;
        double min = Double.MAX_VALUE;
        Container curr;

        DataStructure beforeMedian = new DataStructure();
        DataStructure afterMedian = new DataStructure();

        if (axis.booleanValue()) {
            curr = headX;
            Container newContainerLow1 = null;
            Container newContainerHigh1 = null;
            Container newContainerLow2 = null;
            Container newContainerHigh2 = null;
            if (curr.getData().getX() < median.getData().getX()) {//creates the head of before list
                newContainerLow1 = new Container(curr.getData());
                newContainerLow1.setNewConnectToOtherList(curr.getConnectToOtherList());
                curr.getConnectToOtherList().setNewConnectToOtherList(newContainerLow1);
                curr = curr.getNext();
            }

            beforeMedian.headX = newContainerLow1; //the head is the first container on list

            while (curr != null && curr.getData().getX() < median.getData().getX()) { //building the rest of the list till median
                newContainerHigh1 = new Container(curr.getData());
                newContainerHigh1.setNewConnectToOtherList(curr.getConnectToOtherList());
                curr.getConnectToOtherList().setNewConnectToOtherList(newContainerHigh1);
                newContainerLow1.setNext(newContainerHigh1);
                newContainerHigh1.setPrev(newContainerLow1);
                newContainerLow1 = newContainerHigh1;
                curr = curr.getNext();
            }

            beforeMedian.tailX = newContainerHigh1; //the tail is the last container before the median

            if (curr.getData().getX() == median.getData().getX()) { //creates the head of after list
                newContainerLow2 = new Container(curr.getData());
                newContainerLow2.setNewConnectToOtherList(curr.getConnectToOtherList());
                curr.getConnectToOtherList().setNewConnectToOtherList(newContainerLow2);
                curr = curr.getNext();
            }

            afterMedian.headX = newContainerLow2;//the head is the median container on list

            while (curr != null && curr.getData().getX() > median.getData().getX()) { //building the rest of the list till null
                newContainerHigh2 = new Container(curr.getData());
                newContainerHigh2.setNewConnectToOtherList(curr.getConnectToOtherList());
                curr.getConnectToOtherList().setNewConnectToOtherList(newContainerHigh2);
                newContainerLow2.setNext(newContainerHigh2);
                newContainerHigh2.setPrev(newContainerLow2);
                newContainerLow2 = newContainerHigh1;
                curr = curr.getNext();
            }

            afterMedian.tailX = newContainerHigh2; //the tail is the last container before null

            newContainerLow1 = null;
            newContainerLow2 = null;
            newContainerHigh1 = null;
            newContainerHigh2 = null;
            curr = headY;

            while (curr != null && (beforeMedian.headY == null)) { //FINDING THE HEAD OF OPP AXIS -BEFORE (y)
                if (curr.getNewConnectToOtherList().getData().getX() < median.getData().getX()) {
                    newContainerLow1 = new Container(curr.getData());
                    newContainerLow1.setConnectToOtherList(curr.getNewConnectToOtherList());
                    beforeMedian.headY = newContainerLow1;
                }
                curr = curr.getNext();
            }

            curr = headY;

            while (curr != null && (afterMedian.headY == null)) { //FINDING THE HEAD OF OPP AXIS -AFTER (y)
                if (curr.getNewConnectToOtherList().getData().getX() >= median.getData().getX()) {
                    newContainerLow2 = new Container(curr.getData());
                    newContainerLow2.setConnectToOtherList(curr.getNewConnectToOtherList());
                    afterMedian.headY = newContainerLow2;
                }
                curr = curr.getNext();
            }

            newContainerLow1 = beforeMedian.headY;
            newContainerLow2 = afterMedian.headY;
            newContainerHigh1 = beforeMedian.headY;
            newContainerHigh2 = afterMedian.headY;

            curr = headY.getNext(); //STARTS FROM THE SECOND NODE

            while (curr != null) { //building both of lists - BEFORE&AFTER
                if (curr.getNewConnectToOtherList() != beforeMedian.headY && curr.getNewConnectToOtherList() != afterMedian.headY) { //IF THE CONTAINER IS NOT HEAD
                    if (curr.getNewConnectToOtherList().getData().getX() < median.getData().getX()) { //CONNECT TO BEFORE LIST
                        newContainerHigh1 = new Container(curr.getData());  //NEW CONTAINER - SAME DATA
                        newContainerHigh1.setConnectToOtherList(curr.getNewConnectToOtherList()); //CONNECTS TO THE SAME X IN BEFORE LIST
                        curr.setNewConnectToOtherList(null); //SET TO NULL
                        newContainerHigh1.getConnectToOtherList().setNewConnectToOtherList(null); // set to null
                        newContainerLow1.setNext(newContainerHigh1); //THE HEAD OF BEFORE- SET NEXT TO THE NEW CONTAINER
                        newContainerHigh1.setPrev(newContainerLow1); //THE SAME
                        newContainerLow1 = newContainerHigh1;
                    } else {                                                                            //CONNECT TO AFTER LIST
                        newContainerHigh2 = new Container(curr.getData()); //NEW CONTAINER - SAME DATA
                        newContainerHigh2.setConnectToOtherList(curr.getNewConnectToOtherList()); //CONNECTS TO THE SAME X IN AFTER LIST
                        curr.setNewConnectToOtherList(null);    //SET TO NULL
                        newContainerHigh2.getConnectToOtherList().setNewConnectToOtherList(null); // set to null
                        newContainerLow2.setNext(newContainerHigh2);  //THE HEAD OF BEFORE- SET NEXT TO THE NEW CONTAINER
                        newContainerHigh2.setPrev(newContainerLow2);
                        newContainerLow2 = newContainerHigh2;
                    }
                } else {                                                                                                                 //ELSE THAE CONTAINER IS HEAD
                    curr.setNewConnectToOtherList(null);
                }
                curr = curr.getNext();
            }

            beforeMedian.tailY = newContainerHigh1;
            afterMedian.tailY = newContainerHigh2;  //UPDATES THE TAILS
            beforeMedian.headX.setNewConnectToOtherList(null);
            afterMedian.headX.setNewConnectToOtherList(null); // UPDATES THE HEAD'S NEW CONNECT TO NULL

        } else {
            curr = headY;
            Container newContainerLow1 = null;
            Container newContainerHigh1 = null;
            Container newContainerLow2 = null;
            Container newContainerHigh2 = null;
            if (curr.getData().getY() < median.getData().getY()) {//creates the head of before list
                newContainerLow1 = new Container(curr.getData());
                newContainerLow1.setNewConnectToOtherList(curr.getConnectToOtherList());
                curr.getConnectToOtherList().setNewConnectToOtherList(newContainerLow1);
                curr = curr.getNext();
            }

            beforeMedian.headY = newContainerLow1; //the head is the first container on list

            while (curr != null && curr.getData().getY() < median.getData().getY()) { //building the rest of the list till median
                newContainerHigh1 = new Container(curr.getData());
                newContainerHigh1.setNewConnectToOtherList(curr.getConnectToOtherList());
                curr.getConnectToOtherList().setNewConnectToOtherList(newContainerHigh1);
                newContainerLow1.setNext(newContainerHigh1);
                newContainerHigh1.setPrev(newContainerLow1);
                newContainerLow1 = newContainerHigh1;
                curr = curr.getNext();
            }

            beforeMedian.tailY = newContainerHigh1; //the tail is the last container before the median

            if (curr.getData().getY() == median.getData().getY()) { //creates the head of after list
                newContainerLow2 = new Container(curr.getData());
                newContainerLow2.setNewConnectToOtherList(curr.getConnectToOtherList());
                curr.getConnectToOtherList().setNewConnectToOtherList(newContainerLow2);
                curr = curr.getNext();
            }

            afterMedian.headY = newContainerLow2;//the head is the median container on list

            while (curr != null && curr.getData().getY() > median.getData().getY()) { //building the rest of the list till null
                newContainerHigh2 = new Container(curr.getData());
                newContainerHigh2.setNewConnectToOtherList(curr.getConnectToOtherList());
                curr.getConnectToOtherList().setNewConnectToOtherList(newContainerHigh2);
                newContainerLow2.setNext(newContainerHigh2);
                newContainerHigh2.setPrev(newContainerLow2);
                newContainerLow2 = newContainerHigh1;
                curr = curr.getNext();
            }

            afterMedian.tailY = newContainerHigh2; //the tail is the last container before null

            newContainerLow1 = null;
            newContainerLow2 = null;
            newContainerHigh1 = null;
            newContainerHigh2 = null;
            curr = headX;

            while (curr != null && (beforeMedian.headX == null)) { //FINDING THE HEAD OF OPP AXIS -BEFORE (y)
                if (curr.getNewConnectToOtherList().getData().getY() < median.getData().getY()) {
                    newContainerLow1 = new Container(curr.getData());
                    newContainerLow1.setConnectToOtherList(curr.getNewConnectToOtherList());
                    beforeMedian.headX = newContainerLow1;
                }
                curr = curr.getNext();
            }

            curr = headX;
            while (curr != null && (afterMedian.headX == null)) { //FINDING THE HEAD OF OPP AXIS -AFTER (y)
                if (curr.getNewConnectToOtherList().getData().getY() >= median.getData().getY()) {
                    newContainerLow2 = new Container(curr.getData());
                    newContainerLow2.setConnectToOtherList(curr.getNewConnectToOtherList());
                    afterMedian.headX = newContainerLow2;
                }
                curr = curr.getNext();
            }

            newContainerLow1 = beforeMedian.headX;
            newContainerLow2 = afterMedian.headX;
            newContainerHigh1 = beforeMedian.headX;
            newContainerHigh2 = afterMedian.headX;

            curr = headX.getNext(); //STARTS FROM THE SECOND NODE

            while (curr != null) { //building both of lists - BEFORE&AFTER
                if (curr.getNewConnectToOtherList() != beforeMedian.headX && curr.getNewConnectToOtherList() != afterMedian.headX) { //IF THE CONTAINER IS NOT HEAD
                    if (curr.getNewConnectToOtherList().getData().getY() < median.getData().getY()) { //CONNECT TO BEFORE LIST
                        newContainerHigh1 = new Container(curr.getData());  //NEW CONTAINER - SAME DATA
                        newContainerHigh1.setConnectToOtherList(curr.getNewConnectToOtherList()); //CONNECTS TO THE SAME X IN BEFORE LIST
                        curr.setNewConnectToOtherList(null); //SET TO NULL
                        newContainerHigh1.getConnectToOtherList().setNewConnectToOtherList(null); // set to null
                        newContainerLow1.setNext(newContainerHigh1); //THE HEAD OF BEFORE- SET NEXT TO THE NEW CONTAINER
                        newContainerHigh1.setPrev(newContainerLow1); //THE SAME
                        newContainerLow1 = newContainerHigh1;
                    } else {                                                                            //CONNECT TO AFTER LIST
                        newContainerHigh2 = new Container(curr.getData()); //NEW CONTAINER - SAME DATA
                        newContainerHigh2.setConnectToOtherList(curr.getNewConnectToOtherList()); //CONNECTS TO THE SAME X IN AFTER LIST
                        curr.setNewConnectToOtherList(null);    //SET TO NULL
                        newContainerHigh2.getConnectToOtherList().setNewConnectToOtherList(null); // set to null
                        newContainerLow2.setNext(newContainerHigh2);  //THE HEAD OF BEFORE- SET NEXT TO THE NEW CONTAINER
                        newContainerHigh2.setPrev(newContainerLow2);
                        newContainerLow2 = newContainerHigh2;
                    }
                } else {                                                                                                                 //ELSE THAE CONTAINER IS HEAD
                    curr.setNewConnectToOtherList(null);
                }
                curr = curr.getNext();
            }

            beforeMedian.tailX = newContainerHigh1;
            afterMedian.tailX = newContainerHigh2;  //UPDATES THE TAILS
            beforeMedian.headY.setNewConnectToOtherList(null);
            afterMedian.headY.setNewConnectToOtherList(null);
        }

        Point[] lowPair = beforeMedian.nearestPair();
        Point[] upperPair = afterMedian.nearestPair();

        if (lowPair[0] != null) {
            double lowDistance = pointDistance(lowPair[0], lowPair[1]);
            if (lowDistance < min) {
                min = lowDistance;
                ans = lowPair;
            }
        }
        if (upperPair[0] != null)

        {
            double upDistance = pointDistance(upperPair[0], upperPair[1]);
            if (upDistance < min) {
                min = upDistance;
                ans = upperPair;
            }
        }

        Point[] pairInStrip = nearestPairInStrip(median, (int) (min * 2), axis);
        if (pairInStrip[0] != null)

        {
            double stripDistance = pointDistance(pairInStrip[0], pairInStrip[1]);
            if (stripDistance < min) {
                min = stripDistance;
                ans = pairInStrip;
            }
        }
        return ans;
    }

    public double pointDistance(Point p1, Point p2) {
        return Math.sqrt((Math.pow((p1.getX() - p2.getX()), 2)) + (Math.pow((p1.getY() - p2.getY()), 2)));
    }

    // finds the nearest 2 points at points array that it's length is <=4
    private Point[] nearest4Points() {
        Container curr = headX;
        Container next = headX.getNext();
        boolean flag1 = false;
        Point[] ans = new Point[2];
        double minLengthBetweenPoints = Double.MAX_VALUE;
        while (curr != null && !flag1) {
            while (next != null) {
                if (pointDistance(curr.getData(), next.getData()) < minLengthBetweenPoints) {
                    minLengthBetweenPoints = pointDistance(curr.getData(), next.getData());
                    ans[0] = curr.getData();
                    ans[1] = next.getData();
                }
                next = next.getNext();
            }
            curr = curr.getNext();
            if (curr.getNext() == null) {
                flag1 = true;
            } else
                next = curr.getNext();
        }
        return ans;
    }

    //change linkedlist's connects to help narrowrange function
    private void changeConnect(Container container, boolean axis) {
        if (axis == true) {
            if (container.equals(headX))
                changeHead(container, axis);
            else if (container.equals(tailX))
                changeTail(container, axis);
            else {
                Container prev = container.getPrev();
                Container next = container.getNext();
                prev.setNext(next);
                next.setPrev(prev);
            }
        } else {
            if (container.equals(headY)) {
                changeHead(container, axis);
            } else if (container.equals(tailY)) {
                changeTail(container, axis);
            } else {
                Container prev = container.getPrev();
                Container next = container.getNext();
                prev.setNext(next);
                next.setPrev(prev);
            }
        }
    }


    //change linkedlist head to help narrowrange function
    private void changeHead(Container point, boolean axis) {
        if (axis == true) {
            Container newHead = headX.getNext();
            headX = newHead;
            if (headX != null)
                headX.setPrev(null);
        } else {
            Container newHead = headY.getNext();
            headY = newHead;
            if (headY != null)
                headY.setPrev(null);
        }
    }


    //change linkedlist tail to help narrowrange function
    private void changeTail(Container point, boolean axis) {
        if (axis == true) {
            Container newTail = tailX.getPrev();
            tailX = newTail;
            if (tailX != null)
                tailX.setNext(null);
        } else {
            Container newTail = tailY.getPrev();
            tailY = newTail;
            if (tailY != null)
                tailY.setNext(null);
        }
    }

}

