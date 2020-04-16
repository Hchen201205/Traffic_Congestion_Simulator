/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Data class is a data-processing class.
 * It is designed to be imported by all the classes.
 * It will treat each data set as an object and store it in the Map.
 * It contains a few methods:
 * 1. It can be controlled to set the data.
 * 2. It can be controlled to output data. 
 *    This is especially useful because each class will not have to write their own data; they can simply find data in this class with a right key word.
 * 
 *
 * @author chenhanxi
 */
public class Data {

    /**
     * dataMap will store all the data set, disregard of its format. 
     * It will be a static variable so that whenever a class called a change, the map will respond.
     * It is store in the format of <keyword, dataset>
     * It is private because it will prevent potential errors for accessing the wrong Object in dataMap.
     */
    private static Map<String, Object> dataMap;

    /**
     * file will store the file name of the data file.
     * It is private because the user does not need to know the location of the file.
     */
    private File file;
    /**
     * Data() is a constructor.
     * It is designed to be created by the control class.
     * Once it is called, a parameter of the name of the file will be passed in.
     * It then will access the file using File, FileReader, and BufferedReader.
     * Once it access the file, it will store the data sets in the file to dataMap in some fashion.
     * Once the constructor is executed, dataMap will be able to function.
     * 
     * @param fileLocation
     * @throws FileNotFoundException 
     */
    public Data(String fileLocation) throws FileNotFoundException{
        dataMap = new HashMap<>();
        file = new File(fileLocation);
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        // Store data into dataMap in some format.
    }
    
    /**
     * setData(String, Object) is a modifier funciton.
     * It will receive an key and a data set as parameters.
     * The reason why it receive a data set as an Object is because, so far, we are not sure what format of data should be input.
     * It will then replace the Object in dataMap with the value parameter.
     * 
     * @param key
     * @param value 
     */
    public static void setData(String key, Object value) {
        dataMap.replace(key, value);
    }

    /**
     * Object getData(String) is an accessor function.
     * It will receive a key as a parameter.
     * It then will access and return the value with that key.
     * This function is essentially a Object get(String) in HashMap class.
     * 
     * @param key
     * @return 
     */
    public static Object getData(String key) {

        return dataMap.get(key);
    }
    
    /**
     * writeData() function.
     * Ideally, it will be called at the very instance before the control class is ended.
     * When it is called, it will find the file and write all the keys and data set into the file under some format.
     * 
     * 
     * @throws java.io.FileNotFoundException
     */
    public void writeData() throws FileNotFoundException {

    }
}
