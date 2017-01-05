
public class main {

	public static final int WIDTH = 3;
	public static final int HEIGHT = 3;
	public static final int A = 1000;
	public static final int E = 100;
	public static final int I = 10;
	public static final int O = 1;
	public static final int U = 0;
	public static final int X = -1000;
	public static final int NUM_MACHINES = 4;
	public static int [][] sizes;
	public static char[][] closeness;
	public static int[][] layout;
	public static int systemPoint;
	
	public static void main(String[] args) {
		sizes = new int[NUM_MACHINES][2];
		closeness = new char[NUM_MACHINES][NUM_MACHINES];
		
		//height of the first machine
		sizes[0][0]=1;
		//width of the first machine
		sizes[0][1]=3;
		
		sizes[1][0]=1;
		sizes[1][1]=2;
		
		sizes[2][0]=1;
		sizes[2][1]=2;
		
		sizes[3][0]=1;
		sizes[3][1]=1;
		
		closeness[0][0]=closeness[1][1]=closeness[2][2]=closeness[3][3]='-';
		closeness[0][1]=closeness[1][0]='I';
		closeness[0][2]=closeness[2][0]='O';
		closeness[0][3]=closeness[3][0]='U';
		closeness[1][2]=closeness[2][1]='E';
		closeness[1][3]=closeness[3][1]='I';
		closeness[2][3]=closeness[3][2]='A';
		
		int[] rating = new int[4];
		for(int i=0;i<NUM_MACHINES; i++){
			for(int j=0; j<NUM_MACHINES; j++){
			if(closeness[i][j]=='A'){
					rating[i]+=A;
				} else if(closeness[i][j]=='E'){
					rating[i]+=E;
				} else if(closeness[i][j]=='I'){
					rating[i]+=I;
				} else if(closeness[i][j]=='O'){
					rating[i]+=O;
				} else if(closeness[i][j]=='U'){
					rating[i]+=U;
				}  else if(closeness[i][j]=='X'){
					rating[i]+=X;
				} else {
					rating[i]+=0;
				}
			}
		}
		layout = new int[HEIGHT][WIDTH];
		lowestFirstLayout(rating);
	}
	
	public static int findClosest(int machineIndex){
		for(int i=0;i<NUM_MACHINES; i++){
			if(closeness[machineIndex][i]=='A'){
				return i;
			}
		}
		
		for(int i=0;i<NUM_MACHINES; i++){
			if(closeness[machineIndex][i]=='E'){
				return i;
			}
		}
		
		for(int i=0;i<NUM_MACHINES; i++){
			if(closeness[machineIndex][i]=='I'){
				return i;
			}
		}
		
		for(int i=0;i<NUM_MACHINES; i++){
			if(closeness[machineIndex][i]=='O'){
				return i;
			}
		}
		
		for(int i=0;i<NUM_MACHINES; i++){
			if(closeness[machineIndex][i]=='U'){
				return i;
			}
		}
		return -1;
	}
	
	public static void lowestFirstLayout(int[] rating){
		int x = 0;
		int y = 0;
		int xCounter = 0;
		int yCounter = 0;
		int current = findMin(rating);
		for(int i=0; i<NUM_MACHINES;i++){
			xCounter = x;
			yCounter = y;
			if(sizes[current][0]==1){
				for(int j=x; j<=sizes[current][0]; j++){
					layout[j][y]=current+1;
					xCounter++;
				}
			}else {
				for(int j=x; j<sizes[current][0]; j++){
					layout[j][y]=current+1;
					xCounter++;
				}
			}
			if(sizes[current][1]==1){
				for(int j=y; j<=sizes[current][1]; j++){
					layout[x][j]=current+1;
					yCounter++;
				}
			}else {
				for(int j=y; j<sizes[current][1]; j++){
					layout[x][j]=current+1;
					yCounter++;
				}
			}
		
			System.out.println("New closeness ratings:");
			printDoubleArray(closeness);
			System.out.println("New machine ratings:");
			rating[current]=Integer.MAX_VALUE;
			printArray(rating);
			int prev = current;
			current = findClosest(current);
			for(int j=0; j<closeness.length; j++){
				closeness[prev][j]='-';
				closeness[j][prev]='-';
			}
			System.out.println("Next Machine: "+ (current+1));
			if(current==-1){
				break;
			}
			if(yCounter+sizes[current][1]>layout[0].length){
				x++;
			} 
			else if(xCounter+sizes[current][0]>layout.length){
				y++;
			}
			else {
				x = xCounter;
				y = yCounter;
			}
			System.out.println("Current layout:");
			printDoubleArray(layout);
		}
	}
	
	public static int findMax(int[] arr){
		int max = Integer.MIN_VALUE;
		int maxIndex = -1;
		for(int i=0; i<arr.length; i++){
			if(arr[i]>max){
				max = arr[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public static int findMin(int[] arr){
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		for(int i=0; i<arr.length; i++){
			if(arr[i]<min){
				min = arr[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	public static void printArray(int[] arr){
		for(int i=0; i<arr.length; i++){
			System.out.print(arr[i]+" ");
		}
		System.out.println("");
	}
	
	public static void printArray(char[] arr){
		for(int i=0; i<arr.length; i++){
			System.out.print(arr[i]+" ");
		}
	}
	
	public static void printDoubleArray(int[][] arr){
		for(int i=0; i<arr.length; i++){
			for(int j=0; j<arr[i].length; j++){
				System.out.print(arr[i][j]+" ");
			}
			System.out.println("");
		}
	}
	
	public static void printDoubleArray(char[][] arr){
		for(int i=0; i<arr.length; i++){
			for(int j=0; j<arr[i].length; j++){
				System.out.print(arr[i][j]+" ");
			}
			System.out.println("");
		}
	}
	
	public static void machineArea(){
		int[] area = new int[NUM_MACHINES];
		for(int i=0; i<NUM_MACHINES; i++){
			area[i]=sizes[i][0]*sizes[i][1];
		}
		printArray(area);
	}
	
}
