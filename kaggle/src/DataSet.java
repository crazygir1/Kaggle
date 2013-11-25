import java.util.*;

public class DataSet {
	public Vector<DataPoint> data = new Vector<DataPoint>();

	void SortByViews() {
		Collections.sort(data, DataPoint.cmpViews);
	}
	
	void SortByDescriptLength() {
		Collections.sort(data, DataPoint.cmpDescriptLength);
	}
	
	public void GetTagTypes(Vector<String> types, Vector<Integer> total, Vector<Double> mean) {
		types.clear();
		total.clear();
		mean.clear();
		for (int i = 0; i < data.size(); i ++) {
			String tagtype = data.get(i).tagtype;
			boolean found = false;
			for (int j = 0; j < types.size(); j ++) {
				if (types.get(j).equals(tagtype)) {
					found = true;
					total.set(j, total.get(j) + 1);
					mean.set(j, mean.get(j) + data.get(i).views);
					break;
				}
			}
			if (!found) {
				System.out.println(tagtype);
				types.add(tagtype);
				total.add(1);
				mean.add(new Double(data.get(i).views));
			}
		}
		for (int i = 0; i < types.size(); i ++) {
			mean.set(i, mean.get(i) / total.get(i));
		}
	}
	
	public void GetSourceTypes(Vector<String> types, Vector<Integer> total, Vector<Double> mean) {
		types.clear();
		total.clear();
		mean.clear();
		for (int i = 0; i < data.size(); i ++) {
			String tagtype = data.get(i).source;
			boolean found = false;
			for (int j = 0; j < types.size(); j ++) {
				if (types.get(j).equals(tagtype)) {
					found = true;
					total.set(j, total.get(j) + 1);
					mean.set(j, mean.get(j) + data.get(i).views);
					break;
				}
			}
			if (!found) {
				System.out.println(tagtype);
				types.add(tagtype);
				total.add(1);
				mean.add(new Double(data.get(i).views));
			}
		}
		for (int i = 0; i < types.size(); i ++) {
			mean.set(i, mean.get(i) / total.get(i));
		}
	}
	
	public void GetDayTime(int[] total, double[] mean) {
		for (int i = 0; i < 24; i ++) {
			total[i] = 0; mean[i] = 0;
		}
		for (int i = 0; i < data.size(); i ++) {
			int hr = Util.getHour(data.get(i).ctime);
			total[hr] ++;
			mean[hr] += data.get(i).views;
		}
		for (int i = 0; i < 24; i ++)
			if (total[i] > 0)
				mean[i] /= total[i];
	}
	
	public void GetMonthTime(int[] total, double[] mean) {
		for (int i = 0; i < 30; i ++) {
			total[i] = 0; mean[i] = 0;
		}
		for (int i = 0; i < data.size(); i ++) {
			int mon = Util.getMonth(data.get(i).ctime);
			total[mon] ++;
			mean[mon] += data.get(i).views;
		}
		for (int i = 0; i < 30; i ++)
			if (total[i] > 0)
				mean[i] /= total[i];
	}
	
	public void crossSplit(DataSet s1, DataSet s2) {
		s1.data.clear();
		s2.data.clear();
		for (int i = 0; i < data.size(); i ++) {
			if (i % 2 == 0) {
				s1.data.add(data.get(i));
			} else {
				s2.data.add(data.get(i));
			}
		}
	}
}
