import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ShortestPathGrid {
	/*****
	 * Author:	Chad Fuller
	 * Class:	CS404
	 * Date:	04/18/2016
	 * Desc:	This program is used to calculate the first and second
	 * 			shortest path in a grid with weights in each grid section.
	 * 			Due to the possible large input for this program the
	 * 			only one array of m x n of Integers and one array of 
	 * 			m x n boolean will be used.  
	 */
	
	private static boolean right = true; 
	private static boolean down = false; 
	
	private static int counter;
	
	public static void main(String[] args) {		
		
		//Variable initialization.
		Integer[][] myMap = new Integer[0][0];
		boolean[][] myPath = new boolean[0][0];


		Integer x = 0, y = 0,
				xMax = 0, yMax = 0,
				value = 0;	
		
		//Sample files for scanning
		//String fileName = "data/test1.txt";
		String fileName = "data/test2.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput1.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput10.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput11.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput13.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput14.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput15.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput16.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput17.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput18.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput19.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput2.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput20.txt";
		//String fileName = "data/CS404SP16RewardMatrixInput21.txt";
		String line = null;
        String[] lineVector;
	
        //Read in .txt file to populate grid array
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);
                
            Scanner dis=new Scanner(fileReader);            
            line = dis.nextLine();            
            lineVector = line.split(", ");
            
            //Collects x and y grid size
            yMax=Integer.parseInt(lineVector[0]);
            xMax=Integer.parseInt(lineVector[1]);
            
            //Prints size of grid.
            System.out.println("Grid: " + xMax + " " + yMax );
            
            //Reallocates the arrays to meet with the newly collected max size
            myMap = new Integer[yMax][xMax];
            myPath = new boolean[yMax][xMax];
            
            //initialize map arrays
            for(int i=0;i<yMax;i++){
            	for(int j=0;j<xMax;j++){
            		myMap[i][j] = 1;
            	}
            }

            //Scans coordinates to put into array
            while(dis.hasNextLine()){
            	line = dis.nextLine();
            	lineVector = line.split(", ");
            	y = Integer.parseInt(lineVector[0]);
            	x = Integer.parseInt(lineVector[1]);
            	value = Integer.parseInt(lineVector[2]);
            	myMap[y-1][x-1]=value;
            }
            dis.close();                     
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        } 
        
        counter = 0;
        
        //Starts system clock to record time in nanoseconds
        long startTime = System.nanoTime();
        //Adds the weights of the top of the grid
        for (int j=1; j < xMax; j++){
        	counter++;
        	myMap[0][j] += myMap[0][j-1];
        	myPath[0][j] = right;
        }
        for(int i=1; i < yMax; i++){
        	counter++;
        	myMap[i][0] += myMap[i-1][0]; 
        	myPath[i][0] = down;  
        	//Makes comparisons to which path has the least weight
        	for(int j=1; j < xMax; j++){
        		//Checks which path has the least weight and 
        		//assigns it to the map array
        		counter ++;
        		if(myMap[i-1][j] > myMap[i][j-1]){
        			myMap[i][j] += myMap[i][j-1];
        			myPath[i][j] = right;
        		} else {
        			myMap[i][j] += myMap[i-1][j];
        			myPath[i][j] = down;
        		}
        	}
        }
        
        //Prints results on last position of the array
        printArray(xMax, yMax, myMap, myPath);
        
        //System.out.println("Rejected: " + rejected);
        printPath(xMax, yMax, myMap, myPath);
        System.out.println("Iterations: " + counter);
        long endTime = System.nanoTime();
        System.out.println("Time: " + (endTime-startTime) + "ns");
            
    }
	
	//Method goes back through the adjusted map to gather the min and min min path
	//This Method takes the path grid and follows it from [m, n] to [1,1] as it is 
	//storing the shortest path it is generating the second shortest path. For each 
	//step to calculate the minimum path the other path is checked against the previous 
	//paths not chosen to see how much of a gap is between them and storing the least gap.
	private static void printPath(int xMax, int yMax, Integer[][] map, boolean[][] path){
		int xMin = xMax-1;
		int xMinMin = 0;
		int yMin = yMax-1;
		int yMinMin = 0;
		String minPhrase ="";
		String minMinPhrase ="";
		int minBalance = map[yMin][xMin];
		
		while ((xMin != 0) || (yMin != 0)){	
			counter++;
			if(path[yMin][xMin] == down){
				if(xMin != 0){
					if(minBalance > (map[yMin][xMin-1]-map[yMin-1][xMin])){
						minBalance = map[yMin][xMin-1]-map[yMin-1][xMin];
						xMinMin = xMin-1;
						yMinMin = yMin;
						minMinPhrase = "0" + minPhrase;
					}else{
						if(path[yMinMin][xMinMin] == down){
							minMinPhrase = "1" + minMinPhrase;
							yMinMin--;
						}else{
							minMinPhrase = "0" + minMinPhrase;
							xMinMin--;
						}
					}
				}else{
					if(path[yMinMin][xMinMin] == down){
						minMinPhrase = "1" + minMinPhrase;
						yMinMin--;
					}else{
						minMinPhrase = "0" + minMinPhrase;
						xMinMin--;
					}
				}
				minPhrase = "1"+ minPhrase;
				yMin--;
			} else {
				if(yMin != 0){
					if((minBalance > (map[yMin-1][xMin]-map[yMin][xMin-1]))){
						minBalance = map[yMin-1][xMin]-map[yMin][xMin-1];
						xMinMin = xMin;
						yMinMin = yMin-1;
						minMinPhrase = "1" + minPhrase;	
					}else{
						if(path[yMinMin][xMinMin] == down){
							minMinPhrase = "1" + minMinPhrase;
							yMinMin--;
						}else{
							minMinPhrase = "0" + minMinPhrase;
							xMinMin--;
						}
					}
				} else {
					if(path[yMinMin][xMinMin] == down){
						minMinPhrase = "1" + minMinPhrase;
						yMinMin--;
					}else{
						minMinPhrase = "0" + minMinPhrase;
						xMinMin--;
					}
				}
				minPhrase = "0"+ minPhrase;
				xMin--;
			}
		}
		System.out.println("First Minimal Path: " + minPhrase); 
		System.out.println("Second Minimal Path: " + minMinPhrase);
	}
	
	//Method prints arrays for testing purposes
	private static void printArray(int xMax, int yMax, Integer[][] myMap, boolean[][] myPath){
		System.out.println("Result my map");
        for(int i = 0; i<yMax;i++){        	
        	for(int j = 0;j<xMax;j++){
        		System.out.print( myMap[i][j] + " " );
        	}
        	System.out.println();
        }
        System.out.println();
        
        System.out.println("Result Path");
        for(int i = 0; i<yMax;i++){        	
        	for(int j = 0;j<xMax;j++){
        		if (myPath[i][j] == down)
        			System.out.print( "1 " );
        		else
        			System.out.print("0 ");
        	}
        	System.out.println();
        }
        System.out.println();
        
	}
}