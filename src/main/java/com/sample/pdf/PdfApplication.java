package com.sample.pdf;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sample.pdf.service.PdfService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class PdfApplication {

  public static void main(String[] args) {
    SpringApplication.run(PdfApplication.class, args);
  }

  @Bean
  public InitializingBean test(PdfService pdfService) {
    return () -> {
      Gson gson = new Gson();
      JsonObject object1 = new JsonObject();
      object1.addProperty("uuid", UUID.randomUUID().toString());
      object1.addProperty("user_key", UUID.randomUUID().toString());
      object1.addProperty("recovery_key", UUID.randomUUID().toString());
      String walletInfo = gson.toJson(object1);

      JsonObject object2 = new JsonObject();
      object2.addProperty("uuid", UUID.randomUUID().toString());
      object2.addProperty("user_key", UUID.randomUUID().toString());
      object2.addProperty("recovery_key", UUID.randomUUID().toString());
      String pwInfo = gson.toJson(object2);

      pdfService.pdfGenerator("userID", walletInfo, pwInfo);
    };
  }

}
