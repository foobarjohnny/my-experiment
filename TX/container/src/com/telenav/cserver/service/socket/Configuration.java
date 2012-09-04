package com.telenav.cserver.service.socket;

import java.util.PropertyResourceBundle;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

/**
 * User: llzhang
 * Date: 2010-5-13
 * Time: 15:55:48
 */
public class Configuration {
    private PropertyResourceBundle cfg;

    public static final int DB_TYPE_UNKNOWN = 0;
    public static final int DB_TYPE_ORACLE = 1;
    public static final int DB_TYPE_MYSQL = 2;

    private static HashMap configs = new HashMap();


    private Configuration(String resourceBundle) {
        FileInputStream fis = null;
        try {
            String configFilePath = getConfigurationFilePath(resourceBundle);
            fis = new FileInputStream(configFilePath);
            cfg = new PropertyResourceBundle(fis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static synchronized Configuration getInstance(String resourceBundle) {
        if (configs.keySet().contains(resourceBundle)) {
            return (Configuration) configs.get(resourceBundle);
        } else {
            Configuration config = new Configuration(resourceBundle);
            configs.put(resourceBundle, config);
            return config;
        }
    }


    /**
     * Get property by key.
     *
     * @param key property key
     * @return property value
     */
    public String getProperty(String key) {
        return this.getProperty(key, "");
    }


    public String getProperty(String key, String defaultValue) {
        try {
            return cfg.getString(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getClassesDir() {
        String path = Configuration.class.getResource("Configuration.class")
                .getPath();
        // in a jar file ?
        int nStart = path.lastIndexOf(":");
        if (nStart > 0) {
            path = path.substring(nStart + 1, path.length());
        }

        // is in a jar file?
        int nEnd = path.indexOf("!");
        if (nEnd > 0) {
            path = path.substring(0, nEnd);
            nEnd = path.lastIndexOf("/");
            if (nEnd > 0)
                path = path.substring(0, nEnd + 1);
        } else {
            nEnd = path.indexOf("classes");
            if (nEnd > 0)
                path = path.substring(0, nEnd + 8);
            else {
                nEnd = path.indexOf("bin");
                if (nEnd > 0)
                    path = path.substring(0, nEnd + 4);
            }
        }

        path = path + "../classes";
        return path;
    }


    public static boolean existConfigurationFile(String fileName) {
        String configFile = getConfigurationFilePath(fileName);
        if (configFile == null) return false;
        File aFile = new File(configFile);
        return aFile.exists();
    }

    public static String getJarFileNames() {
        String names = "";
        // in a jar file or in a class file ?
        String path = Configuration.class.getResource("Configuration.class")
                .getPath();
        // in a jar file ?
        int nStart = path.lastIndexOf(":");
        if (nStart > 0) {
            path = path.substring(nStart + 1, path.length());
        }

        // is in a jar file?
        int nEnd = path.indexOf("!");
        if (nEnd > 0) {
            path = path.substring(0, nEnd);
            nEnd = path.lastIndexOf("/");
            if (nEnd > 0)
                path = path.substring(0, nEnd + 1);
        } else {
            nEnd = path.indexOf("classes");
            if (nEnd > 0)
                path = path.substring(0, nEnd + 8);
        }

        // in application environment.
        String dirName = path + "../lib/";

        names += getJarFileNamesInDir(dirName);
        // in web environment.
        dirName = path + "../classes/lib/";
        names += getJarFileNamesInDir(dirName);

        names = names.trim();
        return names;
    }

    private static String getJarFileNamesInDir(String dirName) {
        String names = "";
        File dir = new File(dirName);
        if (!dir.isDirectory()) return names;
        File[] files = dir.listFiles();
        int len = files == null ? 0 : files.length;
        for (int i = 0; i < len; i++) {
            String name = files[i].getName();
            if (!name.endsWith(".jar")) continue;
            names += name + " ";
        }

        return names;
    }

    public static String getConfigurationFilePath(String fileName) {
        // in a jar file or in a class file ?
        String path = Configuration.class.getResource("Configuration.class")
                .getPath();
        // in a jar file ?
        int nStart = path.lastIndexOf(":");
        if (nStart > 0) {
            path = path.substring(nStart + 1, path.length());
        }

        // is in a jar file?
        int nEnd = path.indexOf("!");
        if (nEnd > 0) {
            path = path.substring(0, nEnd);
            nEnd = path.lastIndexOf("/");
            if (nEnd > 0)
                path = path.substring(0, nEnd + 1);
        } else {
            nEnd = path.indexOf("classes");
            if (nEnd > 0)
                path = path.substring(0, nEnd + 8);
        }

        // in application environment.
        String filePath = path + "../config/" + fileName;
//    System.out.println("search " + filePath);
        if (new File(filePath).exists())
            return filePath;

        // in web environment.
        filePath = path + "../classes/config/" + fileName;
//    System.out.println("search " + filePath);
        if (new File(filePath).exists())
            return filePath;
        // String message = "Fatal error: " + fileName
        // + " is not found in config directory";
        // MailUtil.sendMailReceiptMail(message, message);
        // System.out.println(fileName + " is not found in config directory");
        return "";

    }


    public static boolean setKeystore(String fileName, String password) {
        String keystore = getConfigurationFilePath(fileName);
        keystore = keystore == null ? "" : keystore.trim();
        if (keystore.length() == 0)
            return false;
        password = password == null ? "" : password.trim();
        System.setProperty("javax.net.ssl.keyStore", keystore);
        System.setProperty("javax.net.ssl.keyStorePassword", password);
        System.setProperty("javax.net.ssl.trustStore", keystore);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
        return true;

    }

    private static int dbType = DB_TYPE_UNKNOWN;


    /**
     * read the pool.properties and torque.properties, find out database type.
     *
     * @return
     */
    public static int getDbType() {
        if (dbType != DB_TYPE_UNKNOWN)
            return dbType;
        String poolFile = getConfigurationFilePath("pool.properties");
        String torqueFile = getConfigurationFilePath("torque.properties");

        String[] dbConfigFiles = {poolFile, torqueFile};
        for (int m = 0; m < 2; m++) {
            if (dbConfigFiles[m].trim().length() == 0)
                continue;

            File file = new File(dbConfigFiles[m]);
            try {
                String content = readContent(file);
                String[] lines = content.split("\n");
                int len = lines == null ? 0 : lines.length;
                for (int i = 0; i < len; i++) {
                    lines[i] = lines[i].trim();
                    if (lines[i].startsWith("#"))
                        continue;
                    if (lines[i].indexOf("com.mysql.jdbc.Driver") > 0) {
                        dbType = DB_TYPE_MYSQL;
                        return dbType;
                    } else if (lines[i].indexOf("oracle.jdbc.driver.OracleDriver") > 0) {
                        dbType = DB_TYPE_ORACLE;
                        return dbType;
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return DB_TYPE_UNKNOWN;

    }


    public static String readContent(File file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        int lenContent = fin.available();
        byte[] bytes = new byte[lenContent];
        fin.read(bytes);
        fin.close();
        String content = new String(bytes);
        return content;
    }


    public static void writeContent(File file, String content) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(content.getBytes());
        fout.close();
    }
    
}
