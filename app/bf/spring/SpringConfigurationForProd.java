package bf.spring;

import static bf.util.ConfigurationAccessor.i;
import static bf.util.ConfigurationAccessor.s;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import play.Logger;

/**
 * @author rfanego
 */
@Configuration
@Profile("PROD")
@ComponentScan({ "bf" })
public class SpringConfigurationForProd {

	@Lazy
	@Bean
	public Client elasticSearchClient() {
		TransportClient transportClient = null;
		try {
			String elasticSearchHost = s("elasticsearch.host");

			Settings settings = settingsBuilder().put("cluster.name", s("elasticsearch.cluster.name")).put("client.transport.sniff", true)
					.build();

			String[] esHosts = elasticSearchHost.split(",");

			transportClient = new TransportClient(settings);

			for (String host : esHosts) {
				transportClient.addTransportAddress(new InetSocketTransportAddress(host, i("elasticsearch.port")));
				Logger.info("ES - Client Started. Hosts: " + host);
			}
		} catch (Exception e) {
			Logger.error("Error getting ES client.", e);
		}
		return transportClient;
	}
}
