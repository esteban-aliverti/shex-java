package fr.inria.lille.shexjava.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.rdf.api.Graph;
import org.apache.commons.rdf.api.RDFTerm;
import org.apache.commons.rdf.api.Triple;

import fr.inria.lille.shexjava.schema.abstrsynt.ShapeEachOf;
import fr.inria.lille.shexjava.schema.abstrsynt.ShapeExpr;
import fr.inria.lille.shexjava.schema.abstrsynt.TripleConstraint;

public class EachOfIterator {
	private ShapeEachOf shapeExpr;
	private RDFTerm node;
	private Graph graph;
	
	private ArrayList<Triple> keys;
	private ArrayList<List<ShapeExpr>> matches;
	/** Used for the iteration: sizes[i] = matches.get(i).getSize() */
	private int[] sizes;
	/** Used for the iteration:  <= currentIndexes[i] < sizes[i] */
	private int[] currentIndexes;

	
	public EachOfIterator(ShapeEachOf shapeExpr, RDFTerm node, Graph graph) {
		super();
		this.shapeExpr = shapeExpr;
		this.node = node;
		this.graph = graph;
		init();
	}
	
	
	public void init() {
		Map<Triple,List<ShapeExpr>> combi = new HashMap<>();
		DynamicCollectorOfTripleConstraints collector = new DynamicCollectorOfTripleConstraints();
		
		for (ShapeExpr expr:shapeExpr.getSubExpressions()) {
			List<TripleConstraint> constraints = collector.getTCs(expr);
			List<Triple> neighbourhood = ValidationUtils.getMatchableNeighbourhood(graph, node, constraints, false);
			for (Triple tr:neighbourhood) {
				if (!combi.containsKey(tr))
					combi.put(tr, Collections.emptyList());
				combi.get(tr).add(expr);
			}
		}
		
		keys = new ArrayList<>(combi.keySet());
		matches = new ArrayList<>();
		for (Triple tr:keys)
			matches.add(matches.size(), combi.get(tr));
		
		currentIndexes = new int[matches.size()+1]; 
		sizes = new int[matches.size()+1];
		for (int i = 0; i < currentIndexes.length-1; i++) {
			currentIndexes[i+1] = 0;
			sizes[i+1] = matches.get(i).size();
		}
		currentIndexes[0] = 0;
		sizes[0] = 1;
	}

	
	public boolean hasNext() {
		for (int i = 0; i < currentIndexes.length; i++)
			if (currentIndexes[i] >= sizes[i])
				return false;
		return true;
	}
	
	
	private void goToNext () {
		int i = currentIndexes.length - 1;
		boolean incrementsToZero = true;
		while (i > 0 && incrementsToZero) {
			currentIndexes[i] = (currentIndexes[i]+1) % sizes[i];
			incrementsToZero = currentIndexes[i]==0;
			i--;
		}
		if (i == 0 && incrementsToZero)
			currentIndexes[0]++;
	}
	
	
	public Map<ShapeExpr,List<Triple>> next() {
		if (! hasNext())
			throw new NoSuchElementException();
		
		Map<ShapeExpr,List<Triple>> result = new HashMap<ShapeExpr,List<Triple>>();	
		for (ShapeExpr expr:shapeExpr.getSubExpressions())
			result.put(expr,Collections.emptyList());
				
		for (int i = 1; i < currentIndexes.length; i++) {
			ShapeExpr key = matches.get(i-1).get(currentIndexes[i]);
			result.get(key).add(keys.get(i-1));
		}
		
		goToNext();
		
		return result;
	}
	
}
