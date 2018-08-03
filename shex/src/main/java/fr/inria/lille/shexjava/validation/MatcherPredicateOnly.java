/*******************************************************************************
 * Copyright (C) 2018 Université de Lille - Inria
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package fr.inria.lille.shexjava.validation;

import org.apache.commons.rdf.api.RDFTerm;
import org.apache.commons.rdf.api.Triple;

import fr.inria.lille.shexjava.schema.abstrsynt.TripleConstraint;

/** Match only the predicate.
 * 
 * @author Iovka Boneva
 * @deprecated Use {@link ValidationUtils#getPredicateOnlyMatcher()} instead
 * 10 oct. 2017
 */
@Deprecated
public class MatcherPredicateOnly extends Matcher {
	
	@Override
	public boolean apply(RDFTerm focusNode, Triple triple, TripleConstraint tc) {
		if (tc.getProperty().isForward() && triple.getSubject().ntriplesString().equals(focusNode.ntriplesString())) {
			return tc.getProperty().getIri().ntriplesString().equals(triple.getPredicate().ntriplesString());
		}
		if (!tc.getProperty().isForward() && triple.getObject().ntriplesString().equals(focusNode.ntriplesString()))
			return tc.getProperty().getIri().ntriplesString().equals(triple.getPredicate().ntriplesString());
		return false;
	}


}
