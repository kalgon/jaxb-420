package eg;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.api.Mapping;
import com.sun.tools.xjc.api.Property;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

/**
 * This test will try to create the same model over and over until it fails.
 * Each time, schemas'systemId are appended with the iteration number (and
 * resolved via a custom EntityResolver) so that they may appear in a different
 * order in
 * {@linkplain com.sun.tools.xjc.reader.internalizer.DOMForest#getOneDocument()}.
 * 
 * @author Xavier Dury
 * @see {@linkplain com.sun.tools.xjc.reader.internalizer.Internalizer#move()}
 */
public class JAXB420Test {

	private InputSource inputSource(String systemId, String resourceName) {
		InputSource result = new InputSource(JAXB420Test.class.getResourceAsStream(resourceName));
		result.setSystemId(systemId);
		return result;
	}

	@Test
	public void simpleTest() {
		for (int i = 0; i < 100; i++) {
			SchemaCompiler sc = XJC.createSchemaCompiler();
			final String suffix = Integer.toString(i);
			sc.setEntityResolver(new EntityResolver() {

				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					if (systemId.endsWith("common.xsd")) {
						return inputSource(systemId + suffix, "common.xsd");
					}
					if (systemId.endsWith("book.xsd")) {
						return inputSource(systemId + suffix, "book.xsd");
					}
					return null;
				}
			});
			sc.parseSchema(inputSource("bindings.xjb" + suffix, "bindings.xjb"));
			sc.parseSchema(inputSource("root.xsd" + suffix, "root.xsd"));
			for (Mapping mapping : sc.bind().getMappings()) {
				for (Property property : mapping.getWrapperStyleDrilldown()) {
					System.out.println(property.type().fullName());
					Assert.assertEquals("Test failed at iteration " + suffix, "eg.Isbn", property.type().fullName());
				}
			}
		}
	}
}
