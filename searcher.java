<<<<<<< HEAD
package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class searcher {
	private String input_file;
	private String output;
	private String query;

	public searcher(String fileName, String query) {
		this.input_file = fileName;
		this.query = query;
	}

	public void CalcSim() throws Exception {
		//Collection.xml 파일 read
		File collection_file = new File("C:\\Users\\user\\eclipse-workspace\\Opensw\\collection.xml");
		
		//Collecton.xml 파일 파싱 및 아이디 가져오기 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
		Document document = dBuilder.parse(collection_file); 
		document.getDocumentElement().normalize();
		NodeList nList = document.getElementsByTagName("doc");
		
		//collection.xml id -> 문서이름 받아오기 
		String[] idTitle = new String[nList.getLength()]; 
		  for(int i = 0;i<nList.getLength();i++) {
			 Node nNode = nList.item(i); 
			 Element eElement = (Element) nNode;
			 idTitle[i] = eElement.getElementsByTagName("title").item(0).getTextContent();
			 System.out.println(idTitle[i]);
		 }
		
		//query 형태소 분석
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		
		//queryMap에 query 단어 + 가중치 입력 
		HashMap<String,Integer> queryMap = new HashMap<String,Integer>();
		for(int i = 0;i <kl.size();i++) {
			Keyword kwrd = kl.get(i);
			String word = kwrd.getString();
			int freq = kwrd.getCnt();
			if(queryMap.get(kwrd.getString()) == null) {
				queryMap.put(word,freq);
			}
		}
		//post 파일 hashMap으로 변경 
		FileInputStream fstream = new FileInputStream("./index.post");
		ObjectInputStream objectInputstream = new ObjectInputStream(fstream); 
		Object object = objectInputstream.readObject();
		objectInputstream.close();
		HashMap<String, String> hashMap = (HashMap)object;
		
		//hashMap -> post 파일에서 load 
		Set<String> set = hashMap.keySet(); Iterator<String> iterator = set.iterator();
		//setquery queryMap에서 load
		Set<String> setquery = queryMap.keySet(); Iterator<String> it = setquery.iterator();
		
		String[] arr = new String[queryMap.size()]; //post파일에 있는 값을 
		String[] keyarr = new String[queryMap.size()]; //arr
		int Docnum= nList.getLength();
		int cnt=0;
		while(it.hasNext()) {
			String key = it.next();
			keyarr[cnt]= key; //쿼리에서 추출된 키워드 
			arr[cnt++] = hashMap.get(key); // post 값에 0 0.0 1 0.0 2 0.0 
		}
		Double weight = 0.00;
		Double[] maxDoc = new Double[Docnum];
		String[] splitedArr = null;
		for(int t = 0 ;t<Docnum;t++) {//가중치 값 
			weight = 0.00;
			for(int i = 0 ;i<arr.length;i++) {
				splitedArr = arr[i].split(" "); //  0 0.0 1 0.0 2 0.0 를 분해 
				weight += queryMap.get(keyarr[i])*Double.parseDouble(splitedArr[2*t+1]); 
			}
			maxDoc[t] = weight;
		}
		Double[] cosVal = new Double[Docnum]; //
		double sizeA=0; // 전체별 사이즈
		Double[] sizeB = new Double[Docnum]; //문서별 사이즈 
		
		for(int i = 0; i< Docnum;i++) { //문서
			//A의 size 
			Set<String> setqry = queryMap.keySet(); Iterator<String> itqr = setquery.iterator();
			sizeA = 0;
			while(itqr.hasNext()) {
				String key = itqr.next();
				sizeA += queryMap.get(key) * queryMap.get(key);
			}
			sizeB[i] = 0.0;
			for(int t = 0 ; t< arr.length;t++) {
				splitedArr = arr[t].split(" ");
				sizeB[i] += Double.parseDouble(splitedArr[2*i+1])*Double.parseDouble(splitedArr[2*i+1]);				
			}
			cosVal[i] = maxDoc[i] / (Math.sqrt(sizeB[i])*Math.sqrt(sizeA));
		  }
			for(int i = 0; i<Docnum ;i++) {
				System.out.println(cosVal[i]);
			}
		
//		for(int i = 0;i<Docnum;i++) {
//			System.out.print(i +" ");
//			System.out.println(maxDoc[i]);
//		}
//		
//		//마무리 
//		boolean check[] = new boolean[maxDoc.length];
//		int[] indexArr = new int[maxDoc.length];
//		int index=0,t;
//		for(int j = 0 ; j<maxDoc.length;j++){//다섯 번 정렬
//			for(t = 0;t<maxDoc.length;t++) {
//				if(!check[t]) { // t가 쓰였는지 안쓰였는지 알고 
//					index = t;
//					break;
//				}
//			}
//			for(int i = t ;i<maxDoc.length;i++) {
//				if(maxDoc[index] < maxDoc[i]) {
//					if(!check[i]) {
//						index = i;
//						check[i] = true;
//						break;
//					}
//				}
//			}
//			check[index] = true;
//			indexArr[j] = index;
//		}
//		
//		int count= 0;
//		for(int i = 0;i<3;i++) {
//			if(maxDoc[indexArr[i]]!=0) {
//			System.out.println(idTitle[indexArr[i]]);
//			count++;
//			}
//		}
//		if(count==0) System.out.println("검색된 문서가 없습니다."); 
	}

}
=======
package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class searcher {
	private String input_file;
	private String output;
	private String query;

	public searcher(String fileName, String query) {
		this.input_file = fileName;
		this.query = query;
	}

	public void CalcSim() throws Exception {
		//Collection.xml 파일 read
		File collection_file = new File("C:\\Users\\user\\eclipse-workspace\\Opensw\\collection.xml");
		
		//Collecton.xml 파일 파싱 및 아이디 가져오기 
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
		Document document = dBuilder.parse(collection_file); 
		document.getDocumentElement().normalize();
		NodeList nList = document.getElementsByTagName("doc");
	  
		  //collection.xml id -> 문서이름 받아오기 
		 String[] idTitle = new String[nList.getLength()]; 
		  for(int i = 0;i<nList.getLength();i++) {
			 Node nNode = nList.item(i); 
			 Element eElement = (Element) nNode;
			 idTitle[i] = eElement.getElementsByTagName("title").item(0).getTextContent();
			 System.out.println(idTitle[i]);
		  }
		  
		
		
		//query 형태소 분석
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		
		//queryMap에 query 단어 + 가중치 입력 
		HashMap<String,Integer> queryMap = new HashMap<String,Integer>();
		for(int i = 0;i <kl.size();i++) {
			Keyword kwrd = kl.get(i);
			String word = kwrd.getString();
			int freq = kwrd.getCnt();
			if(queryMap.get(kwrd.getString()) == null) {
				queryMap.put(word,freq);
			}
		}
		
		//post 파일 hashMap으로 변경 
		FileInputStream fstream = new FileInputStream("./index.post");
		ObjectInputStream objectInputstream = new ObjectInputStream(fstream); 
		
		Object object = objectInputstream.readObject();
		objectInputstream.close();
		
		HashMap<String, String> hashMap = (HashMap)object;
		
		
		
		
		//hashMap -> post 파일에서 load 
		Set<String> set = hashMap.keySet(); Iterator<String> iterator = set.iterator();
		//setquery queryMap에서 load
		Set<String> setquery = queryMap.keySet(); Iterator<String> it = setquery.iterator();
		
		String[] arr = new String[queryMap.size()];
		String[] keyarr = new String[queryMap.size()];
		int cnt=0;
		while(it.hasNext()) {
			String key = it.next();
			keyarr[cnt]= key;
			arr[cnt++] = hashMap.get(key);
		}
		//arr 에 id 별 가중치 값 들어있음 
//		for(int i = 0;i<arr.length;i++) {
//			System.out.println(arr[i]);
//		}
		Double weight = 0.00;
		Double[] maxDoc = new Double[nList.getLength()];
		for(int t = 0 ;t<nList.getLength();t++) {
			weight = 0.00;
			for(int i = 0 ;i<arr.length;i++) {
				String[] splitedArr = arr[i].split(" ");
				weight += queryMap.get(keyarr[i])*Double.parseDouble(splitedArr[2*t+1]); 
			}
			
			maxDoc[t] = weight;
		}
		for(int i = 0;i<maxDoc.length;i++) {
			System.out.print(i +" ");
			System.out.println(maxDoc[i]);
		}
		//마무리 
		boolean check[] = new boolean[maxDoc.length];
		int[] indexArr = new int[maxDoc.length];
		int index=0,t;
		for(int j = 0 ; j<maxDoc.length;j++){//다섯 번 정렬
			for(t = 0;t<maxDoc.length;t++) {
				if(!check[t]) { // t가 쓰였는지 안쓰였는지 알고 
					index = t;
					break;
				}
			}
			for(int i = t ;i<maxDoc.length;i++) {
					if(maxDoc[index] < maxDoc[i]) {
						if(!check[i]) {
							index = i;
							check[i] = true;
							break;
						}
					}
			}
			check[index] = true;
			indexArr[j] = index;
		}
		
		int count= 0;
		for(int i = 0;i<3;i++) {
			if(maxDoc[indexArr[i]]!=0) {
			System.out.println(idTitle[indexArr[i]]);
			count++;
			}
		}
		if(count==0) System.out.println("검색된 문서가 없습니다."); 
	}

}
>>>>>>> 98fa01ec5957150b879bcc1f239f85ec5f5c6479
