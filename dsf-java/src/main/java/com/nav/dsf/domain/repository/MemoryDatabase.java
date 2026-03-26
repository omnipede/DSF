package com.nav.dsf.domain.repository;

import com.nav.dsf.domain.model.Person;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory database for DSF person records.
 * Simulates the mainframe database defined in database.txt.
 */
public class MemoryDatabase {
    
    private final Map<String, Person> persons;

    public MemoryDatabase() {
        this.persons = new HashMap<>();
        initializeDemoData();
    }

    /**
     * Initializes demo data for testing.
     * Uses valid FNRs that pass checksum validation.
     */
    private void initializeDemoData() {
        // Create demo person 1 - Ola Nordmann (valid FNR: 01010100131, male)
        Person person1 = new Person("01010100131", "Ola Nordmann");
        person1.setTknr(1);
        person1.setLanguage("N");
        person1.getAgePension().setGp(100000);
        person1.getAgePension().setTp(50000);
        person1.getPensionStatus().setPensionType1("AP");
        persons.put(person1.getFnr(), person1);

        // Create demo person 2 - Kari Nordmann (valid FNR: 01010100050, female)
        Person person2 = new Person("01010100050", "Kari Nordmann");
        person2.setTknr(2);
        person2.setLanguage("N");
        person2.getAgePension().setGp(120000);
        person2.getAgePension().setTp(60000);
        person2.getPensionStatus().setPensionType1("AP");
        persons.put(person2.getFnr(), person2);

        // Create demo person 3 - Per Hansen (valid FNR: 13050300182, male)
        Person person3 = new Person("13050300182", "Per Hansen");
        person3.setTknr(3);
        person3.setLanguage("N");
        person3.getPensionStatus().setPensionType1("UP");
        persons.put(person3.getFnr(), person3);

        // Create demo person 4 - Anne Larsen (valid FNR: 13050300263, female)
        Person person4 = new Person("13050300263", "Anne Larsen");
        person4.setTknr(1);
        person4.setLanguage("N");
        person4.getPensionStatus().setPensionType1("EP");
        persons.put(person4.getFnr(), person4);
    }

    /**
     * Finds a person by FNR.
     */
    public Optional<Person> findByFnr(String fnr) {
        if (fnr == null || fnr.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(persons.get(fnr.trim()));
    }

    /**
     * Saves or updates a person record.
     */
    public Person save(Person person) {
        if (person == null || person.getFnr() == null) {
            throw new IllegalArgumentException("Person and FNR cannot be null");
        }
        persons.put(person.getFnr(), person);
        return person;
    }

    /**
     * Deletes a person by FNR.
     */
    public boolean delete(String fnr) {
        return persons.remove(fnr) != null;
    }

    /**
     * Checks if a person exists.
     */
    public boolean exists(String fnr) {
        return persons.containsKey(fnr);
    }

    /**
     * Gets all persons.
     */
    public Map<String, Person> getAllPersons() {
        return new HashMap<>(persons);
    }

    /**
     * Gets the count of persons.
     */
    public int count() {
        return persons.size();
    }

    /**
     * Clears all data (for testing).
     */
    public void clear() {
        persons.clear();
    }
}
