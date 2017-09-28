/*******************************************************************************
 * @author Ram Anand Vutukuru (rxv162130), Christopher Kassap (cxk112830)
 *
 * Program Name: Decision Tree ID3
 * Component:  Tree class
 * Purpose: This component contains the node-based tree data structure used to represent the decision tree
 *******************************************************************************/
 
public class Tree {
	
	public Tree(int i) {
		this.nodeId = i;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	/**
	 * @return the leftChild
	 */
	public Tree getLeftChild() {
		return leftChild;
	}

	/**
	 * @param leftChild the leftChild to set
	 */
	public void setLeftChild(Tree leftChild) {
		this.leftChild = leftChild;
	}

	/**
	 * @return the rightChild
	 */
	public Tree getRightChild() {
		return rightChild;
	}

	/**
	 * @param rightChild the rightChild to set
	 */
	public void setRightChild(Tree rightChild) {
		this.rightChild = rightChild;
	}

	/**
	 * @return the object
	 */
	public int getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(int object) {
		this.object = object;
	}


	/**
	 * @return the dataSet
	 */
	public int[][] getDataSet() {
		return dataSet;
	}

	/**
     * setDataSet: sets the Tree's dataset to the training dataset parsed from an input file
     *
     * @param  dataSet : The training dataset parsed from an input file
     */
	public void setDataSet(int[][] dataSet) {
		this.dataSet = dataSet;
	}

	public Tree leftChild;
	public Tree rightChild;
	public int object;
	public int[][] dataSet;
	
	/**
	 * @return the checkedFeatureValues
	 */
	public String getCheckedFeatureValues() {
		return checkedFeatureValues;
	}

	/**
	 * @param checkedFeatureValues the checkedFeatureValues to set
	 */
	public void setCheckedFeatureValues(String checkedFeatureValues) {
		this.checkedFeatureValues = checkedFeatureValues;
	}

	/**
	 * @return the instanceCount
	 */
	public int getInstanceCount() {
		return instanceCount;
	}

	/**
	 * @param instanceCount the instanceCount to set
	 */
	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}

	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public String checkedFeatureValues;
	public int instanceCount;
	public int nodeId;

	
	public Tree() {
		// TODO Auto-generated constructor stub
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public Tree search(Tree tree, int value) {
		Tree result = null;
		if(tree.leftChild != null) result = search(tree.leftChild, value);
		if(tree.getNodeId() == value) return tree;
		if(result == null && tree.rightChild != null) result = search(tree.rightChild, value);
		return result;
	}
	
	public int noOfLeafNodes(Tree decision_tree) {
		int counter = 0;
		if(decision_tree.leftChild == null && decision_tree.rightChild == null) {
			return 1;
		}
		else {
			counter = noOfLeafNodes(decision_tree.leftChild) + noOfLeafNodes(decision_tree.rightChild);
		}
		return counter;
	}
	
	public int totalNumNodes(Tree decision_tree) {
		int counter = 0;
		if(decision_tree.leftChild != null || decision_tree.rightChild != null) counter = totalNumNodes(decision_tree.leftChild) + totalNumNodes(decision_tree.rightChild);
		return counter+1;
	}
	
	
}
