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


import gnu.trove.map.hash.TIntDoubleHashMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//import net.openhft.koloboke.collect.map.hash.HashIntDoubleMaps;
//import net.openhft.koloboke.collect.map.hash.HashLongLongMaps;



/**
 *
 * @author Behrang QasemiZadeh <zadeh at phil.hhu.de>
 */
public class SimpleSparseDouble {

    public static final String LABEL_DELIMIT = "\t:";
    public static final String VECTOR_IND_VLU_DELIMIT = ":";
    public static final String DIM_DELIMITER = " ";
  //  private TIntDoubleHashMap vector;
           // TIntDoubleHashMap vector;

    private Map<Integer, Double> vector;
    private long frequency;
    private int dimensionality;
    double[] dense;
    private double sum = 0;
    private boolean convetedToDense;

 
    public int getDimensionality() {
        return dimensionality;
    }

    public SimpleSparseDouble(double[] vec) {
         //vector = new TIntDoubleHashMap();
         vector =// HashIntDoubleMaps.newMutableMap()
               new  HashMap<>()
                 ;
        convetedToDense = false;
        this.dimensionality = vec.length;
        for (int i = 0; i < vec.length; i++) {
            vector.put(i, vec[i]);
            
        }
    }

    
    
    
    public SimpleSparseDouble(int dimensionality) {
        vector = //new TIntDoubleHashMap();
         //new TIntDoubleHashMap();
           //  HashIntDoubleMaps.newMutableMap();   
                new HashMap();
        convetedToDense = false;
        this.dimensionality = dimensionality;
    }


   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
//        int[] keys = vector.keys();
        Set<Integer> keys = vector.keySet();
        if(keys!=null){
//        for (int l = 0; l < keys.length; l++) {
//            int key = keys[l];
//            sb.append(key).append(VECTOR_IND_VLU_DELIMIT).append(vector.get(key)).append(DIM_DELIMITER);
//        }}
//        sb.append(keys[keys.length-1]).append(VECTOR_IND_VLU_DELIMIT).append(vector.get(keys[keys.length-1]))
//                .append("\n");
        
        for (int key: keys) {
            
            sb.append(key).append(VECTOR_IND_VLU_DELIMIT).append(vector.get(key)).append(DIM_DELIMITER);
        }
        }
        return sb.toString();
    }

   

    
   

//    private double round(double value, int places) {
//        if (places < 0) {
//            throw new IllegalArgumentException();
//        }
//
//        BigDecimal bd = new BigDecimal(value);
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }
    
    
    public long incFrequency() {
        this.frequency++;
        return frequency;
    }

    public double getFrequency() {
        return frequency;
    }
    

    public void addVector(SimpleSparseDouble v) {
        for (int i : v.getIndices()) {
            double elementValue = this.getValue(i) + (v.getValue(i) );
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }
    
    /**
     *
     * @param minValue
     */
    public void pronVector(double minValue) {
        
        for (int i : this.getIndices()) {
            double elementValue = Math.max(minValue, this.getValue(i));
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }
    
    public void minusVector(SimpleSparseDouble v) {
        for (int i : v.getIndices()) {
            double elementValue = this.getValue(i) - (v.getValue(i));
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }
    
    public void scalarScale(double scale){
        for (int i : getIndices()) {
            double elementValue = this.getValue(i) * scale;
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }
    
    public void avgVector(SimpleSparseDouble v) {
        //AMGM computation
//        for (int i : v.getIndices()) {
//            double gm = Math.sqrt(this.getValue(i)* (v.getValue(i) )); //worked ok
//            double am = .5 * (this.getValue(i) + (v.getValue(i))); //worked ok2
//            for (int j = 0; j < 2; j++) {
//
//                double am1 = .5 * (gm + am);
//                double gm1 = Math.sqrt(am * gm); //worked ok
//                gm = gm1;
//                am = am1;
//            }
//            double elementValue = .5 * (am + gm);
//            this.vector.put(i, elementValue);
//        }
//        convetedToDense = false;

//        for (int i : v.getIndices()) {
//            double power= .5;
//            double hmean = .5*( Math.pow(this.getValue(i),power)+ Math.pow(v.getValue(i),power)) ; //worked ok
//            hmean = Math.pow(hmean, 1.0/power);
//            
//            double elementValue = hmean;
//            this.vector.put(i, elementValue);
//        }
//        convetedToDense = false;

        for (int i : v.getIndices()) {
            double gm = Math.sqrt(this.getValue(i)* (v.getValue(i) )); //worked ok
            double am = .5 * (this.getValue(i) + (v.getValue(i))); //worked ok2
            for (int j = 0; j < 5; j++) {

                double am1 = .5 * (gm + am);
                double gm1 = Math.sqrt(am * gm); //worked ok
                gm = gm1;
                am = am1;
            }
            double elementValue = .5 * (am + gm);
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }
    
    public double[] getSubVector(int start, int end){
        double[] subVec = new double[end-start];
        for (int i = start; i < end; i++) {
            subVec[i-start]= this.getValue(i);
            
        }
       return subVec;
    }
    public void addVector(double[] vdense) {
        for (int i = 0; i < this.dimensionality; i++) {
            double elementValue = this.getValue(i) + (vdense[i]);
            this.vector.put(i, elementValue);

        }

        convetedToDense = false;
    }
    
    
  
    public void maxCoordinateVector(SimpleSparseDouble v) {
        for (int i : v.getIndices()) {
            double elementValue = Math.max(this.getValue(i) ,(v.getValue(i) ));
            double delta = Math.abs(this.getValue(i) -v.getValue(i));
            this.vector.put(i, delta*170);//+delta*elementValue);
        }        convetedToDense = false;
    }
    
    public void multiplyPairWise(SimpleSparseDouble v) {
        for (int i : v.getIndices()) {
            double elementValue = this.getValue(i) * (v.getValue(i));
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }
    
   public void multiplyPairWiseSmoothing(SimpleSparseDouble v) {
        for (int i : v.getIndices()) {
            double elementValue = (this.getValue(i)+1) * (1+v.getValue(i));
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    } 
    public void getRootAllDim(int root) {
        for (int i : this.getIndices()) {
            double elementValue = Math.pow(this.getValue(i), 1.0/root);
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }
    
    public void addVectorMultiplyFreq(SimpleSparseDouble v, double freq) {
        for (int i : v.getIndices()) {
            double elementValue = this.getValue(i) + (v.getValue(i) * freq);
            
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }

    public void addVectorMultiplyFreqShifted(SimpleSparseDouble v, int freq, int shift) throws Exception {
        if (this.dimensionality == 0) {
            throw new Exception("Set dimesnioanluity first");
        }
        for (int i : v.getIndices()) {
            int newDim = i;
            newDim = dimensionality + shift;
            if (newDim < 0) {
                newDim = dimensionality + newDim; // infact subtract
            } else if (newDim > dimensionality) {
                newDim = newDim - dimensionality;
            }

            double elementValue = this.getValue(newDim) + (v.getValue(i) * freq);
            //System.out.println("\t"+elementValue);
            this.vector.put(i, elementValue);
        }
        convetedToDense = false;
    }

    public static SimpleSparseDouble fromStringSparse(String line, int dim) {
            
       SimpleSparseDouble sv = new SimpleSparseDouble(dim);
       if(line.trim().length()>0){
        String[] splitLine = line.trim().split(DIM_DELIMITER);
//        System.out.println("*"+line+"*"+splitLine.length);

        for (int i = 0; i < splitLine.length; i++) {
            String[] splitBit = splitLine[i].split(VECTOR_IND_VLU_DELIMIT);
            int index = Integer.parseInt(splitBit[0]);
            double value = Double.parseDouble(splitBit[1]);
            sv.setValue(index, value);
        }}
        sv.convetedToDense = false;
        return sv;

    }

    
     public static double[] fromStringDense(String line, int dim) {
        //    System.out.println(line);

        //SimpleSparseFloat sv = new SimpleSparseFloat(dim);
        double[] dense = new double[dim];
        
        String[] splitLine = line.split(DIM_DELIMITER);
        // System.out.println(line);
        for (int i = 0; i < dim; i++) {
           
            dense[i] = Double.parseDouble(splitLine[i]);
            if(!Double.isFinite(dense[i])){
                System.err.println(" *uck at line " + line);
            }
        }

        return dense;

    }
    
    
//    public double[] getPPMI(SimpleSparseFloat ssAll) {
//        double sumAll = ssAll.getSum();
//        double sumThisRow = this.getSum();
//        double[] pmiVec = new double[this.dimensionality];
//        for (int idx = 0; idx < this.dimensionality; idx++) {
//            double pmi = Math.log( (getValue(idx) * sumAll*1.5) / (sumThisRow * ssAll.getValue(idx)));
//            pmiVec[idx] = Math.max(0, pmi+1);
//        }
//        return pmiVec;
//
//    }
    
    public void convertToPPMIDouble(SimpleSparseDouble ssAll) {
        double sumAll = ssAll.getSum();
        double sumThisRow = this.getSum();
        double pmiVec;
        for (int idx = 0; idx < this.dimensionality; idx++) {
            double pmi = log2((getValue(idx) * sumAll) / (sumThisRow * ssAll.getValue(idx)));
            pmiVec = Double.max(0, pmi);
            if (!Double.isFinite(pmiVec)) {
                if (Double.isInfinite(pmiVec)) {
                    pmiVec = 0xFFFF;
                    //System.err.println("noway");
                } else {
                    pmiVec = 0;
                }

            }
            this.vector.put(idx, pmiVec);
        }

    }

    
      public double[] getScaledDouble(double sumAll) {
        
        
        double[] scaledVec = new double[this.dimensionality];
        for (int idx = 0; idx < this.dimensionality; idx++) {
            double weight =getValue(idx)*1.0 / sumAll;
            scaledVec[idx] = weight;
            if (!Double.isFinite(scaledVec[idx])) {
                if (Double.isInfinite(scaledVec[idx])) {
                    scaledVec[idx] = 0xFFFF;
                } else {
                    scaledVec[idx] = 0;
                }

            }
        }
        return scaledVec;
    }
      
    public double[] getPPMIDouble(SimpleSparseDouble ssAll) {
        double sumAll = ssAll.getSum();
        double sumThisRow = this.getSum();
        double[] pmiVec = new double[this.dimensionality];
        for (int idx = 0; idx < this.dimensionality; idx++) {
            double pmi = log2((getValue(idx) * sumAll) / (sumThisRow * ssAll.getValue(idx)));
            pmiVec[idx] = Double.max(0, pmi);
            if (!Double.isFinite(pmiVec[idx])) {
                if (Double.isInfinite(pmiVec[idx])) {
                    pmiVec[idx] = 0xFFFF;
                } else {
                    pmiVec[idx] = 0;
                }

            }
        }
        return pmiVec;
    }
    public void toPPMIDouble(SimpleSparseDouble ssAll) {
        double sumAll = ssAll.getSum();
        double sumThisRow = this.getSum();
        //double[] pmiVec = new double[this.dimensionality];
        for (int idx = 0; idx < this.dimensionality; idx++) {
            double pmi = log2((getValue(idx) * sumAll) / (sumThisRow * ssAll.getValue(idx)));
            if(pmi>0 && Double.isFinite(pmi)){
                this.vector.put(idx, pmi);
            }else{
               this.vector.put(idx, 0.0); 
            }
//            if (!Double.isFinite(pmiVal)) {
//                
//            }else{
//                 setValue(idx, 0);
//            }
//                if (Double.isInfinite(pmiVal)) {
//                    pmiVal= 0xFFFF;
//                } else {
//                    pmiVal= 0;
//                }
//
//            }
            
        }

    }

       public double[] getOnlyPMIDouble(SimpleSparseDouble ssAll) {
        double sumAll = ssAll.getSum();
        double sumThisRow = this.getSum();
        double[] pmiVec = new double[this.dimensionality];
        for (int idx = 0; idx < this.dimensionality; idx++) {
            double pmi = log2((getValue(idx) * sumAll) / (sumThisRow * ssAll.getValue(idx)));
            pmiVec[idx] = pmi;
            if (!Double.isFinite(pmiVec[idx])) {
                if (Double.isInfinite(pmiVec[idx])) {
                    pmiVec[idx] = 0xFFFF;
                } else {
                    pmiVec[idx] = 0;
                }

            }
        }
        return pmiVec;
    }
    public static final double log2(double f)
{
    return 
            Math.log(f)/Math.log(2.0);
}
//    public float[] getPMIFloat(SimpleSparseFloat ssAll) {
//        double sumAll = ssAll.getSum();
//        double sumThisRow = this.getSum();
//        float[] pmiVec = new float[this.dimensionality];
//        for (int idx = 0; idx < this.dimensionality; idx++) {
//            pmiVec[idx] = (float) Math.log((getValue(idx) * sumAll * 1.7) / (sumThisRow * ssAll.getValue(idx)));
//        }
//        return pmiVec;
//    }
    public Collection<Double> getVaules() {
        Collection<Double> values = this.vector.values();
        return values;
    }

    public SimpleSparseDouble(SimpleSparseDouble v) {
        this(v.dimensionality);
        for (int i : v.getIndices()) {
            setValue(i, v.getValue(i));

        }

        convetedToDense = false;
    }

    public SimpleSparseDouble(SimpleSparseDouble v, int shift, int dimensionality) {
        this(dimensionality);
        this.dimensionality = dimensionality;
        for (int i : v.getIndices()) {
            int newDim = i+ shift;
            if (newDim < 0) {
                newDim = dimensionality + newDim; // infact subtract
            } else if (newDim > dimensionality) {
                newDim = newDim - dimensionality;
            }
            setValue(newDim, v.getValue(i));
            
        }
        convetedToDense = false;
    }

    final public void setValue(int index, double value) {
        vector.put(index, value);
        convetedToDense = false;
    }
 
    public void incValue(int index, double value) {
        double newValue = this.getValue(index) + value;
        
        vector.put(index, newValue);
        convetedToDense = false;
    }
    
    public void incValueLog(int index, double value) {
        double newValue = Math.log(this.getValue(index)) + value;
        vector.put(index, newValue);
        convetedToDense = false;
    }
    
    public double getValue(int index) {
        if(vector.containsKey(index)){
            double get = vector.get(index);
            return get;
        }else{
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

    public double cosine(SimpleSparseDouble ss) {
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


    public double[] getDense() {
        
        if (!convetedToDense) {
            sum=0.0;
            dense = new double[this.dimensionality];

            for (int i = 0; i < dimensionality; i++) {
                
                dense[i] = getValue(i);
                sum+=dense[i];
            }
            convetedToDense = true;
        }
        return dense;
    }

   public double[] getDenseNoStore(int limDim) throws Exception {
        if(limDim>dimensionality){
            throw new Exception("lowDim > dim");
        }
//        if (!convetedToDense) {
//            sum=0.0;
          double[]  denseNS = new double[limDim];

            for (int i = 0; i < limDim; i++) {
                
                denseNS[i] = getValue(i);
             //   sum+=dense[i];
            }
            //convetedToDense = true;
       // }
        return denseNS;
    }
    public double getLength() {
        double length = 0.0;
        for (double value : vector.values()) {

            length += (value * value);
        }

        return Math.sqrt(length);
    }

    public double getSum() {
//        if (convetedToDense) {
//            return this.sum;
//        }
        double sum = 0;
        for (double value : vector.values()) {
            sum += value;
        }

        return sum;
    }
 
    public double getSumScaled(double alpha) {
//        if (convetedToDense) {
//            return this.sum;
//        }
        double sum = 0;
        for (double value : vector.values()) {
            sum += Math.pow(value,alpha);
        }

        return sum;
    }
    
    public double whatStillDontknowWhatEntropy(int dimension) {
        double sum1 = this.getSum();
        double entropy1 = 0;
        double entropy2 = 0;
        for (int i = 0; i < this.dimensionality; i++) {
            double val = this.getValue(i);
            double prob = val/sum1;
            
            if (prob > 0) {
                entropy1 -= prob * Math.log(prob);
            }
            if(i!=dimension){
                double prob2 = val/(sum1+1);
                if(prob2>0){
                entropy2 -= prob2 * Math.log(prob2);}
            }else{
                double prob2 = (val+1)/(sum1+1);
                if(prob2>0){
                 entropy2 -= prob2 * Math.log(prob2);}
            }
        }
        
        
        return entropy2-entropy1;
    }
    
    
}
