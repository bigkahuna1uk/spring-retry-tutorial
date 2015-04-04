package it.cosenonjaviste;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	@Retryable
	public Double getCurrentRate() {
		
		System.out.println("Calculating - Attempt " + attempts + " at " + sdf.format(new Date()));
		attempts++;
		
		try {
			HttpResponse<JsonNode> response = Unirest.get("http://rate-exchange.herokuapp.com/fetchRate")
				.queryString("from", "EUR")
				.queryString("to","USD")
				.asJson();
			
			if(response.getStatus() == 200){
				return response.getBody().getObject().getDouble("Rate");
			}else{
				throw new RuntimeException("Server Response: " + response.getStatus());
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
