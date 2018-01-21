import java.util.*;

/**
 * <b>FordFulkerson algorithm</b> is used to compute the max flow of a network.
 * This implementation of F-F algorithm exposes a method to
 * {@link #calculateMaxFlow(SimpleGraph)} which returns the max flow of the
 * input graph.
 * 
 * 
 */

public class FordFulkerson {

	/*
	 * bfs method finds the augmented path
	 * @param parent
	 * 			To find the parent of vertices
	 * @param rg
	 * 			A residual graph
	 * @param rgVertices
	 * 			HashTable storing residual parameters
	 * @return whether there is an augmented path
	 */

	public boolean bfs(HashMap<Vertex, VertexEdge> parent, SimpleGraph rg, Hashtable rgVertices){
		Vertex t;
		Set<String> vSet = new LinkedHashSet<String>();


		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.add(rg.aVertex());
		vSet.add((String) rg.aVertex().getName());

		while(queue.size() != 0){
			Vertex u = queue.poll();

			Iterator j;
			for(j = rg.incidentEdges(u); j.hasNext();){
				Edge e;
				Vertex v;
				e = (Edge) j.next();
				v = rg.opposite(u, e);

				String uName = (String) u.getName();
				String vName = (String) v.getName();

				if ((Double)e.getData() <= 0) {
					continue;
				}
				if(!(vSet.contains(v.getName()))){
					queue.add(v);
					vSet.add((String) v.getName());
					VertexEdge ve = new VertexEdge(u, e);
					if (vName == "v9" || vName == "v11" || uName == "v9" || uName == "v11") {
						System.out.println(uName + "-->" + vName + " " + (Double) e.getData());
					}
					parent.put(v, ve);

				}
			}
		}
		return (vSet.contains("t") == true);
	}

	/**
	 * Gets edge connecting v and w
	 * @param sg 
	 * 			Simple input graph
	 * @param v
	 * 			One vertex
	 * @param w
	 * 			Second vertex
	 * @return 
	 * 			Edge between the vertices
	 */
	Edge edgeBetweenVertices(SimpleGraph sg, Vertex v, Vertex w){
		Iterator j;
		for(j = sg.incidentEdges(w); j.hasNext();){
			Edge e = (Edge)j.next();
			if (e.getFirstEndpoint().getName() == v.getName()) {
				return e;
			}
		}
		return null;
	}


	/*
	 * Find the maximum flow in the graph
	 * @param sg
	 * 			Simple input graph
	 * @param residualGraph
	 * 			Simple residual graph
	 * @parama sgVertices
	 * 			HashTable containing simple input graph parameters
	 * @param rgVertices
	 * 			HashTable containing residual input graph parameters
	 * @return The maximum flow in a graph
	 */
	public int fordFulkerson(SimpleGraph sg, SimpleGraph residualGraph, Hashtable sgVertices, Hashtable rgVertices){
		long startTime = System.currentTimeMillis();
		HashMap<Vertex, VertexEdge> parent = new HashMap<Vertex, VertexEdge>();
		int max_flow = 0;

		while(bfs(parent, residualGraph, rgVertices)){
			double path_flow = Integer.MAX_VALUE;

			Vertex end = (Vertex) rgVertices.get("t");
			Vertex start = (Vertex) rgVertices.get("s");
			// iterate from end to start and find the minimum
			// capacity of edges along the BFS path
			while(end.getName() != start.getName()){
				VertexEdge parentVE = parent.get(end);

				Vertex parentVertex = parentVE.getVertex();
				Edge edge = parentVE.getEdge();
				if (edge == null) {
					System.out.println("no path found between " + parentVertex.getName() + " and " + end.getName());
					System.out.println("vertext edge: " + parentVertex.getName() + " " 
							+ (Double) parentVE.getEdge().getData() + " " + end.getName());
				}
				path_flow = Math.min(path_flow, (double) edge.getData());

				end = parentVertex;
			}

			//			System.out.println("path flow: " + path_flow);

			end = (Vertex) rgVertices.get("t");
			start = (Vertex) rgVertices.get("s");
			Vertex prev, prevRes, endRes;

			while(end.getName() != start.getName()){
				String endName = (String) end.getName();
				VertexEdge parentVE = parent.get(end);

				Vertex parentVertex = parentVE.getVertex();
				Edge edge = parentVE.getEdge();
				Edge rgReverseEdge = edgeBetweenVertices(residualGraph, end, parentVertex);

				edge.setData((Double) edge.getData() - path_flow);
				if (rgReverseEdge == null) {
					rgReverseEdge = new Edge(end, parentVertex, path_flow, edge.getName());
				} else {
					rgReverseEdge.setData((Double)rgReverseEdge.getData() + path_flow);
				}

				end = parentVE.getVertex();
			}

			parent = new HashMap<Vertex, VertexEdge>();
			max_flow += path_flow;

		}
		System.out.println("Total time taken to calculate max flow using Ford Fulkerson Algorithm: " + (System.currentTimeMillis() - startTime) + " milliseconds");
		return max_flow;
	}
}
