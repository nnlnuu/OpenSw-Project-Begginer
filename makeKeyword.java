package scripts;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class makeKeyword {
	private String input_file; //collection.xml
	private String output_file = "./index.xml";
	
	public makeKeyword(String file) {
		this.input_file = file;
	}
	
	public void convertXml() throws Exception {
		// File file = new File("C:\\Users\\user\\SimpleR\\html\\collection.xml");
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(input_file);
		document.getDocumentElement().normalize();
		NodeList nList = document.getElementsByTagName("doc");
			
		
		DocumentBuilderFactory docbuildFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docbuildFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
			
		Element title = doc.createElement("title");
		doc.appendChild(title);
			
		//File indexfile = new File("C:\\Users\\user\\SimpleR\\html\\index.xml");
			
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			String bodystr="";
			Element eElement = (Element) nNode;
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				KeywordExtractor ke = new KeywordExtractor();
				KeywordList kl = ke.extractKeyword(eElement.getElementsByTagName("body").item(0).getTextContent(), true);
					
				for (int t = 0; t < kl.size(); t++) {
					Keyword kwrd = kl.get(t);
					System.out.print(kwrd.getString() + ": " + kwrd.getCnt()+"#");
					bodystr+=kwrd.getString() + ": " + kwrd.getCnt()+"#";
					}
					System.out.println();
				}
				
			
			//파일 write 
				Element code = doc.createElement("doc");
				title.appendChild(code);

				code.setAttribute("id", Integer.toString(i));

				// title element
				Element titled = doc.createElement("title");
				titled.appendChild(doc.createTextNode(eElement.getElementsByTagName("title").item(0).getTextContent()));
				code.appendChild(titled);

				// movieDirector element
				Element body = doc.createElement("body");
				body.appendChild(doc.createTextNode(bodystr));
				code.appendChild(body);
					
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indet-amount","4");
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(output_file));
			transformer.transform(source, result);
			}

		System.out.println("3주차 실행 완료");
		
	}
}
