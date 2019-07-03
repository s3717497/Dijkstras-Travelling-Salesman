package map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
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
		
//		String pathString = "(16,5) -> (15,5) -> (14,5) -> (13,5) -> (13,6) -> (13,7) -> (13,8) -> (13,9) -> (13,10) -> "
//		+ "(24,24) -> (24,23) -> (24,22) -> (24,21) -> (24,20) -> (24,19) -> (24,18) -> (24,17) -> (24,16) -> (24,15) -> (23,15) -> (22,15) -> (21,15) -> (20,15) -> (20,14) -> (20,13) -> (20,12) -> (19,12) -> (18,12) -> (17,12) -> (16,12) -> (15,12) -> (14,12) -> (13,12) -> (12,12) -> (12,11) -> (12,10) -> (13,10) -> (14,10) -> (15,10) -> (16,10) -> (16,9) -> (16,8) -> (16,7) -> (16,6) -> (16,5) -> "
//		+ "(24,24) -> (23,24) -> (22,24) -> (21,24) -> (20,24) -> (19,24) -> (18,24) -> (17,24) -> (16,24) -> (15,24) -> (15,23) -> (15,22) -> (15,21) -> (15,20) -> (15,19) -> (15,18) -> "
//		+ "(15,18) -> (14,18) -> (13,18) -> (12,18) -> (12,19) -> (12,20) -> (12,21) -> (12,22) -> (12,23) -> (11,23) -> (10,23) -> (9,23) -> (8,23) -> (7,23) -> (6,23) -> (5,23) -> (4,23) -> (4,24) -> "
//		+ "(4,24) -> (3,24)";
//		
//		
//		for (List<Coordinate> chain : getChains(pathString))
//			System.out.println(chain);
//		
//		String cleanPath = pathCleaner(pathString).toString().replaceAll(", ", " -> ");
//		System.out.println("path: " + cleanPath);
//		System.out.println(pathCleaner(pathString).size());
		

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
				: new LinkedList<Coordinate>();
		
		System.out.println("Show animation process (Y/N) ?: ");
		toAnimate = sc.next().equalsIgnoreCase("Y");
				
		

		
		map.initMap(rows, cols, starts, ends, impassables, terrain, waypoints);
		List<Coordinate> path = (new DijkstraPathFinder(map)).findPath();
		
		print(map, path);
		print(path);
		sc.close();
	}
	
	
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
	
	public static void print(List<Coordinate> path)
	{
		for (Coordinate co : path)
			System.out.print(co + " -> ");
		
	}
	
	private static boolean toAnimate;
    public static void printSearch(PathMap map, Coordinate start, Coordinate goal, 
    List<Coordinate> visited, List<Coordinate> toVisit)
    {
    	if (!toAnimate)
    		return;
    	
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
		try {Thread.sleep(80);} catch (Exception e) {};
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
	
	
	
	
//	// Given a list containing mixed up chains in mixed orders
//	// W1 -> o -> o -> o -> W2
//	// W2 -> o -> W3
//	// W1 -> o -> o -> St
//	// G  -> o -> W3
//	// Finds the correct order
//	// St -> ... -> W1 -> ... -> W2 -> ... W3 ... -> G
//	private static LinkedList<LinkedList<Coordinate>> getChains(String dirtyPath)
//	{
//		String[] coordinates = dirtyPath.split(" -> ");
//		
//		List<Coordinate> cos = new LinkedList<>();
//		for (String co : coordinates)
//			cos.add(new Coordinate(co));
//		
//		LinkedList<LinkedList<Coordinate>> chains = new LinkedList<>();
//		
//		int i=1;
//		while (i < cos.size())
//		{
//			LinkedList<Coordinate> chain = new LinkedList<>();
//			Coordinate prev = cos.get(i-1), current = cos.get(i);
//			
//			try 
//			{
//				while (prev.isAdjacent(current))
//				{
//					chain.add(prev);
//					i++;
//					prev = cos.get(i-1);
//					current = cos.get(i);
//					
//				}
//			} catch (IndexOutOfBoundsException e) {};
//			
//			chain.add(prev);
//			chains.add(chain);
//			i++;
//		}
//		
//		return chains;
//		
//	}
//	
//	private static List<Coordinate> pathCleaner(String dirtyPath) 
//	{
//		LinkedList<LinkedList<Coordinate>> chains = getChains(dirtyPath);
//		List<Coordinate> cleanPath = new LinkedList<>();
//		Coordinate connector = findStart(chains);
//		
//		
//		for (int i=0; i<chains.size(); i++)
//		{
//			LinkedList<Coordinate> chain = chains.get(i);
//			
//			if (chain.contains(connector))
//			{
//				// reverse order if connector is last
//				if (chain.getLast().equals(connector))
//					Collections.reverse(chain);
//				
//				// to avoid connectors repeating
//				connector = chain.removeLast();
//				cleanPath.addAll(chain);
//				
//				chains.remove(i);
//				i=-1;
//			}
//		}
//		cleanPath.add(connector);
//		return cleanPath;
//	}
//	
//	private static Coordinate findStart(LinkedList<LinkedList<Coordinate>> chains)
//	{
//		List<Coordinate> waypoints = new LinkedList<>();
//		//  o-o-o-o-o
//		//  ^       ^
//		//  way points
//		for (LinkedList<Coordinate> chain : chains) {
//			waypoints.add(chain.getFirst());
//			waypoints.add(chain.getLast());
//		}
//		for (Coordinate waypoint : waypoints) {
//			int count = 0;
//			for (Coordinate instance : waypoints)
//				if (instance.equals(waypoint))
//					count++;
//			if (count == 1)
//				return waypoint;
//		}
//		return null;
//	}

}
