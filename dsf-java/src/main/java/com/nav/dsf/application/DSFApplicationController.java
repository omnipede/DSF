package com.nav.dsf.application;

import com.nav.dsf.domain.model.Person;
import com.nav.dsf.domain.service.PersonService;
import com.nav.dsf.domain.service.ValidationService;
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
    
    private User currentUser;
    private Role selectedRole;
    private AppState currentState;
    private boolean running;

    public DSFApplicationController() throws Exception {
        this.terminal = new TerminalUI();
        this.userService = new UserService();
        this.accessControl = new AccessControl(userService);
        this.personService = new PersonService(new com.nav.dsf.domain.repository.MemoryDatabase());
        this.validationService = new ValidationService();
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
        
        if (regCode == null || regCode.equalsIgnoreCase("XX")) {
            currentState = AppState.MAIN_MENU;
            return;
        }
        
        if (!validationService.validateRegCode(regCode)) {
            terminal.showError("UGYLDIG STYREKODE: " + regCode);
            terminal.pressEnterToContinue();
            return;
        }
        
        terminal.showMessage("REGISTRERING", "Registreringsfunksjon for " + regCode + 
            "\n\nIkke implementert i denne versjonen.");
        
        currentState = AppState.MAIN_MENU;
    }

    /**
     * Handles application exit.
     */
    private void handleExit() {
        ScreenRenderer.renderExitScreen(terminal);
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
