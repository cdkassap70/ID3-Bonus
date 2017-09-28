/*******************************************************************************
 * @author Ram Anand Vutukuru (rxv162130), Christopher Kassap (cxk112830)
 *
 * Program Name: Decision Tree ID3
 * Github Repositories: Current Java Version -	https://github.com/Vutukuru7227/Decision-Tree-ID3-Java.git
 *						Original Python Version (unfinished) -	https://github.com/Vutukuru7227/Decision-Tree.git	
 * Component:  DecisionTree class
 * Purpose: This component is responsible for producing the decision tree from the training data, pruning the tree, and printing the tree.
 *******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class DecisionTree {

	public static int max_no_of_feature_values;
	public static int count = 0;
	
	/**
     * buildTree: Builds the Decision Tree by reading the training data from file
     * and calling the recursive treeRecursion_ID3 method with the training instances,
     * feature values, and the base entropy calculation.
     *
     * @param  arg : The path to the training dataset file
     * @return : the completed Decision Tree object
     */
	public Tree buildTree(String arg, String tree_type) {
		String[] feature_values;
		int no_of_feature_values = 0;
		int[][] data_set;
		int no_of_instances = 0;

		
		Tree decisionTree = new Tree(++count);
		File training_set = new File(arg);
		try {
			Scanner sc = new Scanner(training_set);
			
			//TODO: Find the number of feature values
			feature_values = sc.nextLine().split(",");
			
			//TOD): Find the feature value count
			no_of_feature_values = feature_values.length;
			max_no_of_feature_values = feature_values.length;
			
			//TODO: Find the number of instances in the data set
			while(sc.hasNextLine()) {
				sc.nextLine();
				no_of_instances++;
			}
			
			//TODO: Fill in the instances
			sc.close();
			data_set = fillDataSet(training_set, no_of_instances, no_of_feature_values);
			
			int[] class_labels = fillClassLabels(data_set, no_of_instances, no_of_feature_values);
			
			//for(int i=0;i<class_labels.length;i++) {
			//System.out.println(class_labels[i]);
			//}
			
			//TODO: Calculate base entropy
			Calculations calculate = new Calculations();
			double base_entropy = calculate.calculateEntropy(class_labels);	
			
			decisionTree.setDataSet(data_set);
			if (tree_type.equals("ID3"))
			{
				treeRecursion_ID3(decisionTree, no_of_instances, no_of_feature_values, base_entropy, feature_values);
			}
			else
			{
				treeRecursion_Random(decisionTree, no_of_instances, no_of_feature_values, base_entropy, feature_values);
			}
			return decisionTree;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return decisionTree;
	}
	
	/**
     * fillDataSet: Parses the input dataset file into a two dimensional array of instances x feature values
     *
     * @param  data : Path to the training data
     * @param  no_of_instances : The number of training instances
     * @param  no_of_feature_values : The number of feature values per row in the dataset
     * @return : Two-dimensional array consisting of instances x feature values
     */
	public static int[][] fillDataSet(File data,int no_of_instances,int no_of_feature_values) {
		int[][] data_set = new int[no_of_instances][no_of_feature_values];

		try { 
			Scanner sc = new Scanner(data);
			sc.nextLine();
			for(int i=0;sc.hasNextLine();i++) {
				String str = sc.nextLine();
				for(int j=0;j<no_of_feature_values;j++) {
					data_set[i][j] = Integer.parseInt(str.split(",")[j]);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data_set;
	}
	
	/**
     * averageDepth: Computes the average depth of the tree using the leaf node depths
     *
     * @param  totalLeafNodeDepths : Total of the depths of the leaf nodes
     * @param  numOfLeafNodes : Number of leaf nodes
     * @return : The average depth of the tree
     */
	public static double averageDepth(double totalLeafNodeDepths, double numOfLeafNodes) {
		double averageDepth = 0;
		averageDepth = (totalLeafNodeDepths/numOfLeafNodes);
		return averageDepth;
	}
	
	public static int[] fillClassLabels(int[][] data_set, int no_of_instances, int no_of_feature_labels) {
		int[] class_labels = new int[no_of_instances];
		
		for(int i=0;i<no_of_instances;i++) {
			class_labels[i] = data_set[i][no_of_feature_labels - 1];
		}
		return class_labels;
	}
	
	/**
     * treeRecursion_Random: The recursive function used to create the decision tree structure (splits are determined randomly). Each recursive call adds a new node to the tree.
     *
     * @param  decision_tree : Path to the training data
     * @param  no_of_instances : The number of training instances
     * @param  no_of_feature_values : The number of feature values per row in the dataset
     * @param  base_entropy : The entropy calculation of the class labels for the training dataset
     */
	public static void treeRecursion_Random(Tree decision_tree,int no_of_instances,int no_of_feature_values,double base_entropy,String[] feature_values) {
		double highest_information_gain = 0.0;
		Calculations calculate = new Calculations();
		int feature_value_to_be_used = 0;
		int leftChildElementCounter = 0;
		int rightChildElementCounter = 0;
		int leftChildRow = 0;
		int leftChildColumn = 0;
		int rightChildRow = 0;
		int rightChildColumn = 0;
		int depthCounter = -1;
		int leafNodeCounter = 0;

		//TODO: Handle case where there are no instances to classify
		if(no_of_instances == 0) {
			Random random = new Random();
			decision_tree.setObject(random.nextInt(2));
			System.out.println(decision_tree.getObject());
			return;
		}
		
		//TODO: increment the depth counter
		depthCounter++;
		
		//TODO: Handle scenario where there is only one feature value left
		if(no_of_feature_values == 1) {
			int zeroCounter = 0;
			int oneCounter = 0;
			
			for(int i=0;i<no_of_instances;i++) {
				if(decision_tree.dataSet[i][0] == 0) zeroCounter++;
				else oneCounter++;
			}
			
			if(zeroCounter > oneCounter) decision_tree.object = 0;
			else decision_tree.object = 1;
			System.out.println(decision_tree.getObject());
			leafNodeCounter++;
			return;
		}
		
		//TODO: Get the number of class label of each set(values) 
		int class_one_count = 0;
		int class_zero_count = 0;
		
		for(int i=0;i<no_of_instances;i++) {
			if(decision_tree.dataSet[i][no_of_feature_values-1] == 1) class_one_count++;
			else class_zero_count++;
		}
		if(class_one_count == no_of_instances) {
			decision_tree.object = 1;
			System.out.println(decision_tree.getObject());
			return;
		}else if(class_zero_count == no_of_instances){
			decision_tree.object = 0;
			System.out.println(decision_tree.getObject());
			return;
		}
		System.out.println();
		
		String[] remaining_attributes = getNewFeatureValueArray(feature_values, feature_value_to_be_used);
		//TODO: Randomly select the attribute to split on
		Random random = new Random();
		feature_value_to_be_used = random.nextInt(remaining_attributes.length);
		
		//TODO: Get the number of elements on both childs individually
		for(int i=0;i<no_of_instances;i++) {
			if(decision_tree.dataSet[i][feature_value_to_be_used] == 0) rightChildElementCounter++;
			else leftChildElementCounter++;
		}
		
		decision_tree.instanceCount = no_of_instances;
		
		//TODO: Construct the left and right tree from the above details
		decision_tree.leftChild = new Tree(++count);
		//decision_tree.leftChild.dataSet = decision_tree.dataSet[no_of_instances][feature_value_to_be_used];
		decision_tree.leftChild.dataSet = new int[leftChildElementCounter][no_of_feature_values-1];
		
		decision_tree.rightChild = new Tree(++count);
		decision_tree.rightChild.dataSet = new int[rightChildElementCounter][no_of_feature_values-1];
		
		//TODO: Filling the left tree components and right tree components
		for(int i=0;i<no_of_instances;i++) {
			if(decision_tree.dataSet[i][feature_value_to_be_used] == 1) {
				for(int j=0;j<no_of_feature_values;j++) {
					if(j == feature_value_to_be_used) continue;
					else {
						decision_tree.leftChild.dataSet[leftChildRow][leftChildColumn] = decision_tree.dataSet[i][j];
						leftChildColumn++;
					}
				}
				leftChildRow++;
				leftChildColumn = 0;
			}else {
				for(int j =0;j<no_of_feature_values;j++) {
					if(j == feature_value_to_be_used) continue;
					else {
						decision_tree.rightChild.dataSet[rightChildRow][rightChildColumn] = decision_tree.dataSet[i][j];
						rightChildColumn++;
					}
				}
				rightChildRow++;
				rightChildColumn = 0;
			}
		}	
		
		//String[] remaining_attributes = getNewFeatureValueArray(feature_values, feature_value_to_be_used);
		
		//Building the left tree recursively
		decision_tree.leftChild.setCheckedFeatureValues(feature_values[feature_value_to_be_used]);

		for(int count = max_no_of_feature_values; count>no_of_feature_values;count--) System.out.print("| ");
		System.out.print(decision_tree.leftChild.getCheckedFeatureValues()+"= 1 :");
		decision_tree.leftChild.instanceCount = leftChildElementCounter;
		treeRecursion_Random(decision_tree.leftChild, leftChildElementCounter, no_of_feature_values - 1, calculate.calculatePartialSetEntropy(decision_tree.dataSet, no_of_instances, 1, feature_value_to_be_used, no_of_feature_values - 1), remaining_attributes);
		
		//Building the right tree recursively
		decision_tree.rightChild.setCheckedFeatureValues(feature_values[feature_value_to_be_used]);
		for(int count = max_no_of_feature_values; count>no_of_feature_values;count--) System.out.print("| ");
		System.out.print(decision_tree.rightChild.getCheckedFeatureValues()+"= 0 :");
		decision_tree.rightChild.instanceCount = rightChildElementCounter;
		treeRecursion_Random(decision_tree.rightChild, rightChildElementCounter, no_of_feature_values - 1, calculate.calculatePartialSetEntropy(decision_tree.dataSet, no_of_instances, 0, feature_value_to_be_used, no_of_feature_values - 1), remaining_attributes);
	}
	
	/**
     * treeRecursion_ID3: The recursive function used to create the decision tree structure (splits are determined by the ID3 algorithm). Each recursive call adds a new node to the tree.
     *
     * @param  decision_tree : Path to the training data
     * @param  no_of_instances : The number of training instances
     * @param  no_of_feature_values : The number of feature values per row in the dataset
     * @param  base_entropy : The entropy calculation of the class labels for the training dataset
     */
	public static void treeRecursion_ID3(Tree decision_tree,int no_of_instances,int no_of_feature_values,double base_entropy,String[] feature_values) {
		double highest_information_gain = 0.0;
		Calculations calculate = new Calculations();
		int feature_value_to_be_used = 0;
		int leftChildElementCounter = 0;
		int rightChildElementCounter = 0;
		int leftChildRow = 0;
		int leftChildColumn = 0;
		int rightChildRow = 0;
		int rightChildColumn = 0;

		//TODO: Handle case where there are no instances to classify
		if(no_of_instances == 0) {
			Random random = new Random();
			decision_tree.setObject(random.nextInt(2));
			System.out.println(decision_tree.getObject());
			return;
		}
		
		//TODO: Handle scenario where there is only one feature value left
		if(no_of_feature_values == 1) {
			int zeroCounter = 0;
			int oneCounter = 0;
			
			for(int i=0;i<no_of_instances;i++) {
				if(decision_tree.dataSet[i][0] == 0) zeroCounter++;
				else oneCounter++;
			}
			
			if(zeroCounter > oneCounter) decision_tree.object = 0;
			else decision_tree.object = 1;
			System.out.println(decision_tree.getObject());
			return;
		}
		
		//TODO: Get the number of class label of each set(values) 
		int class_one_count = 0;
		int class_zero_count = 0;
		
		for(int i=0;i<no_of_instances;i++) {
			if(decision_tree.dataSet[i][no_of_feature_values-1] == 1) class_one_count++;
			else class_zero_count++;
		}
		if(class_one_count == no_of_instances) {
			decision_tree.object = 1;
			System.out.println(decision_tree.getObject());
			return;
		}else if(class_zero_count == no_of_instances){
			decision_tree.object = 0;
			System.out.println(decision_tree.getObject());
			return;
		}
		System.out.println();
		//System.out.println("Base Entropy="+base_entropy);
		//TODO: Identify the best attribute to split based on the information gain
		for(int i=0; i < no_of_feature_values-1 ;i++) {
			double information_gain = calculate.informationGain(no_of_instances, decision_tree.dataSet, i, no_of_feature_values-1, base_entropy);
			//System.out.println("Information Gain for :"+feature_values[i]+" = "+information_gain);
			if(information_gain > highest_information_gain) {
				highest_information_gain = information_gain;
				feature_value_to_be_used = i;
			}
		}
		
		//TODO: Get the number of elements on both childs individually
		for(int i=0;i<no_of_instances;i++) {
			if(decision_tree.dataSet[i][feature_value_to_be_used] == 0) rightChildElementCounter++;
			else leftChildElementCounter++;
		}
		
		decision_tree.instanceCount = no_of_instances;
		
		//TODO: Construct the left and right tree from the above details
		decision_tree.leftChild = new Tree(++count);
		//decision_tree.leftChild.dataSet = decision_tree.dataSet[no_of_instances][feature_value_to_be_used];
		decision_tree.leftChild.dataSet = new int[leftChildElementCounter][no_of_feature_values-1];
		
		decision_tree.rightChild = new Tree(++count);
		decision_tree.rightChild.dataSet = new int[rightChildElementCounter][no_of_feature_values-1];
		
		//TODO: Filling the left tree components and right tree components
		for(int i=0;i<no_of_instances;i++) {
			if(decision_tree.dataSet[i][feature_value_to_be_used] == 1) {
				for(int j=0;j<no_of_feature_values;j++) {
					if(j == feature_value_to_be_used) continue;
					else {
						decision_tree.leftChild.dataSet[leftChildRow][leftChildColumn] = decision_tree.dataSet[i][j];
						leftChildColumn++;
					}
				}
				leftChildRow++;
				leftChildColumn = 0;
			}else {
				for(int j =0;j<no_of_feature_values;j++) {
					if(j == feature_value_to_be_used) continue;
					else {
						decision_tree.rightChild.dataSet[rightChildRow][rightChildColumn] = decision_tree.dataSet[i][j];
						rightChildColumn++;
					}
				}
				rightChildRow++;
				rightChildColumn = 0;
			}
		}	
		
		String[] remaining_attributes = getNewFeatureValueArray(feature_values, feature_value_to_be_used);
		
		//Building the left tree recursively
		decision_tree.leftChild.setCheckedFeatureValues(feature_values[feature_value_to_be_used]);

		for(int count = max_no_of_feature_values; count>no_of_feature_values;count--) System.out.print("| ");
		System.out.print(decision_tree.leftChild.getCheckedFeatureValues()+"= 1 :");
		decision_tree.leftChild.instanceCount = leftChildElementCounter;
		treeRecursion_ID3(decision_tree.leftChild, leftChildElementCounter, no_of_feature_values - 1, calculate.calculatePartialSetEntropy(decision_tree.dataSet, no_of_instances, 1, feature_value_to_be_used, no_of_feature_values - 1), remaining_attributes);
		
		//Building the right tree recursively
		decision_tree.rightChild.setCheckedFeatureValues(feature_values[feature_value_to_be_used]);
		for(int count = max_no_of_feature_values; count>no_of_feature_values;count--) System.out.print("| ");
		System.out.print(decision_tree.rightChild.getCheckedFeatureValues()+"= 0 :");
		decision_tree.rightChild.instanceCount = rightChildElementCounter;
		treeRecursion_ID3(decision_tree.rightChild, rightChildElementCounter, no_of_feature_values - 1, calculate.calculatePartialSetEntropy(decision_tree.dataSet, no_of_instances, 0, feature_value_to_be_used, no_of_feature_values - 1), remaining_attributes);
		
		
		
	}
	
	/**
     * getNewFeatureValueArray: Returns a list consisting of the remaining unused feature values
     *
     * @param  Feature_Values : The feature values of a row in the dataset
     * @param  feature_value_to_be_used : Remaining unused feature values
     * @return : An array consisting of the remaining unused feature values
     */
	public static String[] getNewFeatureValueArray(String[] Feature_Values, int feature_value_to_be_used) {
		String newFeatureValueStringArray[] = new String[Feature_Values.length-1];
		for(int i=0,j=0; i< Feature_Values.length;i++) {
			if(feature_value_to_be_used == i) continue;
			else {
				newFeatureValueStringArray[j] = Feature_Values[i];
				j++;
			}
		}
		
		return newFeatureValueStringArray;
	}
	
	
	/**
     * main: Starting point of the application
     *
     * @param  args : Arguments to the application. The arguments are training_set_path, validation_set_path, test_set_path, and pruning factor.
     */
	public static void main(String[] args) {
		
		DecisionTree dt = new DecisionTree();
		Tree decision_tree = new Tree();
		Tree prunedDecisionTree = new Tree();
		double pruning_factor = Double.parseDouble(args[3]);
		String tree_type = args[4];
		
		//System.out.println("Pre-Pruned Accuracy");
		//System.out.println("-----------------------------------");
		Accuracy accuracy = new Accuracy();
		decision_tree = dt.buildTree(args[0], tree_type);
		
		
		System.out.println("Number of training instances = "+ decision_tree.instanceCount);
		System.out.println("Number of training attributes = "+ max_no_of_feature_values);
		accuracy.calculateAccuracyHeuristics(decision_tree, args[0], " training");
		System.out.println();
		
		accuracy.calculateAccuracyHeuristics(decision_tree, args[1], " validation");
		System.out.println();
		
		accuracy.calculateAccuracyHeuristics(decision_tree, args[2], " test");		
		System.out.println();
		
		System.out.println("==============================================");
		
		/*
		System.out.println("Post-Pruning Accuracy");
		System.out.println("---------------------------------");
		
		Pruning prune = new Pruning();
		PrintTree print = new PrintTree();
		
		prunedDecisionTree = prune.pruneTree(decision_tree, pruning_factor);
		print.printTree(prunedDecisionTree, max_no_of_feature_values);
		accuracy.calculateAccuracyHeuristics(prunedDecisionTree, args[0], " Training Set");
		System.out.println();
		
		accuracy.calculateAccuracyHeuristics(prunedDecisionTree, args[1], " Validation Set");
		System.out.println();
		
		accuracy.calculateAccuracyHeuristics(prunedDecisionTree, args[2], " Testing Set");
		*/
	}
}
