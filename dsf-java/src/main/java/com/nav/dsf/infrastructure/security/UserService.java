package com.nav.dsf.infrastructure.security;

import java.util.*;

/**
 * User service for managing DSF users.
 * Simulates ACF2 user authentication and authorization.
 * 
 * This replaces the PL/I ACF2 security calls (K410C002 program).
 */
public class UserService {
    
    private final Map<String, User> users;
    private User currentUser;

    public UserService() {
        this.users = new HashMap<>();
        initializeDemoUsers();
    }

    /**
     * Initializes demo users for testing.
     */
    private void initializeDemoUsers() {
        // Demo user with full access
        User demoUser = User.createDemoUser();
        users.put("DEMO", demoUser);

        // Test users with various roles
        User user1 = User.createTestUser("JDA2970");
        user1.setName("Jon Demo Andersen");
        users.put("JDA2970", user1);

        User user2 = new User("SPA7339", "Siri Pettersen Andersen");
        user2.setRoles(Arrays.asList(Role.SENIOR_SAKSBEHANDLER, Role.SAKSBEHANDLER));
        user2.addTknr(1);
        user2.addTknr(2);
        users.put("SPA7339", user2);

        User user3 = new User("TSB2970", "Tone Solberg Berg");
        user3.setRoles(Arrays.asList(
            Role.AP_SAKSBEHANDLER,
            Role.UP_SAKSBEHANDLER,
            Role.SAKSBEHANDLER
        ));
        user3.addTknr(1);
        users.put("TSB2970", user3);

        User user4 = new User("VEILEDER1", "Ola Veileder");
        user4.setRoles(Collections.singletonList(Role.VEILEDER));
        user4.addTknr(1);
        users.put("VEILEDER1", user4);
    }

    /**
     * Authenticates a user by user ID.
     * In the real system, this would check against ACF2.
     * 
     * @param userId The user ID to authenticate
     * @return User if found and active, null otherwise
     */
    public User authenticate(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }

        userId = userId.trim().toUpperCase();
        User user = users.get(userId);
        
        if (user != null && user.isActive()) {
            currentUser = user;
            return user;
        }
        
        return null;
    }

    /**
     * Validates if a user has access to a specific function.
     * Simulates ACF2 function code check.
     * 
     * @param user The user to check
     * @param functionCode The function code (A, F, R, I, V, X)
     * @return true if authorized, false otherwise
     */
    public boolean hasFunctionAccess(User user, String functionCode) {
        if (user == null || functionCode == null) {
            return false;
        }

        // All authenticated users can use basic functions
        switch (functionCode.toUpperCase()) {
            case "F": // Forespørsel (Inquiry)
            case "X": // Avslutt (Exit)
                return true;
            case "A": // Administrasjon
                return user.hasAnyRole(Role.SYSTEMANSVARLIG, Role.SUPERBRUKER, Role.SENIOR_SAKSBEHANDLER);
            case "R": // Registrering
            case "I": // Justering inntekter
            case "V": // Ventetrans-behandling
                return user.hasAnyRole(
                    Role.SAKSBEHANDLER,
                    Role.SENIOR_SAKSBEHANDLER,
                    Role.AP_SAKSBEHANDLER,
                    Role.UP_SAKSBEHANDLER,
                    Role.EP_SAKSBEHANDLER,
                    Role.FB_SAKSBEHANDLER,
                    Role.BP_SAKSBEHANDLER,
                    Role.FT_SAKSBEHANDLER,
                    Role.YK_SAKSBEHANDLER,
                    Role.AF_SAKSBEHANDLER
                );
            default:
                return false;
        }
    }

    /**
     * Gets the current logged-in user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Gets available roles for a user.
     */
    public List<Role> getAvailableRoles(User user) {
        if (user == null) {
            return Collections.emptyList();
        }
        return user.getRoles();
    }

    /**
     * Gets available TKNRs for a user.
     */
    public List<Integer> getAvailableTknrs(User user) {
        if (user == null) {
            return Collections.emptyList();
        }
        return user.getTknrs();
    }

    /**
     * Checks if user exists.
     */
    public boolean userExists(String userId) {
        return users.containsKey(userId);
    }

    /**
     * Gets a user by ID.
     */
    public User getUser(String userId) {
        return users.get(userId);
    }

    /**
     * Adds a new user (for testing).
     */
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    /**
     * Validates role selection.
     * Simulates ACF2 role validation (K410C002 with function 'RO').
     * 
     * @param user The user
     * @param selectedRole The selected role
     * @param tknr The TKNR
     * @return true if valid, false otherwise
     */
    public boolean validateRoleSelection(User user, Role selectedRole, int tknr) {
        if (user == null || selectedRole == null) {
            return false;
        }
        
        // Check if user has the role
        if (!user.hasRole(selectedRole)) {
            return false;
        }
        
        // Check if TKNR is valid for user
        if (!user.getTknrs().contains(tknr)) {
            return false;
        }
        
        return true;
    }
}
