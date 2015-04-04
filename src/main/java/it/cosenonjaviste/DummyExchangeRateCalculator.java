package it.cosenonjaviste;

import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class DummyExchangeRateCalculator implements ExchangeRateCalculator {
	
	private int attempts = 0;
	
	@Retryable(value=RuntimeException.class)
	//(maxAttempts=10,value=RuntimeException.class,backoff = @Backoff(delay = 1,multiplier=1))
	public Double getCurrentRate(){
		System.out.println("Calculating - Attempt " + attempts);
		attempts++;
		//Do something...
		throw new RuntimeException("Error");
	}
	
	@Recover
	public Double recover(RuntimeException e){
		System.out.println("Recovering - returning safe value");
		return 1.09;
	}
}
