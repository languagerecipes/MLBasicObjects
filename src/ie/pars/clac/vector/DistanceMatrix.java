/* 
 * Copyright 2016 Behrang QasemiZadeh <zadeh at phil.hhu.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ie.pars.clac.vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.TreeMap;

/**
 *
 * @author behqas
 */
public class DistanceMatrix<T> {
    
    TreeMap<Integer, TreeMap<Integer, T>> values;
    //int dimension;
    //double[] values;

    public DistanceMatrix() {
        //this.dimension = dimension;
        values = new TreeMap<>();
    }

   public T fromMatrixToVector(int i, int j) {
        int orderX = i;
        int orderY = j;
        if (i > j) {
            orderX = j;
            orderY = i;
        }
        
        // I do not check the existance of the element by purpose so that the code throw an exception
        return values.get(orderX).get(orderY);
        
            
    }
    
  public  void toMatrix(int i, int j, T value) {
        int orderX = i;
        int orderY = j;
        if (i > j) {
            orderX = j;
            orderY = i;
        }

        if (values.containsKey(orderX)) {
            values.get(orderX).put(orderY, value);
        } else {
            TreeMap<Integer, T> innerValues = new TreeMap<>();
            innerValues.put(orderY, value);
            values.put(orderX, innerValues);
        }
    }
    
    public int getSize(){
        int size =0;
        for(Integer x:this.values.keySet()){
            size+=this.values.get(x).size();
        }
        return size;
    }
}
