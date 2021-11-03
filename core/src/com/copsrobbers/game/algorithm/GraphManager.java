package com.copsrobbers.game.algorithm;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.copsrobbers.game.Utils;

import java.util.ArrayList;
import java.util.LinkedList;

public class GraphManager {
    private final TiledMapTileLayer walls;
    private final TiledMapTileLayer background;
    private final int tileWidth;
    private final int tileHeight;
    private final int mapWidth;
    private final int mapHeight;


    public GraphManager() {
        TiledMap map = Utils.obtain().getMap();
        walls = (TiledMapTileLayer) map.getLayers().get("walls");
        background = (TiledMapTileLayer) map.getLayers().get("background");
        tileWidth = walls.getTileWidth();
        tileHeight = walls.getTileHeight();
        mapWidth = background.getWidth()/tileWidth;
        mapHeight = background.getHeight()/tileHeight;
        }

    public ArrayList<ArrayList<Integer>> generateGraph() {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();

        for (int row = 0; row < mapWidth; row++) {
            for (int col = 0; col < mapWidth; col++) {
                int current = (row * mapWidth) + col;
                graph.add(this.getNeighbours(current));
            }

        }
        return graph;
    }

    private ArrayList<Integer> getNeighbours(Integer pos) {
        ArrayList<Integer> neighbours = new ArrayList<>();
            int x = pos/ mapWidth;
            int y = pos% mapWidth;

        if(x+1< mapWidth && walls.getCell(x+1,y)==null){
            neighbours.add(((x + 1) * mapWidth) + y);
        }
        if(x-1>=0 && walls.getCell(x-1,y)==null){
            neighbours.add((x - 1) * mapWidth + y);
        }
        if(y+1<mapHeight && walls.getCell(x,y+1)==null){
            neighbours.add(x * mapWidth + y + 1);
        }
        if(y-1>=0 && walls.getCell(x,y-1)==null){
            neighbours.add(x * mapWidth + (y - 1));
        }
        return neighbours;
    }
    public LinkedList<Integer> printShortestDistance(
            ArrayList<ArrayList<Integer>> adj,
            int source, int dest)
    {
        // predecessor[i] array stores predecessor of
        // i and distance array stores distance of i
        // from s

        int v = adj.size();
        int pred[] = new int[v];
        int dist[] = new int[v];
        LinkedList<Integer> path = new LinkedList<Integer>();

        if (BFS(adj, source, dest, v, pred, dist) == false) {
            System.out.println("Given source and destination" +
                    "are not connected");
            return path;
        }

        // LinkedList to store path

        int crawl = dest;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }

        // Print distance
        System.out.println("Shortest path length is: " + dist[dest]);

        // Print path
        System.out.println("Path is ::");
//        for (int i = path.size() - 1; i >= 0; i--) {
//          //  System.out.print(path.get(i)/(this.mapWidth/this.tileHeight) +" "+ path.get(i)%(this.mapWidth/this.tileHeight) + ", ");
//        }
        return path;
    }
    private static boolean BFS(ArrayList<ArrayList<Integer>> adj, int src,
                               int dest, int v, int pred[], int dist[])
    {
        // a queue to maintain queue of vertices whose
        // adjacency list is to be scanned as per normal
        // BFS algorithm using LinkedList of Integer type
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // boolean array visited[] which stores the
        // information whether ith vertex is reached
        // at least once in the Breadth first search
        boolean visited[] = new boolean[v];

        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        // now source is first to be visited and
        // distance from source to itself should be 0
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        // bfs Algorithm
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj.get(u).size(); i++) {
                if (visited[adj.get(u).get(i)] == false) {
                    visited[adj.get(u).get(i)] = true;
                    dist[adj.get(u).get(i)] = dist[u] + 1;
                    pred[adj.get(u).get(i)] = u;
                    queue.add(adj.get(u).get(i));

                    // stopping condition (when we find
                    // our destination)
                    if (adj.get(u).get(i) == dest)
                        return true;
                }
            }
        }

        return false;
    }
}

