package bf.elastic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author rfanego
 */
@Component
public class ElasticService {
	private static final String ES_INDEX = "musica";
	
	@Autowired
	private Client client;
	
	public List<String> autocomplete(String word){
		CompletionSuggestionBuilder suggestionsBuilder = new CompletionSuggestionBuilder("completar-banda");
	    suggestionsBuilder.text(word);
	    suggestionsBuilder.field("name");
	    SuggestRequestBuilder suggestRequestBuilder = client.prepareSuggest(ES_INDEX).addSuggestion(suggestionsBuilder);
	    
	    SuggestResponse response = suggestRequestBuilder.execute().actionGet();
	    
	    Iterator<? extends Suggest.Suggestion.Entry.Option> iterator =
	    		response.getSuggest().getSuggestion("completar-banda").iterator().next().getOptions().iterator();
	    
	    List<String> words = new ArrayList<>();
	    while(iterator.hasNext()){
	    	words.add(iterator.next().getText().string());
	    }
		return words;
	}
	
	public List<String> fuzzyAutocomplete(String word){
		
		return null;
	}
}
