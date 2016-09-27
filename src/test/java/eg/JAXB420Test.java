package eg;

import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

/**
 * This test will try to create the same model over and over and each time prepend
 * the schemas'systemId with some random prefix (and resolved via a custom
 * EntityResolver) so that they may appear in a different order in
 * {@linkplain com.sun.tools.xjc.reader.internalizer.DOMForest#getOneDocument()}.
 * For some models, global bindings will be lost.
 * 
 * @author Xavier Dury
 * @see {@linkplain com.sun.tools.xjc.reader.internalizer.Internalizer#move()}
 */
@RunWith(Parameterized.class)
public class JAXB420Test {

	private static final QName DATE = new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "date");

	@Parameters(name = "prefix: ''{0}''")
	public static List<String> prefixes() {

		return Arrays.asList("", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "a", "aa", "ab", "ba", "az", "za1", "45ab", "bz4z", "46546131", "kjhkjh");
	}

	private final String prefix;

	public JAXB420Test(String prefix) {
		this.prefix = prefix;
	}

	@Test
	public void customJavaTypeShouldHaveBeenRegistered() {
		SchemaCompiler sc = XJC.createSchemaCompiler();
		sc.setEntityResolver(new EntityResolver() {

			public InputSource resolveEntity(String publicId, String systemId) {
				return inputSource("included.xsd");
			}
		});
		sc.parseSchema(inputSource("bindings.xjb"));
		sc.parseSchema(inputSource("root.xsd"));
		Assert.assertEquals("java.util.Calendar", sc.bind().getJavaType(DATE).getTypeClass().fullName());
	}

	private InputSource inputSource(String resourceName) {
		InputSource result = new InputSource(getClass().getResourceAsStream(resourceName));
		result.setSystemId(prefix + resourceName); // prepend systemId with prefix
		return result;
	}
}
