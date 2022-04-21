package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class indexer {
	private String index_path;
	public indexer(String path) {
		// TODO Auto-generated constructor stub
		this.index_path = path;
	}
	public void postIndex() throws Exception {
		// TODO Auto-generated method stub
		File index_file = new File(index_path);
		
		//해쉬맵 선언
		HashMap<String,Integer> dfxMap = new HashMap<String,Integer>();
		
		HashMap<String,String> wfxMap = new HashMap<String,String>();
		
		//document 생성
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		//파싱 -> 파싱하면 태그별로 나눠짐 
		Document document = dBuilder.parse(index_file);
		document.getDocumentElement().normalize();
		
		//doc id 별로 분기 -> nList
		NodeList nList = document.getElementsByTagName("doc");
		String arr[];
		String strfre[] = new String[2];
		
		// dfx를 구하기 위한 hashMap : 단어 x 가 몇개의 문서에 등장하는지  
		for(int i = 0;i< nList.getLength();i++) {
			String str;
			Node nNode = nList.item(i);
			Element eElement = (Element) nNode;
			//바디 안에 문자열들 # 기준으로 스플릿
			str = eElement.getElementsByTagName("body").item(0).getTextContent();
			arr = str.split("#");
			
			for(int t = 0;t<arr.length;t++) {
				strfre = arr[t].split(": ");
				String word = strfre[0];
				int freq = Integer.parseInt(strfre[1]);
				
				 if(dfxMap.get(strfre[0]) == null) { //없으면 넣기
					dfxMap.put(word,1); 
				 }else {
				 dfxMap.put(word, dfxMap.get(word)+1);
				 }			
			}
		}
		for(int i = 0;i< nList.getLength();i++) {
			HashMap<String,Double> tfxMap = new HashMap<String,Double>();
			Node nNode = nList.item(i);
			Element eElement = (Element) nNode;
			String str;
			str = eElement.getElementsByTagName("body").item(0).getTextContent();
			arr = str.split("#");
			
			for(int t = 0;t<arr.length;t++) { 
				strfre = arr[t].split(": ");
				String word = strfre[0];
				int freq = Integer.parseInt(strfre[1]);
			
				// tfx hashMap 
				if(tfxMap.get(word) == null) {
					double weight;
					weight = (double)((freq) * Math.log(nList.getLength()/dfxMap.get(word)));
					weight = Math.round(weight *100) /100.0;
					tfxMap.put(word,weight);
					
					if(wfxMap.get(word) == null) {
						String strTotal="";
						strTotal += Integer.toString(i) + " " + Double.toString(weight);
						wfxMap.put(word, strTotal);
					}else {
						String strTo = wfxMap.get(word)+ " " + Integer.toString(i) +" " + Double.toString(weight);
						wfxMap.put(word,strTo);
					}
				}
				}
				//iterator로 tfxMap에 없던 것들은 0.0 으로초기화 
				Iterator <String> itstr = dfxMap.keySet().iterator();
				while(itstr.hasNext()) {
					String keys = itstr.next();
					if(tfxMap.get(keys) == null) {
						if(wfxMap.get(keys) == null) { // 존재하지 않을 때
							wfxMap.put(keys, Integer.toString(i)+ " 0.00");
						}else {
							String str3 = wfxMap.get(keys) +" " +Integer.toString(i) + " 0.00";
						wfxMap.put(keys, str3);
						}
					}
				}
		}
		
		//파일 읽어오기 -> index.post에 wfxMap을 쓰고 close 한다.
		FileOutputStream fileStream = new FileOutputStream("./index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream); 
		
		objectOutputStream.writeObject(wfxMap);
		objectOutputStream.close();

		//읽어온 파일에서 단어들 쓰기 -> index.post를 읽어오고 Inputstream 닫기 
		FileInputStream fstream = new FileInputStream("./index.post");
		ObjectInputStream objectInputstream = new ObjectInputStream(fstream); 
		
		Object object = objectInputstream.readObject();
		objectInputstream.close();
		
		HashMap<String, String> hashMap = (HashMap)object;
		Iterator<String> it = hashMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			String value = hashMap.get(key);
			System.out.println(key + " : " + value);
		}
		
		System.out.println("4주차 실행 완료");
	}
	
}
