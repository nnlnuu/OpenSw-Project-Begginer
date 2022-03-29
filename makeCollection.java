package scripts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {
	private String data_path;
	private String output_file = "./collection.xml";
	
	public makeCollection(String path) {
		this.data_path = path;
	}
	public void makeXml() throws Exception {
		File[] filelist = makeFileList(data_path);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();

		Element docs = doc.createElement("docs");
		doc.appendChild(docs);

		File file = new File(output_file);
		for (int i = 0; i < filelist.length; i++) {
			org.jsoup.nodes.Document html = Jsoup.parse(filelist[i], "UTF-8");
			String titleData = html.title();
			String bodyData = html.body().text();

		// code element
		Element code = doc.createElement("doc");
		docs.appendChild(code);

		code.setAttribute("id", Integer.toString(i));

		// title element
		Element titled = doc.createElement("title");
		titled.appendChild(doc.createTextNode(titleData));
		code.appendChild(titled);

		
		Element body = doc.createElement("body");
		body.appendChild(doc.createTextNode(bodyData));
		code.appendChild(body);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(file));
		transformer.transform(source, result);
		}
		
		System.out.println("2주차 실행완료");
	}
	public static File[] makeFileList(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}
}
