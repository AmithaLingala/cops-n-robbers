package com.copsrobbers.game.algorithm;

import com.copsrobbers.game.managers.MapManager;

import java.util.ArrayList;
import java.util.LinkedList;

public class GraphManager {
    private final int mapWidth;
    private final int mapHeight;
    private final MapManager mapManager;

    /**
     * Class to manage Graph functionalities
     */
    public GraphManager() {
        mapManager = MapManager.obtain();
        mapWidth = mapManager.getRowTileCount();
        mapHeight = mapManager.getColumnTileCount();
    }

    /**
     * Breadth First Search Algorithm
     * @param adj Graph as adjacency list
     * @param src Source node
     * @param dest Destination node
     * @param v Number of vertices
     * @param pred Predecessor array stores predecessor of vertex
     * @param dist Distance array stores distance of i
     * @return returns true if destination is found else returns false
     */
    private static boolean BFS(ArrayList<ArrayList<Integer>> adj, int src,
                               int dest, int v, int[] pred, int[] dist) {

        LinkedList<Integer> queue = new LinkedList<>();

        boolean[] visited = new boolean[v];
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj.get(u).size(); i++) {
                if (!visited[adj.get(u).get(i)]) {
                    visited[adj.get(u).get(i)] = true;
                    dist[adj.get(u).get(i)] = dist[u] + 1;
                    pred[adj.get(u).get(i)] = u;
                    queue.add(adj.get(u).get(i));
                    if (adj.get(u).get(i) == dest)
                        return true;
                }
            }
        }

        return false;
    }

    /**
     * Generates graph as adjacency list
     * @return returns graph
     */
    public ArrayList<ArrayList<Integer>> generateGraph() {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();

        for (int row = 0; row < mapWidth; row++) {
            for (int col = 0; col < mapHeight; col++) {
                int current = (row * mapHeight) + col;
                graph.add(this.getNeighbours(current));
            }

        }
        return graph;
    }

    /**
     * Method to get all neighbours of the given cell
     * @param pos position of cell
     * @return ArrayList of neighbours
     */

    private ArrayList<Integer> getNeighbours(Integer pos) {
        ArrayList<Integer> neighbours = new ArrayList<>();
        int x = pos / mapHeight;
        int y = pos % mapHeight;

        if (x + 1 < mapWidth && mapManager.canMove(x + 1, y)) {
            neighbours.add(((x + 1) * mapHeight) + y);
        }
        if (x - 1 >= 0 && mapManager.canMove(x - 1, y)) {
            neighbours.add((x - 1) * mapHeight + y);
        }
        if (y + 1 < mapHeight && mapManager.canMove(x, y + 1)) {
            neighbours.add(x * mapHeight + y + 1);
        }
        if (y - 1 >= 0 && mapManager.canMove(x, y - 1)) {
            neighbours.add(x * mapHeight + (y - 1));
        }
        return neighbours;
    }

    /**
     * Method to find shortest distance from source to destination
     * @param adj Graph as adjacency list
     * @param source Source node
     * @param dest Destination node
     * @return shortest path from source to destination
     */

    public LinkedList<Integer> findShortestDistance(
            ArrayList<ArrayList<Integer>> adj,
            int source, int dest) {

        int v = adj.size();
        int[] pred = new int[v];
        int[] dist = new int[v];
        LinkedList<Integer> path = new LinkedList<>();

        if (!BFS(adj, source, dest, v, pred, dist)) {
            return path;
        }

        int crawl = dest;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }
        return path;
    }
}

