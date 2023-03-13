package com.example.reactor.ProjectReactor;

import com.example.reactor.ProjectReactor.entity.Contact;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("unit_test")
public class ContactControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    Contact contact;

    @Test
    @Order(1)
    public void createContact() {
        Flux<Contact> saveFlux = webTestClient
                .post()
                .uri("/api/contact/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Contact("1", "Ravi", "Ravi@gmail.com", "352323")))
                .exchange()
                .expectStatus().isAccepted()
                .returnResult(Contact.class).getResponseBody().log();

        saveFlux.next().subscribe(c -> {
            this.contact = c;
        });

        Assertions.assertNotNull(contact);
    }

    @Test
    @Order(2)
    public void getByEmail() {
        Flux<Contact> getByEmail = webTestClient.get().uri("/api/contact/get/email?email={email}", "Ravi@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isFound().
                returnResult(Contact.class).getResponseBody().log();

        StepVerifier.create(getByEmail).expectSubscription().expectNextMatches(c -> c.getPhone().equals("45475434")).verifyComplete();
    }

   /* @Test
    @Order(3)
    public void updateContact() {
        Flux<Contact> updateFlux = webTestClient.put().uri("/api/contact/update/{id}", contact.getId()).accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Contact("WebTestClient", "wtc@email.com", "11111111").setId(contact.getId())))
                .exchange()
                .returnResult(Contact.class).getResponseBody().log();

        StepVerifierEx.create(updateFlux).expectSubscription().expectNextMatches(c -> c.getEmail().equals("wtc@email.com")).verifyComplete();
    }*/


}
