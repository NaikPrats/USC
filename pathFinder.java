import java.io.*;
import java.util.*;
import java.io.FileWriter;

class Node {

	public String name;
	public int pathCost;
	public int timestamp;
	public Node parent;
	public int sundayTraffic;
	public int heuristicCost;
	public ArrayList<Edge> AdjacencyList = new ArrayList<Edge>();

	public Node(String name) {
		super();
		this.name = name;
		this.timestamp = 0;
	}

	public void setTimeStamp(int timeStamp) {
		this.timestamp = timeStamp;
	}

	@Override
	public String toString() {
		return name;
	}

}

class Edge {
	String source, destination;
	int weight;

	public Edge(String source, String destination, int weight) {
		super();
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	@Override
	public String toString() {
		String name = "Source : " + this.source + " Destination :" + this.destination + " Weight : " + this.weight;
		return name;
	}
}

class Graph {

	HashMap<String, Node> nodeList = new HashMap<String, Node>();

	public void addNode(String name) {
		Node e = new Node(name);
		nodeList.put(name, e);
	}

	public void addEdge(String s, String d, int w) {
		Edge e1 = new Edge(s, d, w);
		nodeList.get(s).AdjacencyList.add(e1); // If it were an undirected graph
												// add the edge to source as
												// well as destination node
	}

	public boolean isNeighbour(Node node1, Node node2) {
		int i;

		if (node1.AdjacencyList.isEmpty()) {
			System.out.println("first if case");
			return false;
		}
		for (i = 0; i < node1.AdjacencyList.size(); i++) {
			if ((node1.AdjacencyList.get(i).destination).equals(node2.name)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * This function runs BFS algorithm on given graph and returns shortst path
	 * from startState to goalState.
	 */

	public ArrayList<String> bfs(String startState, String goalState) {
		
		HashMap<Node, Boolean> visited = new HashMap<Node, Boolean>();
		ArrayList<String> shortestPath = new ArrayList<String>();
		HashMap<String, String> parent = new HashMap<String, String>();

		boolean goalFound = false;

		if (startState.equals(goalState)) {
			shortestPath.add(0, startState + " 0");
			System.out.println(startState + " 0");
			return shortestPath;
		}

		Node startNode = nodeList.get(startState);
		parent.put(startState, null);

		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(startNode);

		while (!queue.isEmpty() && !goalFound) {
			Node queuedNode = queue.remove(0);
			visited.put(queuedNode, true);

			for (int i = 0; i < queuedNode.AdjacencyList.size(); i++) {
				Edge e1 = queuedNode.AdjacencyList.get(i);

				Node iterator = nodeList.get(e1.destination);

				if (visited.get(iterator) == null) {
					parent.put(iterator.name, queuedNode.name);
					if ((iterator.name).equals(goalState)) {
						goalFound = true;
						break;
					}
					visited.put(iterator, true);
					queue.add(iterator);
				}
			}
		}

		// Print shortest path and if there are two paths then use the once
		// which is read first in input file.

		String currentNode = goalState;
		shortestPath.add(goalState);
		while (parent.get(currentNode) != null) {
			currentNode = parent.get(currentNode);
			shortestPath.add(currentNode);
		}

		Collections.reverse(shortestPath);

		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < shortestPath.size(); i++) {
			String temp = shortestPath.get(i) + " " + i;
			result.add(temp);
		}

		return result;
	}

	/*
	 * This function runs DFS algorithm on given graph and returns a path from
	 * startState to goalState.
	 */
	public ArrayList<String> dfs(String startState, String goalState) {

		Node source = nodeList.get(startState);
		Node destination = nodeList.get(goalState);
		Stack<Node> main = new Stack<Node>();
		Stack<Node> secondary = new Stack<Node>();
		HashMap<Node, Node> parent = new HashMap<>();
		HashMap<Node, Boolean> visited = new HashMap<>();

		main.push(source);
		visited.put(source, true);
		parent.put(source, null);

		while (source != destination) {
			Node element = main.pop();
			visited.put(element, true);

			int noOfChilds = element.AdjacencyList.size();

			for (int i = 0; i < noOfChilds; i++) {

				Edge e1 = element.AdjacencyList.get(i);
				Node iterator = nodeList.get(e1.destination);
				if (!visited.containsKey(iterator)) {
					secondary.push(iterator);
					visited.put(iterator, true);
					parent.put(iterator, element);
				}
			}

			while (!secondary.isEmpty()) {
				Node temp = secondary.pop();
				main.push(temp);
			}

			source = main.peek();
		}

		String currentNode = goalState;
		ArrayList<String> path = new ArrayList<>();
		path.add(goalState);

		while (parent.get(nodeList.get(currentNode)) != null) {
			currentNode = parent.get(nodeList.get(currentNode)).name;
			path.add(currentNode);
		}

		Collections.reverse(path);

		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < path.size(); i++) {
			String temp = path.get(i) + " " + i;
			result.add(temp);
		}

		return result;
	}

	public static ArrayList<String> printPath(Node target) {
		ArrayList<String> path = new ArrayList<String>();
		for (Node node = target; node != null; node = node.parent) {
			path.add(node.name);
		}

		Collections.reverse(path);

		return path;

	}
	/*
	 * This function runs UCS algorithm on given graph and returns a shortest
	 * path from startState to goalState.
	 */

	public ArrayList<String> ucs(String startState, String goalState) {

		Node source = nodeList.get(startState);
		Node destination = nodeList.get(goalState);
		source.pathCost = 0;
		int nodeCounter = 0;

		PriorityQueue<Node> queue = new PriorityQueue<Node>(200, new Comparator<Node>() {

			// override compare method
			public int compare(Node i, Node j) {
				if (i.pathCost > j.pathCost) {
					return 1;
				} else if (i.pathCost < j.pathCost) {
					return -1;
				} else {
					return i.timestamp - j.timestamp;
				}
			}
		});
		queue.add(source);
		HashSet<Node> explored = new HashSet<Node>();
		boolean found = false;

		do {
			Node current = queue.poll();
			explored.add(current);

			if ((current.name).equals(goalState)) {
				found = true;
				break;// required ???
			}
			for (Edge e : current.AdjacencyList) {
				Node child = nodeList.get(e.destination);
				int cost = e.weight;
				// child.pathCost = current.pathCost + cost;

				if (!explored.contains(child) && !queue.contains(child)) {
					child.parent = current;// check where to update this ???
					child.pathCost = current.pathCost + cost;
					child.setTimeStamp(nodeCounter);
					nodeCounter++;
					queue.add(child);
				} else if ((queue.contains(child)) && (child.pathCost > (current.pathCost + cost))) {
					child.parent = current;
					child.pathCost = current.pathCost + cost;
					queue.remove(child);
					child.setTimeStamp(nodeCounter);
					nodeCounter++;
					queue.add(child);
				}
			}
		} while (!queue.isEmpty());

		ArrayList<String> path = printPath(destination);
		ArrayList<String> result = new ArrayList<String>();

		for (String e : path) {
			String temp = e + " " + nodeList.get(e).pathCost;
			result.add(temp);
		}

		return result;
	}

	public ArrayList<String> astar(String startState, String goalState) {

		Node source = nodeList.get(startState);
		Node destination = nodeList.get(goalState);
		source.pathCost = 0;
		int nodeCounter = 0;
		
		PriorityQueue<Node> queue = new PriorityQueue<Node>(200, new Comparator<Node>() {

			// override compare method
			public int compare(Node i, Node j) {
				if (i.heuristicCost > j.heuristicCost) {
					return 1;
				} else if (i.heuristicCost < j.heuristicCost) {
					return -1;
				} else {
					return i.timestamp - j.timestamp;
				}
			}
		});
		queue.add(source);
		HashSet<Node> explored = new HashSet<Node>();
		boolean found = false;

		do {
			Node current = queue.poll();
			explored.add(current);

			if ((current.name).equals(goalState)) {
				found = true;
				break;// required ???
			}
			for (Edge e : current.AdjacencyList) {
				Node child = nodeList.get(e.destination);
				int heuristicOfChild = child.sundayTraffic;
				int cost = e.weight;
				// child.pathCost = current.pathCost + cost;

				if (!explored.contains(child) && !queue.contains(child)) {
					child.parent = current;
					child.pathCost = current.pathCost + cost;
					child.heuristicCost = child.pathCost + heuristicOfChild;
					child.setTimeStamp(nodeCounter);
					nodeCounter++;
					queue.add(child);
				} else if ((queue.contains(child) || explored.contains(child))
						&& (child.heuristicCost > (current.pathCost + cost + heuristicOfChild))) {
					child.parent = current;
					child.pathCost = current.pathCost + cost;
					child.heuristicCost = child.pathCost + heuristicOfChild;
					queue.remove(child);
					child.setTimeStamp(nodeCounter);
					nodeCounter++;
					queue.add(child);
				}
			}
		} while (!queue.isEmpty());

		ArrayList<String> path = printPath(destination);
		ArrayList<String> result = new ArrayList<String>();

		for (String e : path) {
			String temp = e + " " + nodeList.get(e).pathCost;
			result.add(temp);
		}

		return result;

	}

}

public class pathFinder {

	public static void testEdge(ArrayList<Edge> AdjacencyList) {
		if (AdjacencyList.isEmpty()) {
			System.out.println("There is no edge to this node");
		}

		for (Edge e : AdjacencyList) {
			System.out.println(e);
		}
	}

	public static void testNumberofNodes(HashMap<String, Node> nodeList) {
		System.out.println("Number of distinct Nodes :" + nodeList.size());
	}

	public static ArrayList<String> runAlgorithm(Graph g, String algo, String startState, String goalState) {
		ArrayList<String> path = new ArrayList<String>();
		switch (algo) {
		case "BFS":
			path = g.bfs(startState, goalState);
			break;
		case "DFS":
			path = g.dfs(startState, goalState);
			break;
		case "UCS":
			path = g.ucs(startState, goalState);
			break;
		case "A*":
			path = g.astar(startState, goalState);
			break;
		}

		return path;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String fileName = "input.txt";
		String output = "output.txt";
		FileWriter writer = new FileWriter(output);

		int i = 0, numofTrafficLines = 0, numofsundayTrafficLines = 0;
		int weight = 0;
		Graph g = new Graph();
		String algoName = null;
		String startState = null;
		String goalState = null;
		String numofLines = null;
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			algoName = bufferedReader.readLine();
			startState = bufferedReader.readLine();
			goalState = bufferedReader.readLine();
			numofLines = bufferedReader.readLine();

			try {
				numofTrafficLines = Integer.parseInt(numofLines);
			} catch (NumberFormatException e) {

			}

			for (i = 0; i < numofTrafficLines; i++) {
				String temp = bufferedReader.readLine();
				String[] tokens = temp.split("\\s+");

				// token[0] = start
				// token[1] = destination
				// token[2] = weight
				if (!g.nodeList.containsKey(tokens[0])) {
					g.addNode(tokens[0]);
				}

				if (!g.nodeList.containsKey(tokens[1])) {
					g.addNode(tokens[1]);
				}

				// add edge from source to destination with weight.
				weight = Integer.parseInt(tokens[2]);

				g.addEdge(tokens[0], tokens[1], weight);
			}

			String numofSundayLines = bufferedReader.readLine();

			try {
				numofsundayTrafficLines = Integer.parseInt(numofSundayLines);
			} catch (NumberFormatException e) {

			}

			for (i = 0; i < numofsundayTrafficLines; i++) {
				String temp = bufferedReader.readLine();
				String[] tokens = temp.split("\\s+");
				g.nodeList.get(tokens[0]).sundayTraffic = Integer.parseInt(tokens[1]);
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}

		ArrayList<String> path = runAlgorithm(g, algoName, startState, goalState);

		for (String str : path) {
			writer.write(str);
			writer.write("\n");
		}
		writer.close();

	}

}
