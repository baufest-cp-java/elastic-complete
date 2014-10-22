/**
 * 
 */
package bf.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import bf.elastic.ElasticService;
import bf.util.JsonUtil;

/**
 * @author rfanego
 */
@org.springframework.stereotype.Controller
public class Words extends Controller {
	
	@Autowired
	private ElasticService elasticService;
	
	public Result autoComplete(String word){
		List<String> words = elasticService.autocomplete(word);
		return ok(Json.parse(JsonUtil.parseListToJson(words)));
	}
	
	public Result fuzzyAutoComplete(String word){
		List<String> words = elasticService.fuzzyAutocomplete(word);
		return ok(Json.parse(JsonUtil.parseListToJson(words)));
	}
}
