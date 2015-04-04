package it.cosenonjaviste;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;


@Configuration
@EnableRetry
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class RetryApplicationContext {
	
	@Bean(name="dummyRateCalculator")
	public DummyExchangeRateCalculator getDummyExchangeRateCalculator() {
	      return new DummyExchangeRateCalculator();
	}
	
	@Bean(name="advancedDummyRateCalculator")
	public AdvancedDummyExchangeRateCalculator getAdvancedDummyExchangeRateCalculator() {
	      return new AdvancedDummyExchangeRateCalculator();
	}
	
	@Bean(name="realRateCalculator")
	public RealExchangeRateCalculator getRealExchangeRateCalculator() {
	      return new RealExchangeRateCalculator();
	}
	
}
