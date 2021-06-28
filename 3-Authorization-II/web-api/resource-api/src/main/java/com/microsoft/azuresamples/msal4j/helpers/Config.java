// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.azuresamples.msal4j.helpers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads properties file when the servlet starts.
 * MSAL Java apps using this sample repo's paradigm will require this.
 */

public class Config {
    private static Logger logger = Logger.getLogger(Config.class.getName());
    private static Properties props = instantiateProperties();
    private static final String[] REQUIRED = {"aad.authority", "aad.clientId", "aad.scopes"}; // scopes required for this sample (User.Read)
    private static final List<String> REQ_PROPS = Arrays.asList(REQUIRED);

    private static Properties instantiateProperties() {
        final Properties props = new Properties();
        try {
            props.load(Config.class.getClassLoader().getResourceAsStream("authentication.properties"));
        } catch (final IOException ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "Could not load properties file. Exiting");
            logger.log(Level.SEVERE, Arrays.toString(ex.getStackTrace()));
            System.exit(1);
            return null;
        }
        return props;
    }

    public static final String AUTHORITY = Config.getProperty("aad.authority");
    public static final String CLIENT_ID = Config.getProperty("aad.clientId");
    public static final String SCOPES = Config.getProperty("aad.scopes");
    public static final String VERSION = Config.getProperty("aad.version");

    public static String getProperty(final String key) {
        String prop = null;
        if (props != null) {
            prop = Config.props.getProperty(key);
            if (prop != null) {
                Config.logger.log(Level.FINE, "{0} is {1}", new String[] { key, prop });
                return prop;
            } else if (REQ_PROPS.contains(key)) {
                Config.logger.log(Level.SEVERE, "FATAL: Could not load required key {0} from config! EXITING", key);
                System.exit(1); // HANDLE THIS BETTER IN YOUR APP.
                return null;
            } else {
                Config.logger.log(Level.WARNING, "Could not load {0}!", key);
                return "";
            }
        } else {
            Config.logger.log(Level.SEVERE, "FATAL: Could not load property reader! EXITING!");
            System.exit(1);
            return null;
        }
    }

}