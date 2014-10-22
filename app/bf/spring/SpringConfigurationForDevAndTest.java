package bf.spring;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import play.Logger;
import bf.elastic.ElasticSearchEmbeddedServer;

/**
 * @author rfanego
 */
@Configuration
@Profile({ "DEV", "TEST" })
@ComponentScan({ "bf" })
public class SpringConfigurationForDevAndTest {

	@Lazy
	@Bean
	public Client elasticSearchClient() {
		return embeddedElasticSearchServer().getClient();
	}

	@Lazy
	@Bean
	public ElasticSearchEmbeddedServer embeddedElasticSearchServer() {
		ElasticSearchEmbeddedServer esServer = new ElasticSearchEmbeddedServer();
		Client client = null;
		try {
			client = esServer.getClient();
			client.admin().indices().prepareRefresh().execute().actionGet();
		} catch (Exception e) {
			Logger.error("Could not initialize embedded Elastic Search Server for DEV/TEST", e);
			esServer.close();
		} finally {
			client.close();
		}
		return esServer;
	}
}
