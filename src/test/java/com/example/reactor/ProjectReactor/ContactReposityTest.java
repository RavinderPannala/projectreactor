package com.example.reactor.ProjectReactor;

import com.example.reactor.ProjectReactor.entity.Contact;
import com.example.reactor.ProjectReactor.repository.ContactRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactReposityTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ReactiveMongoOperations reactiveMongoOperations;

    @BeforeAll
    public void insertData() {

        Contact c = new Contact();
        c.setName("Raja");
        c.setEmail("Raja@gmail.com");
        c.setPhone("7546");

        Contact c1 = new Contact();
        c1.setName("Revan");
        c1.setEmail("Revan@gmail.com");
        c1.setPhone("12324");

        Contact c2 = new Contact();
        c2.setName("Arya");
        c2.setEmail("Arya@gmail.com");
        c2.setPhone("534646");

       /* Contact c3 = new Contact();
        c3.setName("Raja");
        c3.setEmail("Raja@gmail.com");
        c3.setPhone("7546");

        Contact c4 = new Contact();
        c4.setName("Raja");
        c4.setEmail("Raja@gmail.com");
        c4.setPhone("7546");*/

        StepVerifier.create(contactRepository.insert(c).log()).expectSubscription().expectNextCount(1).verifyComplete();

        StepVerifier.create(contactRepository.save(c1).log()).expectSubscription().expectNextCount(1).verifyComplete();

        StepVerifier.create(contactRepository.save(c2).log()).expectSubscription().expectNextMatches(a -> a.getId() != null).verifyComplete();
    }

    @Test
    @Order(1)
    public void findAll() {
        StepVerifier.create(contactRepository.findAll().log()).expectSubscription().expectNextCount(4).expectComplete();
    }

    @Test
    @Order(2)
    public void findByEmail() {
        StepVerifier.create(contactRepository.findFirstByEmail("Arya@gmail.com").log()).expectSubscription().expectNextCount(1).verifyComplete();
    }

    @Test
    @Order(3)
    public void updateContact() {
        Mono<Contact> firstByEmail = contactRepository.findFirstByEmail("Arya@gmail.com")
                .map(c -> {
                    c.setPhone("11111");
                    return c;
                }).flatMap(c1 -> contactRepository.save(c1));
        StepVerifier.create(firstByEmail.log()).expectSubscription().expectNextMatches(c -> c.getPhone().equals("11111")).verifyComplete();
    }

    @Test
    @Order(4)
    public void deleteContact() {
        Mono<Void> firstByEmail = contactRepository.findFirstByEmail("Arya@gmail.com").flatMap(c1 -> {
            return contactRepository.deleteById(c1.getId());
        }).log();

        StepVerifier.create(firstByEmail).expectSubscription().verifyComplete();

    }

}
