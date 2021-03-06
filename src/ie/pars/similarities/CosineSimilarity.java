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
package ie.pars.similarities;

import ie.pars.clac.vector.SimpleSparse;

/**
 *
 * @author Behrang QasemiZadeh <zadeh at phil.hhu.de>
 */
public class CosineSimilarity {

    /**
     * 
     * @param ss
     * @param ss2
     * @return 
     */
    public static double cosine(SimpleSparse ss, SimpleSparse ss2) {
        double cosine = 0.0;

        double thisLength = ss2.getLength();
        double thatLength = ss.getLength();
        double dot = 0.0;
        for (Integer key : ss2.getIndices()) {
            dot += (ss.getValue(key) * ss2.getValue(key));
        }
        cosine = dot / (thisLength * thatLength); // so funny, I thought * has privilige to / but appreantly not on my computer.. without pranthesis I get some crap

        return cosine;
    }
    
    
     public static double cosine(double[] ss, double[] ss2) throws Exception {
        double  l1 = 0.0;
        for(double d: ss){
            l1=+Math.pow(d,2);
        }
        double l2=0.0;
        double dot=0.0;
         for (int i = 0; i < ss2.length; i++) {
             double d = ss2[i];
             l2+=Math.pow(d,2);
             dot+=d*ss[i];
         }
         return dot/Math.sqrt(l1*l2);
    }
    
       

}
