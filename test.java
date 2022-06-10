package scripts;

//네이버 검색 API예제는 블로그를 비롯 전문자료까지 호출방법이 동일하므로 blog검색만 대표로 예제를 올렸습니다.
//네이버 검색 API 예제 - blog 검색
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class test {


 public static void main(String[] args) throws ParseException {
   //  String clientId = "PEyMsnlSinx3pRDYgl7F"; //애플리케이션 클라이언트 아이디값"
     //String clientSecret = "_GjnQDv8iW"; //애플리케이션 클라이언트 시크릿값"
	 String clientId = "MwB6usX4EKYLT99kcV_V"; //애플리케이션 클라이언트 아이디값"
     String clientSecret = "WloYHEC4tk"; //애플리케이션 클라이언트 시크릿값"

     String text = "기생충";
     try {
         text = URLEncoder.encode("기생충", "UTF-8");
     } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("검색어 인코딩 실패",e);
     }


    // String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text;    // json 결과
    // String apiURL = "https://openapi.naver.com/v1/search/movie?query=" + text;    // json 결과
     String apiURL =  "https://openapi.naver.com/v1/search/movie?query=" + text;
     //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

     
     Map<String, String> requestHeaders = new HashMap<>();
     requestHeaders.put("X-Naver-Client-Id", clientId);
     requestHeaders.put("X-Naver-Client-Secret", clientSecret);
     String responseBody = get(apiURL,requestHeaders);


     System.out.println(responseBody);
     
//     JSONParser jsonParser = new JSONParser();
//     JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
//     JSONArray infoArray = (JSONArray) jsonObject.get("items");
//     for(int i = 0 ;i<infoArray.size();i++) {
//    	 System.out.println("item_="+i+"=================================");
//    	 JSONObject itemObject = (JSONObject) infoArray.get(i);
//    	 System.out.println("titie:\t"+itemObject.get("title"));
//    	 System.out.println("link:\t"+itemObject.get("link"));
//    	 System.out.println("description:\t"+itemObject.get("description"));
//    	 System.out.println("bloggername:\t"+itemObject.get("bloggername"));
//     }
     
   JSONParser jsonParser = new JSONParser();
   JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
   JSONArray infoArray = (JSONArray) jsonObject.get("items");
   for(int i = 0 ;i<infoArray.size();i++) {
  	 System.out.println("item_="+i+"=================================");
  	 JSONObject itemObject = (JSONObject) infoArray.get(i);
  	 System.out.println("titie:\t"+itemObject.get("title"));
  	 System.out.println("subtitle:\t"+itemObject.get("subtitle"));
  	 System.out.println("director:\t"+itemObject.get("director"));
  	 System.out.println("actor:\t"+itemObject.get("actor"));
  	 System.out.println("userRating:\t"+itemObject.get("userRating"));
   }
     
 }


 private static String get(String apiUrl, Map<String, String> requestHeaders){
     HttpURLConnection con = connect(apiUrl);
     try {
         con.setRequestMethod("GET");
         for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
             con.setRequestProperty(header.getKey(), header.getValue());
         }


         int responseCode = con.getResponseCode();
         if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
             return readBody(con.getInputStream());
         } else { // 에러 발생
             return readBody(con.getErrorStream());
         }
     } catch (IOException e) {
         throw new RuntimeException("API 요청과 응답 실패", e);
     } finally {
         con.disconnect();
     }
 }


 private static HttpURLConnection connect(String apiUrl){
     try {
         URL url = new URL(apiUrl);
         return (HttpURLConnection)url.openConnection();
     } catch (MalformedURLException e) {
         throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
     } catch (IOException e) {
         throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
     }
 }


 private static String readBody(InputStream body){
     InputStreamReader streamReader = new InputStreamReader(body);


     try (BufferedReader lineReader = new BufferedReader(streamReader)) {
         StringBuilder responseBody = new StringBuilder();


         String line;
         while ((line = lineReader.readLine()) != null) {
             responseBody.append(line);
         }


         return responseBody.toString();
     } catch (IOException e) {
         throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
     }
 }
}
