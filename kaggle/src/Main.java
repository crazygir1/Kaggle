import java.io.*;
import java.util.*;

public class Main {

	static DataSet train = new DataSet();
	static DataSet test = new DataSet();
	static DataSet split_train = new DataSet();
	static DataSet split_test = new DataSet();
	
	static Vector<String> tag_types = new Vector<String>();
	static Vector<Integer> tag_total = new Vector<Integer>();
	static Vector<Double> tag_mean = new Vector<Double>();
	
	static Vector<String> source_types = new Vector<String>();
	static Vector<Integer> source_total = new Vector<Integer>();
	static Vector<Double> source_mean = new Vector<Double>();

	static int[] daytime_total = new int[24];
	static double[] daytime_mean = new double[24];
	
	static double[][][] stat_mean;
	static int[][][] stat_total;
	
	public static void TypeAnalysis(DataSet ds) {

		System.out.println("\nSource types:");
		train.GetSourceTypes(source_types, source_total, source_mean);
		
		for (int i = 0; i < source_types.size(); i ++) {
			System.out.println(source_types.get(i) + " " + source_total.get(i) + " " + source_mean.get(i));
		}
		
		System.out.println("\nTag types:");
		train.GetTagTypes(tag_types, tag_total, tag_mean);
		for (int i = 0; i < tag_types.size(); i ++) {
			System.out.println(tag_types.get(i) + " " + tag_total.get(i) + " " + tag_mean.get(i));
		}
		
		System.out.println("\nHour Zone:");
		train.GetDayTime(daytime_total, daytime_mean);
		for (int i = 0; i < 24; i ++) {
			System.out.println(i + " " + daytime_total[i] + " " + daytime_mean[i]);
		}
		
		stat_mean = new double[source_types.size()][tag_types.size()][24];
		stat_total = new int[source_types.size()][tag_types.size()][24];
		
		for (int i = 0; i < ds.data.size(); i ++) {
			int source_ind = findSourceIndex(ds.data.get(i).source);
			int tag_ind = findTagTypeIndex(ds.data.get(i).tagtype);
			int hr_ind = Util.getHour(ds.data.get(i).ctime);
			stat_mean[source_ind][tag_ind][hr_ind] += ds.data.get(i).views;
			stat_total[source_ind][tag_ind][hr_ind] ++;
		}
		
		for (int i = 0; i < source_types.size(); i ++)
			for (int j = 0; j < tag_types.size(); j ++)
				for (int k = 0; k < 24; k ++) {
					if (stat_total[i][j][k] > 0) {
						stat_mean[i][j][k] /= stat_total[i][j][k];
					}
				}
	}
	
	static public int findTagTypeIndex(String str) {
		for (int j = 0; j < tag_types.size(); j ++) {
			if (str.equals(tag_types.get(j))) {
				return j;
			}
		}
		return -1;
	}
	
	static public int findSourceIndex(String str) {
		for (int j = 0; j < source_types.size(); j ++) {
			if (str.equals(source_types.get(j))) {
				return j;
			}
		}
		return -1;
	}
	
	static public double PredictTest(DataSet test, boolean calc) {
		double score = 0;
		int total = 0;
		
		Vector<DataPoint> data = test.data;
		for (int i = 0; i < data.size(); i ++) {
			
			double sum = 0;
			
			if (data.get(i).source.equals("remote_api_created")) {
				
			} else {

				int source_ind = findSourceIndex(test.data.get(i).source);
				int tag_ind = findTagTypeIndex(test.data.get(i).tagtype);
				int hr_ind = Util.getHour(test.data.get(i).ctime);
				if (source_ind == -1 || tag_ind == -1) sum = 0; else
				sum = source_mean.get(source_ind) * 0.5 + tag_mean.get(tag_ind) * 0.3 + daytime_mean[hr_ind] * 0.2;
			}
			
			if (calc) {
				score += (Math.log((int)sum + 1) - Math.log(data.get(i).views + 1))
						*(Math.log((int)sum + 1) - Math.log(data.get(i).views + 1));
				score += (Math.log((int)(Math.log((int)sum + 1)) + 1) - Math.log(data.get(i).votes + 1))
						*(Math.log((int)(Math.log((int)sum + 1)) + 1) - Math.log(data.get(i).votes + 1));
				score += (Math.log(0 + 1) - Math.log(data.get(i).comments + 1))
						*(Math.log(0 + 1) - Math.log(data.get(i).comments + 1));
				total += 3;
			}
			data.get(i).views = (int)(sum);
			data.get(i).votes = (int)(Math.log((int)sum + 1));
			data.get(i).comments = 0;
		}
		
		if (calc) {
			score /= total;
			return score;
		} else return 0;
	}
	
	public static void main(String[] args) throws IOException {
		
		CSV.read(train, "train.csv", false);
		
		/*
		train.crossSplit(split_train, split_test);
		TypeAnalysis(split_train);
		Double score = PredictTest(split_test, true);
		System.out.println(score);
		*/
		//CSV.write(split_train, "split_train.csv");
		//CSV.write(split_test, "split_test.csv");
		
		TypeAnalysis(train);
		CSV.read(test, "test.csv", true);
		PredictTest(test, false);
		CSV.writeAnswer(test, "submission.csv");
		
		System.out.println("Done.");
	}

}
