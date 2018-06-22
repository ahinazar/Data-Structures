import java.util.ArrayList;

//SUBMIT
public class BNode implements BNodeInterface {

    // ///////////////////BEGIN DO NOT CHANGE ///////////////////
    // ///////////////////BEGIN DO NOT CHANGE ///////////////////
    // ///////////////////BEGIN DO NOT CHANGE ///////////////////
    private final int t;
    private int numOfBlocks;
    private boolean isLeaf;
    private ArrayList<Block> blocksList;
    private ArrayList<BNode> childrenList;

    /**
     * Constructor for creating a node with a single child.<br>
     * Useful for creating a new root.
     */
    public BNode(int t, BNode firstChild) {
        this(t, false, 0);
        this.childrenList.add(firstChild);
    }

    /**
     * Constructor for creating a <b>leaf</b> node with a single block.
     */
    public BNode(int t, Block firstBlock) {
        this(t, true, 1);
        this.blocksList.add(firstBlock);
    }

    public BNode(int t, boolean isLeaf, int numOfBlocks) {
        this.t = t;
        this.isLeaf = isLeaf;
        this.numOfBlocks = numOfBlocks;
        this.blocksList = new ArrayList<Block>();
        this.childrenList = new ArrayList<BNode>();
    }

    // For testing purposes.
    public BNode(int t, int numOfBlocks, boolean isLeaf,
                 ArrayList<Block> blocksList, ArrayList<BNode> childrenList) {
        this.t = t;
        this.numOfBlocks = numOfBlocks;
        this.isLeaf = isLeaf;
        this.blocksList = blocksList;
        this.childrenList = childrenList;
    }

    @Override
    public int getT() {
        return t;
    }

    @Override
    public int getNumOfBlocks() {
        return numOfBlocks;
    }

    @Override
    public boolean isLeaf() {
        return isLeaf;
    }

    @Override
    public ArrayList<Block> getBlocksList() {
        return blocksList;
    }

    @Override
    public ArrayList<BNode> getChildrenList() {
        return childrenList;
    }

    @Override
    public boolean isFull() {
        return numOfBlocks == 2 * t - 1;
    }

    @Override
    public boolean isMinSize() {
        return numOfBlocks == t - 1;
    }

    @Override
    public boolean isEmpty() {
        return numOfBlocks == 0;
    }

    @Override
    public int getBlockKeyAt(int indx) {
        return blocksList.get(indx).getKey();
    }

    @Override
    public Block getBlockAt(int indx) {
        return blocksList.get(indx);
    }

    @Override
    public BNode getChildAt(int indx) {
        return childrenList.get(indx);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((blocksList == null) ? 0 : blocksList.hashCode());
        result = prime * result
                + ((childrenList == null) ? 0 : childrenList.hashCode());
        result = prime * result + (isLeaf ? 1231 : 1237);
        result = prime * result + numOfBlocks;
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
        BNode other = (BNode) obj;
        if (blocksList == null) {
            if (other.blocksList != null)
                return false;
        } else if (!blocksList.equals(other.blocksList))
            return false;
        if (childrenList == null) {
            if (other.childrenList != null)
                return false;
        } else if (!childrenList.equals(other.childrenList))
            return false;
        if (isLeaf != other.isLeaf)
            return false;
        if (numOfBlocks != other.numOfBlocks)
            return false;
        if (t != other.t)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BNode [t=" + t + ", numOfBlocks=" + numOfBlocks + ", isLeaf="
                + isLeaf + ", blocksList=" + blocksList + ", childrenList="
                + childrenList + "]";
    }

    // ///////////////////DO NOT CHANGE END///////////////////
    // ///////////////////DO NOT CHANGE END///////////////////
    // ///////////////////DO NOT CHANGE END///////////////////


    @Override
    //searches the block that contains key
    public Block search(int key) {
        int index = keyIndex(key); //find index that fits to key
        if (index < this.numOfBlocks && key == this.getBlockKeyAt(index)) // block found, just return it
            return this.getBlockAt(index);
        else if (!isLeaf()) // block not found and we want to check if there is child that contains key
            return this.getChildAt(index).search(key);
        return null; // we didn't find block that contains key
    }

    @Override
    // insert a new key in this node, The assumption is, the node must be non-full when this function is called
    public void insertNonFull(Block d) {
        int i = this.numOfBlocks - 1; //index
        if (this.isLeaf()) { //leaf - simple case, just add it
            while (i >= 0 && d.getKey() < this.getBlockKeyAt(i)) {
                i--;
            }
            this.blocksList.add(i + 1, d);
            this.numOfBlocks++;
        } else { //not leaf
            while (i >= 0 && d.getKey() < this.getBlockKeyAt(i)) {
                i--;
            }
            i++;
            if (this.getChildAt(i).isFull()) { //if child is full we need to split it
                this.splitChild(i);
                if (d.getKey() > this.getBlockKeyAt(i))
                    i++;
            }
            this.getChildAt(i).insertNonFull(d); //recursive call
        }
    }

    @Override
    // A function to delete key from the sub-tree rooted with this node
    public void delete(int key) {
        int i = keyIndex(key); //find index
        if (i < this.getNumOfBlocks() && key == this.getBlockKeyAt(i)) { //key is at this node
            if (this.getNumOfBlocks() > t - 1 && this.isLeaf()) { //easy case - more than t-1 blocks - just remove it
                this.blocksList.remove(i);
                this.numOfBlocks = this.numOfBlocks - 1;
            /*(key is in this BNode & this BNode is internal
                & the child y that precedes key has at least t keys)
                recursively delete the predecessor key′ of key, replace key by key′ in this BNode*/
            } else if (!isLeaf() && this.getChildAt(i).getNumOfBlocks() >= this.getT()) {
                Block pred = getPred(i);
                blocksList.set(i, pred);
                getChildAt(i).delete(pred.getKey());
            }
            //Symmetrically, if (key is in this BNode & this BNode is internal & the child z that follows key has at least t keys)
            else if (!isLeaf() && getChildAt(i).isMinSize() && getChildAt(i + 1).getNumOfBlocks() >= this.getT()) {
                Block succ = getSucc(i);
                blocksList.set(i, succ);
                getChildAt(i + 1).delete(succ.getKey());
            } else if (!isLeaf() && getChildAt(i).isMinSize() && getChildAt(i + 1).isMinSize()) { // both are minimum size, so we need to merge them
                BNode child = getChildAt(i); // child of this at index i
                mergeWithRightSibling(i);
                child.delete(key);
            }
        } else { //key is not at this node
            BNode child = getChildAt(i); // going to be child of this at index i
            if (child.isMinSize()) //we need to shift or merge in order to extend num of blocks at this BNode
                shiftOrMergeChildIfNeeded(i);
            child.delete(key); //recursive call to delete method
        }
    }

    @Override
    //creates sign of B-Tree as Merkle-B-Tree
    public MerkleBNode createHashNode() {
        if (this.isLeaf) { // easy case - we want to calculate by hash function all leaf's blocks data
            ArrayList<byte[]> dataList = new ArrayList<byte[]>();
            for (int i = 0; i < this.numOfBlocks; i++) {
                dataList.add(getBlockAt(i).getData()); //data of every node is array of bytes
            }
            byte[] hashValue = HashUtils.sha1Hash(dataList);
            return new MerkleBNode(hashValue);
        } else { //not leaf - create recursivly
            ArrayList<MerkleBNode> childrenList = new ArrayList<MerkleBNode>();
            for (int i = 0; i < this.getChildrenList().size(); i++) {
                childrenList.add(getChildAt(i).createHashNode()); //recursive call
            }
            ArrayList<byte[]> childs = new ArrayList<byte[]>();
            for (int i = 0; i < childrenList.size(); i++) {
                childs.add(childrenList.get(i).getHashValue());
            }
            ArrayList<byte[]> blocks = new ArrayList<byte[]>();
            int childIndex = 0;
            int blockIndex = 0;
            for (int i = 0; i < this.numOfBlocks * 2 + 1; i++) { //2*numblocks + 1 - me and my childs
                if (i % 2 == 0) { //go to child
                    blocks.add(childs.get(childIndex));
                    childIndex++;
                } else { //go to block
                    blocks.add(this.getBlockAt(blockIndex).getData());
                    blockIndex++;
                }
            }
            byte[] hashValue = HashUtils.sha1Hash(blocks);
            return new MerkleBNode(hashValue, false, childrenList);
        }
    }

    //Splits the child node at childIndex into 2 nodes.
    public void splitChild(int childIndex) {
        BNode y = this.getChildAt(childIndex);
        BNode z = new BNode(t, y.isLeaf(), t - 1); //allocate new node
        for (int j = 0; j <= (t - 1); j++) { //z's  blocks are y blocks from mid till end
            if (j < (t - 1)) {
                z.blocksList.add(y.getBlockAt(t));
                y.blocksList.remove(t);
            }
            if (!y.isLeaf()) { //updates z's children
                z.childrenList.add(y.getChildAt(t));
                y.childrenList.remove(t);
            }
        }
        this.blocksList.add(childIndex, (y.blocksList).get(t - 1));
        y.blocksList.remove(t - 1);
        this.childrenList.add(childIndex + 1, z);
        this.numOfBlocks++;
        y.numOfBlocks = t - 1;
    }

    //add additional block to the child node at childIndx, by shifting from left sibling.
    private void shiftFromLeftSibling(int childIndx) {
        BNode v = this.getChildAt(childIndx);
        BNode u = this.getChildAt(childIndx - 1);

        v.blocksList.add(0, this.getBlockAt(childIndx - 1)); // adding parent block that separates v and u to v
        v.numOfBlocks = v.numOfBlocks + 1; //update v's number of blocks
        this.blocksList.set(childIndx - 1, u.getBlockAt(u.getNumOfBlocks() - 1)); // u's biggest block key at his index at his parent
        u.blocksList.remove(u.getNumOfBlocks() - 1); // removes block that was copied to his parent
        if (!u.isLeaf()) { //moving u's child to v
            v.childrenList.add(0, u.getChildAt(u.getNumOfBlocks()));
            u.childrenList.remove(u.getChildAt(u.getNumOfBlocks()));
        }
        u.numOfBlocks = u.numOfBlocks - 1;
    }

    //Add additional block to the child node at childIndx, by shifting from right sibling.
    private void shiftFromRightSibling(int childIndx) {
        BNode v = this.getChildAt(childIndx);
        BNode w = this.getChildAt(childIndx + 1);

        v.blocksList.add(this.getBlockAt(childIndx)); //add parent's block
        v.numOfBlocks = v.numOfBlocks + 1; //update v numofblocks
        this.blocksList.set(childIndx, w.getBlockAt(0));  // w's smallest block key at his index at his parent
        w.blocksList.remove(0); // removes block that was copied to his parent
        if (!w.isLeaf()) { //moving w's child to v
            v.childrenList.add(w.getChildAt(0));
            w.childrenList.remove(w.getChildAt(0));
        }
        w.numOfBlocks = w.numOfBlocks - 1;
    }

     //Merges the child node at childIndx with its left sibling
     // The left sibling node is removed.
    private void mergeWithLeftSibling(int childIndx) {
        BNode child = this.childrenList.get(childIndx);
        BNode brother = this.childrenList.get(childIndx - 1);
        child.blocksList.add(0, this.blocksList.get(childIndx - 1)); //add parent block
        this.blocksList.remove(childIndx - 1); //remove parent block
        this.numOfBlocks = this.numOfBlocks - 1; //update parent number of blocks
        child.numOfBlocks = child.numOfBlocks + 1; //update child number of blocks
        for (int i = (brother.getNumOfBlocks() - 1); i >= 0; i--) { //copying sibling to child, includes it's children if exist
            child.blocksList.add(0, brother.blocksList.get(i));
            child.numOfBlocks = child.numOfBlocks + 1;
        }
        if (!brother.isLeaf()) // copying last sibling's child
            for (int i = brother.numOfBlocks; i >= 0; i--) {
                child.childrenList.add(0, brother.childrenList.get(i));
            }
        this.childrenList.remove(childIndx - 1); //remove sibling from children list of parent
    }

     //Merges the child node at childIndx with its right sibling
     //The right sibling node is removed.
    private void mergeWithRightSibling(int childIndx) {
        BNode child = this.childrenList.get(childIndx);
        BNode sibling = this.childrenList.get(childIndx + 1);
        child.blocksList.add(this.blocksList.get(childIndx)); //add parent block
        this.blocksList.remove(childIndx); //remove parent block
        this.numOfBlocks = this.numOfBlocks - 1; //update parent number of blocks
        child.numOfBlocks = child.numOfBlocks + 1; //update child number of blocks
        for (int i = 0; i < sibling.numOfBlocks; i++) { //copying sibling to child, includes it's children if exist
            child.blocksList.add(sibling.blocksList.get(i));
            child.numOfBlocks = child.numOfBlocks + 1;
            if (!sibling.isLeaf())
                child.childrenList.add(sibling.childrenList.get(i));
        }
        if (!sibling.isLeaf()) // copying last sibling's child
            child.childrenList.add(sibling.childrenList.get(sibling.numOfBlocks));
        this.childrenList.remove(childIndx + 1); //remove sibling from children list of parent
    }

    //returns the index of the first key that is greater than or equal to key
    public int keyIndex(int key) {
        int i = 0;
        while (i < getNumOfBlocks() && getBlockKeyAt(i) < key)
            i++;
        return i;
    }

    // A function to get predecessor of getBlockAt(idx)
    private Block getPred(int idx) {
        BNode curr = getChildAt(idx);
        while (!curr.isLeaf()) //  moves to the right most node until we reach a leaf
            curr = curr.getChildAt(curr.getNumOfBlocks());
        return curr.getBlockAt(curr.getNumOfBlocks() - 1); // return the last block of the leaf
    }

    // A function to get successor of getBlockAt(idx)
    private Block getSucc(int idx) {
        BNode curr = getChildAt(idx + 1);
        while (!curr.isLeaf()) // moves the left most node until we reach a leaf
            curr = curr.getChildAt(0);
        return curr.getBlockAt(0); // return the first key of the leaf
    }

    // True iff the child node at childIndx-1 exists and has more than t-1 blocks.
    private boolean childHasNonMinimalLeftSibling(int childIndx) {
        if (childIndx == 0)
            return false;
        return getChildAt(childIndx - 1).getNumOfBlocks() > (t - 1);
    }

    //True iff the child node at childIndx+1 exists and has more than t-1 blocks.
    private boolean childHasNonMinimalRightSibling(int childIndx) {
        if (childIndx == this.getNumOfBlocks())
            return false;
        return getChildAt(childIndx + 1).getNumOfBlocks() > (t - 1);
    }


    //Verifies the child node at childIndx has at least t blocks
    //If necessary a shift or merge is performed
    private void shiftOrMergeChildIfNeeded(int childIndx) {
        if (childHasNonMinimalRightSibling(childIndx))
            shiftFromRightSibling(childIndx);
        else {
            if (childHasNonMinimalLeftSibling(childIndx))
                shiftFromLeftSibling(childIndx);
            else {
                if (childIndx < childrenList.size() - 1)
                    mergeWithRightSibling(childIndx);
                else
                    mergeWithLeftSibling(childIndx);
            }
        }
    }

    //Setter of numOfBlocks
    public void setNumOfBlocks(int numOfBlocks) {
        this.numOfBlocks = numOfBlocks;
    }
}
