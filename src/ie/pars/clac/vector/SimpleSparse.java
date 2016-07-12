/*
 * Copyright (C) 2016 Behrang QasemiZadeh <zadeh at phil.hhu.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ie.pars.clac.vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Set;

/**
 *
 * @author Behrang QasemiZadeh <zadeh at phil.hhu.de>
 */
public class SimpleSparse {

    public static final String LABEL_DELIMIT = "\t:";
    public static final String VECTOR_IND_VLU_DELIMIT = ":";
    public static final String DIM_DELIMITER = " ";
    private String label;
    private TreeMap<Integer, Double> vector;

    private int dimensionality;
    double[] dense;
    private boolean convetedToDense;

    public void setDimensionality(int dimensionality) {
      //  System.out.println("Dim set to " +dimensionality);
        this.dimensionality = dimensionality;
    }

    public int getDimensionality() {
        return dimensionality;
    }

    
    public SimpleSparse() {
        vector = new TreeMap<Integer, Double>();
        convetedToDense = false;
        
    }
    
    public void normalizeToL2Unity(){
    
        double oldLenth = this.getLength();
        for(int i: getIndices()){
            double value = this.getValue(i);
            value =value/oldLenth;
            this.setValue(i, value);
        }
        
    }
    
  public String toStringPerLine() {
        StringBuilder sb = new StringBuilder();
        vector.keySet().stream().forEach((i) -> {
            sb.append(i).append(" : ").append(vector.get(i).toString()).append("\n");
        });
        return sb.toString();
    }
  
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        vector.keySet().stream().forEach((i) -> {
         sb.append(i).append(VECTOR_IND_VLU_DELIMIT).append(vector.get(i).toString()). append(DIM_DELIMITER);
        });
        return sb.toString().trim();
    }
    

    public String toStringShorten() {
        StringBuilder sb = new StringBuilder();
        vector.keySet().stream().forEach((i) -> {
         sb.append(i).append(VECTOR_IND_VLU_DELIMIT).append(
                 round(vector.get(i),2)
         ). append(DIM_DELIMITER);
        });
        return sb.toString().trim();
    }
    public void setLabel(String label) {
        this.label = label;
    }

  private double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}
       
    public void addVectorMultiplyFreq(SimpleSparse v, double freq) {
        for (int i : v.vector.keySet()) {
            Double elementValue = this.getValue(i) + (v.getValue(i) * freq);
            //System.out.println("\t"+elementValue);
            this.vector.put(i, elementValue);
        }
        convetedToDense =false;
    }
    
    
    public void addVectorMultiplyFreqShifted(SimpleSparse v, double freq, int shift) throws Exception {
        if(this.dimensionality==0){
            throw new Exception("Set dimesnioanluity first");
        }
        for (int i : v.vector.keySet()) {
            int newDim = i;
            newDim = dimensionality + shift;
            if (newDim < 0) {
                newDim = dimensionality + newDim; // infact subtract
            } else if (newDim > dimensionality) {
                newDim = newDim - dimensionality;
            }
            
            Double elementValue = this.getValue(newDim) + (v.getValue(i) * freq);
            //System.out.println("\t"+elementValue);
            this.vector.put(i, elementValue);
        }
        convetedToDense =false;
    }
  
    

    
    public static SimpleSparse fromString(String line) {
    //    System.out.println(line);
        SimpleSparse sv = new SimpleSparse();
        String[] splitLine = line.split(DIM_DELIMITER);
        for (int i = 0; i < splitLine.length; i++) {
            String[] splitBit = splitLine[i].split(VECTOR_IND_VLU_DELIMIT);
            int index = Integer.parseInt(splitBit[0]);
            double value = Double.parseDouble(splitBit[1]);
            sv.vector.put(index, value);
        }
        sv.convetedToDense =false;
        return sv;

    }

    public Collection<Double> getVaules() {
        return this.vector.values();
    }

    public SimpleSparse(SimpleSparse v) {
        this();
        for (int i : v.getIndices()) {
            setValue(i, v.getValue(i));
        }
        convetedToDense = false;
    }

    public SimpleSparse(SimpleSparse v, int shift, int dimensionality) {
        this();
        this.dimensionality = dimensionality;
        for (int i : v.getIndices()) {
            int newDim = i;
            newDim = dimensionality + shift;
            if (newDim < 0) {
                newDim = dimensionality + newDim; // infact subtract
            } else if (newDim > dimensionality) {
                newDim = newDim - dimensionality;
            }
            setValue(newDim, v.getValue(i));
        }
        convetedToDense = false;
    }

    public void setValue(int index, double value) {
        vector.put(index, value);
        convetedToDense = false;
    }

    public double getValue(int index) {
        Double value = vector.get(index);
        if (value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public Set<Integer> getIndices() {
        return vector.keySet();
    }

    public boolean hasIndice(int key) {
        return this.vector.containsKey(key);
    }

    public int size() {
        return vector.size();
    }

    public double cosine(SimpleSparse ss) {
        double cosine = 0.0;

        double thisLength = getLength();
        if (thisLength == 0.0) {
            return 0;
        }
        double thatLength = ss.getLength();
        if (thatLength == 0.0) {
            return 0;
        }
        double dot = 0.0;
        for (Integer key : getIndices()) {
            dot += (ss.getValue(key) * this.getValue(key));
        }
        cosine = dot / (thisLength * thatLength); 

        return cosine;
    }
    
     public double logcosine(SimpleSparse ss) {
        double cosine = 0.0;

        double thisLength = getLength();
        if (thisLength == 0.0) {
            return 0;
        }
        double thatLength = ss.getLength();
        if (thatLength == 0.0) {
            return 0;
        }
        double dot = 0.0;
        for (Integer key : getIndices()) {
            dot += (ss.getValue(key) + this.getValue(key));
        }
        cosine = dot - (thisLength -thatLength); 

        return cosine;
    }
    public double euclid(SimpleSparse ss) {
        double euclid = 0.0;

//        double thisLength = getLength();
//        if (thisLength == 0.0) {
//            return 0;
//        }
//        double thatLength = ss.getLength();
//        if (thatLength == 0.0) {
//            return 0;
//        }
        
        for (Integer key : getIndices()) {
            euclid += Math.pow(ss.getValue(key) - this.getValue(key),2);
        }
        //cosine = dot / (thisLength * thatLength); // so funny, I thought * has privilige to / but appreantly not on my computer.. without pranthesis I get some crap

        return Math.sqrt(euclid);
    }
    
    
    public double[] getDense() {
        if (!convetedToDense) {
            dense = new double[this.dimensionality];

            for (int i = 0; i < dimensionality; i++) {
                dense[i] = getValue(i);
            }
            convetedToDense = true;
        }
        return dense;
    }
    
    public double[] getDenseLog() {
        double[] d = new double[this.dimensionality];
        for (int i = 0; i < dimensionality; i++) {
            d[i] = Math.log(getValue(i));
        }
        return d;
    }
      public double semiGeometric(SimpleSparse ss) {
        double geometricInner = 0.0;

//        double thisLength = getLength();
//        if (thisLength == 0.0) {
//            return 0;
//        }
//        double thatLength = ss.getLength();
//        if (thatLength == 0.0) {
//            return 0;
//        }
        
        for (Integer key : getIndices()) {
            double diff = ss.getValue(key) - this.getValue(key);
            if(diff!=0){
            //geometricInner += Math.log(diff);}
                geometricInner *= Math.log(diff);}
        }
        //cosine = dot / (thisLength * thatLength); // so funny, I thought * has privilige to / but appreantly not on my computer.. without pranthesis I get some crap

        return Math.sqrt(geometricInner);
    }
    public double getLength() {
        double length = 0.0;
        for (double value : vector.values()) {

            length += (value * value);
        }

        return Math.sqrt(length);
    }
    
    public double getSum() {
        double sum = 0.0;
        for (double value : vector.values()) {

            sum += value;
        }

        return sum;
    }
//
//    public double dot(SimpleSparse v2) {
//        int i = 0, j = 0;
//        double ret = 0;
//
//        if (!converted) {
//            convert2Arrays();
//        }
//        if (!v2.converted) {
//            v2.convert2Arrays();
//        }
//
//        while (i < indices.length && j < v2.indices.length) {
//            if (indices[i] > v2.indices[j]) {
//                j++;
//            } else if (indices[i] < v2.indices[j]) {
//                i++;
//            } else {
//                ret += values[i] * v2.values[j];
//                i++;
//                j++;
//            }
//        }
//        return ret;
//    }
//
//    public void clearConversion() {
//        converted = false;
//        indices = null;
//        values = null;
//    }
//
//    private void convert2Arrays() {
//        indices = new int[vector.size()];
//        values = new double[vector.size()];
//
//        int i = 0;
//        for (int key : vector.keySet()) {
//            indices[i] = key;
//            values[i] = vector.get(key);
//            i++;
//        }
//        converted = true;
//    }
    
    

//    public static void main(String[] s) {
//        SimpleSparse tsp = new SimpleSparse();
//        tsp.setValue(1, 2.6776767);
//        tsp.setValue(2, 2.66767);
//        tsp.setValue(7, 9.4676767);
//        // tsp.add(7, 9.4876776767);
//        System.out.println(tsp.cosine(tsp));
//
//    }

    

    
}
