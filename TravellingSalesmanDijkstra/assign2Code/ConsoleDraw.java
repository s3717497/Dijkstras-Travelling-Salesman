
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import map.*;
import pathFinder.Math;

import pathFinder.DijkstraPathFinder;

// could not read the ming visualization bc it did not fit in my laptop size
// also unable to test bugs with an IDE so created this class
public class ConsoleDraw {

	
	public static void main(String[] args) throws FileNotFoundException {
		
		

		PathMap map = new PathMap();
		ConsoleDraw cd = new ConsoleDraw();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Choose parameter file no.: ");
		Object[] p = cd.parameters(new File("assign2Code\\example" + sc.next() + ".para"));
		int rows = (int) p[0];
		int cols = (int) p[1];
		List<Coordinate> starts = (List<Coordinate>) p[2];
		List<Coordinate> ends = (List<Coordinate>) p[3];
		Set<Coordinate> impassables = (Set<Coordinate>) p[4];
		
		System.out.println("Choose terrain file no.: ");
		int num = Integer.parseInt(sc.next());
		Map<Coordinate, Integer> terrain = num > 0 ? 
				cd.terrainCells(new File("assign2Code\\terrain" + num + ".para")) 
				: new HashMap<Coordinate, Integer>();
				
		System.out.println("Choose waypoints file no.: ");
		int num2 = Integer.parseInt(sc.next());
		LinkedList<Coordinate> waypoints = num2 > 0 ? 
				cd.waypointCells(new File("assign2Code\\waypoints" + num2 + ".para"))
				: new LinkedList<Coordinate>() ;

		
		map.initMap(rows, cols, starts, ends, impassables, terrain, waypoints);
		List<Coordinate> path = (new DijkstraPathFinder(map)).findPath();
		
		print(map, path);
		sc.close();
	}
	
	// uncomment NodeEdge line 88 to see
	public static void print(PathMap map, List<Coordinate> path) 
	{
	    for (int i=map.cells.length-1; i>=0; i--)
		{
			for (int j=0; j<map.cells[0].length; j++)
			{
				String cellStr = "  ";
				Coordinate cell = map.cells[i][j];
				if (cell.getTerrainCost() > 1)			cellStr = "__";
				if (cell.getImpassable())				cellStr = "BB";
				if (path.contains(cell))				cellStr = "* ";
				if (map.originCells.contains(cell))		cellStr = "St";
				if (map.destCells.contains(cell))		cellStr = "Go";
				if (map.waypointCells.contains(cell))	cellStr = "W" + map.waypointCells.indexOf(cell);
				System.out.print(cellStr);
			}
			System.out.println();
		}	
	    System.out.println("\n\n");
	}
	
	// console version showing search processs
	// uncomment NodeEdge line 115 to watch animation
    public static void printSearch(PathMap map, Coordinate start, Coordinate goal, 
    List<Coordinate> visited, List<Coordinate> toVisit)
    {
	    for (int i=map.cells.length-1; i>=0; i--)
		{
			for (int j=0; j<map.cells[0].length; j++)
			{
				String cellStr = ". ";
				Coordinate cell = map.cells[i][j];
				
				if (toVisit.contains(cell))	cellStr = "O ";
				if (visited.contains(cell))	cellStr = "x ";
				if (cell.getImpassable())	cellStr = "BB";
				if (cell.equals(goal))		cellStr = "Go";
				if (cell.equals(start))		cellStr = "St";
				
				System.out.print(cellStr);		
			}
			System.out.println("");
		}
		System.out.println("\n\n");
		try {Thread.sleep(20);} catch (Exception e) {};
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private Object[] parameters(File file) throws FileNotFoundException
	{
		int rows = -1;
		int cols = -1;
		List<Coordinate> starts = new LinkedList<>();
		List<Coordinate> ends = new LinkedList<>();
		Set<Coordinate> impassables = new HashSet<>();
		
		Scanner sc = new Scanner(file);
		for (int i=0; sc.hasNext(); i++)
		{
			List<Integer> parameters = new LinkedList<>();
			for (String s : sc.nextLine().split(" "))
				parameters.add(Integer.parseInt(s));
				
			if (i==0)
			{
				rows = parameters.get(0);
				cols = parameters.get(1);
			}
			else if (i==1)
			{
				int a=0;
				while (a<parameters.size())
				{
					int row = parameters.get(a++);
					int col = parameters.get(a++);
					starts.add(new Coordinate(row, col));
				}
			}
			else if (i==2)
			{
				int a=0;
				while (a<parameters.size())
				{
					int row = parameters.get(a++);
					int col = parameters.get(a++);
					ends.add(new Coordinate(row, col));
				}
			}
			else if (i>=3)
			{
				int row = parameters.get(0);
				int col = parameters.get(1);
				impassables.add(new Coordinate(row, col));
			}
		}
		Object[] a = {rows, cols, starts, ends, impassables};
		return a;
	}
	private Map<Coordinate, Integer> terrainCells(File file) throws FileNotFoundException
	{
		Map<Coordinate, Integer> terrains = new HashMap<>();
		Scanner sc = new Scanner(file);
		for (int i=0; sc.hasNext(); i++)
		{
			List<Integer> parameters = new LinkedList<>();
			for (String s : sc.nextLine().split(" "))
				parameters.add(Integer.parseInt(s));
			int row = parameters.get(0);
			int col = parameters.get(1);
			int terrainCost = parameters.get(2);
			
			terrains.put(new Coordinate(row, col), terrainCost);
		}
		return terrains;
	}
	
	private LinkedList<Coordinate> waypointCells(File file) throws FileNotFoundException
	{
		LinkedList<Coordinate> waypoints = new LinkedList<>();
		Scanner sc = new Scanner(file);
		for (int i=0; sc.hasNext(); i++)
		{
			List<Integer> parameters = new LinkedList<>();
			for (String s : sc.nextLine().split(" "))
				parameters.add(Integer.parseInt(s));
			int row = parameters.get(0);
			int col = parameters.get(1);
			
			waypoints.add(new Coordinate(row, col));
		}
		return waypoints;
	}

}
