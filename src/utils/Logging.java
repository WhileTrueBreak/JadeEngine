package utils;

import java.util.ArrayList;

public class Logging {
	
	public static boolean DEBUG = true;
	
	private static ArrayList<String> logs = new ArrayList<String>();
	
	public static void addLog(String log) {
		logs.add(log);
	}
	
	public static void dumpLog() {
		System.out.println("[Dump]\t\t---Start Dump---");
		for(String log:logs) {
			System.out.println("[Dump]\t\t\t"+log);
		}
		System.out.println("[Dump]\t\t----End Dump----");
		clearLog();
	}
	
	public static void clearLog() {
		logs = new ArrayList<String>();
	}
	
	public static void debugPrint(String str) {
		if(DEBUG) {
			System.out.println("|\t"+str);
		}
	}
	
	public static void printStack() {
		System.out.println("Stack trace:");
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (int i = 2; i < elements.length; i++) {
		     StackTraceElement s = elements[i];
		     System.out.println("\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
		}
	}
}
