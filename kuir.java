package scripts;

public class kuir {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String command = args[0];
		String path = args[1];
		
		if(command.equals("-c")) {
			makeCollection collection = new makeCollection(path);
			collection.makeXml();
		}else if(command.equals("-k")) {
			makeKeyword keyword = new makeKeyword(path);
			keyword.convertXml();
		}else if(command.equals("-i")) {
			//객체 생성
			indexer index = new indexer(path);
			index.postIndex();
		}
	}

}
