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
package maps;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

/**
 *
 * @author Behrang QasemiZadeh <zadeh at phil.hhu.de>
 * @param <T>
 */
public class StringMap<T> extends TCustomHashMap<String, T> {

    private static final HashingStrategy<String> STRING_HASH = new HashingStrategy<String>() {
        @Override
        public int computeHashCode(String t) {
            return t.hashCode();
        }

        @Override
        public boolean equals(String t, String t1) {
            return t.equals(t1);
        }

    };
    
    public StringMap(){
        super(STRING_HASH,10,0.97f);
    }
}
