package scripts;

public class kuir {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String command = args[0];
		String path = args[1];
		
		 String command2 = args[2]; String query = args[3];
		 
		
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
			  else if(command.equals("-s")) { 
				  if(command2.equals("-q")) { 
					  searcher search = new searcher(path,query); 
					  search.CalcSim(); } }
			 
	}

}
