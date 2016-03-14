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
