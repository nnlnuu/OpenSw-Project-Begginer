package scripts;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Midterm {
	private String filePath;
	private String query;
	public Midterm(String filePath,String query) { // collection.xml 받아오기
		this.filePath = filePath;
		this.query = query;
	}
	public void showSnippet() throws Exception {
		File collection_file = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
		Document document = dBuilder.parse(collection_file); 
		document.getDocumentElement().normalize();
		NodeList nList = document.getElementsByTagName("doc");
		// 매치 스코어 
		int[] matchScore = new int[nList.getLength()];
		int cnt=0;
		//idTitle 
		String[] idTitle = new String[nList.getLength()]; 
		String[] snip = new String[nList.getLength()];
		for(int w =0;w<nList.getLength();w++) {
			snip[w] = "";
		}
		for(int j = 0;j<nList.getLength();j++) {
			 Node nNode = nList.item(j); 
			 Element eElement = (Element) nNode;
			 //title 받아오기 
			 idTitle[j] = eElement.getElementsByTagName("title").item(0).getTextContent();
			 //System.out.println(idTitle[j]);
			 // query 형태소 분석
			 KeywordExtractor qke = new KeywordExtractor();
			 KeywordList qkl = qke.extractKeyword(query, true);
			 String[] qArr = new String[qkl.size()];
			 for(int t = 0; t<qkl.size();t++) {
				 Keyword kwrd = qkl.get(t);
				 qArr[t] = kwrd.getString();
				// System.out.println(qArr[t]);
			 }
			 //바디 형태소 분석 
			 KeywordExtractor ke = new KeywordExtractor();
			 KeywordList kl = ke.extractKeyword(eElement.getElementsByTagName("body").item(0).getTextContent(), true);
			 String[] Arr = new String[kl.size()];
			 int insert =0;
			 for(int s = 0 ;s<kl.size();s++) {
				 Keyword kwrd = kl.get(s);
				 Arr[s] = kwrd.getString();
				 insert = 1;
				 while(snip[j].length()<30) {
					 snip[j] += Arr[s];
					 insert =0;
					 if(insert==0) break;
				 }
					 
					 
//				 if(j==4) {
//					 System.out.println(Arr[s]);
//				 }
			 }
			 for(int k = 0; k<kl.size();k++) {
				 for(int q = 0; q<qkl.size();q++) {
					 if(Arr[k].indexOf(qArr[q])==0) {
						 matchScore[cnt] +=1;
					 }
				 }
			 }
			 cnt++;
		  }
		int maxIndex = 0;
		for(int i = 0;i<nList.getLength();i++) {
			//System.out.println(idTitle[i] + matchScore[i]);
			if(matchScore[maxIndex] < matchScore[i]) {
				maxIndex = i;
			}
		}
		
		System.out.print(idTitle[maxIndex]+", ");
		System.out.println("스니펫 : "+ snip[maxIndex]);
		System.out.println("최대 매칭 점수 :" + matchScore[maxIndex]+" ");
		
		}
	
}








