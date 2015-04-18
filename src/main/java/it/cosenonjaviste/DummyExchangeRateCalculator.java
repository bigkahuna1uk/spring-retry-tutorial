package it.cosenonjaviste;

import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class DummyExchangeRateCalculator implements ExchangeRateCalculator {
	
	private int attempts = 0;
	private static final double BASE_EXCHANGE_RATE = 1.09;
	
	@Retryable(value=RuntimeException.class)
	public Double getCurrentRate(){
		System.out.println("Calculating - Attempt " + attempts);
		attempts++;
		//Do something...
		throw new RuntimeException("Error");
	}
	
	@Recover
	public Double recover(RuntimeException e){
		System.out.println("Recovering - returning safe value");
		return BASE_EXCHANGE_RATE;
	}
}
