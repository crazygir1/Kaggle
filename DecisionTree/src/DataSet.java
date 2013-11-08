import java.io.*;
import java.util.*;

public class DataSet {
	
	String relation;
	Vector<String> attrName = new Vector<String>();
	Vector<String> attrType = new Vector<String>();
	Vector<Vector<String>> attrValues = new Vector<Vector<String>>();
	
	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	
	public DataSet() { }
	
	public void readin(String fileName, boolean testFlag) throws IOException {
		File file = new File(fileName);
		Scanner scanner = new Scanner(file);
		String temp;
		String[] tmpArr;
		
		//Read relation name
		temp = scanner.nextLine();
		tmpArr = temp.split(" ");
		relation = tmpArr[1];
		//Read attribute name and candidate values
		while (scanner.hasNextLine()) {
			temp = scanner.nextLine();
			tmpArr = temp.split(" ");
			if (tmpArr[0].equals("@data")) break;
			
			attrName.add(tmpArr[1].substring(1, tmpArr[1].length() - 1));
			if (tmpArr[2].startsWith("{")) {
				attrType.add("enumerate");
				Vector<String> values = new Vector<String>();
				for (int i = 2; i < tmpArr.length; i ++) {
					String word = tmpArr[i];
					word = word.replaceAll("[{}]","");
					word = word.replaceAll(",","");
					if (!word.equals(""))
						values.add(word);
				}
				attrValues.add(values);
			} else {
				attrType.add("double");
				attrValues.add(null);
			}
		}
		
		//Read data
		while (scanner.hasNextLine()) {
			temp = scanner.nextLine();
			tmpArr = temp.split(",");
			if (tmpArr.length != attrType.size()) {
				System.out.println("Attribute number doesn't match!");
				return;
			}
			
			Vector<Object> singleData = new Vector<Object>();
			for (int i = 0; i < attrType.size(); i ++) {
				if (attrType.get(i).equals("double")) {
					Double v = Double.parseDouble(tmpArr[i]);
					singleData.add(v);
				} else {
					singleData.add(tmpArr[i]);
				}
			}
			data.add(singleData);
		}
		
		scanner.close();
	}
	
	public void output(String fileName) {
		System.out.println(relation);
		System.out.println();
		for (int i = 0; i < attrName.size(); i ++) {
			System.out.print(attrName.get(i) + " ");
			if (attrType.get(i).equals("double"))
				System.out.println("double");
			else {
				System.out.print("enumerate < ");
				for (int j = 0; j < attrValues.get(i).size(); j ++) {
					System.out.print(attrValues.get(i).get(j) + " ");
				}
				System.out.println(">");
			}
		}
		
		for (int i = 0; i < data.size(); i ++) {
			for (int j = 0; j < attrType.size(); j ++) {
				if (attrType.get(j).equals("double")) {
					System.out.print((Double)(data.get(i).get(j)) + " ");
				} else {
					System.out.print((String)(data.get(i).get(j)) + " ");
				}
			}
			System.out.println();
		}
	}
}
