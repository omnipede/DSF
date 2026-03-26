package com.nav.dsf.infrastructure.datafile;

import com.nav.dsf.domain.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JsonFileRepository.
 */
class JsonFileRepositoryTest {

    @TempDir
    Path tempDir;

    private JsonFileRepository repository;
    private String testDbPath;

    @BeforeEach
    void setUp() {
        testDbPath = tempDir.resolve("test_database.json").toString();
        repository = new JsonFileRepository(testDbPath);
    }

    @Test
    @DisplayName("새 데이터베이스 생성")
    void constructor_NewDatabase() {
        // Given & When - repository created in setUp

        // Then
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("사람 저장 성공")
    void save_Success() {
        // Given
        Person person = new Person("01010100131", "Ola Nordmann");

        // When
        Person saved = repository.save(person);

        // Then
        assertNotNull(saved);
        assertEquals(1, repository.count());
        assertTrue(repository.exists("01010100131"));
    }

    @Test
    @DisplayName("FNR 로 사람 찾기 - 존재함")
    void findByFnr_Exists() {
        // Given
        Person person = new Person("01010100131", "Ola");
        repository.save(person);

        // When
        var foundOpt = repository.findByFnr("01010100131");

        // Then
        assertTrue(foundOpt.isPresent());
        assertEquals("Ola", foundOpt.get().getName());
    }

    @Test
    @DisplayName("FNR 로 사람 찾기 - 존재하지 않음")
    void findByFnr_NotExists() {
        // When
        var foundOpt = repository.findByFnr("99999999999");

        // Then
        assertFalse(foundOpt.isPresent());
    }

    @Test
    @DisplayName("데이터베이스 저장 및 로드")
    void saveAndLoadDatabase() {
        // Given
        Person person1 = new Person("01010100131", "Ola");
        Person person2 = new Person("13050300182", "Per");
        repository.save(person1);
        repository.save(person2);

        // When
        repository.saveDatabase();

        // Then - create new repository and load
        JsonFileRepository repository2 = new JsonFileRepository(testDbPath);
        assertEquals(2, repository2.count());
    }

    @Test
    @DisplayName("백업 생성")
    void createBackup() {
        // Given
        Person person = new Person("01010100131", "Ola");
        repository.save(person);

        // When
        String backupPath = repository.createBackup();

        // Then
        assertNotNull(backupPath);
        assertTrue(new File(backupPath).exists());
    }

    @Test
    @DisplayName("백업에서 복원")
    void restoreFromBackup() {
        // Given
        Person person = new Person("01010100131", "Ola");
        repository.save(person);
        repository.saveDatabase();
        String backupPath = repository.createBackup();
        
        // Clear and modify
        repository.clear();
        assertEquals(0, repository.count());

        // When
        boolean restored = repository.restoreFromBackup(backupPath);

        // Then
        assertTrue(restored);
        assertTrue(repository.exists("01010100131"));
    }

    @Test
    @DisplayName("사람 삭제")
    void delete_Success() {
        // Given
        Person person = new Person("01010100131", "Ola");
        repository.save(person);

        // When
        boolean deleted = repository.delete("01010100131");

        // Then
        assertTrue(deleted);
        assertFalse(repository.exists("01010100131"));
    }

    @Test
    @DisplayName("모든 사람 가져오기")
    void getAllPersons() {
        // Given
        Person person1 = new Person("01010100131", "Ola");
        Person person2 = new Person("13050300182", "Per");
        repository.save(person1);
        repository.save(person2);

        // When
        Map<String, Person> all = repository.getAllPersons();

        // Then
        assertNotNull(all);
        assertEquals(2, all.size());
        assertTrue(all.containsKey("01010100131"));
        assertTrue(all.containsKey("13050300182"));
    }

    @Test
    @DisplayName("파일로 내보내기")
    void exportToFile() {
        // Given
        Person person = new Person("01010100131", "Ola");
        repository.save(person);
        String exportPath = tempDir.resolve("export.json").toString();

        // When
        repository.exportToFile(exportPath);

        // Then
        assertTrue(new File(exportPath).exists());
    }

    @Test
    @DisplayName("파일에서 가져오기")
    void importFromFile() {
        // Given
        Person person = new Person("01010100131", "Ola");
        repository.save(person);
        repository.saveDatabase();
        
        // Create new repository
        JsonFileRepository repository2 = new JsonFileRepository(tempDir.resolve("test2.json").toString());

        // When
        repository2.importFromFile(testDbPath);

        // Then
        assertEquals(1, repository2.count());
        assertTrue(repository2.exists("01010100131"));
    }

    @Test
    @DisplayName("저장 - null 예외")
    void save_NullPerson() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
    }
}
