package map;

import java.util.*;

/**
 * @author Jeffrey Chan, Youhan Xia, Phuc Chu
 * RMIT Algorithms & Analysis, 2019 semester 1
 * <p>
 * Class representing a coordinate.
 */

/*implement comparable so it can be inputted into a sorted set*/
public class Coordinate// implements Comparable<Coordinate> 
{
	
	
    /**
     * row
     */
    protected int r;

    /**
     * column
     */
    protected int c;

    /**
     * Whether coordinate is impassable or not.
     */
    protected boolean isImpassable;

    /**
     * Terrain cost.
     */
    protected int terrainCost;


    // score asssigned and used for calculating the path cost
    private int minPathCost;
    // a connection to another coordinate
    // used to form the shortest path
    private Coordinate link;

    /**
     * Construct coordinate (r, c).
     *
     * @param r Row coordinate
     * @param c Column coordinate
     */
    public Coordinate(int r, int c) {
        this(r, c, false);
    } // end of Coordinate()


    /**
     * Construct coordinate (r,c).
     *
     * @param r Row coordinate
     * @param c Column coordinate
     * @param b Whether coordiante is impassable.
     */
    public Coordinate(int r, int c, boolean b) {
        this.r = r;
        this.c = c;
        this.isImpassable = b;
        this.terrainCost = 1;
        // to ensure gets updated the first time its explored
        // otherwise it has a minPathCost of 0, aka would never get updated
        // as future paths will always have a greater score
        this.minPathCost = Integer.MAX_VALUE;
    } // end of Coordinate()


    /**
     * Default constructor.
     */
    public Coordinate() {
        this(0, 0);
    } // end of Coordinate()


    //
    // Getters and Setters
    //

    public int getRow() { return r; }

    public int getColumn() { return c; }


    public void setImpassable(boolean impassable) {
        isImpassable = impassable;
    }

    public boolean getImpassable() { return isImpassable; }

    public void setTerrainCost(int cost) {
        terrainCost = cost;
    }

    public int getTerrainCost() { return terrainCost; }


    //
    // Override equals(), hashCode() and toString()
    //

    @Override
    public boolean equals(Object o) {
      
    	if (this == o)
          	return true;
        if (o == null || getClass() != o.getClass()) 
        	return false;

        Coordinate coord = (Coordinate) o;
        return r == coord.getRow() 
        		&& c == coord.getColumn();
    } // end of equals()


    @Override
    public int hashCode() {
        return Objects.hash(r,c);
    } // end of hashCode()


    @Override
    public String toString() {
        return "(" + r + "," + c + "), " + isImpassable + ", " + terrainCost;
    } // end of toString()
    
    
    
    
    
    
    
    
    
//    @Override
//	public int compareTo(Coordinate co) 
//	{
////		// to avoid set NOT adding just because minPathCost is same
////		if (equals(co))	
////			return 0;
//    	if (minPathCost == co.minPathCost)
//    		return 0;
//		if 		(minPathCost >  co.minPathCost)				
//			return 1;
//		else 											
//			return -1;
//	} 
    
    public Coordinate getLink()
    {
    	return link;
    }
    
    public void setLink(Coordinate co)
    {
    	int newminPathCost = co.minPathCost + terrainCost;
		if (newminPathCost <= minPathCost)
		{
			// so when goal node reached, a chain of prev nodes formed
			link = co;
			minPathCost = newminPathCost;
		}
    }
    
    // implies the start coordinate, the first coordinate
    // so no other coordinates yet
    public void setStartMinPathCost()
    {
    	minPathCost = terrainCost;
    }
    
    public void reset()
    {
    	minPathCost = Integer.MAX_VALUE;
    	link = null;
    }


    // used to retrieve edge weight between waypoints
	public int getMinPathCost() 
	{
		return minPathCost;
	}
    
    
} // end of class Coordinate
