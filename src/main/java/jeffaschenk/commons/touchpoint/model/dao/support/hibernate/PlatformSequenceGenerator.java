package jeffaschenk.commons.touchpoint.model.dao.support.hibernate;

import jeffaschenk.commons.parsers.AnnotationParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.exception.JDBCExceptionHelper;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;
import jeffaschenk.commons.touchpoint.model.RootElement;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Override Hibernate Sequence Generation Capabilities.
 *
 * @author jeffaschenk@gmail.com
 *         <p/>
 *         * Adapted from Community version.
 * @version $Id: $
 */
public class PlatformSequenceGenerator implements
        PersistentIdentifierGenerator, Configurable {

    // **********************************************
    // Logging
    private static final Log log = LogFactory
            .getLog(PlatformSequenceGenerator.class);

    // **********************************************
    // Cache of Injected Sequences to Class Mapping
    private static Map<String, Sequence> sequences = new TreeMap<String, Sequence>();

    private Dialect dialect;

    private String sequenceName;

    /**
     * {@inheritDoc}
     * <p/>
     * Generate the Sequence Identifier
     */
    @SuppressWarnings("unchecked")
    @Override
    public Serializable generate(SessionImplementor session, Object obj)
            throws HibernateException {
        // *************************************
        // Initialize
        if (!(obj instanceof RootElement)) {
            log.error("Illegal Object Class:[" + obj.getClass().getName()
                    + "] using Element Sequence Generator!");
            throw new IllegalArgumentException(
                    "The Specified Object is not derived from RootElement!");
        }
        // *************************************
        // Check for Sequence
        // to use by Class lookup in our Cache.
        String sequenceName = null;
        String sequenceSQL = null;
        if (sequences.containsKey(obj.getClass().getName())) {
            sequenceName = sequences.get(obj.getClass().getName()).getName();
            sequenceSQL = sequences.get(obj.getClass().getName()).getSql();
        } else {
            sequenceName = obtainIdentitySequence((Class<? extends RootElement>) obj.getClass());
            sequenceSQL = dialect.getSequenceNextValString(sequenceName);
            Sequence sequence = new Sequence(((RootElement) obj).getClass(), sequenceName, sequenceSQL);
            sequences.put(obj.getClass().getName(), sequence);
        }
        // *************************************
        // Now Create SQL
        PreparedStatement sqlStatement = null;
        try {
            sqlStatement = session.getBatcher().prepareSelectStatement(sequenceSQL);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        // *************************************
        // Obtain our Next Index Value.
        try {
            try {
                ResultSet rs = sqlStatement.executeQuery();
                final Serializable result;
                try {
                    rs.next();
                    result = rs.getInt(1);
                } finally {
                    rs.close();
                }
                if (log.isDebugEnabled()) {
                    log.debug("Sequence identifier generated:[" + result + "]");
                }
                return result;
            } finally {
                session.getBatcher().closeStatement(sqlStatement);
            }
        } catch (SQLException sqle) {
            throw
                    JDBCExceptionHelper.convert(session.getFactory()
                            .getSQLExceptionConverter(), sqle,
                            "could not get next sequence value", sequenceSQL);
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Configuration of the Sequence Mapping.
     */
    @Override
    public void configure(Type type, Properties params, Dialect dialect)
            throws MappingException {
        this.dialect = dialect;
        // This is only used during Database creation, which is only used in test
        String table = PropertiesHelper.getString("target_table", params, "TABLE");
        sequenceName = table + "_seq_id";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] sqlCreateStrings(Dialect dialect) throws HibernateException {
        String[] ddl = dialect.getCreateSequenceStrings(sequenceName, 0, 1);

        return ddl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] sqlDropStrings(Dialect dialect) throws HibernateException {
        return dialect.getDropSequenceStrings(sequenceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object generatorKey() {
        return sequenceName;
    }

    // **************************************
    // Private Helper Methods
    // **************************************

    /**
     * Obtain the Identity Sequence which we are to
     * use based upon the getId method for the specified Class.
     */
    private String obtainIdentitySequence(Class<? extends RootElement> clazz) {
        // *************************************
        // Obtain the getID method for this
        // RootElement Entity Class.
        Method getIDMethod = null;
        try {
            getIDMethod = clazz.getMethod("getId", new Class<?>[]{});
        } catch (NoSuchMethodException nsme) {
            log.error("Illegal Object Class:[" + clazz.getName()
                    + "] no Method Specified for getId()!");
            throw new IllegalArgumentException(
                    "The Specified Object has no getId() method signature!");
        }
        if (getIDMethod == null) {
            log.error("Illegal Object Class:[" + clazz.getName()
                    + "] no Method Specified for getId()!");
            throw new IllegalArgumentException(
                    "The Specified Object has no getId() method signature!");
        }
        // **************************************
        // Determine the Sequence we need to use
        // for this RootElement Class from the
        // specified annotation.
        String annotationString = null;
        AnnotationParser annotationParser = new AnnotationParser();
        Map<String, String> methodAnnotations = annotationParser
                .parseSpecificClassMethodAnnotations(getIDMethod);
        if (log.isDebugEnabled()) {
            for (String key : methodAnnotations.keySet()) {
                log.debug("Annotation Mapping Key:[" + key + "], Value:["
                        + methodAnnotations.get(key) + "]");
            }
        }
        if (methodAnnotations != null) {
            annotationString = methodAnnotations
                    .get("getId:org.hibernate.annotations.GenericGenerator.parameters");
        }
        if (annotationString == null) {
            log
                    .error("Illegal Object Class:["
                            + clazz.getName()
                            + "] no valid Annotations found for getId() method, unable to determine Sequence Name!");
            throw new IllegalArgumentException(
                    "Illegal Object Class:["
                            + clazz.getName()
                            + "] no valid Annotations found for getId() method, unable to determine Sequence Name!");
        }
        // **************************************
        // Now Parse The Annotation String
        String[] array = annotationString.split("value=|\\)|}");
        if (log.isDebugEnabled()) {
            log.debug("Parsing Annotation String:[" + annotationString + "]");
            for (int i = 0; i < array.length; i++) {
                log.debug("Array[" + i + "]:[" + array[i] + "]");
            }
        }
        if ((array == null) || (array.length <= 1) || (array[1] == null)
                || (array[1].isEmpty())) {
            log
                    .error("Illegal Object Class:["
                            + clazz.getName()
                            + "] no valid Annotation Found Sequence Value found in :["
                            + annotationString
                            + "], for getId() method, unable to determine Sequence Name!");
            throw new IllegalArgumentException(
                    "Illegal Object Class:["
                            + clazz.getName()
                            + "] no valid Annotation Found Sequence Value found in:["
                            + annotationString
                            + "], for getId() method, unable to determine Sequence Name!");
        }
        String sequenceName = array[1];
        if (log.isDebugEnabled()) {
            log.debug("Parsed Sequence Name:[" + sequenceName + "]");
        }
        // **************************************************************
        // Return the Sequence Name to use for this Identity.
        return sequenceName;
    }

}
