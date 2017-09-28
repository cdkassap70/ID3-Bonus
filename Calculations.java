/*******************************************************************************
 * @author Ram Anand Vutukuru (rxv162130), Christopher Kassap (cxk112830)
 *
 * Program Name: Decision Tree ID3
 * Github Repositories: Current Java Version -	https://github.com/Vutukuru7227/Decision-Tree-ID3-Java.git
 *						Original Python Version (unfinished) -	https://github.com/Vutukuru7227/Decision-Tree.git	
 * Component:  Calculations class
 * Purpose: This component is responsible for computing the information gain/entropy calculations utilized for selecting the best attributes for splitting the tree.
 *******************************************************************************/

public class Calculations {
	/**
     * calculateEntropy: Calculates the entropy of the input dataset using the following formula: -p/(p+n) log (p/p+n) - (n/p+n) log(n/p+n)
     * where p = positive outputs / all outputs
     * and n = negative outputs / all outputs
     * @param  dataSet : input array of output values
     * @return : The entropy of all class labels
     */
	public double calculateEntropy(int[] dataSet) {
		//Hashtable<Integer, Integer> countTable = new Hashtable<>();
		int oneCounter = 0;
		int zeroCounter = 0;
		
		for(int i : dataSet) {
			if(i == 0) zeroCounter++;
			else oneCounter++;
		}
		
		double pValue = oneCounter/(double)dataSet.length;
		
		double nValue = zeroCounter/(double)dataSet.length;
		
		if(pValue == 0) pValue = 1;
		else if (nValue == 0) nValue = 1;
		
		double entropy = ((-pValue) * (logBase2(pValue))) + ((-nValue) * (logBase2(nValue)));
		
		return entropy;
	}
	
	/**
     * splitDataEntropy: Calculates the information gain by subtracting average entropy from the base entropy (entropy of all class labels).
     *
     * @param  no_of_instances : The number of training instances
	 * @param  label : 
     * @param  feature_value_num : The feature value for which the split data entropy is being calculated
     * @param  class_label : The class label being used to calculate the split data entropy
     * @param  base_entropy : The entropy of all class labels
     * @return : The information gain result
     */
	public double splitDataEntropy(int no_of_instances, int label, int[][] data_set, int feature_value_num, int class_label) {
		int[] class_label_data = new int[no_of_instances];
		int satisfying_instances = 0;
		int total_instances = 0;
		
		for(int i=0, j=0;i<no_of_instances;i++) {
			if(data_set[i][feature_value_num] == label) {
				class_label_data[j] = data_set[i][class_label];
				j++;
				satisfying_instances++;
				total_instances++;
			}
			else {
				total_instances++;
			}
		}
		
		double result = ((satisfying_instances/(double)total_instances) * calculateEntropy(class_label_data));
		//System.out.println(label+"="+result);
		return result;
	}
	
	/**
     * informationGain: Calculates the information gain by subtracting average entropy from the base entropy (entropy of all class labels).
     *
     * @param  args : Arguments to the application. The arguments are training_set_path, validation_set_path, test_set_path, and pruning factor.
     * @param  no_of_instances : The number of training instances
     * @param  feature_value_num : The feature value for which the split data entropy is being calculated
     * @param  class_label : The class label being used to calculate the split data entropy
     * @param  base_entropy : The entropy of all class labels
     * @return : The information gain result
     */
	public double informationGain(int no_of_instances,int[][] data_set,int feature_value_num,int class_label, double base_entropy) {
		double total_entropy = splitDataEntropy(no_of_instances, 1, data_set, feature_value_num, class_label) + splitDataEntropy(no_of_instances, 0, data_set, feature_value_num, class_label);
		double result = base_entropy - total_entropy;
		
		return result;
	}
	
	/**
     * calculatePartialSetEntropy:
     *
     * @param  data_set : The parsed input data set
     * @param  no_of_instances : The number of training instances
     * @param  value : The feature value for which the partial data entropy is being calculated
     * @param  feature_value_column_id : The feature value for which the split data entropy is being calculated
     * @param  class_label : The class label being used to calculate the partial set entropy
     * @return : The entropy result
     */
	public double calculatePartialSetEntropy(int[][] data_set, int no_of_instances, int value, int feature_value_column_id, int class_label) {
		int[] class_label_for_attribute_id = new int[no_of_instances];
		
		for(int i=0,j=0; i<no_of_instances;i++) {
			if(data_set[i][feature_value_column_id] == value) {
				class_label_for_attribute_id[j] = data_set[i][class_label];
				j++;
			}
		}
		double result = calculateEntropy(class_label_for_attribute_id);
		
		return result;
	}
	
	/**
     * logBase2: Calculate the log base 2 of the given parameter
     *
     * @param  pnValue : The value for which we are trying to calculate the log base 2
     * @return : The log base 2 of pnValue
     */
	public double logBase2(double pnValue) {
		return (Math.log(pnValue)/Math.log(2));
	}
	
//	public static void main(String[] ar) {
//		int[] dataSet = {1,1,1,1,0,0,0,0};
//		
//		Calculations calc = new Calculations();
//		double entropy = calc.calculateEntropy(dataSet);
//		System.out.println(entropy);
//	}
}
