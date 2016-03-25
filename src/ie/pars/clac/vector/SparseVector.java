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

/**
 *
 * @author behqas
 */
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class mainly similar to what can be found in JavaML with some changes f
 *
 * @author Behrang QasemiZadeh <zadeh at phil.hhu.de>
 */
public class SparseVector extends TreeMap<Integer, Double> {

    

   

    /**
     * eventually the map must be replaced with a more proper (memory friendly)
     * imple.
     */
    public static final String LABEL_DELIMIT = "\t:";
    public static final String VECTOR_IND_VLU_DELIMIT = ":";
    private int diemension = 0; // note dimension and the size of the map are different!
    private boolean convertedToArray;
    private int[] keySetArray;
    private double[] valueSet;
    double lengthPower2;
    double lengthPower4;
    boolean lengthCompuetd;
    String strLabel;
    double[] arrayValueSet;

    
    /**
     * create empty vector
     */
    public SparseVector() {
        super();
        lengthCompuetd = false;
        convertedToArray = false;
    }

    public SparseVector(TreeMap<Integer, Double> treemap) {
        super(treemap);
        lengthCompuetd = false;
        convertedToArray = false;
    }
    /**
     * create empty vector with length
     */
    public SparseVector(int i) {
        this();
        diemension = i;
        lengthCompuetd = false;
        convertedToArray = false;
    }

    /**
     * create vector from dense vector
     *
     * @param x
     */
    public SparseVector(double[] x) {
        this(x.length);
        for (int i = 0; i < x.length; i++) {
            if (x[i] != 0) {
                put(i, x[i]);
            }
        }
        lengthCompuetd = false;
        convertedToArray = false;
    }

    /**
     * copy constructor
     *
     * @param v
     */
    public SparseVector(SparseVector v) {
        super(v);
        this.diemension = v.diemension;
        lengthCompuetd = false;
        convertedToArray = false;
    }

    /**
     * get ensures it returns 0 for empty hash values or if index exceeds
     * length.
     *
     * @param key
     * @return val
     */
    @Override
    public Double get(Object key) {
        Double b = super.get(key);
        if (b == null) {
            return 0.;
        }
        return b;
    }

    /**
     * put increases the matrix size if the index exceeds the current size.
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Double put(Integer key, Double value) {
        diemension = Math.max(diemension, key + 1);
        if (value == 0) {
            return remove(key);
        }
        return super.put(key, value);
    }

    public void incValue(Integer key, Double value) {
        Double c = get(key);
        c += value;
        put(key, c);
    }

    /**
     * normalises the vector to 1.
     */
    public void normalise() {
        double invsum = 1. / sum();
        for (int i : keySet()) {
            mult(i, invsum);
        }
    }

    /**
     * normalises the vector to newsum
     *
     * @param the value to which the element sum
     * @return the old element sum
     */
    public double normalise(double newsum) {
        double sum = sum();
        double invsum = newsum / sum;
        Set<Integer> keys = new HashSet<Integer>();
        keys.addAll(keySet());
        for (int i : keys) {
            mult(i, invsum);
        }
        return sum;
    }

    /**
     * sum of the elements
     *
     * @return
     */
    public double sum() {
        double sum = 0;
        for (double a : values()) {
            sum += a;
        }
        return sum;
    }

    public void multiplyByConstant(int constantFreq) {
        for (int i : this.keySet()) {
            mult(i, constantFreq);
        }
    }

    /**
     * add vector to another vector
     *
     * @param v
     */
    public void addVector(SparseVector v) {
        for (int i : v.keySet()) {
            Double elementValue = this.get(i) + v.get(i);
            this.put(i, elementValue);
        }
    }

    public void addMultiplyVector(SparseVector v, int mult) {
        for (int i : v.keySet()) {
            Double elementValue = this.get(i) + (v.get(i) * mult);
            this.put(i, elementValue);
        }
    }
    
 public void addMultiplyVector(SparseVector v, double mult) {
        for (int i : v.keySet()) {
            Double elementValue = this.get(i) + (v.get(i) * mult);
            this.put(i, elementValue);
        }
    }
    public void addVectorPermuted(SparseVector v, int permutationOffset) {
        for (int index : v.keySet()) {
            int newPermutedOffset = permutationOffset + index;
            if (newPermutedOffset < this.diemension && newPermutedOffset >= 0) {
                Double elementValue = this.get(newPermutedOffset) + v.get(index);
                this.put(index, elementValue);
            } else {
                // I ignor the rest of elements in this permutation, I believe it is not important, at least 
                // for the time that I have this very large beta values -- maybe add new permutation later
            }
        }
    }

    /**
     * mutable mult
     *
     * @param i index
     * @param a value
     */
    public void mult(int i, double a) {
        Double c = get(i);
        c *= a;
        put(i, c);
    }

    /**
     * scalar product
     *
     * @param v
     * @return scalar product
     */
    public double times(SparseVector v) {
        double sum = 0;
        for (int i : keySet()) {
            sum += get(i) * v.get(i);
        }
        return sum;
    }

    /**
     * get the length of the vector
     *
     * @return
     */
//    public final int getDimension() {
//        return diemension;
//    }

    public int[] getKeySetArray() {
        if (convertedToArray) {
            return keySetArray;
        } else {
            this.convertToArrays();
            return keySetArray;
        }
    }

    public double[] getValueSet() {
        if (convertedToArray) {
            return valueSet;
        } else {
            this.convertToArrays();
            return valueSet;
        }
    }

    /**
     * set the new length of the vector (regardless of the maximum index).
     *
     * @param length
     */
    public final void setLength(int length) {
        this.diemension = length;
    }

    /**
     * copy the contents of the sparse vector
     *
     * @return
     */
    public SparseVector copy() {
        return new SparseVector(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        keySet().stream().forEach((i) -> {
            sb.append(i).append(":").append(get(i).toString()).append(" ");
        });
        return sb.toString();
    }
public static SparseVector fromString(String line) {

        SparseVector sv = new SparseVector();
        String[] splitLine = line.split(" ");
        for (int i = 0; i < splitLine.length; i++) {
            String[] splitBit = splitLine[i].split(":");
            int index = Integer.parseInt(splitBit[0]);
            double value = Double.parseDouble(splitBit[1]);
            sv.put(index, value);
        }
        return sv;

    }
    public String toStringDensePresentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < diemension; i++) {
            if (keySet().contains(i)) {
                sb.append(get(i).toString().substring(0, 1)).append(",");
            } else {
                sb.append("0,");
            }

        }
        return sb.toString();
    }

    

    public boolean isConvertedToArray() {
        return convertedToArray;
    }

    public double getLength() {
        double length = 0;
        for (int i : keySet()) {
            length += Math.pow(get(i), 2);
        }
        return Math.sqrt(length);
    }

    public void convertToArrays() {

        keySetArray = new int[keySet().size()];
        valueSet = new double[keySet().size()];
        Iterator<Integer> iterator = keySet().iterator();
        int counter = 0;
        while (iterator.hasNext()) {
            keySetArray[counter] = iterator.next();
            valueSet[counter] = this.get(keySetArray[counter]);
            counter++;
        }
        convertedToArray = true;
    }

    /**
     * remove all elements whose magnitude is < threshold
     *
     * @param threshold
     */
    public void prune(double threshold) {
        for (Iterator<Integer> it = keySet().iterator(); it.hasNext();) {
            int key = it.next();
            if (Math.abs(get(key)) < threshold) {
                it.remove();
            }
        }
    }

    public double getCosineSimHPScaled(SparseVector v, double[] scaleWeight) {
        double cosine = 0.0;
        double length1 = 0;

        if (!this.isConvertedToArray()) {
            this.convertToArrays();
        }
        if (!v.isConvertedToArray()) {
            v.convertToArrays();
        }
        int index1 = 0;
        int index2 = 0;
        int[] keys1 = this.getKeySetArray();
        int[] keys2 = v.getKeySetArray();
        double[] vals1 = this.getValueSet();
        double[] vals2 = v.getValueSet();

        for (int i = 0; i < vals1.length; i++) {
            length1 += Math.pow((vals1[i] / scaleWeight[keys1[i]]), 2);
        }
        length1 = Math.sqrt(length1);
        if (length1 == 0) {
            return 0.0;
        }

        double length2 = 0;
        for (int i = 0; i < vals2.length; i++) {
            length1 += Math.pow((vals2[i] / scaleWeight[keys2[i]]), 2);
        }
        length2 = Math.sqrt(length2);
        if (length2 == 0) {
            return 0.0;
        }

        while (index1 < keys1.length && index2 < keys2.length) {
            int comp = keys1[index1] - keys2[index2];
            int currentUsedIndexOfVec = keys1[index1];
            if (comp == 0) {
                cosine += ((vals1[index1++] * vals2[index2++]) / (scaleWeight[currentUsedIndexOfVec] * scaleWeight[currentUsedIndexOfVec]));
            } else if (comp < 0) {
                index1++;
            } else {
                index2++;
            }
        }

        return (cosine / (length1 * length2));

    }

    public double getCosineSimHP(SparseVector v) {
        double cosine = 0.0;
        double length1 = 0;

        if (!this.isConvertedToArray()) {
            this.convertToArrays();
        }
        if (!v.isConvertedToArray()) {
            v.convertToArrays();
        }

        int[] keys1 = this.getKeySetArray();
        int[] keys2 = v.getKeySetArray();
        double[] vals1 = this.getValueSet();
        double[] vals2 = v.getValueSet();

        for (double i : vals1) {
            length1 += Math.pow(i, 2);
        }
        length1 = Math.sqrt(length1);
        if (length1 == 0) {
            return 0.0;
        }

        double length2 = 0;
        for (double i : vals2) {
            length2 += Math.pow(i, 2);
        }
        length2 = Math.sqrt(length2);
        if (length2 == 0) {
            return 0.0;
        }

        int index1 = 0;
        int index2 = 0;
        while (index1 < keys1.length && index2 < keys2.length) {
            int comp = keys1[index1] - keys2[index2];
            if (comp == 0) {
                cosine += (vals1[index1++] * vals2[index2++]);
            } else if (comp < 0) {
                index1++;
            } else {
                index2++;
            }
        }

        return (cosine / (length1 * length2));

    }

    public double getInnerItSelf() {
        if (lengthCompuetd) {
            return this.lengthPower2;
        } else {

            double length2 = 0.0;
            if (!this.isConvertedToArray()) {
                this.convertToArrays();
            }

            double[] vals1 = this.getValueSet();

            for (double i : vals1) {
                length2 += Math.pow(i, 2);
            }
            this.lengthPower2 = length2;
            lengthCompuetd = true;
            return length2;
        }
    }

    public double sparseFEuclidDistance(
            SparseVector v) {
        if (!this.isConvertedToArray()) {
            this.convertToArrays();
        }
        if (!v.isConvertedToArray()) {
            v.convertToArrays();
        }
        double sum = 0.0;
        int index1 = 0;
        int index2 = 0;
        int[] keys1 = this.getKeySetArray();
        int[] keys2 = v.getKeySetArray();
        double[] vals1 = this.getValueSet();
        double[] vals2 = v.getValueSet();
        while (index1 < keys1.length && index2 < keys2.length) {
            int comp = keys1[index1] - keys2[index2];
            //  int currentUsedIndexOfVec = keys1[index1];
            double diff
                    = (comp == 0)
                            ? (vals1[index1++] - vals2[index2++])
                            : ((comp < 0)
                                    ? vals1[index1++]
                                    : vals2[index2++]);
            // here do the double thingy weightScale[currentUsedIndexOfVec]
            sum += diff * diff;
        }
        for (; index1 < keys1.length; ++index1) {
            sum += vals1[index1] * vals1[index1];
        }
        for (; index2 < keys2.length; ++index2) {
            sum += vals2[index2] * vals2[index2];
        }
        return Math.sqrt(sum);
    }

    public double sparseFEuclidDistanceScaled(
            SparseVector v, double[] weightsScale) {
        if (!this.isConvertedToArray()) {
            this.convertToArrays();
        }
        if (!v.isConvertedToArray()) {
            v.convertToArrays();
        }
        double sum = 0.0;
        int index1 = 0;
        int index2 = 0;
        int[] keys1 = this.getKeySetArray();
        int[] keys2 = v.getKeySetArray();
        double[] vals1 = this.getValueSet();
        double[] vals2 = v.getValueSet();
        while (index1 < keys1.length && index2 < keys2.length) {
            int comp = keys1[index1] - keys2[index2];
            int currentUsedIndexOfVec = keys1[index1];
            double diff
                    = (comp == 0)
                            ? (vals1[index1++] - vals2[index2++])
                            : ((comp < 0)
                                    ? vals1[index1++]
                                    : vals2[index2++]);
            sum += (diff * diff) / (weightsScale[currentUsedIndexOfVec] * weightsScale[currentUsedIndexOfVec]);
        }
        for (; index1 < keys1.length; ++index1) {
            int currentUsedIndexOfVec = keys1[index1];
            sum += vals1[index1] * vals1[index1] / (weightsScale[currentUsedIndexOfVec] * weightsScale[currentUsedIndexOfVec]);
        }
        for (; index2 < keys2.length; ++index2) {
            int currentUsedIndexOfVec = keys2[index2];
            sum += vals2[index2] * vals2[index2] / (weightsScale[currentUsedIndexOfVec] * weightsScale[currentUsedIndexOfVec]);
        }
        return Math.sqrt(sum);
    }

    public double sparseRMIIDistanceL1(
            SparseVector v) {
        if (!this.isConvertedToArray()) {
            this.convertToArrays();
        }
        if (!v.isConvertedToArray()) {
            v.convertToArrays();
        }
        double sum = 0.0;
        int index1 = 0;
        int index2 = 0;
        int[] keys1 = this.getKeySetArray();
        int[] keys2 = v.getKeySetArray();
        double[] vals1 = this.getValueSet();
        double[] vals2 = v.getValueSet();
        while (index1 < keys1.length && index2 < keys2.length) {
            int comp = keys1[index1] - keys2[index2];
            double diff
                    = (comp == 0)
                            ? (vals1[index1++] - vals2[index2++])
                            : ((comp < 0)
                                    ? vals1[index1++]
                                    : vals2[index2++]);

            if (diff != 0.0) {
                sum += Math.log(Math.abs(diff));
            }

        }
        for (; index1 < keys1.length; ++index1) {
            //   int currentUsedIndexOfVec = keys1[index1];
            if (vals1[index1] != 0.0) {
                sum += Math.log(Math.abs(vals1[index1]));
            }

        }
        for (; index2 < keys2.length; ++index2) {
            if (vals2[index2] != 0.0) {
                sum += Math.log(Math.abs(vals2[index2]));
            }
        }
        return Math.sqrt(sum);
    }

    public double sparseRMIIDistanceL1Scaled(
            SparseVector v, double[] weightScale) {
        if (!this.isConvertedToArray()) {
            this.convertToArrays();
        }
        if (!v.isConvertedToArray()) {
            v.convertToArrays();
        }

        double sum = 0.0;
        int index1 = 0;
        int index2 = 0;
        int[] keys1 = this.getKeySetArray();
        int[] keys2 = v.getKeySetArray();
        double[] vals1 = this.getValueSet();
        double[] vals2 = v.getValueSet();
        while (index1 < keys1.length && index2 < keys2.length) {

            int comp = keys1[index1] - keys2[index2];
            int currentUsedIndexOfVec = keys1[index1];
            double diff
                    = (comp == 0)
                            ? (vals1[index1++] - vals2[index2++])
                            : ((comp < 0)
                                    ? vals1[index1++]
                                    : vals2[index2++]);
            if (diff != 0.0) {
                sum += Math.log(Math.abs(diff / weightScale[currentUsedIndexOfVec]));
            }

        }
        for (; index1 < keys1.length; ++index1) {
            int currentUsedIndexOfVec = keys1[index1];
            if (vals1[index1] != 0.0) {
                sum += Math.log(Math.abs(vals1[index1] / weightScale[currentUsedIndexOfVec]));
            }

        }
        for (; index2 < keys2.length; ++index2) {
            int currentUsedIndexOfVec = keys2[index2];
            if (vals2[index2] != 0.0) {
                sum += Math.log(Math.abs(vals2[index2] / weightScale[currentUsedIndexOfVec]));
            }
        }
        return sum;
    }

    public double SparceL4Distance(
            SparseVector v) {
        if (!this.isConvertedToArray()) {
            this.convertToArrays();
        }
        if (!v.isConvertedToArray()) {
            v.convertToArrays();
        }

        double sum = 0.0;
        int index1 = 0;
        int index2 = 0;
        int[] keys1 = this.getKeySetArray();
        int[] keys2 = v.getKeySetArray();
        double[] vals1 = this.getValueSet();
        double[] vals2 = v.getValueSet();

        double Sumx2y2Inner = 0.0;
        double Sumx3yInner = 0.0;
        double Sumxy3Inner = 0.0;

        while (index1 < keys1.length && index2 < keys2.length) {

            int comp = keys1[index1] - keys2[index2];
            //  int currentUsedIndexOfVec = keys1[index1];
            double currentValX = 0.0;
            double currentValY = 0.0;
            if (comp == 0) {
                currentValX = vals1[index1++];
                currentValY = vals2[index2++];
                double xyInner = currentValX * currentValY;
                Sumx2y2Inner += (xyInner * xyInner);
                Sumx3yInner += (xyInner * currentValY * currentValY);
                Sumxy3Inner += (xyInner * currentValX * currentValX);

            } else if (comp < 0) {
                currentValX = vals1[index1++];
                //   currentValY = 0.0;
            } else {
                // currentValX=0;
                currentValY = vals2[index2++];
            }
        }
        double lengthPower21 = this.getInnerItSelf();
        double lengthPower22 = v.getInnerItSelf();
        double l4 = lengthPower21 * lengthPower21 + lengthPower22 * lengthPower22
                + 6 * Sumx2y2Inner - 4 * Sumx3yInner - 4 * Sumxy3Inner;
        return l4;
    }

    public void setLabel(String vectorLabel) {
        this.strLabel = vectorLabel;
    }

    public String getLabel() {
        return strLabel;
    }

}
