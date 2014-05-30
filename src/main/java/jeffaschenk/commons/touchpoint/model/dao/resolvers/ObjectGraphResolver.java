package jeffaschenk.commons.touchpoint.model.dao.resolvers;

import jeffaschenk.commons.parameters.EntityPath;
import jeffaschenk.commons.parameters.EntityPathGroup;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Object Graph Resolver This will provide utilities to allow for proper
 * hydration as requested by the upstream service layer.
 *
 * @author jeffaschenk@gmail.com
 */
public class ObjectGraphResolver {

    /**
     * Logging
     */
    protected static Log log = LogFactory.getLog(ObjectGraphResolver.class);

    /**
     * Resolve the Object's DATA while still within session with the underlying
     * ORM. This will in essence allow for specific path hydration provided by
     * an upstream layer knowledgeable of the object model and the required and
     * necessary DATA needed to be presented.
     *
     * @param object            Object whose paths should be resolved.
     * @param pathsToBeResolved Object Paths to be resolved.
     */
    public static void resolve(Object object, String[] pathsToBeResolved) {
        if (object == null) {
            throw new IllegalArgumentException(
                    "Object is null, object must be initially instantiated.");
        }
        if ((pathsToBeResolved == null) || (pathsToBeResolved.length <= 0)) {
            log.warn("No Paths to be Resolved have been specified, ignoring.");
            return;
        }
        for (String path : pathsToBeResolved) {
            touchEntityPath(object, path);
        } // End of Path For Each Loop.
    }

    /**
     * Resolve the Object's DATA while still within session with the underlying
     * ORM. This will in essence allow for specific path hydration provided by
     * an upstream layer knowledgeable of the object model and the required and
     * necessary DATA needed to be presented.
     *
     * @param object           Object whose paths should be resolved.
     * @param pathToBeResolved Object Paths to be resolved.
     */
    public static void resolve(Object object, String pathToBeResolved) {
        if (object == null) {
            throw new IllegalArgumentException(
                    "Object is null, object must be initially instantiated.");
        }
        if ((pathToBeResolved == null) || (pathToBeResolved.isEmpty())) {
            log.warn("No Paths to be Resolved have been specified, ignoring.");
            return;
        }
        // ******************************
        // Touch the Path.
        touchEntityPath(object, pathToBeResolved);
    }

    /**
     * Resolve the Object's DATA while still within session with the underlying
     * ORM. This will in essence allow for specific path hydration provided by
     * an upstream layer knowledgeable of the object model and the required and
     * necessary DATA needed to be presented.
     *
     * @param object      Object whose paths should be resolved.
     * @param entityPaths Paths Specified in EntityPath wrapper.
     */
    public static void resolve(Object object, EntityPath[] entityPaths) {
        if (object == null) {
            throw new IllegalArgumentException(
                    "Object is null, object must be initially instantiated.");
        }
        if ((entityPaths == null) || (entityPaths.length <= 0)) {
            log.warn("No Paths to be Resolved have been specified, ignoring.");
            return;
        }
        /**
         * Iterate Over the EntityPaths
         */
        for (EntityPath entityPath : entityPaths) {
            touchEntityPath(object, entityPath.getEntityPath());
        } // End of Path Outer For Each Loop.
    }

    /**
     * Resolve the Object's DATA while still within session with the underlying
     * ORM. This will in essence allow for specific path hydration provided by
     * an upstream layer knowledgeable of the object model and the required and
     * necessary DATA needed to be presented.
     *
     * @param object     Object whose paths should be resolved.
     * @param entityPath Path Specified in EntityPath wrapper.
     */
    public static void resolve(Object object, EntityPath entityPath) {
        if (object == null) {
            throw new IllegalArgumentException(
                    "Object is null, object must be initially instantiated.");
        }
        if ((entityPath == null) || (entityPath.isEmpty())) {
            log.warn("No Paths to be Resolved have been specified, ignoring.");
            return;
        }
        // ******************************
        // Touch the Path.
        touchEntityPath(object, entityPath.getEntityPath());
    }

    /**
     * Resolve the Object's DATA while still within session with the underlying
     * ORM. This will in essence allow for specific path hydration provided by
     * an upstream layer knowledgeable of the object model and the required and
     * necessary DATA needed to be presented.
     *
     * @param object           Object whose paths should be resolved.
     * @param entityPathGroups Groups of Paths
     */
    public static void resolve(Object object, EntityPathGroup[] entityPathGroups) {
        if (object == null) {
            throw new IllegalArgumentException(
                    "Object is null, object must be initially instantiated.");
        }
        if ((entityPathGroups == null) || (entityPathGroups.length <= 0)) {
            log.warn("No Paths to be Resolved have been specified, ignoring.");
            return;
        }
        /**
         * Iterate Over all of the Entity Paths within the Group.
         */
        for (EntityPathGroup pathGroup : entityPathGroups) {
            resolve(object, pathGroup.getEntityPathArray());
        } // End of Path For Each Loop.
    }

    /**
     * Resolve the Object's DATA while still within session with the underlying
     * ORM. This will in essence allow for specific path hydration provided by
     * an upstream layer knowledgeable of the object model and the required and
     * necessary DATA needed to be presented.
     *
     * @param object          Object whose paths should be resolved.
     * @param entityPathGroup Groups of Paths
     */
    public static void resolve(Object object, EntityPathGroup entityPathGroup) {
        if (object == null) {
            throw new IllegalArgumentException(
                    "Object is null, object must be initially instantiated.");
        }
        if ((entityPathGroup == null) || (entityPathGroup.isEmpty())) {
            log.warn("No Paths to be Resolved have been specified, ignoring.");
            return;
        }
        // ******************************
        // Touch the Path.
        for (EntityPath entityPath : entityPathGroup.getEntityPathArray()) {
            resolve(object, entityPath.getEntityPath());
        } // End of Path For Each Loop.
    }

    /**
     * Resolve the Object's DATA while still within session with the underlying
     * ORM. This will in essence allow for specific path hydration provided by
     * an upstream layer knowledgeable of the object model and the required and
     * necessary DATA needed to be presented.
     *
     * @param object             Object whose paths should be resolved.
     * @param optionalParameters Variable arguments passed from upstream layer.
     */
    public static void resolve(Object object, Object... optionalParameters) {
        if (object == null) {
            throw new IllegalArgumentException(
                    "Object is null, object must be initially instantiated.");
        }
        if ((optionalParameters == null) || (optionalParameters.length <= 0)) {
            log.warn("No Paths to be Resolved have been specified, ignoring.");
            return;
        }
        /**
         * Iterate Over all of the Entity Paths within Parameters.
         */
        for (Object parameter : optionalParameters) {
            if ((parameter.getClass().isArray()) && (EntityPathGroup.class.isInstance(parameter))) {
                resolve(object, ((EntityPathGroup[]) parameter));
            } else if ( ((parameter.getClass().isArray()) && (EntityPath.class.isInstance(parameter))) ||
                         (parameter.getClass().getName().equalsIgnoreCase("[L"+EntityPath.class.getName()+";")))
            {
                resolve(object, ((EntityPath[]) parameter));
            } else if ((parameter.getClass().isArray()) && (String.class.isInstance(parameter))) {
                resolve(object, ((String[]) parameter));
            } else if (EntityPathGroup.class.isInstance(parameter)) {
                resolve(object, ((EntityPathGroup) parameter));
            } else if (EntityPath.class.isInstance(parameter)) {
                resolve(object, ((EntityPath) parameter));
            } else if (String.class.isInstance(parameter)) {
                resolve(object, ((String) parameter));
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Not Processing this parameter class:[" + parameter.getClass().getName() + "] for Object:["+object.getClass().getName()+"]");
                }
            }
        } // End of Path For Each Loop.
    }

    /**
     * Private Helper Method which Performs the first level touch against an Objects
     * Field/Properties using the XPath of the Entity Value.
     *
     * @param object          Object whose paths should be resolved.
     * @param entityPathValue Path to be touched with Object Graph.
     */
    private static void touchEntityPath(Object object, final String entityPathValue) {
        // *********************************************************
        // Determine if this entityPath is a full Path?
        if (!entityPathValue.contains(".")) {
            accessEntityPath(object, entityPathValue);
            return;
        }
        // *********************************************************
        // Process nested Path Element
        String[] pathNodes = entityPathValue.split("\\.");
        Object nodeObject = object; // Set Reference.
        for (int nodeObjectIndex = 0; nodeObjectIndex < pathNodes.length; nodeObjectIndex++) {
            nodeObject = accessEntityPath(nodeObject, pathNodes[nodeObjectIndex]);
            if (nodeObject == null) {
                return;
            }
            if (log.isDebugEnabled())
                { log.debug("Nested Object Class:[" + nodeObject.getClass().getName() + "]"); }
            // ***********************************
            // Check for Collection, Map or Array
            if (((nodeObject instanceof java.util.Collection) ||
                    (nodeObject instanceof java.util.Map) ||
                    (nodeObject.getClass().isArray())) &&
                    (pathNodes.length > nodeObjectIndex + 1)) {
                // Pop off this name, since we do not check
                // content type, just if that type has a
                // method to obtain the next DATA Element.
                nodeObjectIndex++;
                StringBuffer embeddedPathValue = new StringBuffer();
                for (nodeObjectIndex++; nodeObjectIndex < pathNodes.length; nodeObjectIndex++) {
                    if (embeddedPathValue.length() > 0) {
                        embeddedPathValue.append(".");
                    }
                    embeddedPathValue.append(pathNodes[nodeObjectIndex]);
                }
                // *******************************************
                // Check to ensure we have a Path remaining?
                // If not, we are done.
                if (embeddedPathValue.length() == 0) {
                    break;
                }
                if (log.isDebugEnabled()) {
                    log.debug("New Embedded Path Value:[" + embeddedPathValue.toString() + "]");
                }
                // *******************************************
                // Now Force the Recursion Against the
                // objects contained with the collection.
                if (nodeObject instanceof java.util.Collection) {
                    // Cast
                    java.util.Collection collection = (java.util.Collection) nodeObject;
                    if (!collection.isEmpty()) {
                        for (Object element : collection.toArray()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Collection Element Value:[" + element.getClass().getName() + "]");
                            }
                            touchEntityPath(element, embeddedPathValue.toString());
                        }
                    }

                    // *******************************************
                    // Now Force the Recursion Against the
                    // objects contained with the collection.
                } else if (nodeObject instanceof java.util.Map) {
                    // Cast
                    java.util.Map map = (java.util.Map) nodeObject;
                    if (!map.isEmpty()) {
                        for (Object element : map.values()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Map Element Value:[" + element.getClass().getName() + "]");
                            }
                            touchEntityPath(element, embeddedPathValue.toString());
                        }
                    }

                }
                // *******************************************
                // Ok, now break out of this Loop as we
                // should have exhausted our Path.
                break;
            }

        } // End of For Each Recursion for nested Path.

    }

    /**
     * Private Helper Method which Performs the Access against an Objects
     * Field/Properties using the XPath of the Entity Value.
     *
     * @param object Object whose paths should be resolved.
     * @param path   Path to be touched with Object Graph.
     * @return Object resolved from embedded method invocation, if Method Not Found return can be Null.
     */
    private static Object accessEntityPath(Object object, final String path) {

        // *********************************************************
        // Use JXPath to Access the Path within the Object.
        try {
            JXPathContext context = JXPathContext.newContext(object);
            Pointer referencePointer = context.getPointer(path);
            if ((referencePointer == null) ||
                    (referencePointer.getValue() == null)) {
                return null;
            }
            Class<?> valueClass = referencePointer.getValue().getClass();
            String referencePath = referencePointer.asPath();
            if (log.isDebugEnabled()) {
                log.debug("Base Class:[" + object.getClass().getSimpleName() + "], Value Class:[" + valueClass.getName() + "], Path:["
                        + referencePath + "]");
            }

            // *********************************************
            // Obtain the method Name.
            String methodPrefix;
            if (valueClass.getSimpleName().equalsIgnoreCase(
                    Boolean.class.getSimpleName())) {
                methodPrefix = "is";
            } else {
                methodPrefix = "get";
            }
            String methodName = methodPrefix
                    + path.substring(0, 1)
                    .toUpperCase()
                    + path.substring(1);

            // *********************************************
            // Obtain the method for this path/field name.
            Method method = getMethod(object.getClass(), methodName);
            if (method != null) {
                // ***************************
                // Invoke the Method
                return methodInvoker(object, method);
            }
        } catch (org.apache.commons.jxpath.JXPathNotFoundException e) {
            if (object != null) {
                log.warn("Specified Path Not Found:["
                    + path + "], for Object:["+object.getClass().getName()+"], Ignoring, check your FetchGroup or String Bean Syntax.");
            } else {
                log.warn("Object is null");
            }
        } catch (Exception e) {
            log.warn("Issue Encountered for Accessing Path:["
                    + path + "], for Object:["+object.getClass().getName()+"], Issue:["+e.getMessage()+"], Ignoring.");
        }
        // *********************
        // Nothing to return.
        return null;
    }

    /**
     * Private Helper method to invoke a read-only method
     * to trigger the underlying ORM Framework to
     * load that DATA element.
     *
     * @param object Object whose paths should be resolved.
     * @param method Method to be invoked.
     * @return Returned Object from invoked method.
     */
    private static Object methodInvoker(Object object, Method method) {
        Object resultObject = null;
        try {
            resultObject = method.invoke(object);
            if (resultObject != null) {
                Hibernate.initialize(resultObject);
                // *************************************
                // Does this Object have an IsEmpty
                // method?  If so, invoke it.
                try {
                    Method isEmptyMethod = resultObject.getClass().getMethod("isEmpty");
                    if (isEmptyMethod != null) {
                        isEmptyMethod.invoke(resultObject);
                    }
                } catch (NoSuchMethodException nsme) {
                    // **************************
                    // No IsEmpty method,
                    // so ignore, we have gone
                    // as far enough here.
                }
            }
            return resultObject;
        } catch (IllegalArgumentException e) {
            log.debug(e);
        } catch (IllegalAccessException e) {
            log.debug(e);
        } catch (InvocationTargetException e) {
            log.debug(e);
        } catch (NullPointerException npe) {
            // *****************************
            // An Null Pointer Exception or
            // NPE here is OK, this just
            // the getter value was null.
            // So we simply Ignore the Benign
            // NPE.  NPE's are not always
            // Bad :)
            // *****************************
        }
        return resultObject;
    }

    /**
     * Private Helper Method to get a Method on a specified Class.
     *
     * @param clazz      Class whose Method is to be found by Name.
     * @param methodName Name used to find Method.
     * @return Method
     */
    private static Method getMethod(Class<?> clazz, String methodName) {
        try {
            return clazz.getMethod(methodName);
        } catch (SecurityException e) {
            log.error("Security Exception Accessing " + clazz.getName() + "."
                    + methodName + "!");
            return null;
        } catch (NoSuchMethodException e) {
            if (log.isDebugEnabled()) {
                log.debug("No Such Method Found for " + clazz.getName() + "."
                        + methodName + "!");
            }
            return null;
        }
    }

}
