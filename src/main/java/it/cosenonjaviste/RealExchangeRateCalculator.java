package it.cosenonjaviste;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class RealExchangeRateCalculator implements ExchangeRateCalculator {
	
	private int attempts = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	@Retryable(maxAttempts=10,value=RuntimeException.class,backoff = @Backoff(delay = 10000,multiplier=2))
	public Double getCurrentRate() {
		
		System.out.println("Calculating - Attempt " + attempts + " at " + sdf.format(new Date()));
		attempts++;
		
		try {
			HttpResponse<JsonNode> response = Unirest.get("http://rate-exchange.herokuapp.com/fetchRate")
				.queryString("from", "EUR")
				.queryString("to","USD")
				.asJson();
			
			switch (response.getStatus()) {
			case 200:
				return response.getBody().getObject().getDouble("Rate");
			case 503:
				throw new RuntimeException("Server Response: " + response.getStatus());
			default:
				throw new IllegalStateException("Server not ready");
			}
		} catch (UnirestException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Recover
	public Double recover(RuntimeException e){
		System.out.println("Recovering - returning safe value");
		return 1.09;
	}

}
