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
package fr.inria.lille.shexjava.schema.abstrsynt;

import java.util.List;
import java.util.Map;

import fr.inria.lille.shexjava.schema.analysis.ShapeExpressionVisitor;
import fr.inria.lille.shexjava.util.CollectionToString;

/**
 * 
 * @author Iovka Boneva
 * @author Antonin Durey
 * @author Jérémie Dusart
 */
public class ShapeEachOf extends AbstractNaryShapeExpr{


	public ShapeEachOf(List<ShapeExpr> subExpressions) {
		super(subExpressions);
	}
	
	@Override
	public String toPrettyString(Map<String,String> prefixes) {
		return CollectionToString.collectionToString(getSubExpressions(), " SEachOf ", "(", ")");
	}

	@Override
	public <ResultType> void accept(ShapeExpressionVisitor<ResultType> visitor, Object... arguments) {
		visitor.visitShapeEachOf(this, arguments);
	}
	
}
