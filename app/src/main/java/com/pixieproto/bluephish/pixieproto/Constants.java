package com.pixieproto.bluephish.pixieproto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by nadunoorup on 4/17/17.
 */

public class Constants {
    // indices for intent
    public static final long EMAIL_INDEX = 2 << 0;
    public static final long PHONE_NO_INDEX = 2 << 1;
    public static final long ADDRESS_INDEX = 2 << 2;
    public static final long URL_INDEX = 2 << 3;
    public static final long NAME_INDEX = 2 << 4;

    // Contexts
    public static final String EMAIL = "EMAIL";
    public static final String PHONENUMBER = "PHONENUMBER";
    public static final String ADDRESS = "ADDRESS";
    public static final String URL = "URL";
    public static final String COMPLETETEXT = "COMPLETETEXT";

    // intent type
    public static final String SAVE_TO_CONTACTS = "SAVE TO CONTACTS";
    public static final String MAIL = "MAIL";
    public static final String SHARE = "SHARE";
    public static final String CLIPBOARD = "CLIPBOARD";
    public static final String CALL = "CALL";
    public static final String MESSAGE = "MESSAGE";
    public static final String OPEN = "OPEN";
    public static final String SAVE = "SAVE";


    public static final HashMap<String, ArrayList> MAP_METADATA_TO_BUTTONS = new HashMap<String, ArrayList>();
    static {
        ArrayList<String> emailButtons = new ArrayList<String>(Arrays.asList(MAIL, SAVE_TO_CONTACTS, SHARE, CLIPBOARD));
        MAP_METADATA_TO_BUTTONS.put(EMAIL, emailButtons);

        ArrayList<String> phoneNumberButtons = new ArrayList<String>(Arrays.asList(CALL, SAVE_TO_CONTACTS, MESSAGE, SHARE, CLIPBOARD));
        MAP_METADATA_TO_BUTTONS.put(PHONENUMBER, phoneNumberButtons);

        ArrayList<String> addrsButtons = new ArrayList<String>(Arrays.asList(SAVE_TO_CONTACTS, SHARE, CLIPBOARD));
        MAP_METADATA_TO_BUTTONS.put(ADDRESS, addrsButtons);

        ArrayList<String> urlButtons = new ArrayList<String>(Arrays.asList(OPEN, SAVE_TO_CONTACTS, SHARE, CLIPBOARD));
        MAP_METADATA_TO_BUTTONS.put(URL, urlButtons);

        ArrayList<String> completeTextButtons = new ArrayList<String>(Arrays.asList(SAVE, SHARE, CLIPBOARD));
        MAP_METADATA_TO_BUTTONS.put(COMPLETETEXT, completeTextButtons);
    }
}