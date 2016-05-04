/*
 * All rights reserved
 * Copyright Behrang Qasemizadeh
 */
package de.phil.hhu.randomized;


import ie.pars.clac.vector.SimpleSparse;
import ie.pars.clac.vector.SparseVector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


/**
 *
 * @author Behrang QasemiZadeh <behrang@pars.ie>
 */
public class NormalRandomSimpleVector {

   
    /**
     *
     * @param dimension is the diemnsion of vector
     * @param beta is the probability of the
     */
    double beta;
    int reducedDimension;
    int originalDimension;
    int numberNonZeroElement;
    final double DATA_SPARSITY = 0.75;
    Random randomElement = new Random();
    Random randomValuePositive = new Random();
    Random randomValueNegative = new Random();

    boolean verbose = false;

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
   
    public NormalRandomSimpleVector(int originalDimension, int reducedDimension, int directNumberOfNoneZeroElement) {
       
        this.originalDimension = originalDimension;
        this.reducedDimension = reducedDimension;
        
        if (directNumberOfNoneZeroElement == 0) {
            this.beta = 1 / Math.sqrt(originalDimension * DATA_SPARSITY);
            //numberNonZeroElement = (int) (2 * Math.round(beta * reducedDimension / 2)); // get the closet 
        } else {
            this.beta = 1/ (reducedDimension * 1.0/directNumberOfNoneZeroElement);
            //numberNonZeroElement =   directNumberOfNoneZeroElement ; // get the closet 
        }
        randomElement = new Random();
        randomValuePositive = new Random();
        randomValueNegative = new Random();
        
        numberNonZeroElement = directNumberOfNoneZeroElement;
               // (int) (2 * Math.round(beta * reducedDimension / 2)); // get the closet 
        if (verbose) {
            System.out.println("Parameters are n=" + originalDimension + " m=" + reducedDimension + " beta=" + beta);
            System.out.println("Number of non-zero elements: " + numberNonZeroElement);
        }
    }

    /**
     * Get the next element of index vectors that will be non-zero randomly
     *
     * @return
     */
    private int getNextRandomIndexElement() {
        return randomElement.nextInt(reducedDimension);
    }

    public SimpleSparse getRandomVector() {
               int numberNonZeroElement = 
                (int) (2 * Math.round(beta * reducedDimension / 2)); // get the closet 
        SimpleSparse spd = new SimpleSparse();
        spd.setDimensionality(this.reducedDimension);
        Set<Integer> nonzeroElement = new HashSet<>();
        while (nonzeroElement.size() != numberNonZeroElement) {
            nonzeroElement.add(getNextRandomIndexElement());
        }
        Iterator<Integer> iterator = nonzeroElement.iterator();
        // set half to poisitve another half to negative

        boolean isPostiveTurn = true;
        while (iterator.hasNext()) {
            if (isPostiveTurn) {
                
                spd.setValue(iterator.next(), +1.0);
                isPostiveTurn = false;
            } else {
                
                spd.setValue(iterator.next(), -1.0);
                isPostiveTurn = true;
            }
        }
        
        return spd;
    }
    
    
      public SimpleSparse getPositiveOnlyRandomVector() {
          
        SimpleSparse spd = new SimpleSparse();
        spd.setDimensionality(this.reducedDimension);
        Set<Integer> nonzeroElement = new HashSet<>();
        
//        while (nonzeroElement.size() != numberNonZeroElement/2) {
//            nonzeroElement.add(getNextRandomIndexElement());
//        }
        
         while (nonzeroElement.size() != numberNonZeroElement) {
//             Random r = new Random(reducedDimension);
            nonzeroElement.add(getNextRandomIndexElement());
        }
        Iterator<Integer> iterator = nonzeroElement.iterator();
        // set half to poisitve another half to negative


        

        boolean isPostiveTurn = true;
        while (iterator.hasNext()) {
            //if (isPostiveTurn) {
              double val =  1.0/
                           Math.pow( 
                           randomValuePositive.nextDouble()
                           ,.7);
            // I verified that it works with ceil too
              double ceil = 
                    Math.ceil(val);
                spd.setValue(iterator.next(), 
                    
                        ceil
                );
               
        }
        
        return spd;
    }
    
    
    public SparseVector getSparseDoubleRandomVector() {
        SparseVector spd = new SparseVector(reducedDimension);
        Set<Integer> nonzeroElement = new HashSet<>();
        while (nonzeroElement.size() != numberNonZeroElement) {
            nonzeroElement.add(getNextRandomIndexElement());
        }
        Iterator<Integer> iterator = nonzeroElement.iterator();
        // set half to poisitve another half to negative

        boolean isPostiveTurn = true;
        while (iterator.hasNext()) {
            if (isPostiveTurn) {
                
                spd.put(iterator.next(), +1.0);
                isPostiveTurn = false;
            } else {
                
                spd.put(iterator.next(), -1.0);
                isPostiveTurn = true;
            }
        }
        return spd;
    }
    
    public  String getInfo(){
        return "#\t data_sparsity_factor:" + this.DATA_SPARSITY+"\t non-zero elements:" + this.numberNonZeroElement + "\tbeta:" + this.beta;
    }

    public int getNumberNonZeroElement() {
        return numberNonZeroElement;
    }
    
    
    
    
    
}
