import java.io.*;

public class Main {
	static public String trainFileName = null;
	static public String testFileName = null;
	
	static public void main(String[] args) throws IOException {
		//Command-line arguments for train/test files
		if (args.length < 1) {
			System.out.println("Missing training set file name!.Exit.");
			return;
		} else {
			trainFileName = args[0];
		}
		if (args.length >= 2) {
			testFileName = args[1];
		}
		
		DecisionTree dtTree = new DecisionTree();
		
		DataSet trainData = new DataSet();
		trainData.readin(trainFileName, false);
		trainData.output("train.txt");
		
		//dtTree.train(trainData);
		
		if (testFileName != null) {
			DataSet testData = new DataSet();
			testData.readin(testFileName, true);
		}
		//dtTree.test(testData);
		
		//testData.output
	}
}
