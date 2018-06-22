
//Don't change the class name
public class Container {
    private Point data;//Don't delete or change this field;
    private Container next;
    private Container prev;
    private Container connectToOtherList;
    private Container newConnectToOtherList;

    public Container(Point data, Container next, Container prev) {
        this(data);
        this.next = next;
        this.prev = prev;
    }

    public Container(Point data) {
        this.data = data;
        this.next = null;
        this.connectToOtherList = null;
        this.prev = null;
        this.newConnectToOtherList = null;
    }


    // Getters ans Setters
    public void setNewConnectToOtherList(Container container) {
        this.newConnectToOtherList = container;
    }


    public void setConnectToOtherList(Container container) {
        this.connectToOtherList = container;
    }

    public Container getConnectToOtherList() {
        return connectToOtherList;
    }

    public Container getNewConnectToOtherList() {
        return newConnectToOtherList;
    }

    //Don't delete or change this function
    public Point getData() {
        return data;
    }


    public Object setData(Point data) {
        Object tmp = this.data;
        this.data = data;
        return tmp;
    }

    public Container getNext() {
        return next;
    }

    public void setNext(Container next) {
        this.next = next;
    }


    public Container getPrev() {
        return prev;
    }

    public void setPrev(Container prev) {
        this.prev = prev;
    }


    //toString
    public String toString() {
        return data.toString();
    }

    //equals
    public boolean equals(Object other) {
        return data.equals(((Container) other).getData());
    }
}
