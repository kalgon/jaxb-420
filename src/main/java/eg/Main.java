package eg;

import java.io.File;

import org.xml.sax.InputSource;

import com.sun.tools.xjc.api.Mapping;
import com.sun.tools.xjc.api.Property;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

public class Main {
	
	public static void main(String... args) {
		SchemaCompiler sc = XJC.createSchemaCompiler();
		sc.parseSchema(new InputSource(new File("src/main/resources/eg/bindings.xjb").toURI().toASCIIString()));
		sc.parseSchema(new InputSource(new File("src/main/resources/eg/root.xsd").toURI().toASCIIString()));
		for (Mapping mapping : sc.bind().getMappings()) {
			System.out.printf("%s : %s%n", mapping.getElement(), mapping.getType().getTypeClass().fullName());
			for (Property property : mapping.getWrapperStyleDrilldown()) {
				System.out.printf("+ %s : %s%n", property.elementName(), property.type().fullName());
			}
		}
	}
}
