package it.cosenonjaviste;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class AdvancedDummyExchangeRateCalculator implements ExchangeRateCalculator {
	
	private static final double BASE_EXCHANGE_RATE = 1.09;
	private int attempts = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	@Retryable(maxAttempts=10,backoff = @Backoff(delay = 5000,multiplier=1.5))
	public Double getCurrentRate(){
		System.out.println("Calculating - Attempt " + attempts + " at " + sdf.format(new Date()));
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
