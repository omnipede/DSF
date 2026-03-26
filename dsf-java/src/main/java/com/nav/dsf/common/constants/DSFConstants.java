package com.nav.dsf.common.constants;

/**
 * DSF Application Constants.
 * 
 * Contains system-wide constants mapped from the original PL/I system.
 */
public class DSFConstants {

    // Transaction codes (TRANSKODE)
    public static final String TRANS_R010 = "R010"; // Start
    public static final String TRANS_R020 = "R020"; // User validation
    public static final String TRANS_R030 = "R030"; // Function selection
    public static final String TRANS_R040 = "R040"; // Registration menu
    public static final String TRANS_F410 = "F410"; // Inquiry
    public static final String TRANS_RA20 = "RA20"; // Administration
    public static final String TRANS_R0I1 = "R0I1"; // Income adjustment
    public static final String TRANS_R048 = "R048"; // Waiting transactions

    // Function codes
    public static final String FUNC_ADMINISTRASJON = "A";
    public static final String FUNC_FORESPORSEL = "F";
    public static final String FUNC_REGISTRERING = "R";
    public static final String FUNC_JUSTERING = "I";
    public static final String FUNC_VENTETRANS = "V";
    public static final String FUNC_AVSLUTT = "X";

    // Registration codes (STYREKODE)
    public static final String REG_AP = "AP"; // Alderspensjon
    public static final String REG_UP = "UP"; // Uførepensjon
    public static final String REG_EP = "EP"; // Etterlatt ektefelle
    public static final String REG_FB = "FB"; // Foreldreløse barn
    public static final String REG_BP = "BP"; // Etterlatte barn
    public static final String REG_FT = "FT"; // Forsørgingstillegg
    public static final String REG_TG = "TG"; // Tilleggsblankett
    public static final String REG_E1 = "E1"; // Endringsblankett-1
    public static final String REG_O1 = "O1"; // Opphørsblankett-1
    public static final String REG_O2 = "O2"; // Opphørsblankett-2
    public static final String REG_AF = "AF"; // AFP
    public static final String REG_YK = "YK"; // Yrkesskade
    public static final String REG_UF = "UF"; // Unge uføre

    // System identifiers
    public static final String SYSTEM_NAME = "DSF";
    public static final String SYSTEM_FULL_NAME = "Det Sentrale Folketrygdsystemet";
    public static final String VERSION = "1.0.0";

    // Date formats
    public static final String DATE_FORMAT_COMPACT = "yyyyMMdd";
    public static final String DATE_FORMAT_NORWEGIAN = "yyyy.MM.dd";
    public static final String DATE_FORMAT_SHORT = "dd.MM.yyyy";

    // Field lengths
    public static final int FNR_LENGTH = 11;
    public static final int USER_ID_LENGTH = 8;
    public static final int NAME_LENGTH = 25;
    public static final int ROLE_LENGTH = 4;
    public static final int TKNR_LENGTH = 5;

    // Screen dimensions
    public static final int SCREEN_WIDTH_DEFAULT = 70;
    public static final int SCREEN_HEIGHT_DEFAULT = 24;

    // ACF2 return codes
    public static final int ACF2_RC_OK = 0;
    public static final int ACF2_RC_WARNING = 4;
    public static final int ACF2_RC_DENIED = 8;
    public static final int ACF2_RC_ERROR = 12;
    public static final int ACF2_RC_SYSTEM_DOWN = 16;

    // Pension type codes
    public static final String PENSJON_AP = "AP"; // Alderspensjon
    public static final String PENSJON_UP = "UP"; // Uførepensjon
    public static final String PENSJON_EP = "EP"; // Etterlattepensjon ektefelle
    public static final String PENSJON_FB = "FB"; // Foreldreløse barn
    public static final String PENSJON_BP = "BP"; // Etterlatte barn
    public static final String PENSJON_FT = "FT"; // Forsørgingstillegg
    public static final String PENSJON_YK = "YK"; // Yrkesskadepensjon

    // Language codes
    public static final String LANG_NORSK = "N";
    public static final String LANG_ENGELSK = "E";
    public static final String LANG_SAMISK = "S";

    // Gender codes
    public static final String GENDER_MALE = "M";
    public static final String GENDER_FEMALE = "F";

    // Status codes
    public static final char STATUS_ACTIVE = ' ';
    public static final char STATUS_DECEASED = 'X';
    public static final char STATUS_TERMINATED = 'O';

    private DSFConstants() {
        // Private constructor to prevent instantiation
    }
}
