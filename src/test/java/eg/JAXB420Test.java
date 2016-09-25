package eg;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.sun.tools.xjc.api.Mapping;
import com.sun.tools.xjc.api.Property;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

/**
 * This test will try to create the same model over and over until it fails.
 * Each time, schemas'systemId are appended with some random suffix (and
 * resolved via a custom EntityResolver) so that they may appear in a different
 * order in
 * {@linkplain com.sun.tools.xjc.reader.internalizer.DOMForest#getOneDocument()}.
 * 
 * @author Xavier Dury
 * @see {@linkplain com.sun.tools.xjc.reader.internalizer.Internalizer#move()}
 */
@RunWith(Parameterized.class)
public class JAXB420Test {

	@Parameters(name = "suffix: {0}")
	public static List<String> suffices() {
		return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "a", "aa", "ab", "ba", "az", "za1", "46546131", "kjhkjh");
	}

	private final String suffix;

	public JAXB420Test(String suffix) {
		this.suffix = suffix;
	}

	private InputSource inputSource(String resourceName) {
		InputSource result = new InputSource(getClass().getResourceAsStream(resourceName));
		result.setSystemId(resourceName + this.suffix); // add the suffix to the systemId
		return result;
	}

	@Test
	public void propertyTypeShouldBeCustomizedThroughGlobalBindings() {
		SchemaCompiler sc = XJC.createSchemaCompiler();
		sc.setEntityResolver(new EntityResolver() {

			public InputSource resolveEntity(String publicId, String systemId) {
				return inputSource("common.xsd");
			}
		});
		sc.parseSchema(inputSource("bindings.xjb"));
		sc.parseSchema(inputSource("book.xsd"));
		for (Mapping mapping : sc.bind().getMappings()) {
			for (Property property : mapping.getWrapperStyleDrilldown()) {
				Assert.assertEquals("eg.Isbn", property.type().fullName());
			}
		}
	}
}
