package pathFinder;

import map.Coordinate;
import map.PathMap;

import java.util.*;

// a global class calculating math operations for PathFinder
public class Math
{
	// derived from old euler project code
	// retrieves an array of all possible permutations of waypoints
	public static int[][] allPermutations(int size)
	{
		int[][] allPermutations = new int[fact(size)][size];
		// for each permutation position
		for (int i=0; i<allPermutations.length; i++)
			allPermutations[i] = permutation(i, size);
		return allPermutations;
	}
	
	// returns factorial
	public static int fact(int n)			
	{	
		int a=1;	
		for (int i=1; i<=n; i++)	
			a *= i;		
		return a;	
	}
	
	
	
	
	
	
	
	
	// derived from old euler project code
	// given a permutation position, finds that permutation
	private static int[] permutation(int permPos, int size)
	{
		ArrayList<Integer> dig = new ArrayList<>();
		for (int i=0; i<size; i++)
			dig.add(i);
		
		int d;
		int[] permutation = new int[size];
		for (int i=1; i<=size; i++)	
		{
			//x/rate of digit change
			d  = permPos/fact(size - i);			
			// cycle thru the digits
			d %= dig.size();							
			
			permutation[i-1] = dig.get(d);
			// so d doesn't get readded
			dig.remove(d);								
		}
		return permutation;
	}
	
	
	
	
}
