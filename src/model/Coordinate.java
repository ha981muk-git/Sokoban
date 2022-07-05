package model;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Coordinate {

	// 2a;
	private  int x_Koordinate;
	private  int y_Koordinate;
	
	public Coordinate(int x, int y) {
		this.x_Koordinate = x;
		this.y_Koordinate = y;
		
	}
	
	
	// 2b;
	
	public void setX(int x) { this.x_Koordinate = x; }
	
	public int  getX(){  return this.x_Koordinate;  }
	
	
	
	public void setY(int y) {this.y_Koordinate = y; }
	
	public int  getY(){ return this.y_Koordinate;  }
	
	
	
	public void setPosition(int x, int y) {
		this.x_Koordinate = x;
		this.y_Koordinate = y;	
	}
	
	
	// 2c
	public boolean equals(Coordinate obj) {
		
		//2d
		if (!(obj instanceof Coordinate) || obj == null) {
			return false;
		}
		
		if( obj != null && this.x_Koordinate == obj.x_Koordinate && this.y_Koordinate == obj.y_Koordinate) {
			return true;
		}
		
		return false;
		
	}
	
	// 2e
	public String toString() {
		
		String returnString = "["+this.x_Koordinate+","+this.y_Koordinate+"]";
		return returnString;
		
	}


	/*
	* package read2darray;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author DevMecha
 */
	/*
	public class Read2DArray {

		private static final int ROWS = 10;
		private static final int COLUMNS = 10;

		public static void main(String[] args) throws FileNotFoundException {
			readFile();
		}

		private static void readFile() throws FileNotFoundException {
			int[][] numArray = new int[ROWS][COLUMNS];
			Scanner sc = new Scanner(choseTextFile());
			while (sc.hasNextLine()) {
				for (int i = 0; i < numArray.length; i++) {
					String[] line = sc.nextLine().trim().split("," + " ");
					for (int j = 0; j < line.length; j++) {
						numArray[i][j] = Integer.parseInt(line[j]);
					}
				}
			}
			System.out.println(Arrays.deepToString(numArray));
		}

		private static File choseTextFile() {
			FileDialog dialog = new FileDialog((Frame) null, "Select File To Open");
			dialog.setMode(FileDialog.LOAD);
			dialog.setVisible(true);
			File[] file = dialog.getFiles();
			return file[0];
		}

	}
	*/
 /*
	private static int lengthOfRow(){

	}

	private static int lengthOfColumn(){
	 ret
	}

	private static int lengthOfRow(){

	}

	private static int lengthOfColumn(){
	 ret
	}

  */
	
	/*
	private static int lengthOfRow(){
		int max = 0,row;
		Scanner sc = new Scanner(choseTextFile());

		for (int i = 0; i < c  .length; i++) {
			for (int j = 0; j < charArray.length; j++) {
				if (sc[j] == "\n") {
					row = j;
				}
				if (max < row) {
					max = row;
				}
				++i;
			}
		}
		return row;

	}
	private static int lengthOfColumn() throws FileNotFoundException{
		int column = 0;
		Scanner sc = new Scanner(choseTextFile());
		while (sc.hasNextLine()) {
			column +=1;
		}
		return column;
	}

	private static int row_length = lengthOfRow();
	private static int col_length = lengthOfColumn();



	public static void main(String[] args) throws FileNotFoundException {
		readFile();
	}

	private static void readFile() throws FileNotFoundException {
		char [][] charArray = new char[row_length][col_length];
		Scanner sc = new Scanner(choseTextFile());
		while (sc.hasNextLine()) {
			for (int i = 0; i < charArray.length; i++) {
				//String[] line = sc;
				for (int j = 0; j < charArray.length; j++) {
					charArray[i][j] = sc[j];
				}
			}
		}
		System.out.println(Arrays.deepToString(charArray));
	}
	private static File choseTextFile() {
		FileDialog dialog = new FileDialog((Frame) null, "Select File To Open");
		dialog.setMode(FileDialog.LOAD);
		dialog.setVisible(true);
		File[] file = dialog.getFiles();
		return file[0];
	}
	*/
	
	
	

}
