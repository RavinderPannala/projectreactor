package com.example.reactor.ProjectReactor.repository;

import com.example.reactor.ProjectReactor.entity.Contact;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ContactRepository extends ReactiveMongoRepository<Contact,String> {
    Mono<Contact> findFirstByEmail(String email);

    //Mono<Contact> findAllByPhoneOrName(String phoneOrName);
}
