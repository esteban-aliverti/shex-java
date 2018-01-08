/**
Copyright 2017 University of Lille

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/



package fr.univLille.cristal.shex.schema.abstrsynt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.univLille.cristal.shex.schema.ShapeExprLabel;

/**
 * 
 * @author Iovka Boneva
 * @author Antonin Durey
 *
 */
public abstract class AbstractNaryShapeExpr extends ShapeExpr {
	
	private List<ShapeExpr> subExpressions;
		
	public AbstractNaryShapeExpr (List<ShapeExpr> subExpressions) {
		this(null, subExpressions);
	}
	
	public AbstractNaryShapeExpr (ShapeExprLabel id, List<ShapeExpr> subExpressions) {
		super();
		this.subExpressions = new ArrayList<>(subExpressions);
	}
	
	public List<ShapeExpr> getSubExpressions (){
		return Collections.unmodifiableList(this.subExpressions);
	}
	
}