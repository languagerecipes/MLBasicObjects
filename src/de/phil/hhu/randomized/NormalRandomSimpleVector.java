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
        System.out.println("Parameters are n=" + originalDimension + " m=" + reducedDimension + " beta=" + beta);
        numberNonZeroElement = (int) (2 * Math.round(beta * reducedDimension / 2)); // get the closet 
        System.out.println("Number of non-zero elements: " + numberNonZeroElement);
        
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
        while (nonzeroElement.size() != numberNonZeroElement) {
            nonzeroElement.add(getNextRandomIndexElement());
        }
        Iterator<Integer> iterator = nonzeroElement.iterator();
        // set half to poisitve another half to negative


        

        boolean isPostiveTurn = true;
        while (iterator.hasNext()) {
            //if (isPostiveTurn) {
                
                spd.setValue(iterator.next(), 
                       //1);
                     //   +5.0/ //Math.log(
                    //        1.0/   
                  //                  Math.pow(
                      randomValuePositive.nextDouble()
                //                            ,.8)
                );
                               // ) //power did not work for pow>1
                              //  )
                
               // );
              // isPostiveTurn = false;
           // } else {
                // 1 /Math.sqrt(randomValuePositive.nextDouble())
            //    spd.setValue(iterator.next(), 
            //            1.0/
                       // Math.log(
                     //   Math.sqrt(
             //                Math.pow    (randomValuePositive.nextDouble(),0.3)
                     //   )
               // )
              //          );
           //    isPostiveTurn = true;
         //   }
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
