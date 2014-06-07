package jeffaschenk.commons.frameworks.cnxidx.utility.commandlinearguments;

import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLogger;
import jeffaschenk.commons.frameworks.cnxidx.utility.logging.FrameworkLoggerLevel;

import java.util.*;

/**
 * Java Class to provide a standard parser interface for incoming arguments
 * to a utility with a Main class.
 * This class will maintain a TreeMap of all arguments and values for the
 * Parsed incoming Argument list.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxArgParser {
    // ***********************************************
    // Logging Facilities.
    public static final String CLASSNAME = idxArgParser.class.getName();

    // ***********************************************
    // Globals
    private Map<String,Object> INA = null;
    private List<String> UNNAMED = null;
    private String INAPREFIX = "--";
    private int MAX_INAPREFIX_LENGTH = 3;

    /**
     * Initial Constructor used when no argument supplied.
     */
    public idxArgParser() {
        INA = new TreeMap<>();
        UNNAMED = new LinkedList<>();
    } // end of Constructor

    /**
     * Constructor used when a String Array argument supplied.
     *
     * @param _args Argument Array.
     */
    public idxArgParser(String[] _args) {
        INA = new TreeMap<>();
        UNNAMED = new LinkedList<>();
        parse(_args);
    } // end of Constructor.

    /**
     * Method to Set Prefix.
     *
     * @param _prefix to set Argument Prefix, default "--".
     * @return boolean indicator if set successful.
     */
    public boolean setPrefix(String _prefix) {
        if ((_prefix == null) ||
                (_prefix.length() > MAX_INAPREFIX_LENGTH)) {
            return (false);
        }

        // ************************
        // Set the new Prefix.
        INAPREFIX = _prefix;
        return (true);
    } // End of Method.

    /**
     * Method to Set Standard UNIX Prefix to "--".
     *
     * @return boolean indicator if set successful.
     */
    public boolean setPrefix() {
        return (setPrefix("--"));
    } // End of Method.

    /**
     * Method to Get Prefix.
     *
     * @return String of Current Argument Prefix.
     */
    public String getPrefix() {
        return (INAPREFIX);
    } // end of Method

    /**
     * Method to Build the initial Argument TreeMap.
     *
     * @param _args Argument String Array.
     */
    public void parse(String[] _args) {
        String METHODNAME = "parse";
        int maxelements = _args.length;
        for (int index = 0; index < maxelements; index++) {
            // *******************************************
            // Remove any single quoted values as we
            // interrogate the entry.
            //
            if ((_args[index].startsWith("\'")) &&
                    (_args[index].endsWith("\'"))) {
                int alen = _args[index].length();
                _args[index] = _args[index].substring(1, alen - 1);
            }

            // ***********************
            // Debugging.
            FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                    MessageConstants.ARGPARSER_Argument_ToBeParsed,
                    new String[]{Integer.toString(index), _args[index]});

            // *******************************************
            // Now determine if we have a Parameter Name
            // or an actual value.
            // indicated by our prefix.
            //
            if ((_args[index].length() > INAPREFIX.length()) &&
                    (INAPREFIX.equals(_args[index].substring(0, INAPREFIX.length())))) { // Parameter Name Found.
                String Name = (_args[index].substring(INAPREFIX.length())).toLowerCase().trim();
                INA.put(Name, Boolean.TRUE);

                FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                        MessageConstants.ARGPARSER_Argument_Name_Found, new String[]{Name});

            } else { // Possible Parameter Value Found.
                // Determine if the Value was properly Tagged, so we can give a name to the value.
                if (((index - 1) >= 0) &&
                        (_args[index - 1].length() > INAPREFIX.length()) &&
                        (INAPREFIX.equals(_args[index - 1].substring(0, INAPREFIX.length())))) {
                    String Name = (_args[index - 1].substring(INAPREFIX.length())).toLowerCase().trim();
                    INA.remove(Name);
                    INA.put(Name, _args[index]);

                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.DEBUG,
                            MessageConstants.ARGPARSER_Argument_Name_Value_Found,
                            new String[]{Name, _args[index]});

                    // The Value was not properly Tagged, so place it in our lost and found bucket.
                } else {
                    UNNAMED.add(_args[index]);
                    FrameworkLogger.log(CLASSNAME, METHODNAME, FrameworkLoggerLevel.WARNING,
                            MessageConstants.ARGPARSER_Unknown_Associated_Value,
                            new String[]{_args[index]});
                } // End of Inner Else.
            } // End of Outer Else.
        } // End of For Loop.
        return;
    } // end of parse Method

    /**
     * Method used to get Object Content Value
     *
     * @param _Name Name of Object.
     * @return Object as Named by Name Parameter.
     */
    public Object getValue(String _Name) {
        _Name = _Name.toLowerCase();
        Object Value = INA.get(_Name);
        return (Value);
    } // End of Exception

    /**
     * Method used to determine if Object Exists
     *
     * @param _Name Name of Object.
     * @return boolean indicator whether Object Exists or not.
     */
    public boolean doesNameExist(String _Name) {
        _Name = _Name.toLowerCase();
        Object Value = INA.get(_Name);
        if (Value == null) {
            return (false);
        }
        return (true);
    } // End of Exception

    /**
     * Method used to Add new Object into Container
     *
     * @param _Name  Name of Object.
     * @param _Value to be associated with Name.
     */
    public void put(String _Name, Object _Value) {
        INA.put(_Name.toLowerCase(), _Value);
        return;
    } // end of Method

    /**
     * Method used to return an Empty Indicator.
     *
     * @return boolean indicates whether TreeMap is Empty or not.
     */
    public boolean isEmpty() {
        return (INA.isEmpty());
    } // End of Method.

    /**
     * Method used to return the Number of Entries
     * in the Map.
     *
     * @return int number of entries in TreeMap.
     */
    public int getSize() {
        return (INA.size());
    } // End of Method.

    /**
     * Method used to remove an Object
     *
     * @param _Name Name of Object to be Removed.
     */
    public void removeObject(String _Name) {
        _Name = _Name.toLowerCase();
        INA.remove(_Name);
        return;
    } // End of Method.

    /**
     * Method used to remove All Objects
     */
    public void removeAllObjects() {
        INA.clear();
        return;
    } // End of Method.

    /**
     * Method to Display All Parsed Entries to STDOUT.
     */
    public void show() {
        Set mySet = INA.entrySet();
        Iterator itr = mySet.iterator();
        while (itr.hasNext()) {
            java.util.Map.Entry oit = (java.util.Map.Entry) itr.next();
            String Name = (String) oit.getKey();
            String Value = null;
            if (this.getValue(Name) instanceof Boolean) {
                Value = ((Boolean) this.getValue(Name)).toString();
            } else if (this.getValue(Name) instanceof String) {
                Value = (String) this.getValue(Name);
            }
            // ********************************
            // Show Message.
            FrameworkLogger.log(CLASSNAME, "show", FrameworkLoggerLevel.INFO,
                    MessageConstants.ARGPARSER_SHOW_PARSED_NAMED_ENTRY,
                    new String[]{Name, Value});
        } // End of While.
    } // end of Method

    // *************************************************
    // UNNAMED List Methods.
    // *************************************************

    /**
     * Method which return whether or not UnNamed Argument
     * TreeMap is Empty or not.
     *
     * @return boolean Indicator whether UnNamed TreeMap is Empty or not.
     */
    public boolean IsUnNamedEmpty() {
        if (UNNAMED.size() == 0) {
            return (true);
        }
        return (false);
    } // end of Method

    /**
     * Method to clean the UnNamed TreeMap.
     */
    public void clearUnNamed() {
        UNNAMED.clear();
    } // end of Method

    /**
     * Method to indicate the number of entries in the UnNamed TreeMap.
     *
     * @return int Number of Entries contained in UnNamed Argument TreeMap.
     */
    public int sizeUnNamed() {
        return (UNNAMED.size());
    } // end of Method

    /**
     * Method used to get String Value of an UnNamed Argument.
     *
     * @param _index Index of String Value contained in Array.
     * @return String associated with incoming Index.
     */
    public String get(int _index) {
        if (_index < UNNAMED.size() - 1) {
            Object unnamedentry = UNNAMED.get(_index);
            return ((String) unnamedentry);
        } else {
            return ("");
        }
    } // end of Method

    /**
     * Method used to Displays All Entries in List to STDOUT.
     */
    public void showUnNamed() {
        Iterator itr = UNNAMED.iterator();
        while (itr.hasNext()) {
            Object unnamedentry = itr.next();
            FrameworkLogger.log(CLASSNAME, "showUnNamed", FrameworkLoggerLevel.DEBUG,
                    MessageConstants.ARGPARSER_SHOW_PARSED_UNNAMED_ENTRY,
                    new String[]{(String) unnamedentry});
        } // End of While.
    } // end of Method

} ///: End of idxArgParser Class.
