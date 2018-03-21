/*
 * Copyright (C) 2011-2018 clueminer.org
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
package org.clueminer.chart.data;

/**
 * <p>
 * Class for accessing a specific column of a data source. The data of the
 * column can be accessed using the {@code get(int)} method.</p>
 *
 * <p>
 * Example for accessing value at column 2, row 3 of a data source:</p>
 * <pre>
 * Column col = new Column(dataSource, 2);
 * Number v = col.get(3);
 * </pre>
 *
 * @see DataSource
 */
public abstract class Column extends DataAccessor {

    /**
     * Version id for serialization.
     */
    private static final long serialVersionUID = 7380420622890027262L;

    /**
     * Initializes a new instance with the specified data source and column
     * index.
     *
     * @param source Data source.
     * @param col    Column index.
     */
    public Column(DataSource source, int col) {
        super(source, col);
    }

    @Override
    public Comparable<?> get(int row) {
        DataSource source = getSource();
        if (source == null) {
            return null;
        }
        return source.get(getIndex(), row);
    }

    @Override
    public int size() {
        return getSource().getRowCount();
    }

    /**
     * Returns whether this column only contains numbers.
     *
     * @return {@code true} if this column is numeric, otherwise {@code false}.
     */
    public boolean isNumeric() {
        return getSource().isColumnNumeric(getIndex());
    }
}
