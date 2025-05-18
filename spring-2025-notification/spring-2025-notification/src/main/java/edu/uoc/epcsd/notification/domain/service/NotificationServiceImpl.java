package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.ProductMessage;
import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Log4j2
@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${userService.getUsersToAlert.url}")
    private String userServiceUrl;

    @Value("${productService.getProductDetails.url}")
    private String productServiceUrl;

    @Override
    public void notifyProductAvailable(ProductMessage productMessage) {

        // TODO: Use RestTemplate with the above userServiceUrl to query the User microservice in order to get the users that have an alert for the specified product (the date specified in the parameter may be the actual date: LocalDate.now()).
        //  Then simulate the email notification for the alerted users by logging a line with INFO level for each user saying "Sending an email to user " + the user fullName

        log.trace("notifyProductAvailable");
        log.info("Notifying product available for product " + productMessage.getProductId());

        LocalDate date = LocalDate.now();

        String url = userServiceUrl
                .replace("{productId}", String.valueOf(productMessage.getProductId()))
                .replace("{availableOnDate}", date.toString());

        String productUrl = productServiceUrl
                .replace("{productId}", String.valueOf(productMessage.getProductId()));


        try {
            RestTemplate restTemplate = new RestTemplate();
            GetUserResponse[] users = restTemplate.getForObject(url, GetUserResponse[].class);
            if (users != null) {
                for (GetUserResponse user : users) {
                    log.info("Sending an email to user " + user.getFullName() + " for product " + productMessage.getProductId());
                }
            } else {
                log.warn("No users found for product " + productMessage.getProductId());
            }
        } catch (Exception e) {
            log.error("Error while sending email notifications: ", e);
        }

    }
}
