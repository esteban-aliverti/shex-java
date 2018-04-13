package fr.univLille.cristal.shex.schema.FOL.formula;

import java.util.Map;
import java.util.Set;

import org.eclipse.rdf4j.model.Value;

import fr.univLille.cristal.shex.schema.Label;
import fr.univLille.cristal.shex.util.Pair;

public interface Sentence {
	/** This function return 0 if the result is false, 1 if the result is true, 2 if the affectation is incomplete. 
	 * @throws Exception if operator on a literal with a datatype not supported.
	 * 
	 */
	public int evaluate(Map<Variable,Value> affectations,
							Set<Pair<Value, Label>> shapes,
							Set<Pair<Pair<Value,Value>, Label>> triples) throws Exception;
}