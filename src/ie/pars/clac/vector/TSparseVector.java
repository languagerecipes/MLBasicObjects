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

import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;



/**
 *
 * @author Behrang QasemiZadeh <zadeh at phil.hhu.de>
 */
public class TSparseVector {
 



    /**
     * This code is taken from http://pr-toolkit.googlecode.com/svn/pr-toolkit/trunk/src/util/SparseVector.java 
     * As said by kuzman, not the most efficient one but certainly better than TreeMap based I used (in the sense that eats less memory)
     * This implementation suits the requirements of my usecase
     * a sparse vector implementation. Not necessarily the most efficient.
     *
     * @author kuzman
     *
     */
    

        TIntArrayList indices;
        TDoubleArrayList values;
        TIntIntHashMap positions;
        

        public TSparseVector() {
            indices = new TIntArrayList();
            values = new TDoubleArrayList();
            positions = new TIntIntHashMap();
        }

        TSparseVector(TIntArrayList indices, TIntIntHashMap positions, TDoubleArrayList values) {
            this.indices = indices;
            this.values = values;
            this.positions = positions;
        }

        public void add(int index, double value) {
		indices.add(index);
		values.add(value);
		positions.put(index, indices.size()-1);
	}

	/**
	 * Gets Index at position i
	 * @param i
	 * @return
	 */
	public int getIndexAt(int i) {
		return indices.get(i);
	}

	/**
	 * Gets value at position i.
	 * Note not value at index i but at position i
	 * @param i
	 * @return
	 */
	public double getValueAt(int i) {
		if(i>=values.size()){
			return 0;
		}
		return values.get(i);
	}

	/**
	 * Gets value at index index
	 * @param index
	 * @return
	 */
	public double getValue(int index) {
		if(!positions.contains(index)){
			return 0;
		}
		return values.get(positions.get(index));
	}
	
	
	public int numEntries() {
		return indices.size();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < indices.size(); i++){
			sb.append(indices.get(i) + ":"+values.get(i)+" ");
		}
		return sb.toString();
	}
	
	
	/**
	 * Adds all entries of the sparse vector
	 * @return
	 */
	public  final double sum(){
		return TroveUtils.sum(values);
	}
	
	
	/**
	 * The max value of the list
	 * @return
	 */
	public  final double max(){
		return values.max();
	}
	
	/**
	 * The min value of the list
	 * @return
	 */
	public  final double min(){
		return values.min();
	}
	
	/** Adds num to all elements of the list
	 * 
	 * @param num
	 */
	public final void plusEquals(double num){
		TroveUtils.addValue(values, num);
	}
	
	/**
	 * Return a new sparse vector with exponential of each entry
	 * @return
	 */
	public final TSparseVector expEntries(){
		return new TSparseVector(indices,positions,TroveUtils.exp(values));
	}

	
	

}
