package pathFinder;

import map.Coordinate;
import map.PathMap;

import java.util.*;

public class DijkstraPathFinder implements PathFinder
{
	
	/*			NOTE: Task D expands this into a graph representation 	
	 * 							v
	 * Task A:	a) Chose a Coordinate representation, where "edge weights" are represented
	 * 		&	by the terrain cost. For all inbound edges, weight = coordinate terrain cost.
	 * 	Task B:	Instead of having multiple inbound edges to represent the same weight,
	 * 			each coordinate has 1 value for upto 4 edges. 
	 * 
	 * 			b) Coordinates directly adjacent in NORTH, SOUTH, EAST, WEST directions are called
	 * 			neighbours. VALID neighbours are IMPASSABLE and UNVISITED.
	 * 			
	 * 			c) Using buildLinks() every coordinate is linked to a neighbour with the least overall
	 * 			path cost (called minPathCost) until the goal coordinate is reached. We wait until all 
	 * 			relevant coordinates have a link BEFORE generating the path.
	 * 			Explored coordinates: 
	 * 				i) visited: a List of visited coordinates, with the last visited coordinate's
	 * 				neighbours being added to toVisit.
	 * 				ii) toVisit: a "waiting list" of coordinates sorted by their minPathCost. The
	 * 				coordinate with the lowest minPathCost is moved into visited.
	 * 
	 * 			d) When generating the path, all links are connected from the goal -> start node.
	 * 
	 * 	Task C:	a) Starts / goals (nodes) were abstracted into a graph representation where 
	 * 			"edges" were the least cost path between any 2 nodes. The edge weights was the
	 * 			least cost of the path. These abstracted edges are called NodeEdges.
	 * 
	 * 			b) Hence implemented by checking all permutations of (start/node) tuples, and comparing
	 * 			edge weights to find the cheapest edge with the least cost path.
	 * 
	 * 			b) Clean-slate approach in buildLinks(). Explored coordinates (visited + toVisit) were 
	 * 			reset before finding another path, avoiding the build process from being cut short from 
	 * 			an already visited goal. Setting any 2 nodes as the start and goal, will recreate an 
	 * 			"edge" between. This way, all edges between each node is created.
	 * 			This means each node forms an edge to every other node, AND a path won't overlap itself.
	 * 
	 * 			NOTE: No links to code/articles referenced since being mainly developed by ourselves,
	 * 			after working on problems from https://projecteuler.net
	 * 						v v v
	 * 	Task D: a) As an extension of the travelling salesman problem (transform-and-conquer), 
	 * 			chose to brute force with O((n-2)!) time. 
	 * 
	 * 			b) In addition to starts and goals, waypoints were considered as nodes.
	 * 
	 * 			c) Node paths from WAYPOINT permutations to ensure each edge is only calculated ONCE.
	 * 			Rather than going through ALL NODE permutations and choosing orders where
	 * 			the start and goal are the first/last nodes in the order.
	 * 			This reduces from:	O(n!) complexity 		(+ overhead to remove invalid orders)
	 * 			To:					O((n-2)!) complexity 	(-start node -goal node)
	 * 
	 * 			d) Created an Order class to facilitate the logic from c). This class is an abstraction
	 * 			of NodeEdge, the optimal order of nodes whose edges yield the least cost path.
	 * 			
	 * 			e) This class is an abstraction of the Order class, comparing all orders to find
	 * 			the order with the least path cost.
	 */
	
	
	private PathMap map;
	// all possible edges between all nodes (start + goal + waypoints)
	// we calculate the edges first to amortize the cost of
	// finding the path and its path cost multiple times in bestOrder() and findpath()
	List<NodeEdge> edges;
	
    public DijkstraPathFinder(PathMap map) 
    {
    	this.map = map;
    	
    	// find all edges here to amortize the cost later
    	edges = new ArrayList<>();
    	List<Coordinate> nodes = new LinkedList<>();
    	nodes.addAll(map.originCells);
    	nodes.addAll(map.destCells);
    	nodes.addAll(map.waypointCells);
    	
    	for (Coordinate node : nodes)
    	for (Coordinate node2: nodes)
    	{
    		edges.add(new NodeEdge(node, node2, map));
    	}
    	
    }

    
    
    


    // abstracted version of Order.findPath
    // path with least cost through ALL NODES and every (start, goal) tuple
    // Now that the best order through every start/goal is known
    public List<Coordinate> findPath() {
    	
    	int bestCost = Integer.MAX_VALUE;
        List<Coordinate> bestPath = new LinkedList<Coordinate>();

        // for multiple starts and goals
        for (Coordinate start : map.originCells)
        for (Coordinate goal : map.destCells)
        {
        	// find the best path using their costs
        	Order bestOrder = new Order(start, goal, map, edges);
        	if (bestOrder.cost < bestCost)
        	{
        		bestCost = bestOrder.cost;
        		bestPath = bestOrder.findPath();
        	}
        }
        return bestPath;
    }
    
   

    @Override
    public int coordinatesExplored() 
    {
    	int explored = 0;
    	// the coordinates explored by each edge
    	for (NodeEdge edge : edges)
    		explored += edge.coordinatesExplored();
    	return explored;
    }
  
    
    


} // end of class DijsktraPathFinder
