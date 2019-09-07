import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;

public class ToJSON {
	//private CompilationUnit cu;
	
	public ToJSON(CompilationUnit cu) {
		//this.cu = cu;
	}
	
	public ToJSON() {
		// TODO Auto-generated constructor stub
	}

	public void parseCU(JSONObject head, List<Node> list) {
		Iterator<Node> iter = list.iterator();
		while(iter.hasNext()) {
			Node node = iter.next();
			if(node instanceof PackageDeclaration) {
				processPackage(head, node);
			}else if(node instanceof ImportDeclaration) {
				processImport(head, node);
			}else if(node instanceof ClassOrInterfaceDeclaration) {
				processClassOrInterface(head, node);
			}else if(node instanceof FieldDeclaration) {
				processField(head, node);
			}else if(node instanceof VariableDeclarator) {
				processVariable(head, node);
			}else if(node instanceof ConstructorDeclaration) {
				processConstructor(head, node);
			}else if(node instanceof MethodDeclaration) {
				processMethod(head, node);
			}else {
				if(node instanceof SimpleName) continue;
				System.out.println("Warning !! ==================================");
				System.out.println(node.getClass().getName());
			}
		}
	}
	
	private void processPackage(JSONObject head, Node node) {
		PackageDeclaration pd = (PackageDeclaration) node;
		JSONObject packageObj = new JSONObject();
		JSONArray name = new JSONArray();
		name.put(pd.getName().toString());
		name.put(pd.getName().getBegin());
		name.put(pd.getName().getEnd());
		packageObj.put("name", name);
		packageObj.put("begin", pd.getBegin().get());
		packageObj.put("end", pd.getEnd().get());
		head.put("package", packageObj);
	}
	
	private void processImport(JSONObject head, Node node) {
		ImportDeclaration id = (ImportDeclaration) node;
		JSONObject importsObj;
		if(head.has("imports")) {
			importsObj = head.getJSONObject("imports");
			
			JSONArray importsList = importsObj.getJSONArray("list");
			importsList.put(id.getName().toString());
			importsObj.put("end", id.getEnd().get());
		}else {
			importsObj = new JSONObject();
			head.put("imports", importsObj);
			
			JSONArray importsList = new JSONArray();
			
			JSONArray name = new JSONArray();
			name.put(id.getName());
			name.put(id.getName().getBegin());
			name.put(id.getName().getEnd());
			importsList.put(name);
			
			importsObj.put("list", importsList);

			importsObj.put("begin", id.getBegin().get());
			importsObj.put("end", id.getEnd().get());
			
		}
	}
	
	private void processClassOrInterface(JSONObject head, Node node) {
		ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) node;
		JSONObject classesObj;
		if(head.has("classes")) {
			classesObj = head.getJSONObject("classes");
		}else {
			classesObj = new JSONObject();
			head.put("classes", classesObj);
		}
		JSONObject coiObj = new JSONObject();
		classesObj.put(coid.getName().toString(), coiObj);
		
		coiObj.put("begin", coid.getBegin().get());
		coiObj.put("end", coid.getEnd().get());
		coiObj.put("isInterface", String.valueOf(coid.isInterface()));
		coiObj.put("modifiers", coid.getModifiers());
		coiObj.put("extendedType", coid.getExtendedTypes().toString());
		coiObj.put("implementedTypes", coid.getImplementedTypes().toString());
		parseCU(coiObj, coid.getChildNodes());
	}
	
	private void processField(JSONObject head, Node node) {
		FieldDeclaration fd = (FieldDeclaration) node;
		JSONObject fieldsObj;
		if(head.has("fields")) {
			fieldsObj = head.getJSONObject("fields");
		}else {
			fieldsObj = new JSONObject();
			head.put("fields", fieldsObj);
		}
		parseCU(fieldsObj, fd.getChildNodes());
	}
	
	private void processVariable(JSONObject head, Node node) {
		VariableDeclarator vd = (VariableDeclarator) node;
		JSONObject variableObj = new JSONObject();
		head.put(vd.getName().toString(), variableObj);
		
		variableObj.put("modifiers", ((FieldDeclaration)vd.getParentNode().get()).getModifiers());
		variableObj.put("type", vd.getType());
	}
	
	private void processConstructor(JSONObject head, Node node) {
		ConstructorDeclaration cd = (ConstructorDeclaration) node;
		JSONArray constructorsArray;
		if(head.has("constructors")) {
			constructorsArray = head.getJSONArray("constructors");
		}else {
			constructorsArray = new JSONArray();
			head.put("constructors", constructorsArray);
		}
		
		JSONObject constructorObj = new JSONObject();
		constructorsArray.put(constructorObj);
		
		constructorObj.put("modifiers", cd.getModifiers());
		constructorObj.put("parameters", cd.getParameters().toString());
		constructorObj.put("begin", cd.getBegin().get());
		constructorObj.put("end", cd.getEnd().get());
		
	}
	
	private void processMethod(JSONObject head, Node node) {
		MethodDeclaration md = (MethodDeclaration) node;
		JSONObject methodsObj;
		if(head.has("methods")) {
			methodsObj = head.getJSONObject("methods");
		}else {
			methodsObj = new JSONObject();
			head.put("methods", methodsObj);
		}
		
		String methodName = md.getNameAsString();
		JSONArray methodArray;
		if(methodsObj.has(methodName)) {
			methodArray = methodsObj.getJSONArray(methodName);
		}else {
			methodArray = new JSONArray();
			methodsObj.put(methodName, methodArray);
		}
		
		JSONObject detailofMethod = new JSONObject();
		detailofMethod.put("begin", md.getBegin().get());
		detailofMethod.put("end", md.getEnd().get());
		detailofMethod.put("typeParameter", md.getTypeParameters().toString());
		detailofMethod.put("modifiers", md.getModifiers());
		detailofMethod.put("parameters", md.getParameters().toString());
		
		methodArray.put(detailofMethod);
	}
	
}
