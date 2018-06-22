// SUBMIT
public class BTree implements BTreeInterface {

    // ///////////////////BEGIN DO NOT CHANGE ///////////////////
    // ///////////////////BEGIN DO NOT CHANGE ///////////////////
    // ///////////////////BEGIN DO NOT CHANGE ///////////////////
    private BNode root;
    private final int t;

    /**
     * Construct an empty tree.
     */
    public BTree(int t) { //
        this.t = t;
        this.root = null;
    }

    // For testing purposes.
    public BTree(int t, BNode root) {
        this.t = t;
        this.root = root;
    }

    @Override
    public BNode getRoot() {
        return root;
    }

    @Override
    public int getT() {
        return t;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((root == null) ? 0 : root.hashCode());
        result = prime * result + t;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BTree other = (BTree) obj;
        if (root == null) {
            if (other.root != null)
                return false;
        } else if (!root.equals(other.root))
            return false;
        if (t != other.t)
            return false;
        return true;
    }

    // ///////////////////DO NOT CHANGE END///////////////////
    // ///////////////////DO NOT CHANGE END///////////////////
    // ///////////////////DO NOT CHANGE END///////////////////

    //searches the block that contains key
    @Override
    public Block search(int key) {
        if (root == null) { //empty b-tree - nothing to search
            return null;
        }
        return root.search(key);
    }

    @Override
    //inserts a new key in this B-Tree
    public void insert(Block b) {
        BNode r = root;
        if (r == null) { //empty tree
            root = new BNode(t, b);
        } else { //not empty tree
            if (r.isFull()) {  // height needs to grow
                BNode s = new BNode(t, r);
                root = s;
                s.splitChild(0); // 1 key is moving to root
                s.insertNonFull(b);
            } else {  // if root is not full
                r.insertNonFull(b);
            }
        }
    }

    @Override
    //deletes key from this B-Tree
    public void delete(int key) {
        if (search(key) != null) { //check that key exist, if not - do nothing
            if (root.getNumOfBlocks() == 1 && root.getChildrenList().size() == 0) // if there is only 1 block without children - just delete it immediately
                root = null;
            else if (root.getChildrenList().size() == 0){ // simple delete from root that he is also leaf
                int i = 0;
                while (i < root.getNumOfBlocks() && root.getBlockKeyAt(i) < key)
                    i++;
                root.getBlocksList().remove(i);
                root.setNumOfBlocks(root.getNumOfBlocks()-1);
            }else { //recursive delete by b-tree delete function
                root.delete(key);
                if (root.getNumOfBlocks() == 0 && !root.isLeaf())
                    root = root.getChildAt(0);
            }
        }
    }

    @Override
    //create MBT sign
    public MerkleBNode createMBT() {
        if(root == null){ //noting to do because BTree is empty
            return null;
        }
        return root.createHashNode(); //recursive createMBT by b-tree createHashNode function
    }
}
