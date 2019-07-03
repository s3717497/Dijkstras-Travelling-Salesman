package map;

import java.util.*;
import map.Coordinate;

/**
 * Class of a map (for path finding).
 * It is a grid representation.
 */
public class PathMap
{
    /**
     * map properties
     */
    // number of rows
    public int sizeR;
    // number of columns
    public int sizeC;
    // 2D grid of cells
    public Coordinate cells[][] = null;
    // List of origin cells/coordinates
    public List<Coordinate> originCells;
    // list of destination cells/coordinates
    public List<Coordinate> destCells;
    // list of waypoint cells/coordinates
    public List<Coordinate> waypointCells;
    // whether to visualise or not
    public boolean isVisu = true;


    /**
     * Initialise the map.
     *
     * @param rowNum Number of rows.
     * @param colNum Number of columns.
     * @param oriCells List of origin coordinates.
     * @param desCells List of destination coordinates.
     * @param impassableCells List of impassable coordinates.
     * @param terrainCells Map of terrain coordinates and their costs.
     * @param waypointCells List of waypoint coordinates.
     */
    public void initMap(int rowNum, int colNum, List<Coordinate> oriCells, List<Coordinate> desCells, 
    Set<Coordinate> impassableCells, Map<Coordinate, Integer> terrainCells, List<Coordinate> waypointCells)
    {
        // initialise parameters
        sizeR = rowNum;
        sizeC = colNum;
        originCells = oriCells;
        destCells = desCells;
        this.waypointCells = waypointCells;

        cells = new Coordinate[sizeR][sizeC];

        // construct the coordinates in the grid and also update inforamtion about impassable
        // and terrain costs.
        for (int i = 0; i < sizeR; i++) {
            for (int j = 0; j < sizeC; j++) {
                Coordinate coord = new Coordinate(i, j);
                // add impassable cells
                if (impassableCells.contains(coord)) {
                    coord.setImpassable(true);
                }
                // add terrain information
                // should not be both
                if (terrainCells.containsKey(coord)) {
                    int cost = terrainCells.get(coord).intValue();
                    coord.setTerrainCost(cost);
                }

                cells[i][j] = coord;
            }
        }
        // make sure map.origins/dests/waypoints are passed by reference to map.cells
        originCells = new LinkedList<>();
        destCells = new LinkedList<>();
        this.waypointCells = new LinkedList<>();
        for (Coordinate origin : oriCells)	
        	originCells.add(cells[origin.getRow()][origin.getColumn()]);
        for (Coordinate dest : desCells)	
        	destCells.add(cells[dest.getRow()][dest.getColumn()]);
        for (Coordinate wp : waypointCells)	
        	this.waypointCells.add(cells[wp.getRow()][wp.getColumn()]);
        
    } // end of initMap()

    public void resetCells()
    {
    	for (Coordinate[] row : cells)
    	for (Coordinate cell : row)
    		cell.reset();
    }

    //
    // Auxiliary functions
    //


    /**
     * Check whether coordinate (r, c) is in the map.
     *
     * @param r Row coordinate
     * @param c Column coordinate
     * @return True if in the maze. Otherwise false.
     */
    public boolean isIn(int r, int c) {
        return r >= 0 && r < sizeR && c >= 0 && c < sizeC;
    } // end of isIn()


    /**
     * Check whether the coordinate is in the map.
     *
     * @param coord The coordinate being checked.
     * @return True if in the map. Otherwise false.
     */
    public boolean isIn(Coordinate coord) {
        if (coord == null)
            return false;
        return isIn(coord.getRow(), coord.getColumn());
    } // end of isIn()


    /**
     * Check if a coordinate (r,c) is passable/can be traversed.
     */
    public boolean isPassable(int r, int c) {
        return isIn(r, c) && !cells[r][c].getImpassable();
    } // end of isPassable()


} // end of class PathMap
