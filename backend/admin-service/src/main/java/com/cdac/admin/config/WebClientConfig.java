package com.cdac.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


/*provide use the webclient builder
  flow  :   build requestion : send -> map response -> block / or delegate

*/

@Configuration
public class WebClientConfig {
     
	
	@Bean
	public WebClient.Builder  webClientBuilder() {
		 return WebClient.builder();
	}
 }
