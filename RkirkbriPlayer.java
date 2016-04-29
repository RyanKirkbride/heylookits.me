import java.util.Comparator;
import java.util.PriorityQueue;



public class RkirkbriPlayer implements PokerSquaresPlayer {

	final int ROW = 0, COL = 1;
	private Card[][] board = new Card[5][5];
	private int[][] space = new int[2][5], potential = new int[2][5];
    private PriorityQueue<Integer[]> rows;
	private PriorityQueue<Integer[]> cols;
	
	
	public RkirkbriPlayer()
	{
		
	}
	
	public void setPointSystem(PokerSquaresPointSystem system, long millis)
	{
		
	}
	
	public void init()
	{	
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[i].length; j++)
			{
				board[i][j] = null;
			}
		}
		
		for(int i = 0; i < space.length; i++)
		{
			for(int j = 0; j < space[i].length; j++)
			{
				space[i][j] = 5;
			}
		}
		
		Comparator<Integer[]> c = new IntArrayComparator();
		rows = new PriorityQueue<Integer[]>(5, c);
		cols = new PriorityQueue<Integer[]>(5, c);
	}
	
	public int[] getPlay(Card card, long millisRemaining)
	{
		int[] play = new int[2];
				
		evaluateRow(card);
		evaluateCol(card);
		
		Integer[] rowA = rows.poll(), 
				  rowB = rows.poll(),
				  rowC = rows.poll(),
				  rowD = rows.poll(),
				  rowE = rows.poll();
		
		
		
		boolean working = true;
		for(int j = cols.size(); j > 0 && working; j--)
		{
			
			Integer[] colA = cols.remove();
			
			if(board[rowA[0]][colA[0]] == null)
			{	
				if(rowB != null && board[rowB[0]][colA[0]] == null
					&&	rowB[2] > rowA[2])
				{
					board[rowB[0]][colA[0]] = card;
					play[ROW] = rowB[0];
					play[COL] = colA[0];
					space[ROW][rowB[0]]--;
					space[COL][colA[0]]--;
				}
				else
				{
					board[rowA[0]][colA[0]] = card;
					play[ROW] = rowA[0];
					play[COL] = colA[0];
					space[ROW][rowA[0]]--;
					space[COL][colA[0]]--;
				}
				
				working = false;
			}			
			else if(rowB != null && board[rowB[0]][colA[0]] == null)
			{
				if(rowC != null && board[rowC[0]][colA[0]] == null
						&&	rowC[2] > rowB[2])
					{
						board[rowC[0]][colA[0]] = card;
						play[ROW] = rowC[0];
						play[COL] = colA[0];
						space[ROW][rowC[0]]--;
						space[COL][colA[0]]--;
					}
				
				else
				{
					board[rowB[0]][colA[0]] = card;
					play[ROW] = rowB[0];
					play[COL] = colA[0];
					space[ROW][rowB[0]]--;
					space[COL][colA[0]]--;
				}

				working = false;
				
			}
			else if(rowC != null && board[rowC[0]][colA[0]] == null)
			{
				if(rowD != null && board[rowD[0]][colA[0]] == null
						&&	rowD[2] > rowC[2])
					{
						board[rowD[0]][colA[0]] = card;
						play[ROW] = rowD[0];
						play[COL] = colA[0];
						space[ROW][rowD[0]]--;
						space[COL][colA[0]]--;
					}
				else
				{
					board[rowC[0]][colA[0]] = card;
					play[ROW] = rowC[0];
					play[COL] = colA[0];
					space[ROW][rowC[0]]--;
					space[COL][colA[0]]--;
				}
				working = false;
			}
			else if(rowD != null && board[rowD[0]][colA[0]] == null)
			{
				if(rowE != null && board[rowE[0]][colA[0]] == null
						&&	rowE[2] >  rowD[2])
					{
						board[rowE[0]][colA[0]] = card;
						play[ROW] = rowE[0];
						play[COL] = colA[0];
						space[ROW][rowE[0]]--;
						space[COL][colA[0]]--;
					}
				else
				{
					board[rowD[0]][colA[0]] = card;
					play[ROW] = rowD[0];
					play[COL] = colA[0];
					space[ROW][rowD[0]]--;
					space[COL][colA[0]]--;
				}
			
				working = false;
			}
			else if(rowE != null && board[rowE[0]][colA[0]] == null)
			{			
				board[rowE[0]][colA[0]] = card;
				play[ROW] = rowE[0];
				play[COL] = colA[0];
				space[ROW][rowE[0]]--;
				space[COL][colA[0]]--;
				working = false;
			}
		}
						
		rows.clear();
		cols.clear();
		
//      View the amount of space left in the row and column the card is being placed into
//		System.out.println("Space in row[" + play[ROW] + "]: " + space[ROW][play[ROW]] + "  col[" + play[COL] +"]: " + space[COL][play[COL]]);
		return play;								
	}
	
	private void evaluateRow(Card card)
	{
		boolean suit, one, two, three, isStraight[] = new boolean[5];
		int numSuit;
		int straight[][] = new int[5][5];
						
		
		for(int i = 0; i < 5; i++)
		{
			for(int n = 0; n < 5; n++)
			{
				isStraight[n] = true;
				for(int j = 0; j < 5; j++)
				{
					if(j == n)
					{
						straight[n][j] = card.getRank();
					}
					else
					{
						straight[n][j] = -1;
					}
				}
			}
			
			
			suit = true;
			Integer[] rowEval = new Integer[3];
			rowEval[0] = i;
			rowEval[2] = 10;
			potential[ROW][i] = 12;
			
			if(space[ROW][i] > 0)
			{
				
				rowEval[2] += 5 * space[ROW][i];
				potential[ROW][i] += 15 * space[ROW][i];
				one = false;
				two = false;
				three = false;
				numSuit = 0;
	
				
				for(int j = 0; j < 5; j++)
				{
					
					if(board[i][j] != null)
					{
						
						for(int k = 0; k < 5; k++)
						{
							if(isStraight[k])
							{
								if(card.getRank() == board[i][j].getRank())
								{
									isStraight[k] = false;
								}
							
								else if(card.getRank() - board[i][j].getRank() > 0 && card.getRank() - board[i][j].getRank() < 5) 
								{
														
															
										if(card.getRank() - board[i][j].getRank() == 1)
										{
											if(k == 1 && straight[k][0] == -1)
											{
												straight[k][0] = board[i][j].getRank();
											}
											else if(k == 2 && straight[k][1] == -1)
											{
												straight[k][1] = board[i][j].getRank();
											}
											else if(k == 3 && straight[k][2] == -1)
											{
												straight[k][2] = board[i][j].getRank();
											}
											else if(k == 4 && straight[k][3] == -1)
											{
												straight[k][3] = board[i][j].getRank();
											}
											else isStraight[k] = false;
										}
			
										else if(card.getRank() - board[i][j].getRank() == 2 )
										{
											if(k == 2 && straight[k][0] == -1)
											{
												straight[k][0] = board[i][j].getRank();
											}
											else if(k == 3 && straight[k][1] == -1)
											{
												straight[k][1] = board[i][j].getRank();
											}
											else if(k == 4 && straight[k][2] == -1)
											{
												straight[k][2] = board[i][j].getRank();
											}
											else isStraight[k] = false;
											
										}
									
										else if(card.getRank() - board[i][j].getRank() == 3)
										{
											if(k == 3 && straight[k][0] == -1)
											{
												straight[k][0] = board[i][j].getRank();
											}
											else if(k == 4 && straight[k][1] == -1)
											{
												straight[k][1] = board[i][j].getRank();
											}
											else isStraight[k] = false;
										}
										
										
										else if(card.getRank() - board[i][j].getRank() == 4)
										{
											if(k == 4 && straight[k][0] == -1)
											{
												straight[k][0] = board[i][j].getRank();
											}
											else isStraight[k] = false;
										}
								}
								
								
								
								else if(board[i][j].getRank() - card.getRank() > 0 && board[i][j].getRank() - card.getRank() < 5)
								{	
								
									if(board[i][j].getRank() - card.getRank() == 1)
									{
										if(k == 0 && straight[k][1] == -1)
										{
											straight[k][1] = board[i][j].getRank();
										}
										
										else if(k == 1 && straight[k][2] == -1)
										{
											straight[k][2] = board[i][j].getRank();
										}
										else if(k == 2 && straight[k][3] == -1)
										{
											straight[k][3] = board[i][j].getRank();
										}
										else if(k == 3 && straight[k][4] == -1)
										{
											straight[k][4] = board[i][j].getRank();
										}
										else isStraight[k] = false;
									}
									else if(board[i][j].getRank() - card.getRank() == 2)
									{
										if(k == 0 && straight[k][2] == -1)
										{
											straight[k][2] = board[i][j].getRank();
										}
										else if(k == 1 && straight[k][3] == -1)
										{
											straight[k][3] = board[i][j].getRank();
										}
										else if(k == 2 && straight[k][4] == -1)
										{
											straight[k][4] = board[i][j].getRank();
										}
									}
									
									else if(board[i][j].getRank() - card.getRank() == 3) 
									{
										if(k == 0 && straight[k][3] == -1)
										{
											straight[k][3] = board[i][j].getRank();
										}
										if(k == 1 && straight[k][4] == -1)
										{
											straight[k][4] = board[i][j].getRank();
										}
										else isStraight[k] = false;
										
									}
									else if(board[i][j].getRank() - card.getRank() == 4)
									{
										if(k == 0 && straight[k][4] == -1)
										{
											straight[k][4] = board[i][j].getRank();
										}
										else isStraight[k] = false;
									}
							
								}
								else
								{
									isStraight[k] = false;
								}
				
							}
						
						
						
						
						}
						
		/*
		 * 	View the potential straights of row[i] from placing the new card
		 * 
		 * 				for(int l = 0; l < 5; l++)
		 *
						{
							if(isStraight[l])
							{
								System.out.print("Row[" + i + "] straight[" + l + "]: ");
								for(int m = 0; m < 5; m++)
								{
									System.out.print(straight[l][m] + " ");
								}
								System.out.print("\n");
							}
					
						}
						
			*/			
						
						if(card.getRank() == board[i][j].getRank())
						{
							if(!one)
							{
								rowEval[2] += 20;
								potential[ROW][i] += 23;
								one = true;
							}
							else if(!two)
							{
								rowEval[2] += 50;
								potential[ROW][i] += 55;
								two = true;
							}
							else if(!three)
							{
								rowEval[2] += 100;
								potential[ROW][i] += 110;
								three = true;
							}
							else
							{
								rowEval[2] += 500;
								potential[ROW][i] += 250;
							}
						
						}
					
						if(suit && card.getSuit() == board[i][j].getSuit())
						{
							rowEval[2] += 30;
							potential[ROW][i] += 35;
							numSuit++;
						}
						else if(suit)
						{
							suit = false;
							rowEval[2] -= (30 * numSuit);
							potential[ROW][i] -=  (35 * numSuit);
							numSuit = 0;
						}
					}
				}
				
				boolean potStraight = false;
				for(int j = 0; j < 5; j++)
				{
					int count = 0;
					if(isStraight[j])
					{
						for(int k = 0; k < 5; k++)
						{
							if(straight[j][k] != -1)
							{
								count++;
							}
						}
						
						potStraight = true;
						rowEval[2] += (150 * count);
						potential[ROW][i] += (153 * count);
					}
				}
				
				if(potStraight && suit)
				{
					rowEval[2] += 750;
					potential[ROW][i] += 375;
				}
				
				potential[ROW][i] += space[ROW][i] * 5 + numSuit * 20;
				rowEval[1] = (int)(((double)rowEval[2].intValue()/potential[ROW][i])*100);
	
	//			View the number which will be compared when adding onto th
	//			
	//			System.out.println("Row[" + i + "]: " + rowEval[2].toString() + "/" + potential[ROW][i]  + " = " + rowEval[1].toString());
				rows.add(rowEval);
			}	
			
		}
		
	}
	
	private void evaluateCol(Card card)
	{
		boolean suit, one, two, three, isStraight[] = new boolean[5];
		int numSuit;
		int straight[][] = new int[5][5];
		
			
		for(int i =0; i < 5; i++)
		{
			for(int n = 0; n < 5; n++)
			{
				isStraight[n] = true;
				for(int j = 0; j < 5; j++)
				{
					if(j == n)
					{
						straight[n][j] = card.getRank();
					}
					else
					{
						straight[n][j] = -1;
					}
				}
			}
			
				
			suit = true;
			one = false;
			two = false;
			three = false;
			Integer[] colEval = new Integer[3];
			numSuit = 0;
			colEval[0] = i;
			colEval[2] = 10;
			potential[COL][i] = 12;
			
			if(space[COL][i] > 0)
			{
				colEval[2] += 5 * space[COL][i];
				potential[COL][i] += 15 * space[COL][i];
				for(int j = 0; j < 5; j++)
				{
			
					if(board[j][i] != null)
					{
						for(int k = 0; k < 5; k++)
						{
							if(isStraight[k])
							{
								if(card.getRank() == board[j][i].getRank())
								{
									isStraight[k] = false;
								}
							
								else if(card.getRank() - board[j][i].getRank() > 0 && card.getRank() - board[j][i].getRank() < 5) 
								{
														
			
										if(card.getRank() - board[j][i].getRank() == 1)
										{
											if(k == 1 && straight[k][0] == -1)
											{
												straight[k][0] = board[j][i].getRank();
											}
											else if(k == 2 && straight[k][1] == -1)
											{
												straight[k][1] = board[j][i].getRank();
											}
											else if(k == 3 && straight[k][2] == -1)
											{
												straight[k][2] = board[j][i].getRank();
											}
											else if(k == 4 && straight[k][3] == -1)
											{
												straight[k][3] = board[j][i].getRank();
											}
											else isStraight[k] = false;
										}
			
										else if(card.getRank() - board[j][i].getRank() == 2 )
										{
											if(k == 2 && straight[k][0] == -1)
											{
												straight[k][0] = board[j][i].getRank();
											}
											else if(k == 3 && straight[k][1] == -1)
											{
												straight[k][1] = board[j][i].getRank();
											}
											else if(k == 4 && straight[k][2] == -1)
											{
												straight[k][2] = board[j][i].getRank();
											}
											else isStraight[k] = false;
											
										}
									
										else if(card.getRank() - board[j][i].getRank() == 3)
										{
											if(k == 3 && straight[k][0] == -1)
											{
												straight[k][0] = board[j][i].getRank();
											}
											else if(k == 4 && straight[k][1] == -1)
											{
												straight[k][1] = board[j][i].getRank();
											}
											else isStraight[k] = false;
										}
										
										
										else if(card.getRank() - board[j][i].getRank() == 4)
										{
											if(k == 4 && straight[k][0] == -1)
											{
												straight[k][0] = board[j][i].getRank();
											}
											else isStraight[k] = false;
										}
								}
								
								
								
								else if(board[j][i].getRank() - card.getRank() > 0 && board[j][i].getRank() - card.getRank() < 5)
								{	
								
									if(board[j][i].getRank() - card.getRank() == 1)
									{
										if(k == 0 && straight[k][1] == -1)
										{
											straight[k][1] = board[j][i].getRank();
										}
										
										else if(k == 1 && straight[k][2] == -1)
										{
											straight[k][2] = board[j][i].getRank();
										}
										else if(k == 2 && straight[k][3] == -1)
										{
											straight[k][3] = board[j][i].getRank();
										}
										else if(k == 3 && straight[k][4] == -1)
										{
											straight[k][4] = board[j][i].getRank();
										}
										else isStraight[k] = false;
									}
									else if(board[j][i].getRank() - card.getRank() == 2)
									{
										if(k == 0 && straight[k][2] == -1)
										{
											straight[k][2] = board[j][i].getRank();
										}
										else if(k == 1 && straight[k][3] == -1)
										{
											straight[k][3] = board[j][i].getRank();
										}
										else if(k == 2 && straight[k][4] == -1)
										{
											straight[k][4] = board[j][i].getRank();
										}
									}
									
									else if(board[j][i].getRank() - card.getRank() == 3) 
									{
										if(k == 0 && straight[k][3] == -1)
										{
											straight[k][3] = board[j][i].getRank();
										}
										if(k == 1 && straight[k][4] == -1)
										{
											straight[k][4] = board[j][i].getRank();
										}
										else isStraight[k] = false;
										
									}
									else if(board[j][i].getRank() - card.getRank() == 4)
									{
										if(k == 0 && straight[k][4] == -1)
										{
											straight[k][4] = board[j][i].getRank();
										}
										else isStraight[k] = false;
									}
							
								}
								else
								{
									isStraight[k] = false;
								}
				
							}
						
						
						
						
						}
						
						
				/*	 View the potential straights of col[i] from placing the card	
				 * 
				 * 		for(int l = 0; l < 5; l++)
						{
							if(isStraight[l])
							{
								System.out.print("Col[" + i + "] straight[" + l + "]: ");
								for(int m = 0; m < 5; m++)
								{
									System.out.print(straight[l][m] + " ");
								}
								System.out.print("\n");
							}
					
						}
					*/	
						if(card.getRank() == board[j][i].getRank())
						{
							if(!one)
							{
								colEval[2] += 20;
								potential[COL][i] += 25;
								one = true;
							}
							else if(!two)
							{
								colEval[2] += 100;
								potential[COL][i] += 110;
								two = true;
							}
							else if(!three)
							{
								colEval[2] += 500;
								potential[COL][i] += 250;
								three = true;
							}
							
						
						}
					
						if(suit && card.getSuit() == board[j][i].getSuit())
						{
							colEval[2] += 50;
							potential[COL][i] += 55;
							numSuit++;
						}
						else if(suit)
						{
							suit = false;
							potential[COL][i] -= (50 * numSuit);
							colEval[2] -= (55 * numSuit);
							numSuit = 0;
						}
						
					}
				}
			
				boolean potStraight = false;
				for(int j = 0; j < 5; j++)
				{
					int count = 0;
					if(isStraight[j])
					{
						for(int k = 0; k < 5; k++)
						{
							if(straight[j][k] != -1)
							{
								count++;
							}
						}
						
						potStraight = true;
						colEval[2] += (count * 100);
						potential[COL][i] += (count * 110);
					}
				}
				
				if(potStraight && suit)
				{
					colEval[2] += 750;
					potential[COL][i] += 375;
				}
				
				potential[COL][i] += space[COL][i] * 5 + numSuit * 20;
				colEval[1] = (int)(((double)colEval[2].intValue()/potential[COL][i])*100);
			
//				View the number which will be used to compare when placing an item onto the p. queue			
//				
//				System.out.println("Column[" + i +"]: " + colEval[2].toString() + "/" 
//									+ potential[COL][i]  + " = " + colEval[1].toString());
//				
				
				cols.add(colEval);
			}
		
			
		}
	}
	
	public String getName()
	{
		return "Ryan Kirkbride";
	}
	
	
	class IntArrayComparator implements Comparator<Integer[]>
	{
		public IntArrayComparator()
		{
			
		}
		
		public int compare(Integer[] a, Integer[] b)
		{
			return b[1] - a[1];
		}
		
		
	}
	
	
	
	public static void main(String[] args) {
		PokerSquaresPointSystem system = PokerSquaresPointSystem.getAmericanPointSystem();
		System.out.println(system);
		
		new PokerSquares(new RkirkbriPlayer(), system).play(); // play a single game
	}
	
	
}
