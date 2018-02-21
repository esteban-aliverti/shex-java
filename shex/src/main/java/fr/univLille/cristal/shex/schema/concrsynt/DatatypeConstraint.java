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
package fr.univLille.cristal.shex.schema.concrsynt;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.datatypes.XMLDatatypeUtil;

import fr.univLille.cristal.shex.graph.TCProperty;

/**
 * 
 * @author Iovka Boneva
 * 10 oct. 2017
 */
public class DatatypeConstraint implements Constraint {
	
	private IRI datatypeIri;
	
	public DatatypeConstraint(IRI datatypeIri) {
		this.datatypeIri = datatypeIri;
	}

	public IRI getDatatypeIri() {
		return datatypeIri;
	}

	@Override
	public boolean contains(Value node) {
		if (! (node instanceof Literal)) return false;
		Literal lnode = (Literal) node;
		if (!(datatypeIri.equals(lnode.getDatatype()))) return false;
		if ((XMLDatatypeUtil.isBuiltInDatatype(lnode.getDatatype()))) {
			return XMLDatatypeUtil.isValidValue(lnode.stringValue(), lnode.getDatatype());
		}

		return true;
	}
	
	@Override
	public String toString() {
		return datatypeIri.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatatypeConstraint other = (DatatypeConstraint) obj;
		return datatypeIri.equals(other.getDatatypeIri());
	}

}