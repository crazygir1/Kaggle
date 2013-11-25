
public class Util {
	static public int getHour(String str) {
		String[] list = str.split(" ");
		String[] list2 = list[1].split(":");
		return Integer.parseInt(list2[0]);
	}
	static public int getMonth(String str) {
		String[] list = str.split(" ");
		String[] list2 = list[0].split("-");
		int year = Integer.parseInt(list2[0]);
		int month = Integer.parseInt(list2[1]);
		return month - 1;
	}
}
