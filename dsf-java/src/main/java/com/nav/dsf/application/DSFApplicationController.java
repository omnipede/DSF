package com.nav.dsf.application;

import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.model.Transaction;
import com.nav.dsf.domain.repository.MemoryDatabase;
import com.nav.dsf.domain.repository.TransactionRepository;
import com.nav.dsf.domain.service.AgePensionService;
import com.nav.dsf.domain.service.DisabilityPensionService;
import com.nav.dsf.domain.service.PersonService;
import com.nav.dsf.domain.service.TransactionService;
import com.nav.dsf.domain.service.ValidationService;
import com.nav.dsf.infrastructure.datafile.JsonFileRepository;
import com.nav.dsf.infrastructure.security.AccessControl;
import com.nav.dsf.infrastructure.security.Role;
import com.nav.dsf.infrastructure.security.User;
import com.nav.dsf.infrastructure.security.UserService;
import com.nav.dsf.infrastructure.terminal.InputHandler;
import com.nav.dsf.infrastructure.terminal.ScreenRenderer;
import com.nav.dsf.infrastructure.terminal.TerminalUI;

import java.util.List;
import java.util.Optional;

/**
 * Main application controller for DSF.
 * Handles the menu flow and user interactions.
 *
 * This replaces the PL/I main program flow:
 * R0010101 (Start) -> R0010201 (User validation) -> R0010301 (Function selection)
 */
public class DSFApplicationController {

    private final TerminalUI terminal;
    private final UserService userService;
    private final AccessControl accessControl;
    private final PersonService personService;
    private final ValidationService validationService;
    private final AgePensionService agePensionService;
    private final DisabilityPensionService disabilityPensionService;
    private final TransactionService transactionService;
    private final JsonFileRepository jsonRepository;

    private User currentUser;
    private Role selectedRole;
    private AppState currentState;
    private boolean running;

    public DSFApplicationController() throws Exception {
        this.terminal = new TerminalUI();
        this.userService = new UserService();
        this.accessControl = new AccessControl(userService);

        // Initialize database with JSON persistence
        JsonFileRepository jsonRepo = new JsonFileRepository();
        MemoryDatabase personDatabase = new MemoryDatabase();
        
        // Load persons from JSON if available, otherwise use demo data
        if (jsonRepo.count() > 0) {
            var allPersons = jsonRepo.getAllPersons();
            for (var person : allPersons.values()) {
                personDatabase.save(person);
            }
            System.out.println("Loaded " + jsonRepo.count() + " persons from JSON database");
        } else {
            System.out.println("Using demo data (no JSON database found)");
        }
        
        TransactionRepository txRepository = new TransactionRepository();

        this.personService = new PersonService(personDatabase);
        this.validationService = new ValidationService();
        this.agePensionService = new AgePensionService(validationService);
        this.disabilityPensionService = new DisabilityPensionService(validationService);
        this.transactionService = new TransactionService(txRepository, personDatabase);
        
        // Store reference for saving
        this.jsonRepository = jsonRepo;

        this.currentState = AppState.START;
        this.running = true;
    }

    /**
     * Main application loop.
     */
    public void run() {
        try {
            while (running) {
                switch (currentState) {
                    case START:
                    case LOGIN:
                        handleLogin();
                        break;
                    case ROLE_SELECTION:
                        handleRoleSelection();
                        break;
                    case MAIN_MENU:
                        handleMainMenu();
                        break;
                    case INQUIRY:
                        handleInquiry();
                        break;
                    case REGISTRATION_MENU:
                        handleRegistrationMenu();
                        break;
                    case EXIT:
                        handleExit();
                        break;
                    default:
                        currentState = AppState.MAIN_MENU;
                }
            }
        } catch (Exception e) {
            terminal.showError("Application error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    /**
     * Handles the login screen (R0010101 equivalent).
     */
    private void handleLogin() {
        ScreenRenderer.renderLoginScreen(terminal, "RA001");
        terminal.println();
        
        String userId = InputHandler.readString(terminal, "BRUKER-ID", 8);
        
        if (userId == null) {
            // User pressed Ctrl+C or similar
            currentState = AppState.EXIT;
            return;
        }
        
        if (userId.equalsIgnoreCase("F3") || userId.equalsIgnoreCase("EXIT")) {
            currentState = AppState.EXIT;
            return;
        }
        
        // Authenticate user
        currentUser = userService.authenticate(userId);
        
        if (currentUser == null) {
            terminal.showError("UGYLDIG BRUKERIDENT");
            terminal.pressEnterToContinue();
            currentState = AppState.LOGIN;
            return;
        }
        
        // Check if user has any roles
        List<Role> roles = userService.getAvailableRoles(currentUser);
        if (roles.isEmpty()) {
            terminal.showError("INGEN ROLLER TILGJENGELIG");
            terminal.pressEnterToContinue();
            currentState = AppState.LOGIN;
            return;
        }
        
        currentState = AppState.ROLE_SELECTION;
    }

    /**
     * Handles role selection (part of R0010201).
     */
    private void handleRoleSelection() {
        List<Role> roles = userService.getAvailableRoles(currentUser);
        List<Integer> tknrs = userService.getAvailableTknrs(currentUser);
        
        String[] roleStrings = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            roleStrings[i] = String.format("%-40s %s", role.getCode(), role.getDescription());
        }
        
        ScreenRenderer.renderRoleSelection(terminal, currentUser.getUserId(), 
            currentUser.getCicsName(), roles, tknrs);
        
        String input = terminal.readLine("VALG");
        
        if (input == null || input.equalsIgnoreCase("F3")) {
            currentState = AppState.LOGIN;
            return;
        }
        
        try {
            int selection = Integer.parseInt(input.trim());
            if (selection >= 1 && selection <= roles.size()) {
                selectedRole = roles.get(selection - 1);
                
                // Validate role selection
                int rc = accessControl.checkRole(currentUser, "DSF", 
                    selectedRole.getCode(), tknrs.get(0));
                
                if (rc != 0) {
                    terminal.showError(accessControl.getNorwegianErrorMessage(rc));
                    terminal.pressEnterToContinue();
                    return;
                }
                
                currentState = AppState.MAIN_MENU;
            } else {
                terminal.showError("UGYLDIG VALG");
                terminal.pressEnterToContinue();
            }
        } catch (NumberFormatException e) {
            terminal.showError("UGYLDIG INPUT");
            terminal.pressEnterToContinue();
        }
    }

    /**
     * Handles the main menu (R0010301 equivalent).
     */
    private void handleMainMenu() {
        ScreenRenderer.renderMainMenu(terminal, currentUser.getUserId(),
            currentUser.getCicsName(), selectedRole != null ? selectedRole.getCode() : "");

        Character functionCode = InputHandler.readChoice(terminal, "TAST KODE",
            new char[]{'A', 'F', 'R', 'I', 'V', 'X'});

        if (functionCode == null) {
            return;
        }

        // Check function access
        int rc = accessControl.checkFunctionAccess(currentUser, functionCode.toString());
        if (rc != 0) {
            terminal.showError(accessControl.getNorwegianErrorMessage(rc));
            terminal.pressEnterToContinue();
            return;
        }

        switch (functionCode) {
            case 'A':
                terminal.showMessage("ADMINISTRASJON", "Administrasjonsfunksjoner er ikke implementert i denne versjonen.");
                break;
            case 'F':
                currentState = AppState.INQUIRY;
                break;
            case 'R':
                currentState = AppState.REGISTRATION_MENU;
                break;
            case 'I':
                terminal.showMessage("JUSTERING INNTEKTER", "Inntektsjustering er ikke implementert i denne versjonen.");
                break;
            case 'V':
                terminal.showMessage("VENTETRANS", "Ventetrans-behandling er ikke implementert i denne versjonen.");
                break;
            case 'X':
                currentState = AppState.EXIT;
                break;
        }
    }

    /**
     * Handles person inquiry (F410 / R0010410 equivalent).
     */
    private void handleInquiry() {
        ScreenRenderer.renderInquiryScreen(terminal, currentUser.getUserId(),
            currentUser.getCicsName(), selectedRole != null ? selectedRole.getCode() : "", null);
        
        String fnr = InputHandler.readFNR(terminal, "FØDSELSNUMMER");
        
        if (fnr == null || fnr.isEmpty()) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Look up person
        Optional<Person> personOpt = personService.findPerson(fnr);
        
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            ScreenRenderer.renderPersonInfo(terminal, currentUser.getUserId(),
                currentUser.getCicsName(), selectedRole != null ? selectedRole.getCode() : "",
                person.toDisplayString());
            terminal.pressEnterToContinue();
        } else {
            terminal.showError("PERSON IKKE FUNNET: " + fnr);
            terminal.pressEnterToContinue();
        }
        
        currentState = AppState.MAIN_MENU;
    }

    /**
     * Handles registration menu (R0010401 equivalent).
     */
    private void handleRegistrationMenu() {
        ScreenRenderer.renderRegistrationMenu(terminal, currentUser.getUserId(),
            currentUser.getCicsName(), selectedRole != null ? selectedRole.getCode() : "");
        
        String regCode = InputHandler.readString(terminal, "STYREKODE", 2);
        
        if (regCode == null || regCode.trim().isEmpty()) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        regCode = regCode.trim().toUpperCase();
        
        // Check for exit code
        if (regCode.equals("XX")) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Validate registration code
        if (!validationService.validateRegCode(regCode)) {
            terminal.showError("UGYLDIG STYREKODE: " + regCode);
            terminal.pressEnterToContinue();
            return;
        }
        
        // Route to appropriate pension type handler
        routeToPensionType(regCode);
    }

    /**
     * Routes to the appropriate pension type handler.
     */
    private void routeToPensionType(String regCode) {
        switch (regCode) {
            case "AP":
                handleAgePension();
                break;
            case "UP":
                handleDisabilityPension();
                break;
            case "EP":
                terminal.showMessage("ETTERLATTPENSJON EKTEFELLE", "Etterlattpensjon ektefelle registrering\n\nIkke implementert ennå.");
                break;
            case "FB":
                terminal.showMessage("FORELDRELØSE BARN", "Foreldreløse barn registrering\n\nIkke implementert ennå.");
                break;
            case "BP":
                terminal.showMessage("ETTERLATTE BARN", "Etterlatte barn registrering\n\nIkke implementert ennå.");
                break;
            case "FT":
                terminal.showMessage("FORSØRGINGSTILLEG", "Forsørgingstillegg registrering\n\nIkke implementert ennå.");
                break;
            case "TG":
                terminal.showMessage("TILLEGGSBLANKETT", "Tilleggsblankett registrering\n\nIkke implementert ennå.");
                break;
            case "E1":
                terminal.showMessage("ENDRINGSBLANKETT-1", "Endringsblankett-1 registrering\n\nIkke implementert ennå.");
                break;
            case "O1":
                terminal.showMessage("OPPHØRSBLANKETT-1", "Opphørsblankett-1 registrering\n\nIkke implementert ennå.");
                break;
            case "O2":
                terminal.showMessage("OPPHØRSBLANKETT-2", "Opphørsblankett-2 registrering\n\nIkke implementert ennå.");
                break;
            case "AF":
                terminal.showMessage("AFP", "AFP (Avtalefestet pensjon) registrering\n\nIkke implementert ennå.");
                break;
            case "YK":
                terminal.showMessage("YRKESKADE", "Yrkesskade registrering\n\nIkke implementert ennå.");
                break;
            case "UF":
                terminal.showMessage("UNGE UFØRE", "Unge uføre før 1967 registrering\n\nIkke implementert ennå.");
                break;
            default:
                terminal.showMessage("REGISTRERING", "Registrering for " + regCode + "\n\nIkke implementert ennå.");
        }
        
        currentState = AppState.MAIN_MENU;
    }

    /**
     * Handles Age Pension (Alderspensjon) registration.
     * R0010501 equivalent - Main control program for AP blankett.
     * Uses R0012001 transaction processing.
     */
    private void handleAgePension() {
        // Step 1: Get FNR
        String fnr = InputHandler.readFNR(terminal, "FØDSELSNUMMER");
        if (fnr == null || fnr.trim().isEmpty()) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 2: Look up person
        var personOpt = personService.findByFnr(fnr);
        String name = personOpt.map(Person::getName).orElse("UKJENT");
        
        // Step 3: Check age eligibility
        String eligibilityStatus = agePensionService.getEligibilityStatus(fnr);
        terminal.showMessage("ALDERSKONTROLL", eligibilityStatus);
        
        if (!agePensionService.checkAgeEligibility(fnr)) {
            terminal.showError("IKKE BERETTIGET: Må være minst 67 år for alderspensjon");
            terminal.pressEnterToContinue();
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 4: Display AP registration screen
        ScreenRenderer.renderAPRegistration(terminal, currentUser.getUserId(), 
            currentUser.getCicsName(), selectedRole != null ? selectedRole.getCode() : "", fnr, name);
        
        // Step 5: Get insurance period (trygdetid)
        String trygdetid = InputHandler.readString(terminal, "TRYGDETID (ÅR)", 2);
        if (trygdetid == null || trygdetid.trim().isEmpty()) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 6: Get basic pension (grunnpensjon)
        String grunnpensjon = InputHandler.readString(terminal, "GRUNNPENSJON", 6);
        if (grunnpensjon == null) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 7: Get special text (særtekst)
        String saertekst = InputHandler.readString(terminal, "SÆRTEKST", 20);
        
        // Step 8: Create registration data
        AgePensionService.APRegistrationData data = agePensionService.createRegistrationData(
            fnr, name, trygdetid, grunnpensjon, saertekst);
        
        // Step 9: Create transaction (R0012001)
        Transaction transaction = transactionService.createTransaction(
            fnr, 
            "R050",  // AP transaction code
            "AP",    // Blankett type
            currentUser.getUserId(),
            data
        );
        
        // Step 10: Process transaction
        TransactionService.TransactionResult result = transactionService.processTransaction(transaction);
        
        if (!result.isSuccess()) {
            terminal.showError("TRANSAKSJON FEILET: " + result.getErrorMessage());
            terminal.pressEnterToContinue();
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 11: Display confirmation with transaction details
        Transaction tx = result.getTransaction();
        var updatedPerson = personService.findByFnr(fnr).orElse(null);
        
        String confirmation = String.format(
            "TRANSAKSJON: %s\n" +
            "FNR: %s\n" +
            "Navn: %s\n" +
            "Transkode: %s\n" +
            "Trygdetid: %d år\n" +
            "Grunnpensjon (GP): %d kr\n" +
            "Tilleggspensjon (TP): %d kr\n" +
            "Totalt: %d kr/år\n" +
            "Status: %s\n" +
            "\nRegistrering fullført.",
            tx.getId(),
            fnr,
            name,
            tx.getTransCode(),
            data.getTrygdetid(),
            updatedPerson != null ? updatedPerson.getAgePension().getGp() : 0,
            updatedPerson != null ? updatedPerson.getAgePension().getTp() : 0,
            (updatedPerson != null ? updatedPerson.getAgePension().getGp() : 0) + 
            (updatedPerson != null ? updatedPerson.getAgePension().getTp() : 0),
            tx.getStatus()
        );
        
        terminal.showMessage("ALDERSPENSJON REGISTRERT", confirmation);

        currentState = AppState.MAIN_MENU;
    }

    /**
     * Handles Disability Pension (Uførepensjon) registration.
     * R0010601 equivalent - Main control program for UP blankett.
     * Uses R0012001 transaction processing.
     */
    private void handleDisabilityPension() {
        // Step 1: Get FNR
        String fnr = InputHandler.readFNR(terminal, "FØDSELSNUMMER");
        if (fnr == null || fnr.trim().isEmpty()) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 2: Look up person
        var personOpt = personService.findByFnr(fnr);
        String name = personOpt.map(Person::getName).orElse("UKJENT");
        
        // Step 3: Display UP registration screen
        ScreenRenderer.renderUPRegistration(terminal, currentUser.getUserId(), 
            currentUser.getCicsName(), selectedRole != null ? selectedRole.getCode() : "", fnr, name);
        
        // Step 4: Get disability grade (uførhetsgrad)
        String uforhetsgrad = InputHandler.readString(terminal, "UFØRHETSGRAD (%)", 3);
        if (uforhetsgrad == null || uforhetsgrad.trim().isEmpty()) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 5: Validate disability grade
        int uforhet;
        try {
            uforhet = Integer.parseInt(uforhetsgrad.trim());
        } catch (NumberFormatException e) {
            terminal.showError("UFØRHETSGRAD må være et tall");
            terminal.pressEnterToContinue();
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        String eligibilityStatus = disabilityPensionService.getEligibilityStatus(uforhet);
        terminal.showMessage("UFØRHETSKONTROLL", eligibilityStatus);
        
        if (!disabilityPensionService.checkDisabilityEligibility(uforhet)) {
            terminal.showError("IKKE BERETTIGET: Må være minst 20% ufør");
            terminal.pressEnterToContinue();
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 6: Get disease code (sykdomskode)
        String sykdomskode = InputHandler.readString(terminal, "SYKDOMSKODE", 10);
        
        // Step 7: Get occupation code (yrkeskode)
        String yrkeskode = InputHandler.readString(terminal, "YRKESKODE (4 siffer)", 4);
        
        // Step 8: Get basic pension (grunnpensjon)
        String grunnpensjon = InputHandler.readString(terminal, "GRUNNPENSJON", 6);
        if (grunnpensjon == null) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 9: Get special text (særtekst)
        String saertekst = InputHandler.readString(terminal, "SÆRTEKST", 20);
        
        // Step 10: Create registration data
        DisabilityPensionService.UPRegistrationData data = disabilityPensionService.createRegistrationData(
            fnr, name, uforhetsgrad, sykdomskode, yrkeskode, grunnpensjon, saertekst);
        
        // Step 11: Create transaction (R0012001)
        Transaction transaction = transactionService.createTransaction(
            fnr, 
            "R060",  // UP transaction code
            "UP",    // Blankett type
            currentUser.getUserId(),
            data
        );
        
        // Step 12: Process transaction
        TransactionService.TransactionResult result = transactionService.processTransaction(transaction);
        
        if (!result.isSuccess()) {
            terminal.showError("TRANSAKSJON FEILET: " + result.getErrorMessage());
            terminal.pressEnterToContinue();
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        // Step 13: Display confirmation with transaction details
        Transaction tx = result.getTransaction();
        var updatedPerson = personService.findByFnr(fnr).orElse(null);
        
        String confirmation = String.format(
            "TRANSAKSJON: %s\n" +
            "FNR: %s\n" +
            "Navn: %s\n" +
            "Transkode: %s\n" +
            "UFØRHETSGRAD: %d%%\n" +
            "SYKDOMSKODE: %s\n" +
            "YRKESKODE: %s\n" +
            "Grunnpensjon (GP): %d kr\n" +
            "Tilleggspensjon (TP): %d kr\n" +
            "Totalt: %d kr/år\n" +
            "Status: %s\n" +
            "\nRegistrering fullført.",
            tx.getId(),
            fnr,
            name,
            tx.getTransCode(),
            data.getUforhetsgrad(),
            data.getSykdomskode() != null ? data.getSykdomskode() : "INGEN",
            data.getYrkeskode() != null ? data.getYrkeskode() : "INGEN",
            updatedPerson != null ? updatedPerson.getDisabilityPension().getGp() : 0,
            updatedPerson != null ? updatedPerson.getDisabilityPension().getTp() : 0,
            (updatedPerson != null ? updatedPerson.getDisabilityPension().getGp() : 0) + 
            (updatedPerson != null ? updatedPerson.getDisabilityPension().getTp() : 0),
            tx.getStatus()
        );
        
        terminal.showMessage("UFØREPENSJON REGISTRERT", confirmation);
        
        currentState = AppState.MAIN_MENU;
    }

    /**
     * Handles application exit.
     */
    private void handleExit() {
        ScreenRenderer.renderExitScreen(terminal);
        
        // Save all data to JSON before exiting
        if (jsonRepository != null) {
            System.out.println("\nSaving database...");
            var allPersons = personService.getAllPersons();
            for (var person : allPersons.values()) {
                jsonRepository.save(person);
            }
            jsonRepository.saveDatabase();
            jsonRepository.createBackup();
            System.out.println("Database saved successfully.");
        }
        
        running = false;
    }

    /**
     * Cleans up resources.
     */
    private void cleanup() {
        try {
            terminal.close();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    /**
     * Gets the current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets the current application state.
     */
    public AppState getCurrentState() {
        return currentState;
    }
}
