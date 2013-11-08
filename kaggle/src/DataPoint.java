import java.util.*;

public class DataPoint {
	public Integer id;
	public Double latitude;
	public Double longitude;
	public String summary;
	public String descript;
	public String source;
	public String ctime;
	public String tagtype;
	public Integer votes = 0;
	public Integer comments = 0;
	public Integer views = 0;
	
	public static Comparator<DataPoint> cmpViews = new Comparator<DataPoint>() {
		public int compare(DataPoint arg0, DataPoint arg1) {
			return arg1.views - arg0.views;
		}
	};
	
	public static Comparator<DataPoint> cmpDescriptLength = new Comparator<DataPoint>() {
		public int compare(DataPoint arg0, DataPoint arg1) {
			return arg1.descript.length() - arg0.descript.length();
		}
	};
	
}
