import java.io.*;
import java.util.*;

public class CSV {
	static String refineStr(String str) {
		str = str.replace('\"', ' ');
		str = str.trim();
		return str;
	}
	
	static public void read(DataSet dataSet, String fileName, boolean test) throws IOException {
		Scanner scan = new Scanner(new FileReader(fileName));
		
		String buffer;
		String[] bufferList;
		buffer = scan.nextLine();
		bufferList = buffer.split(",");
		
		while (scan.hasNextLine()) {
			buffer = scan.nextLine();
			bufferList = buffer.split("\",\"");
			if (!test) {
				if (bufferList.length > 11) {
					System.out.println(buffer);
					continue;
				}
			} else {
				if (bufferList.length > 8) {
					System.out.println(buffer);
					continue;
				}
			}
			
			for (int i = 0; i < bufferList.length; i ++) {
				bufferList[i] = refineStr(bufferList[i]);
			//	System.out.print(bufferList[i] + " ");
			}
			//System.out.println();
			
			DataPoint point = new DataPoint();
			if (!test) {
				point.id = Integer.parseInt(bufferList[0]);
				point.latitude = Double.parseDouble(bufferList[1]);
				point.longitude = Double.parseDouble(bufferList[2]);
				point.summary = bufferList[3];
				point.descript = bufferList[4];
				
				point.votes = Integer.parseInt(bufferList[5]);
				point.comments = Integer.parseInt(bufferList[6]);
				point.views = Integer.parseInt(bufferList[7]);
				
				point.source = bufferList[8];
				point.ctime = bufferList[9];
				point.tagtype = bufferList[10];
			} else {
				point.id = Integer.parseInt(bufferList[0]);
				point.latitude = Double.parseDouble(bufferList[1]);
				point.longitude = Double.parseDouble(bufferList[2]);
				point.summary = bufferList[3];
				point.descript = bufferList[4];
				
				/*
				point.votes = Integer.parseInt(bufferList[5]);
				point.comments = Integer.parseInt(bufferList[6]);
				point.views = Integer.parseInt(bufferList[7]);
				*/
				point.source = bufferList[5];
				point.ctime = bufferList[6];
				point.tagtype = bufferList[7];	
			}
			dataSet.data.add(point);
		}
		
		scan.close();
	}
	
	static public void write(DataSet dataSet, String fileName) throws IOException {
		PrintStream ps = new PrintStream(fileName);
		for (int i = 0; i < dataSet.data.size(); i ++) {
			DataPoint dp = dataSet.data.get(i);
			ps.println(
					dp.id + "\t\t\t" +
					dp.votes + "\t\t\t" + 
					dp.comments + "\t\t\t" + 
					dp.views + "\t\t\t" + 
					dp.tagtype + "\t\t\t\t" +
					dp.ctime + "\t\t\t" +
					dp.source + "\t\t\t" +
					dp.latitude + "\t\t\t" +
					dp.longitude + "\t\t\t" +
					dp.summary + "\t\t\t" +
					dp.descript.length());
		}
		ps.close();
	}
	
	static public void writeAnswer(DataSet dataSet, String fileName) throws IOException {
		PrintStream ps = new PrintStream(fileName);
		ps.println("id,num_views,num_votes,num_comments");
		for (int i = 0; i < dataSet.data.size(); i ++) {
			DataPoint dp = dataSet.data.get(i);
			ps.println(dp.id + "," + dp.views + "," + dp.votes + "," + dp.comments);
		}
		ps.close();
	}
}
