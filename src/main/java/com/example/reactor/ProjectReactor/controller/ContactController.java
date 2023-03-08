package com.example.reactor.ProjectReactor.controller;

import com.example.reactor.ProjectReactor.entity.Contact;
import com.example.reactor.ProjectReactor.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping(value = "/save")
    private Mono<ResponseEntity<Contact>> saveContact(@RequestBody Contact contact) {
        Mono<ResponseEntity<Contact>> responseEntityMono = contactRepository.insert(contact)
                .map(c1 -> new ResponseEntity<>(c1, HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
        return responseEntityMono;
    }

    @GetMapping(value = "/all")
    private Flux<Contact> getAllContact() {
        Flux<Contact> all = contactRepository.findAll();
        return all;
    }

    @GetMapping(value = "/get/{id}")
    private Mono<ResponseEntity<Contact>> getContactById(@PathVariable String id) {
        return contactRepository.findById(id)
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/delete/{id}")
    private Mono<Void> deleteById(@PathVariable String id) {
        return contactRepository.deleteById(id);
    }

    @GetMapping(value = "/get/email")
    private Mono<ResponseEntity<Contact>> findByEmail(@RequestParam("email") String email) {
        return contactRepository.findFirstByEmail(email)
                .map(c -> new ResponseEntity<>(c, HttpStatus.FOUND))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/update/{id}")
    private Mono<ResponseEntity<Contact>> updateContact(@RequestBody Contact contact, @PathVariable String id) {
        return contactRepository.findById(id).flatMap(c -> {
            contact.setId(id);
            return contactRepository.save(contact)
                    .map(c1 -> new ResponseEntity<>(c1, HttpStatus.OK))
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        });
    }

}
