package com.nav.dsf.infrastructure.datafile;

import com.nav.dsf.domain.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JSON-based file repository for DSF person records.
 * Provides persistence layer for saving and loading person data.
 * 
 * File format:
 * - database.json: Main person database
 * - transactions.json: Transaction history
 * - backup/*.json: Backup files
 */
public class JsonFileRepository {

    private static final String DEFAULT_DB_PATH = "database.json";
    private static final String BACKUP_DIR = "backup";
    
    private final String dbPath;
    private final ObjectMapper objectMapper;
    private Map<String, Person> persons;

    public JsonFileRepository() {
        this(DEFAULT_DB_PATH);
    }

    public JsonFileRepository(String dbPath) {
        this.dbPath = dbPath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.persons = new HashMap<>();
        loadDatabase();
    }

    /**
     * Loads the database from JSON file.
     */
    public void loadDatabase() {
        try {
            File file = new File(dbPath);
            if (file.exists()) {
                persons = objectMapper.readValue(file, Map.class);
                System.out.println("Loaded " + persons.size() + " persons from " + dbPath);
            } else {
                persons = new HashMap<>();
                System.out.println("Created new database at " + dbPath);
            }
        } catch (IOException e) {
            System.err.println("Error loading database: " + e.getMessage());
            persons = new HashMap<>();
        }
    }

    /**
     * Saves the database to JSON file.
     */
    public void saveDatabase() {
        try {
            File file = new File(dbPath);
            objectMapper.writeValue(file, persons);
            System.out.println("Saved " + persons.size() + " persons to " + dbPath);
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
        }
    }

    /**
     * Creates a backup of the database.
     */
    public String createBackup() {
        try {
            // Create backup directory if not exists
            Path backupDir = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
            }

            // Generate backup filename with timestamp
            String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupPath = BACKUP_DIR + "/database_" + timestamp + ".json";

            // Save to backup file
            File backupFile = new File(backupPath);
            objectMapper.writeValue(backupFile, persons);

            System.out.println("Created backup at " + backupPath);
            return backupPath;
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
            return null;
        }
    }

    /**
     * Restores database from a backup file.
     */
    public boolean restoreFromBackup(String backupPath) {
        try {
            File backupFile = new File(backupPath);
            if (!backupFile.exists()) {
                System.err.println("Backup file not found: " + backupPath);
                return false;
            }

            // Load from backup
            Map<String, Person> restored = objectMapper.readValue(backupFile, Map.class);
            persons = restored;

            // Save to main database
            saveDatabase();

            System.out.println("Restored database from " + backupPath);
            return true;
        } catch (IOException e) {
            System.err.println("Error restoring backup: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lists all available backups.
     */
    public List<String> listBackups() {
        try {
            Path backupDir = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupDir)) {
                return List.of();
            }

            return Files.list(backupDir)
                .filter(p -> p.toString().endsWith(".json"))
                .map(Path::toString)
                .sorted()
                .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error listing backups: " + e.getMessage());
            return List.of();
        }
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
     * Saves multiple persons.
     */
    public void saveAll(List<Person> persons) {
        for (Person person : persons) {
            save(person);
        }
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
     * Clears all data.
     */
    public void clear() {
        persons.clear();
    }

    /**
     * Exports database to a specific file.
     */
    public void exportToFile(String filePath) {
        try {
            File file = new File(filePath);
            objectMapper.writeValue(file, persons);
            System.out.println("Exported " + persons.size() + " persons to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting: " + e.getMessage());
        }
    }

    /**
     * Imports database from a specific file.
     */
    @SuppressWarnings("unchecked")
    public void importFromFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("Import file not found: " + filePath);
                return;
            }

            Map<String, Person> imported = objectMapper.readValue(file, Map.class);
            persons.putAll(imported);
            System.out.println("Imported " + imported.size() + " persons from " + filePath);
        } catch (IOException e) {
            System.err.println("Error importing: " + e.getMessage());
        }
    }
}
