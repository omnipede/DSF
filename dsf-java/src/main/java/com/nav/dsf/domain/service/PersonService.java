package com.nav.dsf.domain.service;

import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.repository.MemoryDatabase;
import java.util.Optional;

/**
 * Service for managing person records.
 * Provides CRUD operations and business logic for person data.
 */
public class PersonService {
    
    private final MemoryDatabase database;

    public PersonService(MemoryDatabase database) {
        this.database = database;
    }

    /**
     * Finds a person by FNR.
     */
    public Optional<Person> findPerson(String fnr) {
        return database.findByFnr(fnr);
    }

    /**
     * Finds a person by FNR (alias for findPerson).
     */
    public Optional<Person> findByFnr(String fnr) {
        return database.findByFnr(fnr);
    }

    /**
     * Finds a person or returns null if not found.
     */
    public Person findPersonOrNull(String fnr) {
        return database.findByFnr(fnr).orElse(null);
    }

    /**
     * Saves or updates a person record.
     */
    public Person savePerson(Person person) {
        return database.save(person);
    }

    /**
     * Deletes a person record.
     */
    public boolean deletePerson(String fnr) {
        return database.delete(fnr);
    }

    /**
     * Checks if a person exists.
     */
    public boolean personExists(String fnr) {
        return database.exists(fnr);
    }

    /**
     * Gets all persons.
     */
    public java.util.Map<String, Person> getAllPersons() {
        return database.getAllPersons();
    }

    /**
     * Gets the count of persons in the database.
     */
    public int count() {
        return database.count();
    }
}
