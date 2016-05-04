/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.pars.clac.vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;

/**
 * Load a serialised vector into SimpleSparse
 * @author Behrang QasemiZadeh <me at atmykitchen.info>
 */
public class SimpleSparseVectorBuilder {

    
    final TreeMap<String, Integer> keyStringMap;

    public SimpleSparseVectorBuilder() throws Exception {

        //keyStringMap = new ConcurrentSkipListMap();
        keyStringMap = new TreeMap<>();
    }

    public SimpleSparse loadContextFile2Vec(File expVectorFile) throws Exception {
        SimpleSparse spa = new SimpleSparse(); // I get a lot of error using the trove objetcs! :-(
        if (!expVectorFile.exists()) {
            throw new Exception("Context vector not yet aggregated  .. future add calling to aggreg...");
        }
        BufferedReader br = new BufferedReader(new FileReader(expVectorFile));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().length() != 0) {
                String[] split = line.split(" : ");
                Integer key = getKey(split[0]);
                double value = Double.parseDouble(split[1]);
                spa.setValue(key, value);
            }
        }
        br.close();
        return spa;
    }

    private int getKey(String element) {
        synchronized(keyStringMap){
        if (this.keyStringMap.containsKey(element)) {
            return keyStringMap.get(element);
        } else {
            int key = keyStringMap.size();
            key++;
            keyStringMap.put(element, key);
            return key;
        }
        }
    }
    
    public int getSize(){
        return keyStringMap.size();
    }

    
    
}
