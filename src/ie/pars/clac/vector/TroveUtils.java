///*
// * Copyright (C) 2016 Behrang QasemiZadeh <zadeh at phil.hhu.de>
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>.
// */
//package ie.pars.clac.vector;
//
//import gnu.trove.iterator.TDoubleIterator;
//import gnu.trove.iterator.TIntIterator;
//import gnu.trove.list.array.TDoubleArrayList;
//
///**
// * reused codes from kuzman
// * @author Behrang QasemiZadeh <zadeh at phil.hhu.de>
// */
//public class TroveUtils {
//
//
//    /**
//     * If number becomes infinity replace it by a smallest value possible
//     *
//     * @param list
//     * @return
//     */
//    public static TDoubleArrayList exp(TDoubleArrayList list) {
//        TDoubleArrayList exp = new TDoubleArrayList(list.size());
//        for (int i = 0; i < list.size(); i++) {
//            double value = Math.exp(list.getQuick(i));
//            exp.add(value);
//        }
//        return exp;
//    }
//
//    public static double sum(TDoubleArrayList list) {
//        double sum = 0;
//        for (int i = 0; i < list.size(); i++) {
//            double value = list.getQuick(i);
//            if (!(Double.isInfinite(value) || Double.isNaN(value))) {
//                sum += value;
//            }
//        }
//        return sum;
//    }
//
//    public static double max(TDoubleArrayList list) {
//        return list.max();
//    }
//
//    /**
//     * Adds num to all elements of the list
//     *
//     * @param list
//     * @param num
//     * @return
//     */
//    public static void addValue(TDoubleArrayList list, double num) {
//        for (int i = 0; i < list.size(); i++) {
//            list.setQuick(i, list.getQuick(i) + num);
//        }
//    }
//
//    public static double cosineSimilarity(TSparseVector u, TSparseVector v) {
//        double u_norm = norm(u);
//        if (u_norm == 0) {
//            return 0;
//        }
//
//        double v_norm = norm(v);
//        if (v_norm == 0) {
//            return 0;
//        }
//
//        return innerProd(u, v) / (u_norm * v_norm);
//    }
//
//     
//
//    private static double norm(TSparseVector sv) {
//        TDoubleIterator iterator = sv.values.iterator();
//        double norm = 0.0;
//        while (iterator.hasNext()) {
//            double next = iterator.next();
//            norm += (next * next);
//        }
//        norm = Math.sqrt(norm);
//        System.out.println(norm);
//        return norm;
//        
//    }
//    
//    public static double innerProd(TSparseVector vector1, TSparseVector vector2) {
//        double dotProduct = 0.0;
//
//        TIntIterator iter = vector1.indices.iterator();
//        while (iter.hasNext()) {
//            int key = iter.next();
//
//            double value2 = vector2.getValue(key);
//            if (value2 != 0) {
//                double value = vector1.getValue(key);
//                dotProduct += value * value2;
//            }
//        }
//        System.out.println(dotProduct);
//        return dotProduct;
//    }
//
//   
//
//    public static void main(String[] s){
//    //errornous
////        TSparseVector tsp = new TSparseVector();
////        tsp.add(1, 2.6776767);
////        tsp.add(2, 2.66767);
////        tsp.add(7, 1009.4676767);
////        //tsp.add(7, 9.4876776767);
////        System.out.println(cosineSimilarity(tsp, tsp));
//    }
//}
