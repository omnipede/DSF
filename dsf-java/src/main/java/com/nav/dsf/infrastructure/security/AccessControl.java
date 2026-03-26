package com.nav.dsf.infrastructure.security;

/**
 * Access control manager for DSF system.
 * Simulates ACF2 (Access Control Facility 2) security checks.
 * 
 * This replaces the PL/I ACF2 security routines (R0019999, K410C002).
 */
public class AccessControl {
    
    private final UserService userService;

    public AccessControl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Performs an initial resource check (IR).
     * Simulates ACF2 function code 'IR' check.
     * 
     * @param user The user to check
     * @param system The system code (e.g., 'DSF')
     * @param resource The resource name
     * @param function The function code
     * @return Return code: 0=OK, 4=Warning, 8=Denied, 12=Error, 16=System down
     */
    public int checkInitialResource(User user, String system, String resource, String function) {
        if (user == null) {
            return 12; // Error - no user
        }
        
        if (!user.isActive()) {
            return 8; // Denied - inactive user
        }
        
        // In real system, this would check ACF2 rules
        // For now, just check if user has any roles
        if (user.getRoles().isEmpty()) {
            return 8; // Denied - no roles
        }
        
        return 0; // OK
    }

    /**
     * Performs a role check (RO).
     * Simulates ACF2 function code 'RO' check.
     * 
     * @param user The user to check
     * @param system The system code
     * @param role The role to check
     * @param tknr The TKNR (trygdekontor number)
     * @return Return code: 0=OK, 4=Warning, 8=Denied, 12=Error
     */
    public int checkRole(User user, String system, String role, int tknr) {
        if (user == null) {
            return 12; // Error
        }
        
        Role selectedRole = Role.fromCode(role);
        if (selectedRole == null) {
            return 12; // Error - unknown role
        }
        
        if (!user.hasRole(selectedRole)) {
            return 8; // Denied - user doesn't have this role
        }
        
        if (!user.getTknrs().contains(tknr)) {
            return 8; // Denied - TKNR not valid for user
        }
        
        return 0; // OK
    }

    /**
     * Performs a function access check.
     * Simulates ACF2 function code check.
     * 
     * @param user The user to check
     * @param functionCode The function code (A, F, R, I, V, X)
     * @return Return code: 0=OK, 4=Warning, 8=Denied, 12=Error
     */
    public int checkFunctionAccess(User user, String functionCode) {
        if (user == null) {
            return 12; // Error
        }
        
        if (userService.hasFunctionAccess(user, functionCode)) {
            return 0; // OK
        }
        
        return 8; // Denied
    }

    /**
     * Gets error message for a return code.
     * 
     * @param rc The return code
     * @return Error message
     */
    public String getErrorMessage(int rc) {
        switch (rc) {
            case 0:
                return "OK - Access granted";
            case 4:
                return "WARNING - Contact IT department for access";
            case 8:
                return "DENIED - You do not have access to DSF";
            case 12:
                return "ERROR - Unknown function, contact IT department";
            case 16:
                return "SYSTEM ERROR - ATK system is down, contact IT department";
            default:
                return "UNKNOWN ERROR - Contact IT department";
        }
    }

    /**
     * Gets Norwegian error message for a return code.
     * 
     * @param rc The return code
     * @return Norwegian error message
     */
    public String getNorwegianErrorMessage(int rc) {
        switch (rc) {
            case 0:
                return "OK - Tilgang innvilget";
            case 4:
                return "KONTAKT IT AVD. FOR TILGANG";
            case 8:
                return "DU HAR IKKE TILGANG TIL DSF";
            case 12:
                return "UKJENT FUNKSJON KONTAKT IT AVD.";
            case 16:
                return "ATK SYSTEM ER NEDE KONTAKT IT AVD.";
            default:
                return "AKT FEIL KONTAKT IT AVD.";
        }
    }
}
