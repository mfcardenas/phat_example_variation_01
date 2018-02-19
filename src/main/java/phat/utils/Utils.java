package phat.utils;

import phat.body.BodiesAppState;
import phat.structures.houses.HouseFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Marlon on 16/02/2018.
 */
public class Utils {

    Utils(){
        initProperties();
    }

    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    private static Properties properties;

    /**
     * Init properties from file.
     */
    private void initProperties(){
        properties = new Properties();
        try {
//            properties.load(getClass().getResourceAsStream("/api_twitter.properties"));
        } catch (Exception ex) {
            logger.warning("Error get properties from file system... ");
        }
    }


    /**
     * Get Propertes from file.
     * @param key a
     * @return b
     */
    public String getKey(String key){
        return properties.getProperty(key);
    }


    /**
     * Get File Config.
     * @param fileName a
     * @return b
     */
    public FileReader getFileConfig(String fileName){
        FileReader file = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            file = new FileReader(classLoader.getResource(fileName).getFile());
//            fRe = new FileReader(getClass().getResource(fileName).getFile());
        } catch (Exception ex) {
            logger.warning("Error get properties from file system... ");
        }
        return file;
    }

    /**
     * Utils get Type House.
     * @param type house.
     * @return TypeHouse.
     */
    public static HouseFactory.HouseType getTypeHouse(String type) {
        HouseFactory.HouseType ret = HouseFactory.HouseType.BrickHouse60m;
        switch (type.toLowerCase()) {
            case "brickhouse60m":
                ret = HouseFactory.HouseType.BrickHouse60m;
                break;
            case "duplex":
                ret = HouseFactory.HouseType.Duplex;
                break;
            case "house3room2bath":
                ret = HouseFactory.HouseType.House3room2bath;
                break;
        }
        return ret;
    }

    /**
     * Utils for get Type Body.
     * @param type body.
     * @return TypBody class.
     */
    public static BodiesAppState.BodyType getTypeBody(String type) {
        BodiesAppState.BodyType ret = BodiesAppState.BodyType.Elder;
        switch (type.toLowerCase()) {
            case "elder":
                ret = BodiesAppState.BodyType.Elder;
                break;
            case "elderlp":
                ret = BodiesAppState.BodyType.ElderLP;
                break;
            case "young":
                ret = BodiesAppState.BodyType.Young;
                break;
            default:
                ret = BodiesAppState.BodyType.Elder;
                break;
        }
        return ret;
    }
}