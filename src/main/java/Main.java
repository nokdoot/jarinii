import java.io.FileInputStream;

import org.json.JSONObject;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Main {
	public static void main(String[] args) throws Exception {

		FileInputStream in = new FileInputStream("Sockets.java");
		//FileInputStream in = new FileInputStream(args[0]);

        // 샘플 예제

        CompilationUnit compilationUnit = JavaParser.parse(in);
        
        ToJSON tjson = new ToJSON();
        JSONObject head = new JSONObject();
        tjson.parseCU(head, compilationUnit.getChildNodes());
        System.out.println(head.toString());

    }
}