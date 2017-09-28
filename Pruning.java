/*******************************************************************************
 * @author Ram Anand Vutukuru (rxv162130), Christopher Kassap (cxk112830)
 *
 * Program Name: Decision Tree ID3
 * Github Repositories: Current Java Version -	https://github.com/Vutukuru7227/Decision-Tree-ID3-Java.git
 *						Original Python Version (unfinished) -	https://github.com/Vutukuru7227/Decision-Tree.git			
 * Component:  Pruning class
 * Purpose: This component is responsible for pruning the decision tree
 *******************************************************************************/

import java.util.Random;

public class Pruning {

	/**
     * pruneTree: Randomly deletes a number of nodes where the number is determined by the pruning factor
     *
     * @param  decision_tree : The completed decision tree structure
     * @param  pruning_factor : The pruning factor used to determine the number of nodes being deleted
	 * @return :  The pruned decision tree
     */
	public Tree pruneTree(Tree decision_tree, double pruning_factor) {
		Tree temp_decision_tree = new Tree();
		Tree check = new Tree();
		
		System.out.println("Total Number of nodes in the tree sent in the argument"+decision_tree.totalNumNodes(decision_tree));
		temp_decision_tree = decision_tree;
		
		int numOfNodesToPrune = (int) (totalNodesInATree(decision_tree) * pruning_factor);
		int[] pruneList = pruneList(numOfNodesToPrune , totalNodesInATree(decision_tree));
		System.out.println("Length = "+numOfNodesToPrune);
		
		for(int i=0;i<pruneList.length;i++) {
			//System.out.println(/*"I = "  +i+ */ " Value = "+pruneList[i]);
			temp_decision_tree = check.search(decision_tree, pruneList[i]);
			
			if(temp_decision_tree == null) continue;
			if(temp_decision_tree == decision_tree) continue;
			if(temp_decision_tree.leftChild == null || temp_decision_tree.rightChild == null) {
				//System.out.println("I am sending it away");
				continue;
			}
			
			temp_decision_tree.leftChild = null;
			temp_decision_tree.rightChild = null;
			
			int classOneCounter = 0;
			int classZeroCounter = 0;
			
			for(int j=0;j<temp_decision_tree.instanceCount;j++) {
				if(temp_decision_tree.dataSet[j][temp_decision_tree.dataSet[j].length - 1] == 1) classOneCounter++;
				else classZeroCounter++;
			}
			
			if(classOneCounter >= classZeroCounter)temp_decision_tree.object = 1;
			else temp_decision_tree.object = 0;
			
		}
		return decision_tree;
	}
	
	/**
     * totalNodesInATree: Returns the number of nodes in the decision tree
     *
     * @param  decision_tree : The completed decision tree structure
	 * @return :  The number of nodes in the decision tree
     */
	public int totalNodesInATree(Tree tree) {
		
		return tree.totalNumNodes(tree);
	}
	
	/**
     * pruneList: Returns the number of nodes in the decision tree
     *
     * @param  pruneCount : The number of nodes to be pruned
	 * @param  totalNumOfNodes : The total number of nodes in the tree
	 * @return :  The list of node numbers to be pruned from the tree
     */
	public int[] pruneList(int pruneCount,int totalNumOfNodes) {
		int[] pruneList = new int[pruneCount];
		Random random = new Random();
		for(int i=0;i<pruneList.length;i++) {
			int id = random.nextInt(totalNumOfNodes - 1)+1; //To avoid selecting the root element
			pruneList[i] = id;
		}
		return pruneList;
	}
}
