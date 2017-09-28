/*******************************************************************************
 * @author Ram Anand Vutukuru (rxv162130), Christopher Kassap (cxk112830)
 *
 * Program Name: Decision Tree ID3
 * Github Repositories: Current Java Version -	https://github.com/Vutukuru7227/Decision-Tree-ID3-Java.git
 *						Original Python Version (unfinished) -	https://github.com/Vutukuru7227/Decision-Tree.git	
 * Component:  Accuracy class
 * Purpose: This class is used to calculate the accuracy against each set of data
 *******************************************************************************/

import java.io.File;
import java.util.Scanner;

public class Accuracy {
	
	/**
	 * calculateAccuracyHeuristics: Calculates the accuracy against the test data using the following formula:
		Accuracy = 
	 *
	 * @param  decision_tree : The completed decision tree structure
	 * @param  args : The training set path read as input from the commandline
	 * @param  file : Name of the data set for which the accuracy is being computed
	 */
	public void calculateAccuracyHeuristics(Tree decision_tree, String args, String file) {
		Tree temp_decision_tree = decision_tree;
		File training_set = new File(args);
		int no_of_feature_values;
		try {
			Scanner sc = new Scanner(training_set);
			String[] feature_values = sc.nextLine().split(",");
			no_of_feature_values = feature_values.length;
			int matched = 0;
			int total_instances = 0;
			
			while(sc.hasNextLine()) {
				String str = sc.nextLine();
				int column_value[] = new int[no_of_feature_values];
				for(int i=0;i<no_of_feature_values;i++) {
					column_value[i] = Integer.parseInt(str.split(",")[i]);
				}
				int index = 0;
				
				while(temp_decision_tree.leftChild != null || temp_decision_tree.rightChild != null) {
					
					for(int i=0;i<feature_values.length;i++) {
						if(temp_decision_tree.leftChild.getCheckedFeatureValues().equals(feature_values[i])) {
							index = i;
							break;
						}
					}
					if(column_value[index]==0) {
						temp_decision_tree = temp_decision_tree.rightChild;
					}
					else {
						temp_decision_tree = temp_decision_tree.leftChild;
					}
				}
				if(temp_decision_tree.getObject() == column_value[column_value.length-1]) {
					matched++;
					total_instances++;
				}else {
					total_instances++;
				}
				temp_decision_tree = decision_tree;
				
			}
			temp_decision_tree = decision_tree;
			
			System.out.println("Number of"+file+" instances = "+total_instances);
			System.out.println("Number of"+file+" attributes: "+(no_of_feature_values-1));
			
			
			System.out.println("Accuracy on the "+file+" dataset = "+((double)(matched/(double)total_instances)*100));
			sc.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
