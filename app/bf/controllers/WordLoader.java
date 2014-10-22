package bf.controllers;

import static org.springframework.util.StringUtils.hasText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Random;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

@org.springframework.stereotype.Controller
public class WordLoader extends Controller {
	private static final String ES_INDEX = "musica";
	
	@Autowired
	private Client elasticSearchClient;
	
	public Result loadWords(){
		InputStream bandsFile = WordLoader.class.getResourceAsStream("/documentos/bandas_def.txt");
		BufferedReader br = null;
		String line;
		
		br = new BufferedReader(new InputStreamReader(bandsFile, Charset.forName("UTF-8")));
		try {
			while ((line = br.readLine()) != null) {
				String trimmedLine = line.trim();
				if(hasText(trimmedLine)){
					indexSuggestion("bandas", trimmedLine);
				}
			}
		} catch (Exception e) {
			Logger.error("fail reading file", e);
		}
		return ok();
	}

	private void indexSuggestion(String type, String suggestion) throws IOException {
		IndexResponse response = elasticSearchClient.prepareIndex(ES_INDEX,type).setSource(
			XContentFactory.jsonBuilder()
				.startObject()
					.field("name", suggestion)
					.startObject("suggest")
						.array("input", suggestion)
						.array("output", suggestion)
						.startObject("payload")
							.field("id", new Random().nextInt(5000) + 1)
						.endObject()
					.endObject()
				.endObject()).execute().actionGet();
		
		response.getIndex();
	}
}
