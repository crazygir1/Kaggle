
public class Util {
	static public int getHour(String str) {
		String[] list = str.split(" ");
		String[] list2 = list[1].split(":");
		return Integer.parseInt(list2[0]);
	}
}
