package edu.ucar.unidata.rosetta.util;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class RosettaProperties {

    private static String defaultConfigFileName = "defaultRosettaConfig.properties";
    private static String configFileName = "rosettaConfig.properties";
    // if this property does not exist, set to null because you cannot continue
    private static String ROSETTA_HOME = System.getProperty(
            "rosetta.content.root.path", null);

    protected static Logger logger = Logger.getLogger(RosettaProperties.class);

    public RosettaProperties() {
        if (ROSETTA_HOME == null) {
            logger.error("You must set a location for the rosetta content directory:");
            logger.error("    -Drosetta.content.root.path=/path/to/dir");
            throw new RuntimeException("You must set a location for the rosetta content directory. Set the java VM option -Drosetta.content.root.path");
        }
    }

    public static String getDownloadDir() {
        Properties props = getRosettaProps();
        String downloadDirProp = props.getProperty("downloadDir");
        File downloadDir = new File(FilenameUtils.concat(
                getDefaultRosettaHome(), downloadDirProp));
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        return downloadDir.getAbsolutePath();
    }

    public static String getUploadDir() {
        Properties props = getRosettaProps();
        String uploadDirProp = props.getProperty("uploadDir");
        File uploadDir = new File(FilenameUtils.concat(getDefaultRosettaHome(),
                uploadDirProp));
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        return uploadDir.getAbsolutePath();
    }

    // only private methods below here

    private static String getDefaultRosettaHome() {
        return ROSETTA_HOME;
    }

    private static String getConfigFile() {
        String configFileLoc = ROSETTA_HOME;
        String config = FilenameUtils.concat(configFileLoc, configFileName);
        File configFile = new File(config);
        if (!configFile.exists()) {
            getDefaultConfigFile();
        }
        return config;
    }

    private static String getDefaultConfigFile() {
        String config = FilenameUtils.concat(getDefaultRosettaHome(),
                defaultConfigFileName);
        File configFile = new File(config);
        if (!configFile.exists()) {
            System.out.println("creating default config");
            createDefaultConfigFile();
        }
        return config;
    }

    // private static String getDefaultDownloadDir() {
    // String downloadDir = getRosettaProps().getProperty("downloadDir");
    // String defaultDownloadDir = FilenameUtils.concat(getDefaultRosettaHome(),
    // downloadDir);
    // return defaultDownloadDir;
    // }

    private static Properties getRosettaProps() {
        String configFile = getConfigFile();

        Properties prop = new Properties();
        try (FileInputStream configFileIS = new FileInputStream(configFile)) {
            // load a properties file
            prop.load(configFileIS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;

    }

    private static void createDefaultConfigFile() {
        String defaultDownloadDir = FilenameUtils.concat(
                getDefaultRosettaHome(), "downloads");
        String defaultUploadDir = FilenameUtils.concat(getDefaultRosettaHome(),
                "uploads");

        Properties prop = new Properties();
        // set the properties value
        prop.setProperty("downloadDir", defaultDownloadDir);
        prop.setProperty("uploadDir", defaultUploadDir);

        List<String> configNames = Arrays.asList(defaultConfigFileName,
                configFileName);

        File defaultRosettaConfigLoc = new File(getDefaultRosettaHome());

        if (!defaultRosettaConfigLoc.exists()) {
            defaultRosettaConfigLoc.mkdirs();
        }

        for (String configName : configNames) {

            String rosettaConfigFile = FilenameUtils.concat(
                    getDefaultRosettaHome(), configName);

            try (FileOutputStream configFileOS = new FileOutputStream(
                    rosettaConfigFile)) {
                // save properties to project root folder
                prop.store(configFileOS, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}