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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.inria.lille.shexjava.schema.Label;
import fr.inria.lille.shexjava.schema.abstrsynt.EachOf;
import fr.inria.lille.shexjava.schema.abstrsynt.EmptyTripleExpression;
import fr.inria.lille.shexjava.schema.abstrsynt.NodeConstraint;
import fr.inria.lille.shexjava.schema.abstrsynt.OneOf;
import fr.inria.lille.shexjava.schema.abstrsynt.RepeatedTripleExpression;
import fr.inria.lille.shexjava.schema.abstrsynt.Shape;
import fr.inria.lille.shexjava.schema.abstrsynt.ShapeAnd;
import fr.inria.lille.shexjava.schema.abstrsynt.ShapeEachOf;
import fr.inria.lille.shexjava.schema.abstrsynt.ShapeExpr;
import fr.inria.lille.shexjava.schema.abstrsynt.ShapeExprRef;
import fr.inria.lille.shexjava.schema.abstrsynt.ShapeNot;
import fr.inria.lille.shexjava.schema.abstrsynt.ShapeOr;
import fr.inria.lille.shexjava.schema.abstrsynt.TripleConstraint;
import fr.inria.lille.shexjava.schema.abstrsynt.TripleExpr;
import fr.inria.lille.shexjava.schema.abstrsynt.TripleExprRef;
import fr.inria.lille.shexjava.schema.analysis.ShapeExpressionVisitor;
import fr.inria.lille.shexjava.schema.analysis.TripleExpressionVisitor;

/** Allows to compute the triple constraints that appear in a shape.
 * Memorizes already computed results. 
 * 
 * @author Jérémie Dusart
 */
public class DynamicCollectorOfTripleConstraints {
	private final CollectorTCShapeExpressionVisitor collectorSE = new CollectorTCShapeExpressionVisitor();
	private final CollectorTCTripleExpressionVisitor collectorTE = new CollectorTCTripleExpressionVisitor();
	private Map<Label, List<TripleConstraint>> collectedTCs = new HashMap<>();
	
	public List<TripleConstraint> getTCs (TripleExpr texpr) {
		List<TripleConstraint> result = collectedTCs.get(texpr.getId());
		if (result == null) {
			texpr.accept(collectorTE);
			result = collectorTE.getResult();
		}
		return result;
	}
	
	public List<TripleConstraint> getTCs (ShapeExpr sexpr) {
		List<TripleConstraint> result = collectedTCs.get(sexpr.getId());
		if (result == null) {
			sexpr.accept(collectorSE);
			result = collectorTE.getResult();
		}
		return result;
	}
	

	// definition of the visitors
	
	class CollectorTCShapeExpressionVisitor extends ShapeExpressionVisitor<List<TripleConstraint>> {
		private List<TripleConstraint> result;
		
		private void setResult (ShapeExpr expr, List<TripleConstraint> result) {
			this.result = result;
			collectedTCs.put(expr.getId(), result);
		}
		
		@Override
		public List<TripleConstraint> getResult() {
			return result;
		}

		@Override
		public void visitShape(Shape expr, Object... arguments) {
			expr.getTripleExpression().accept(collectorTE);
			result = collectorTE.getResult();
			setResult(expr, getResult());
		}

		@Override
		public void visitNodeConstraint(NodeConstraint expr, Object... arguments) {
			setResult(expr, Collections.emptyList());
		}

		@Override
		public void visitShapeExprRef(ShapeExprRef shapeRef, Object[] arguments) {
			shapeRef.getShapeDefinition().accept(this);
			setResult(shapeRef, getResult());
		}
	
		@Override		
		public void visitShapeAnd (ShapeAnd expr, Object ... arguments) {
			List<TripleConstraint> newResult = new ArrayList<>();
			for (ShapeExpr subExpr : expr.getSubExpressions()) {
				subExpr.accept(this, arguments);
				newResult.addAll(getResult());
			}
			setResult(expr, newResult);
		}
		
		@Override		
		public void visitShapeOr (ShapeOr expr, Object ... arguments) {
			List<TripleConstraint> newResult = new ArrayList<>();
			for (ShapeExpr subExpr : expr.getSubExpressions()) {
				subExpr.accept(this, arguments);
				newResult.addAll(getResult());
			}
			setResult(expr, newResult);
		}
		
		@Override		
		public void visitShapeEachOf(ShapeEachOf expr, Object... arguments) {
			List<TripleConstraint> newResult = new ArrayList<>();
			for (ShapeExpr subExpr : expr.getSubExpressions()) {
				subExpr.accept(this, arguments);
				newResult.addAll(getResult());
			}
			setResult(expr, newResult);
		}
		
		@Override		
		public void visitShapeNot (ShapeNot expr, Object ...arguments) {
			expr.getSubExpression().accept(this, arguments);
			setResult(expr, getResult());		
		}
	}

	
	class CollectorTCTripleExpressionVisitor extends TripleExpressionVisitor<List<TripleConstraint>> {

		private List<TripleConstraint> result;

		private void setResult (TripleExpr expr, List<TripleConstraint> result) {
			this.result = result;
			collectedTCs.put(expr.getId(), result);
		}
		
		@Override
		public List<TripleConstraint> getResult() {
			return result;
		}

		@Override
		public void visitTripleConstraint(TripleConstraint tc, Object... arguments) {
			setResult(tc, Collections.singletonList(tc));
		}

		@Override
		public void visitEmpty(EmptyTripleExpression expr, Object[] arguments) {
			setResult(expr, Collections.emptyList());
		}

		@Override
		public void visitEachOf(EachOf expr, Object... arguments) {
			List<TripleConstraint> newResult = new ArrayList<>();
			for (TripleExpr subExpr : expr.getSubExpressions()) {
				subExpr.accept(this, arguments);
				newResult.addAll(getResult());
			}
			setResult(expr, newResult);
		}

		@Override
		public void visitOneOf(OneOf expr, Object... arguments) {
			List<TripleConstraint> newResult = new ArrayList<>();
			for (TripleExpr subExpr : expr.getSubExpressions()) {
				subExpr.accept(this, arguments);
				newResult.addAll(getResult());
			}
			setResult(expr, newResult);
		}


		@Override
		public void visitRepeated(RepeatedTripleExpression expr, Object[] arguments) {
			expr.getSubExpression().accept(this, arguments);
			setResult(expr, getResult());
		}

		@Override
		public void visitTripleExprReference(TripleExprRef expr, Object... arguments) {
			expr.getTripleExp().accept(this, arguments);
			setResult(expr, getResult());
		}
	}
	
	

}
