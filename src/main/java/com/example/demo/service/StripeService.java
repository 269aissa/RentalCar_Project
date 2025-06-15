package com.example.demo.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    public String createCheckoutSession(double montant, String reservationId) throws Exception {
        Stripe.apiKey = "sk_test_51Ra37uBIj2ZiLwwi3oJhqAfbZeuI4fILRGGbaisnSwBKdqhYWl4KXr0CZZTiVuWNp7RWunHllVSpaecqgDae5law00l7JnIkWu"; // ta clé secrète Stripe

        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8080/reservations/facture/" + reservationId + "?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl("http://localhost:8080/reservations/mes")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setUnitAmount((long) (montant * 100)) // montant en centimes
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Paiement de réservation")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
