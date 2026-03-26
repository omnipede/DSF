package com.nav.dsf.infrastructure.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User representation for DSF system.
 * Maps to ACF2 user records.
 */
public class User {
    private final String userId;
    private String name;
    private List<Role> roles;
    private List<Integer> tknrs;  // Trygdekontor numbers
    private boolean active;
    private String cicsName;

    public User(String userId) {
        this.userId = userId;
        this.roles = new ArrayList<>();
        this.tknrs = new ArrayList<>();
        this.active = true;
        this.cicsName = "RA001";
    }

    public User(String userId, String name) {
        this(userId);
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public boolean hasAnyRole(Role... roles) {
        for (Role role : roles) {
            if (this.roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> getTknrs() {
        return tknrs;
    }

    public void setTknrs(List<Integer> tknrs) {
        this.tknrs = tknrs;
    }

    public void addTknr(int tknr) {
        if (!this.tknrs.contains(tknr)) {
            this.tknrs.add(tknr);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCicsName() {
        return cicsName;
    }

    public void setCicsName(String cicsName) {
        this.cicsName = cicsName;
    }

    @Override
    public String toString() {
        return String.format("User{id=%s, name=%s, roles=%s, active=%b}",
            userId, name, roles, active);
    }

    /**
     * Creates a test user with sample data.
     */
    public static User createTestUser(String userId) {
        User user = new User(userId, "Test Bruker");
        user.setRoles(Arrays.asList(
            Role.SAKSBEHANDLER,
            Role.AP_SAKSBEHANDLER,
            Role.UP_SAKSBEHANDLER,
            Role.FB_SAKSBEHANDLER
        ));
        user.addTknr(1);
        user.addTknr(2);
        user.addTknr(3);
        user.setActive(true);
        return user;
    }

    /**
     * Creates a demo user with all roles.
     */
    public static User createDemoUser() {
        User user = new User("DEMO", "Demo Bruker");
        user.setRoles(Arrays.asList(values()));
        user.addTknr(1);
        user.setActive(true);
        return user;
    }

    private static Role[] values() {
        return Role.values();
    }
}
