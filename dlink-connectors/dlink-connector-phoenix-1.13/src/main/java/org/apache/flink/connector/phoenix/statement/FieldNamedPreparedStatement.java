/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.connector.phoenix.statement;

import java.math.BigDecimal;
import java.sql.*;

/**
 * This is a wrapper around {@link PreparedStatement} and allows the users to set parameters by name
 * instead of by index. This allows users to use the same variable parameter multiple times in a
 * statement.
 *
 * <p>Code such as this:
 *
 * <pre>
 *   Connection con = getConnection();
 *   String query = "select * from my_table where first_name=? or last_name=?";
 *   PreparedStatement st = con.prepareStatement(query);
 *   st.setString(1, "bob");
 *   st.setString(2, "bob");
 *   ResultSet rs = st.executeQuery();
 * </pre>
 *
 * <p>Can be replaced with:
 *
 * <pre>
 *   Connection con = getConnection();
 *   String query = "select * from my_table where first_name=:name or last_name=:name";
 *   FieldNamedPreparedStatement st = FieldNamedPreparedStatement.prepareStatement(con, query, new String[]{"name"});
 *   st.setString(0, "bob");
 *   ResultSet rs = st.executeQuery();
 * </pre>
 */
public interface FieldNamedPreparedStatement extends AutoCloseable {

    /**
     * Creates a <code>NamedPreparedStatement</code> object for sending parameterized SQL statements
     * to the database.
     *
     * @param connection the connection used to connect to database.
     * @param sql an SQL statement that may contain one or more ':fieldName' as parameter
     *     placeholders
     * @param fieldNames the field names in schema order used as the parameter names
     */
    static FieldNamedPreparedStatement prepareStatement(
            Connection connection, String sql, String[] fieldNames) throws SQLException {
        return FieldNamedPreparedStatementImpl.prepareStatement(connection, sql, fieldNames);
    }

    /**
     * Clears the current parameter values immediately.
     *
     * <p>In general, parameter values remain in force for repeated use of a statement. Setting a
     * parameter value automatically clears its previous value. However, in some cases it is useful
     * to immediately release the resources used by the current parameter values; this can be done
     * by calling the method <code>clearParameters</code>.
     *
     * @see PreparedStatement#clearParameters()
     */
    void clearParameters() throws SQLException;

    /**
     * Executes the SQL query in this <code>NamedPreparedStatement</code> object and returns the
     * <code>ResultSet</code> object generated by the query.
     *
     * @see PreparedStatement#executeQuery()
     */
    ResultSet executeQuery() throws SQLException;

    /**
     * Adds a set of parameters to this <code>NamedPreparedStatement</code> object's batch of
     * commands.
     *
     * @see PreparedStatement#addBatch()
     */
    void addBatch() throws SQLException;

    /**
     * Submits a batch of commands to the database for execution and if all commands execute
     * successfully, returns an array of update counts. The <code>int</code> elements of the array
     * that is returned are ordered to correspond to the commands in the batch, which are ordered
     * according to the order in which they were added to the batch.
     *
     * @see PreparedStatement#executeBatch()
     */
    int[] executeBatch() throws SQLException;

    /**
     * Sets the designated parameter to SQL <code>NULL</code>.
     *
     * <p><B>Note:</B> You must specify the parameter's SQL type.
     *
     * @see PreparedStatement#setNull(int, int)
     */
    void setNull(int fieldIndex, int sqlType) throws SQLException;

    /**
     * Sets the designated parameter to the given Java <code>boolean</code> value. The driver
     * converts this to an SQL <code>BIT</code> or <code>BOOLEAN</code> value when it sends it to
     * the database.
     *
     * @see PreparedStatement#setBoolean(int, boolean)
     */
    void setBoolean(int fieldIndex, boolean x) throws SQLException;

    /**
     * Sets the designated parameter to the given Java <code>byte</code> value. The driver converts
     * this to an SQL <code>TINYINT</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setByte(int, byte)
     */
    void setByte(int fieldIndex, byte x) throws SQLException;

    /**
     * Sets the designated parameter to the given Java <code>short</code> value. The driver converts
     * this to an SQL <code>SMALLINT</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setShort(int, short)
     */
    void setShort(int fieldIndex, short x) throws SQLException;

    /**
     * Sets the designated parameter to the given Java <code>int</code> value. The driver converts
     * this to an SQL <code>INTEGER</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setInt(int, int)
     */
    void setInt(int fieldIndex, int x) throws SQLException;

    /**
     * Sets the designated parameter to the given Java <code>long</code> value. The driver converts
     * this to an SQL <code>BIGINT</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setLong(int, long)
     */
    void setLong(int fieldIndex, long x) throws SQLException;

    /**
     * Sets the designated parameter to the given Java <code>float</code> value. The driver converts
     * this to an SQL <code>REAL</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setFloat(int, float)
     */
    void setFloat(int fieldIndex, float x) throws SQLException;

    /**
     * Sets the designated parameter to the given Java <code>double</code> value. The driver
     * converts this to an SQL <code>DOUBLE</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setDouble(int, double)
     */
    void setDouble(int fieldIndex, double x) throws SQLException;

    /**
     * Sets the designated parameter to the given <code>java.math.BigDecimal</code> value. The
     * driver converts this to an SQL <code>NUMERIC</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setBigDecimal(int, BigDecimal)
     */
    void setBigDecimal(int fieldIndex, BigDecimal x) throws SQLException;

    /**
     * Sets the designated parameter to the given Java <code>String</code> value. The driver
     * converts this to an SQL <code>VARCHAR</code> or <code>LONGVARCHAR</code> value (depending on
     * the argument's size relative to the driver's limits on <code>VARCHAR</code> values) when it
     * sends it to the database.
     *
     * @see PreparedStatement#setString(int, String)
     */
    void setString(int fieldIndex, String x) throws SQLException;

    /**
     * Sets the designated parameter to the given Java array of bytes. The driver converts this to
     * an SQL <code>VARBINARY</code> or <code>LONGVARBINARY</code> (depending on the argument's size
     * relative to the driver's limits on <code>VARBINARY</code> values) when it sends it to the
     * database.
     *
     * @see PreparedStatement#setBytes(int, byte[])
     */
    void setBytes(int fieldIndex, byte[] x) throws SQLException;

    /**
     * Sets the designated parameter to the given <code>java.sql.Date</code> value using the default
     * time zone of the virtual machine that is running the application. The driver converts this to
     * an SQL <code>DATE</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setDate(int, Date)
     */
    void setDate(int fieldIndex, Date x) throws SQLException;

    /**
     * Sets the designated parameter to the given <code>java.sql.Time</code> value. The driver
     * converts this to an SQL <code>TIME</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setTime(int, Time)
     */
    void setTime(int fieldIndex, Time x) throws SQLException;

    /**
     * Sets the designated parameter to the given <code>java.sql.Timestamp</code> value. The driver
     * converts this to an SQL <code>TIMESTAMP</code> value when it sends it to the database.
     *
     * @see PreparedStatement#setTimestamp(int, Timestamp)
     */
    void setTimestamp(int fieldIndex, Timestamp x) throws SQLException;

    /**
     * Sets the value of the designated parameter using the given object.
     *
     * @see PreparedStatement#setObject(int, Object)
     */
    void setObject(int fieldIndex, Object x) throws SQLException;

    /**
     * Releases this <code>Statement</code> object's database and JDBC resources immediately instead
     * of waiting for this to happen when it is automatically closed. It is generally good practice
     * to release resources as soon as you are finished with them to avoid tying up database
     * resources.
     *
     * @see PreparedStatement#close()
     */
    void close() throws SQLException;
}
