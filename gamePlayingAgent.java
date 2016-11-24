import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class gamePlayingAgent {
	
	public static void printMatrix(char [][]state)
	{
		for(int i=0;i<state.length;i++)
		{
			for(int j=0;j<state.length;j++)
			{
				System.out.print(state[i][j]);
			}
			System.out.println();
		}
		
		
	}
	
	public static char[][] changeBoardState(String nextMove,char[][] boardState,String yourPlayer,int level,boolean isRaid) 
	{
		
		char rc[] = nextMove.toCharArray();
		int row = rc[1]-49;
		int col = rc[0]-65;
		char yp = ' ';
		char opp = ' ';
		int N = boardState.length;
		
		
		char [][] state = new char[N][N];
		for(int i=0;i<N;i++)
		{
			state[i] = Arrays.copyOf(boardState[i], boardState[i].length);
		}
		
		if(yourPlayer.charAt(0) == 'X')
		{
			yp = 'X';
			opp = 'O';
		}
		else
		{
			yp = 'O';
			opp = 'X';
		}
		
		/* Do we need this after X and O are inyterchanged each time ??
		if(level%2 == 1)
			boardState[row][col] = yp;
		else
			boardState[row][col] = opp;
		*/
		
		
		
		if(!isRaid)
		{
			state[row][col] = yp;
			return state;
		
		}
		else
		{
			state[row][col] = yp;
			
			//Change board state for raid moves.
			
			/*	boardState[row][col]
			neighbours  :
				boardState[row][col-1]
				boardState[row][col+1]
				boardState[row+1][col]
				boardState[row-1][col]
			*/
			
			if(row-1>=0 && (state[row-1][col] == yp))
		 
			{
				if(col-1 >=0 && (state[row][col-1] == opp))
				{
					state[row][col-1] = yp;
				}
				if(col+1 <=N-1 && (state[row][col+1] == opp))
				{
					state[row][col+1] = yp;
				}
				if(row+1 <=N-1 && (state[row+1][col] == opp))
				{
					state[row+1][col] = yp;
				}	
			}
		
			if(row+1<=N-1 && (state[row+1][col] == yp))
			{
				if(col-1 >=0 && (state[row][col-1] == opp))
				{
					state[row][col-1] = yp;
				}
				if(col+1 <=N-1 && (state[row][col+1] == opp))
				{
					state[row][col+1] = yp;
				}
				if(row-1 >=0 && (state[row-1][col] == opp))
				{
					state[row-1][col] = yp;
				}
			
			}
			if(col-1>=0 && (state[row][col-1] == yp))
			{
				if(row+1 <=N-1 && (state[row+1][col] == opp))
				{
					state[row+1][col] = yp;
				}	
				if(row-1 >=0 && (state[row-1][col] == opp))
				{
					state[row-1][col] = yp;
				}
				if(col+1 <=N-1 && (state[row][col+1] == opp))
				{
					state[row][col+1] = yp;
				}
			
			}
			if(col+1<=N-1 && (state[row][col+1] == yp))
			{
				if(row+1 <=N-1 && (state[row+1][col] == opp))
				{
					state[row+1][col] = yp;
				}	
				if(row-1 >=0 && (state[row-1][col] == opp))
				{
					state[row-1][col] = yp;
				}
				if(col-1 >=0 && (state[row][col-1] == opp))
				{
					state[row][col-1] = yp;
				}
			
			}
		
			return state;
		}
	}
	
	public static int eval(String originalPlayer,int level,char boardState[][],int gameValues[][])
	{
		int Xsum = 0;
		int Osum = 0;
		int N = boardState.length;
		
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(boardState[i][j] == 'X')
					Xsum = Xsum + gameValues[i][j];
				else if(boardState[i][j] == 'O')
					Osum = Osum + gameValues[i][j];
			}
		}
		
		if(originalPlayer.charAt(0) == 'O')
		{	//yourOriginal player - Opponent original player
			return Osum - Xsum;
		}
		else
		{
			return Xsum - Osum;
		}
	}
	
	
	public static boolean isRaid(char boardState[][],int row,int col,char yp, char opp)
	{
		int N = boardState.length;
		boolean isRaid =  false;
		
		if(row-1>=0 && (boardState[row-1][col] == yp))
		{
			if(col-1 >=0 && (boardState[row][col-1] == opp))
			{
				isRaid = true;
			}
			if(col+1 <=N-1 && (boardState[row][col+1] == opp))
			{
				isRaid = true;
			}
			if(row+1 <=N-1 && (boardState[row+1][col] == opp))
			{
				isRaid = true;
			}	
		}
		
		if(row+1<=N-1 && (boardState[row+1][col] == yp))
		{
			if(col-1 >=0 && (boardState[row][col-1] == opp))
			{
				isRaid = true;
			}
			if(col+1 <=N-1 && (boardState[row][col+1] == opp))
			{
				isRaid = true;
			}
			if(row-1 >=0 && (boardState[row-1][col] == opp))
			{
				isRaid = true;
			}
			
		}
		if(col-1>=0 && (boardState[row][col-1] == yp))
		{
			if(row+1 <=N-1 && (boardState[row+1][col] == opp))
			{
				isRaid = true;
			}	
			if(row-1 >=0 && (boardState[row-1][col] == opp))
			{
				isRaid = true;
			}
			if(col+1 <=N-1 && (boardState[row][col+1] == opp))
			{
				isRaid = true;
			}
			
		}
		if(col+1<=N-1 && (boardState[row][col+1] == yp))
		{
			if(row+1 <=N-1 && (boardState[row+1][col] == opp))
			{
				isRaid = true;
			}	
			if(row-1 >=0 && (boardState[row-1][col] == opp))
			{
				isRaid = true;
			}
			if(col-1 >=0 && (boardState[row][col-1] == opp))
			{
				isRaid = true;
			}
			
		}
		
		return isRaid;
		
	}
	
	public static ArrayList<String> generateMoves(char boardState[][])
	{
		int N = boardState.length;
		ArrayList<String> moves = new ArrayList<>();
		
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(boardState[i][j] == '.')
				{	
					int row = i+1;
					char col = (char)(65 + j); 
					
					String move = Character.toString(col) + row;
					moves.add(move);
				}
			}
		}
		
		return moves;
	}
	
	public static ArrayList<ArrayList<String>> generateMovesInOrder(char boardState[][], char yp, char opp)
	{
		int N = boardState.length;
		boolean isRaidAtposition = false;
		
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> moves = new ArrayList<>();
		ArrayList<String> raidMoves = new ArrayList<>();
		
		
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(boardState[i][j] == '.')
				{	
					isRaidAtposition = isRaid(boardState, i, j, yp, opp);
		
					int row = i+1;
					char col = (char)(65 + j); 
					String move = Character.toString(col) + row;
					
					//Default stake move.
					moves.add(move);
					
					//If raid possible with conquor, then put in list.
					if(isRaidAtposition)
					{
						raidMoves.add(move);
					}
				}
			}
		}
		
		list.add(moves);
		list.add(raidMoves);
		
		
		return list;
	}
	
	public static int minimax(String originalPlayer,String yourPlayer, int depth, int gameValues[][], char boardState[][],int level)
	{
		
		//ArrayList<String> nextMoves = generateMoves(boardState);
		char yp  =' ';
		char opp =' ';
		
		if(yourPlayer.charAt(0) == 'X')
		{
			yourPlayer = "O";
		}
		else
		{
			yourPlayer = "X";
		}
		
		if(yourPlayer.charAt(0) == 'X')
		{
			yp = 'X';
			opp = 'O';
		}
		else
		{
			yp = 'O';
			opp = 'X';
		}
		
		ArrayList<ArrayList<String>> nextMoves = generateMovesInOrder(boardState,yp,opp);
		
		int bestScore = (level%2 == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore;
		
		if ((nextMoves.get(0).isEmpty() && nextMoves.get(0).isEmpty())|| depth == level) {
	         //Gameover or depth reached, evaluate score
	         bestScore = eval(originalPlayer,level,boardState,gameValues);
	         
	         //System.out.println("Depth == Level And BestScore is : " +bestScore );
	         return bestScore;
	      }
		else
		{
			ArrayList<String> stakeList = new ArrayList<>();
			stakeList = nextMoves.get(0);
			
			ArrayList<String> raidList = new ArrayList<>();
			raidList = nextMoves.get(1);
			
			for(int i=0;i<stakeList.size();i++)
			{
				char [][] tmpboardSt = {};				
				tmpboardSt = changeBoardState(stakeList.get(i), boardState, yourPlayer,level+1,false);
				
				//printMatrix(tmpboardSt);
				//System.out.println();
				currentScore = minimax(originalPlayer,yourPlayer,depth,gameValues,tmpboardSt,level+1);
				
				if (level%2 ==0) //put proper condition here
				{  
		               if (currentScore > bestScore) {
		                  bestScore = currentScore;		       
		               }
		        }
				else 
		        {  //opp is minimizing player
		               
		               if (currentScore < bestScore) {
		                  bestScore = currentScore;	                  
		               }
		        }
				
			}
			
			for(int i=0;i<raidList.size();i++)
			{
				char [][] tmpboardSt = {};				
				tmpboardSt = changeBoardState(raidList.get(i), boardState, yourPlayer,level+1,true);
				
				//printMatrix(tmpboardSt);
				//System.out.println();
				currentScore = minimax(originalPlayer,yourPlayer,depth,gameValues,tmpboardSt,level+1);
				
				if (level%2 ==0) //put proper condition here
				{  
		               if (currentScore > bestScore) {
		                  bestScore = currentScore;		       
		               }
		        }
				else 
		        {  //opp is minimizing player
		               
		               if (currentScore < bestScore) {
		                  bestScore = currentScore;	                  
		               }
		        }
				
			}
		}
	
		return bestScore;
	}
	
	
	public static int alphabeta(String originalPlayer,String yourPlayer, int depth, int gameValues[][], char boardState[][],int level,int alpha,int beta)
	{
		
		//ArrayList<String> nextMoves = generateMoves(boardState);
		char yp  =' ';
		char opp =' ';
		
		if(yourPlayer.charAt(0) == 'X')
		{
			yourPlayer = "O";
		}
		else
		{
			yourPlayer = "X";
		}
		
		if(yourPlayer.charAt(0) == 'X')
		{
			yp = 'X';
			opp = 'O';
		}
		else
		{
			yp = 'O';
			opp = 'X';
		}
		
		ArrayList<ArrayList<String>> nextMoves = generateMovesInOrder(boardState,yp,opp);
		
		//int bestScore = (level%2 == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore;
		
		if ((nextMoves.get(0).isEmpty() && nextMoves.get(0).isEmpty())|| depth == level) {
	         //Gameover or depth reached, evaluate score
			currentScore = eval(originalPlayer,level,boardState,gameValues);
	         
	        // System.out.println("Depth == Level And BestScore is : " +currentScore );
	         return currentScore;
	      }
		else
		{
			ArrayList<String> stakeList = new ArrayList<>();
			stakeList = nextMoves.get(0);
			
			ArrayList<String> raidList = new ArrayList<>();
			raidList = nextMoves.get(1);
			
			for(int i=0;i<stakeList.size();i++)
			{
				char [][] tmpboardSt = {};				
				tmpboardSt = changeBoardState(stakeList.get(i), boardState, yourPlayer,level+1,false);
				
				//printMatrix(tmpboardSt);
				//System.out.println();
				currentScore = alphabeta(originalPlayer,yourPlayer,depth,gameValues,tmpboardSt,level+1,alpha,beta);
				
				if (level%2 ==0) //put proper condition here
				{  
		               if (currentScore > alpha) {
		                  alpha = currentScore;		       
		               }
		        }
				else 
		        {  //opp is minimizing player
		               
		               if (currentScore < beta) {
		                  beta = currentScore;	                  
		               }
		        }
				
				if(alpha>=beta)
					break;
				
			}
			
			for(int i=0;i<raidList.size();i++)
			{
				char [][] tmpboardSt = {};				
				tmpboardSt = changeBoardState(raidList.get(i), boardState, yourPlayer,level+1,true);
				
				//printMatrix(tmpboardSt);
				//System.out.println();
				currentScore = alphabeta(originalPlayer,yourPlayer,depth,gameValues,tmpboardSt,level+1,alpha,beta);
				
				if (level%2 ==0) //put proper condition here
				{  
		               if (currentScore > alpha) {
		                  alpha = currentScore;		       
		               }
		        }
				else 
		        {  //opp is minimizing player
		               
		               if (currentScore < beta) {
		                  beta = currentScore;	                  
		               }
		        }
				
				if(alpha>=beta)
					break;
				
			}
			
		}
		if (level%2 ==0)
			return alpha;
		else
			return beta;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String fileName = "/Users/prathameshnaik/Documents/Testcases/AI_HW2/input.txt";
		String output = "output.txt";
		FileWriter writer = new FileWriter(output);
		
		String boardSize = null;
		String mode = null;
		String yourPlayer = null;
		String depth = null;
		int gameValues[][] = {};
		char boardState[][] = {};
		int N= 0;
		
		
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			boardSize = bufferedReader.readLine();
			mode = bufferedReader.readLine();
			yourPlayer = bufferedReader.readLine();
			depth = bufferedReader.readLine();
			
			N = Integer.parseInt(boardSize);
			
			gameValues = new int[N][N];
			boardState = new char[N][N];
			
			
			for(int i=0;i<N;i++)
			{
				String temp = bufferedReader.readLine();
				String[] tokens = temp.split("\\s+");	
				
				for(int j =0; j<N; j++)
				{
					gameValues[i][j] = Integer.parseInt(tokens[j]);
				}
			}
			
			for(int i=0;i<N;i++)
			{
				String temp = bufferedReader.readLine();
				//String[] tokens = temp.split("\\s+");
				char arr[] = temp.toCharArray();			
				for(int j =0; j<N; j++)
				{
					boardState[i][j] = arr[j];
				}
			}
			
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}

		
		int d = Integer.parseInt(depth);
		
		int bestScore = Integer.MIN_VALUE;
		int currentScore;
		int bestRow = 0;
		int bestCol = 0;
		char bC=0;
		
		char yp  =' ';
		char opp =' ';
		
		if(yourPlayer.charAt(0) == 'X')
		{
			yp = 'X';
			opp = 'O';
		}
		else
		{
			yp = 'O';
			opp = 'X';
		}
		
		boolean isRaid = false;
		
		ArrayList<ArrayList<String>> nextMoves = generateMovesInOrder(boardState,yp,opp);
		
		String originalPlayer = yourPlayer;
		
		if(mode.equals("MINIMAX"))
		{
			System.out.println("Entered MINMAX");
			
			ArrayList<String> stakeList = new ArrayList<>();
			stakeList = nextMoves.get(0);
			
			ArrayList<String> raidList = new ArrayList<>();
			raidList = nextMoves.get(1);
			
			for(int i=0;i<stakeList.size();i++)
			{
				char [][] tmpboardSt = {};
			
				tmpboardSt = changeBoardState(stakeList.get(i), boardState, yourPlayer,1,false);
						
				currentScore = minimax(originalPlayer,yourPlayer,d,gameValues,tmpboardSt,1);
				
				if (currentScore > bestScore) {
               
					bestScore = currentScore;                
					//Store best row and best column.
            
					String move = stakeList.get(i);
					char rc[] = move.toCharArray();
					bestRow = rc[1]-49;          
					bestCol = rc[0]-65;
					bC = rc[0];
					isRaid = false;
				}
			}
			
			for(int i=0;i<raidList.size();i++)
			{
				char [][] tmpboardSt = {};
			
				tmpboardSt = changeBoardState(raidList.get(i), boardState, yourPlayer,1,true);
						
				currentScore = minimax(originalPlayer,yourPlayer,d,gameValues,tmpboardSt,1);
				
				if (currentScore > bestScore) {
               
					bestScore = currentScore;                
					//Store best row and best column.
            
					String move = raidList.get(i);
					char rc[] = move.toCharArray();
					bestRow = rc[1]-49;          
					bestCol = rc[0]-65;
					bC = rc[0];
					isRaid = true;
				}
			}
		}
		else if(mode.equals("ALPHABETA"))
		{
			System.out.println("Entered ALPHABETA");
			int alpha = Integer.MIN_VALUE;
			int beta = Integer.MAX_VALUE;	
			
			ArrayList<String> stakeList = new ArrayList<>();
			stakeList = nextMoves.get(0);
			
			ArrayList<String> raidList = new ArrayList<>();
			raidList = nextMoves.get(1);
			
			for(int i=0;i<stakeList.size();i++)
			{
				char [][] tmpboardSt = {};
			
				tmpboardSt = changeBoardState(stakeList.get(i), boardState, yourPlayer,1,false);
				//printMatrix(tmpboardSt);
				//System.out.println();			
			
				currentScore = alphabeta(originalPlayer,yourPlayer,d,gameValues,tmpboardSt,1,alpha,beta);
			
			
				if (currentScore > alpha) {
               
					alpha = currentScore;                
					//Store best row and best column.
                
					String move = stakeList.get(i);
					char rc[] = move.toCharArray();
					bestRow = rc[1]-49;          
					bestCol = rc[0]-65;
					bC = rc[0];
					isRaid = false;
				}
			
				if(alpha>=beta)
					break;
			
			}
			
			for(int i=0;i<raidList.size();i++)
			{
				char [][] tmpboardSt = {};
			
				tmpboardSt = changeBoardState(raidList.get(i), boardState, yourPlayer,1,true);
				//printMatrix(tmpboardSt);
				//System.out.println();			
			
				currentScore = alphabeta(originalPlayer,yourPlayer,d,gameValues,tmpboardSt,1,alpha,beta);
			
			
				if (currentScore > alpha) {
               
					alpha = currentScore;                
					//Store best row and best column.
                
					String move = raidList.get(i);
					char rc[] = move.toCharArray();
					bestRow = rc[1]-49;          
					bestCol = rc[0]-65;
					bC = rc[0];
					isRaid = true;
				}
			
				if(alpha>=beta)
					break;
			
			}
		}
				
		//find raid or not and update board accordingly.
		
		boardState[bestRow][bestCol] = yourPlayer.charAt(0);
		
		
		if(isRaid)
		{ if(bestRow-1>=0 && (boardState[bestRow-1][bestCol] == yp))
		{
			if(bestCol-1 >=0 && (boardState[bestRow][bestCol-1] == opp))
			{
				boardState[bestRow][bestCol-1] = yp;
				
			}
			if(bestCol+1 <=N-1 && (boardState[bestRow][bestCol+1] == opp))
			{
				boardState[bestRow][bestCol+1] = yp;
				
			}
			if(bestRow+1 <=N-1 && (boardState[bestRow+1][bestCol] == opp))
			{
				boardState[bestRow+1][bestCol] = yp;
				
			}	
		}
		
		if(bestRow+1<=N-1 && (boardState[bestRow+1][bestCol] == yp))
		{
			if(bestCol-1 >=0 && (boardState[bestRow][bestCol-1] == opp))
			{
				boardState[bestRow][bestCol-1] = yp;
				
			}
			if(bestCol+1 <=N-1 && (boardState[bestRow][bestCol+1] == opp))
			{
				boardState[bestRow][bestCol+1] = yp;
				
			}
			if(bestRow-1 >=0 && (boardState[bestRow-1][bestCol] == opp))
			{
				boardState[bestRow-1][bestCol] = yp;
				
			}
			
		}
		if(bestCol-1>=0 && (boardState[bestRow][bestCol-1] == yp))
		{
			if(bestRow+1 <=N-1 && (boardState[bestRow+1][bestCol] == opp))
			{
				boardState[bestRow+1][bestCol] = yp;
				
			}	
			if(bestRow-1 >=0 && (boardState[bestRow-1][bestCol] == opp))
			{
				boardState[bestRow-1][bestCol] = yp;
				
			}
			if(bestCol+1 <=N-1 && (boardState[bestRow][bestCol+1] == opp))
			{
				boardState[bestRow][bestCol+1] = yp;
				
			}
			
		}
		if(bestCol+1<=N-1 && (boardState[bestRow][bestCol+1] == yp))
		{
			if(bestRow+1 <=N-1 && (boardState[bestRow+1][bestCol] == opp))
			{
				boardState[bestRow+1][bestCol] = yp;
				
			}	
			if(bestRow-1 >=0 && (boardState[bestRow-1][bestCol] == opp))
			{
				boardState[bestRow-1][bestCol] = yp;
				
			}
			if(bestCol-1 >=0 && (boardState[bestRow][bestCol-1] == opp))
			{
				boardState[bestRow][bestCol-1] = yp;
				
			}
			
		}
		
		}
		
		//Using best row and column decide whether the move was a raid or stake.
		//Result move :
		
		
		//System.out.println("Best Score :" + bestScore);
		System.out.println("Row :" + bestRow + "column" + bestCol);
		
		int bR =  bestRow +1;
		String m = Character.toString( bC) + bR;
		
		String result = " ";
		if(isRaid)
			result = "Raid";
		else
			result = "Stake";
		
		System.out.println(m + " " + result);
		writer.write(m+" "+result+ "\n");
		
		for(int i=0;i<boardState.length;i++)
		{
			for(int j=0;j<boardState.length;j++)
			{
				writer.write(boardState[i][j]);
			}
			writer.write("\n");
		}
		
		printMatrix(boardState);
		writer.close();
	}
}
