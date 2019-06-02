package pathFinder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import map.Coordinate;
import map.PathMap;

// Task C + D
// a least cost path between 2 nodes (Nodes = start/goal/waypoints)
// abstracted "edge" consisting of smaller "edges" between neighbours
// Dijkstra's algorithm implemented here
public class NodeEdge implements PathFinder {
	
	// directions relative to the original coordinate
	private static final int NORTH = 0;
	private static final int SOUTH = 1;
	private static final int EAST = 2;
	private static final int WEST = 3;
	private static final int[] directions = {NORTH, SOUTH, EAST, WEST};
	
	private PathMap map;
	private Coordinate start;
	private Coordinate goal;
	
	private LinkedList<Coordinate> visited;
	private LinkedList<Coordinate> toVisit;
	
	// a one-time instantiation of the least cost path between the two nodes
	public LinkedList<Coordinate> path;
	// least path cost
	public Integer weight;
	
	
	public NodeEdge(Coordinate start, Coordinate goal, PathMap map)
	{
		this.map = map;
		// not passing by reference to map.cells bc they are raw coordinates
		this.start = map.cells[start.getRow()][start.getColumn()];
		this.goal = map.cells[goal.getRow()][goal.getColumn()];
		
		findThePath();
		// to ensure it does NOT get a reference
		for (weight=0; weight<goal.getMinPathCost(); weight++) {}
	}
	
	public Coordinate getStart()
	{
		return start;
	}
	
	public Coordinate getGoal()
	{
		return goal;
	}
	
	
	
	
	
	
	
	
	// clean-slate approach so every least cost path for an edge
	// does not dynamically rely on the map when called
	// other edges can build their own paths simultaneously
	private void findThePath()
	{
    	// clean-slate approach so every separate
    	// start-goal path does not interfere with other paths
		visited = new LinkedList<>();
		toVisit = new LinkedList<>();
		map.resetCells();
		
		buildLinks();
		path = new LinkedList<>();
		
	    // 	from the goal node, chains previous nodes together
	    // 	until start node reached forming the path
		path.add(goal);
		while(!path.contains(start))
			path.add(path.getLast().getLink());
		
// 		// prints out one path between 2 nodes
//		ConsoleDraw.print(map, path);
	}
	
	public List<Coordinate> findPath()
	{
		return path;
	}
	
    private void buildLinks()
    {	
		// no visited coordinates (links) for the start coordinate
		start.setStartMinPathCost();
		visited.add(start);
		while (!visited.contains(goal))
		{
			// look thru the neighbours of the last visited Coordinate for a candidate
			Coordinate current = visited.getLast();
			for (Coordinate neighbour : neighbours(current))
			{
				neighbour.setLink(current);
				toVisit.add(neighbour);
			}
			// to avoid restepping into visited coordinates (neighbours)
			// AND removing previous toVisit converted to visited
			toVisit.removeAll(visited);
			
//			// prints a console animation of the search process		
//			ConsoleDraw.printSearch(map, start, goal, visited, toVisit);
			
			//Once toVisit Coordinates have a path cost, ready to move on
			//choose the Coordinate with the lowest path cost
	    	Coordinate cheapest = toVisit.get(0);
	    	for (Coordinate co : toVisit)
	    		if (co.getMinPathCost() < cheapest.getMinPathCost())
	    			cheapest = co;
			visited.add(cheapest);
		}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // returns all VALID neighbours
    private Set<Coordinate> neighbours(Coordinate co)
    {
    	Set<Coordinate> neighbours = new HashSet<>();
    	for (int direction : directions) 
    	{
    		Coordinate neighbour = validNeighbour(co, direction);
    		if (neighbour != null)
    			neighbours.add(neighbour);
    	}
    	return neighbours;
    }
    
    // gets a PASSABLE UNVISITED adjacent coordinate in the requested direction
    private Coordinate validNeighbour(Coordinate co, int direction)
    {
    	int row = co.getRow();
    	int col = co.getColumn();
    	switch (direction)
    	{
	    	case NORTH : 	row++;	break;
	    	case SOUTH : 	row--;	break;
	    	case EAST : 	col++;	break;
	    	case WEST : 	col--;	break;
    	}
    	
    	if (!map.isPassable(row, col))
    		return null;
//    	// to avoid the path restepping into a visited coordinate
    	if (visited.contains(map.cells[row][col]))
    		return null;
    	return map.cells[row][col];
    	
    }
    
    public String toString()
    {
    	String startStr = "";
    	String goalStr = "";
    	if (map.waypointCells.contains(start)) 	startStr = "W" + map.waypointCells.indexOf(start);
    	if (map.originCells.contains(start)) 	startStr = "St";
    	if (map.destCells.contains(start)) 		startStr = "Go";
    	
    	if (map.waypointCells.contains(goal)) 	goalStr = "W" + map.waypointCells.indexOf(goal);
    	if (map.originCells.contains(goal)) 	goalStr = "St";
    	if (map.destCells.contains(goal)) 		goalStr = "Go";
    	return startStr + "->" + goalStr;
    }

	@Override
	public int coordinatesExplored()
	{
		return visited.size() + toVisit.size();
	}

}
