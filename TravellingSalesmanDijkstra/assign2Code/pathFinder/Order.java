package pathFinder;


import map.Coordinate;
import map.PathMap;

import java.util.*;

// Task D
// optimal order from a single (Start, Goal) tuple: 
// Every node order is structured as: Start -> Waypoints -> Goal
public class Order implements PathFinder
{
	
	private PathMap map;
	// all possible edges between all nodes (start + goal + waypoints)
	// we calculate the edges first to amortize the cost of
	// finding the path and its path cost multiple times in bestOrder() and findpath()
	private List<NodeEdge> edges;
	private Coordinate start;
	private Coordinate goal;
	
	// a one-time instantiation of the least cost path through the order
	private List<Coordinate> path;
	// least path cost through its edges for ALL WAYPOINT ORDERS
	int cost;
	
    public Order(Coordinate start, Coordinate goal, PathMap map, List<NodeEdge> edges) 
    {
    	this.map = map;
    	this.edges = edges;
    	path = new LinkedList<>();
    	cost = Integer.MAX_VALUE;
		// not passing by reference to map.cells bc they are
    	// raw coordinates from parsing the file
		this.start = map.cells[start.getRow()][start.getColumn()];
		this.goal = map.cells[goal.getRow()][goal.getColumn()];
    	
    	findThePath();
    }

    

    
    
    
     
    
    
    public List<Coordinate> findPath()
    {
    	// only create path once
    	return path;
    }
    
	// Nodes: Start S, Waypoints W1 W2 W3, Goal G
	//
	// 1st permutation:	Path: S W1 W2 W3 G	Waypoint order: 1,2,3
	// 2nd permutation: Path: S W1 W2 W3 G	Waypoint order: 1,3,2
	// .
	// .
	// . (no. waypoints)! permutations later 
	// .
	// last permutation:Path: S W3 W2 W1 G	Waypoint order: 3,2,1
	//
	//
    // Abstraction of NodeEdge.findPath
	// least cost path through its edges for ALL WAYPOINTS ORDERS
    // for ONE (start, goal) tuple
	// brute-force through every permutation
	private void findThePath()
	{
		// all possible waypoint orders
		for (int[] waypointOrder : Math.allPermutations(map.waypointCells.size()))
		{
			// attach start and end nodes
			List<Coordinate> order = buildOrder(start, waypointOrder, goal);
			// select this path if it has least cost
			if (minPathCost(order) < cost)
			{
				cost = minPathCost(order);
				path.clear();
				// path formed from all edge paths between each node
				for(NodeEdge edge : toEdges(order))
					path.addAll(edge.path);								
			}
		}
	}
	
	// start/goal/waypoints are nodes
	// uses an ordered list of nodes to traverse
	private int minPathCost(List<Coordinate> order)
	{
		int minPathCost = 0;
		for (NodeEdge edge : toEdges(order))
			minPathCost += edge.weight;
		return minPathCost;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
    
	
	// ordered list of nodes to traverse to find its cost
	private List<Coordinate> buildOrder(Coordinate start, int[] waypointOrder, Coordinate goal)
	{
		LinkedList<Coordinate> order = buildOrder(waypointOrder);
		order.addFirst(start);
		order.add(goal);
		return order;
	}
	
	// for reusing in constructor
	private LinkedList<Coordinate> buildOrder(int[] waypointOrder)
	{
		LinkedList<Coordinate> wpOrder = new LinkedList<>();
		for (int i=0; i<waypointOrder.length; i++)
			wpOrder.add(map.waypointCells.get(waypointOrder[i]));
		return wpOrder;
	}
    
    // map alternative - get bi directional edge
    private NodeEdge getEdge(Coordinate start, Coordinate goal)
    {
    	for (NodeEdge edge : edges)
    	{
    		if (edge.getStart().equals(start)&& edge.getGoal().equals(goal))	return edge;
    		if (edge.getStart().equals(goal) && edge.getGoal().equals(start))	return edge;
    	}
    	return null;
    }
    
    private List<NodeEdge> toEdges(List<Coordinate> order)
    {
    	List<NodeEdge> edges = new LinkedList<>();
    	for (int i=1; i<order.size(); i++)
    	{
    		Coordinate node1 = order.get(i-1); 
    		Coordinate node2 = order.get(i); 
    		edges.add(getEdge(node1, node2));
    	}
    	return edges;
    }

	@Override
	public int coordinatesExplored() 
	{
		// since all edges instrantiated in DijkstraPathFinder
		return 0;
	}
  
    
    


} // end of class DijsktraPathFinder
