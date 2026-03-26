package com.nav.dsf;

import com.nav.dsf.application.DSFApplicationController;

/**
 * DSF Application - Main Entry Point
 * 
 * Java port of the Norwegian National Insurance System (DSF).
 * Originally operated 1967-2018, written in PL/I for IBM mainframe with CICS.
 * 
 * This Java version provides a terminal-based interface that mimics
 * the original CICS screen flow.
 * 
 * @author NAV Port Team
 * @version 1.0.0
 */
public class DSFApplication {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║   DSF - Det Sentrale Folketrygdsystemet                  ║");
        System.out.println("║   Java Port v1.0.0                                       ║");
        System.out.println("║                                                          ║");
        System.out.println("║   Original system: 1967-2018                             ║");
        System.out.println("║   Ported from PL/I + CICS to Java                        ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        try {
            DSFApplicationController controller = new DSFApplicationController();
            controller.run();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("Takk for nå!");
        System.out.println();
    }
}
