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
	
	static int[] month_total = new int[30];
	static double[] month_mean = new double[30];
	
	static double[][][][] stat_mean;
	static int[][][][] stat_total;
	
	static double[][][][] vote_mean;
	static double[][][][] comment_mean;
	
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
		
		System.out.println("\nMonth Zone:");
		train.GetMonthTime(month_total, month_mean);
		for (int i = 0; i < 12; i ++) {
			System.out.println(i + " " + month_total[i] + " " + month_mean[i]);
		}
		
		stat_mean = new double[source_types.size()][tag_types.size()][24][5];
		vote_mean = new double[source_types.size()][tag_types.size()][24][5];
		comment_mean = new double[source_types.size()][tag_types.size()][24][5];
		stat_total = new int[source_types.size()][tag_types.size()][24][5];
		
		for (int i = 0; i < ds.data.size(); i ++) {
			int source_ind = findSourceIndex(ds.data.get(i).source);
			int tag_ind = findTagTypeIndex(ds.data.get(i).tagtype);
			int mon_ind = Util.getMonth(ds.data.get(i).ctime);
			int hr_ind = Util.getHour(ds.data.get(i).ctime);
			int place_ind = findCityIndex(ds.data.get(i).latitude, ds.data.get(i).longitude);
			stat_mean[source_ind][tag_ind][hr_ind][place_ind] += ds.data.get(i).views;
			vote_mean[source_ind][tag_ind][hr_ind][place_ind] += ds.data.get(i).votes;
			comment_mean[source_ind][tag_ind][hr_ind][place_ind] += ds.data.get(i).comments;
			stat_total[source_ind][tag_ind][hr_ind][place_ind] ++;
		}
		
		for (int i = 0; i < source_types.size(); i ++)
			for (int j = 0; j < tag_types.size(); j ++)
				for (int k = 0; k < 24; k ++) 
					for (int l = 0; l < 4; l ++) {
						if (stat_total[i][j][k][l] > 0) {
							stat_mean[i][j][k][l] /= stat_total[i][j][k][l];
							vote_mean[i][j][k][l] /= stat_total[i][j][k][l];
							comment_mean[i][j][k][l] /= stat_total[i][j][k][l];
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
	
	static public int findCityIndex(double latitude, double longitude) {
		if (36 < latitude && latitude < 39 && -79 < longitude && longitude < -76)
			return 0;
		if (40 < latitude && latitude < 43 && -75 < longitude && longitude < -71)
			return 1;
		if (36 < latitude && latitude < 39 && -124 < longitude && longitude < -120)
			return 2;
		if (39 < latitude && latitude < 44 && -89  < longitude && longitude < -85)
			return 3;
		System.out.println("not match! " + latitude + " " + longitude);
		return 4;
	}
	
	static public double PredictTest(DataSet test, boolean calc) {
		double score = 0;
		int total = 0;
		
		Vector<DataPoint> data = test.data;
		int unmatched = 0;
		
		for (int i = 0; i < data.size(); i ++) {
			
			double view = 0;
			double vote = 0;
			double comment = 0;
			
			boolean matched = true;
			
			int source_ind = findSourceIndex(test.data.get(i).source);
			int tag_ind = findTagTypeIndex(test.data.get(i).tagtype);
			int mon_ind = Util.getMonth(test.data.get(i).ctime);
			int hr_ind = Util.getHour(test.data.get(i).ctime);
			int place_ind = findCityIndex(test.data.get(i).latitude, test.data.get(i).longitude);
			if (source_ind == -1 || tag_ind == -1) {
				view = 0; vote = 0; comment = 0; 
				matched = false;
			} else {
				if (stat_total[source_ind][tag_ind][hr_ind][place_ind] > 0) { 
					view = stat_mean[source_ind][tag_ind][hr_ind][place_ind];
					vote = vote_mean[source_ind][tag_ind][hr_ind][place_ind];
					comment = comment_mean[source_ind][tag_ind][hr_ind][place_ind];
					matched = true;
				}
				else {
					view = source_mean.get(source_ind) * 0.5 + tag_mean.get(tag_ind) * 0.2 + daytime_mean[hr_ind] * 0.3;
					vote = (int)(Math.log((int)view+1))+1;
					comment = 0;
					unmatched ++;
					matched = false;
				}
			}
			
			if (calc) {
				double delta1, delta2, delta3;
				delta1 = (Math.log((int)view + 1) - Math.log(data.get(i).views + 1))
						*(Math.log((int)view + 1) - Math.log(data.get(i).views + 1));
				delta2 = (Math.log((int)vote + 1) - Math.log(data.get(i).votes + 1))
						*(Math.log((int)vote + 1) - Math.log(data.get(i).votes + 1));
				delta3 = (Math.log((int)comment + 1) - Math.log(data.get(i).comments + 1))
						*(Math.log((int)comment + 1) - Math.log(data.get(i).comments + 1));
				score += delta1 + delta2 + delta3;
				total += 3;
			}
			data.get(i).views = (int)view;
			data.get(i).votes = (int)vote;
			data.get(i).comments = (int)comment;
		}
		
		System.out.println("Total unmatched = " + unmatched);
		if (calc) {
			score /= total;
			return score;
		} else return 0;
	}
	
	public static void main(String[] args) throws IOException {
		
		CSV.read(train, "train.csv", false);
		
		int sign = 2;
		
		if (sign == 1) {
			train.crossSplit(split_train, split_test);
			TypeAnalysis(split_train);
			Double score = PredictTest(split_test, true);
			System.out.println(score);
			
			CSV.write(split_train, "split_train.csv");
			CSV.write(split_test, "split_test.csv");
		} else {
			TypeAnalysis(train);
			CSV.read(test, "test.csv", true);
			PredictTest(test, false);
			CSV.writeAnswer(test, "submission.csv");
		}
		
		System.out.println("Done.");
	}

}
