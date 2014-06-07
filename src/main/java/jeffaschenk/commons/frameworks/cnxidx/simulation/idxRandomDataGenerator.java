package jeffaschenk.commons.frameworks.cnxidx.simulation;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxTimeStamp;

import java.util.*;
import java.io.*;

/**
 * Java class to perform Random Data Generation for Simulation
 * Tests.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2003
 */

public class idxRandomDataGenerator {
    private idxTimeStamp TIMESTAMP = null;
    private Random RANDOM;

    private static String CHARSET = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

    private boolean isWORDLISTAvailable = false;
    private LinkedList<String> WORDLIST = null;

    private static int DEFAULT_MAXPHRASES = 16;                   // 16 Phrases.  
    private static int DEFAULT_MAXPHRASESIZE = 1024 * 64;          // 64KB Phrases.
    private static int DEFAULT_MAX_RSIZE = ((1024 * 16) * 1024);     // 16MB, Maximum String Size.

    private int MAXPHRASES = DEFAULT_MAXPHRASES;
    private int MAXPHRASESIZE = DEFAULT_MAXPHRASESIZE;
    private boolean isPHRASELISTAvailable = false;
    private LinkedList<String> PHRASELIST = null;
    private boolean isSEARCHWORDSAvailable = false;
    private LinkedList<String> SearchWords = null;

    private boolean VERBOSE = false;
    private int MAX_RSIZE = DEFAULT_MAX_RSIZE;

    // ***********************************************
    // Last Unique ID Generation.
    private String LAST_KEY_TIMESTAMP = "";
    private int LAST_KEY_INCREMENT = 0;

    // ***********************************************
    // LAP Timers
    idxLapTime LP_RANDOM_STRING_GENERATION = new idxLapTime();
    idxLapTime LP_RANDOM_WORD_STRING_GENERATION = new idxLapTime();
    idxLapTime LP_RANDOM_XML_DOCUMENT_GENERATION = new idxLapTime();
    idxLapTime LP_RANDOM_PHRASE_STRING_GENERATION = new idxLapTime();

    // ***********************************************
    // Defined Arrays for Random Specific Data.
    private LinkedList<String> RESOURCE_VENDORS = null;
    private LinkedList<String> RESOURCE_TYPES = null;
    private LinkedList<String> RESOURCE_MODELS = null;
    private LinkedList<String> RESOURCE_ACTUAL_MODELS = null;
    private LinkedList<String> RESOURCE_OS_VERSIONS = null;

    /**
     * Initial constructor with no Parameters.
     */
    public idxRandomDataGenerator() {
        TIMESTAMP = new idxTimeStamp();
        TIMESTAMP.enableLocalTime();
        RANDOM = new Random(obtainTimeAsLong());
    } // End of Constructor.

    /**
     * Verbose setting.
     */
    public void setVerbose(boolean _setting) {
        VERBOSE = _setting;
    } // End of setVerbose.

    /**
     * Set Maximum Phrases.
     */
    public void setMaxPhrases(int _number) {
        if (_number > DEFAULT_MAXPHRASES) {
            MAXPHRASES = DEFAULT_MAXPHRASES;
        } else {
            MAXPHRASES = _number;
        }
    } // End of setMaxPhrases.

    /**
     * Set Maximum Phrase Segment Size.
     */
    public void setMaxPhraseSize(int _number) {
        if (_number > DEFAULT_MAXPHRASESIZE) {
            MAXPHRASESIZE = DEFAULT_MAXPHRASESIZE;
        } else {
            MAXPHRASESIZE = _number;
        }
    } // End of setMaxPhraseSize.

    /**
     * Set Maximum String Size.
     */
    public void setMaxStringSize(int _number) {
        if (_number > DEFAULT_MAX_RSIZE) {
            MAX_RSIZE = DEFAULT_MAX_RSIZE;
        } else {
            MAX_RSIZE = _number;
        }
    } // End of setMaxStringSize.

    /**
     * Method to build the Dictionary file for random word and phrase
     * generation.
     */
    public void buildWordDictionary()
            throws IOException {
        // ************************************
        // Build the Word Dictionary.
        buildWordDictionary(System.getProperty("simulator.word.dictionary"));
    } // End of buildWordDictionary Method.

    /**
     * Method to build the Dictionary file for random word and phrase
     * generation.
     *
     * @param _filename String of Word FileName.
     */
    public void buildWordDictionary(String _filename)
            throws IOException {
        if ((_filename == null) ||
                (_filename.equalsIgnoreCase(""))) {
            return;
        }

        // **********************************
        // Opend the Buffered Reader.
        BufferedReader wordin = new BufferedReader(
                new FileReader(_filename), 16384);

        // **********************************
        // Build our Word Dictionary.
        buildWordDictionary(wordin);
    } // End of buildWordDictionary Method. 

    /**
     * Method to build the Dictionary file for random word and phrase
     * generation.
     *
     * @param _in BufferedReader of Word File.
     */
    private void buildWordDictionary(BufferedReader _in)
            throws IOException {

        // *****************************************
        // Initialize Word List.
        WORDLIST = new LinkedList<>();
        PHRASELIST = new LinkedList<>();

        // *****************************************
        // Loop through the Word File Line by Line
        String str = null;
        while ((str = _in.readLine()) != null) {

            // *******************************
            // Parse the Schema
            str = str.trim();

            // *******************************
            // Is this a Null or Blank Line?
            // Ignore it...
            if ((str == null) || (str.equals(""))) {
                continue;
            }

            // *******************************
            // Parse the Detail.
            int b = 0;
            String LWORD = null;
            String LWORD2 = null;
            String LPhrase = null;
            StringTokenizer st = new StringTokenizer(str);
            while (st.hasMoreTokens()) {
                switch (b) {
                    case 0:
                        LWORD = st.nextToken();
                        b++;
                        break;

                    case 1:
                        LWORD2 = st.nextToken();
                        b++;
                        break;

                    default:
                        if (LPhrase == null) {
                            LPhrase = st.nextToken();
                        } else {
                            LPhrase = LPhrase + " " + st.nextToken();
                        }
                        b++;
                        break;
                } // End of Switch.
            } // End of Tokenizer While.

            // ****************************************
            // Add the Word to the List
            WORDLIST.addLast(LWORD);

        } // End of While

        // *******************************
        // Indicate Word List is Available.
        isWORDLISTAvailable = true;

        // *******************
        // Done Parsing.
        _in.close();

        // *****************************
        // Now Build Large Phrase Lists.
        for (int i = 0; i < MAXPHRASES; i++) {
            // ****************************************
            // Add Phrase.
            PHRASELIST.addLast(obtainRandomWordString(MAXPHRASESIZE));

        } // End of For Loop.

        // *******************************
        // Indicate Phrase List is Available.
        isPHRASELISTAvailable = true;

        // ******************
        // Return.
        return;

    } // End of buildWordDictionary Method.

    /**
     * Method to build a LinkedList from a String.
     *
     * @param str String of Comma Seperated Data.
     */
    private LinkedList<String> buildListFromString(String str) {

        // *****************************************
        // Initialize List.
        LinkedList<String> _LIST = new LinkedList<>();

        // *******************************
        // Is this a Null or Blank Line?
        // Ignore it...
        if ((str == null) || (str.equals(""))) {
            return (_LIST);
        }

        // *******************************
        // Parse the String Data.
        StringTokenizer st = new StringTokenizer(str, ",:;\n\r");
        while (st.hasMoreTokens()) {
            String LWORD = st.nextToken();
            _LIST.addLast(LWORD);
        } // End of Tokenizer While.

        // ******************
        // Return.
        return (_LIST);

    } // End of buildListFromString Method.

    /**
     * Method to build a small random set of search words.
     */
    public void buildSearchWordList(String _SLIST) {
        SearchWords = new LinkedList<>(buildListFromString(_SLIST));
        // *******************************
        // Indicate List is Available.
        isSEARCHWORDSAvailable = true;

        // *******************************
        // Return.
        return;
    } // End of buildSearchWordList Method.

    /**
     * Method to build a Vendor List.
     */
    public void buildVendorList(String _SLIST) {
        RESOURCE_VENDORS = new LinkedList<>(buildListFromString(_SLIST));
        return;
    } // End of buildVendorList Method.

    /**
     * Method to build a Type List.
     */
    public void buildTypeList(String _SLIST) {
        RESOURCE_TYPES = new LinkedList<>(buildListFromString(_SLIST));
        return;
    } // End of buildTypeList Method.

    /**
     * Method to build a Model List.
     */
    public void buildModelList(String _SLIST) {
        RESOURCE_MODELS = new LinkedList<>(buildListFromString(_SLIST));
        return;
    } // End of buildModelList Method.

    /**
     * Method to build a Actual Model List.
     */
    public void buildActualModelList(String _SLIST) {
        RESOURCE_ACTUAL_MODELS = new LinkedList<>(buildListFromString(_SLIST));
        return;
    } // End of buildActualModelList Method.

    /**
     * Method to build a OS Versions List.
     */
    public void buildOSVersionList(String _SLIST) {
        RESOURCE_OS_VERSIONS = new LinkedList<>(buildListFromString(_SLIST));
        return;
    } // End of buildOSVersionList Method.

    /**
     * Method to Obtain the Current Time as a Long.
     */
    private long obtainTimeAsLong() {
        long _LVALUE = 0;
        try {
            _LVALUE = Long.parseLong(TIMESTAMP.get().substring(0, 14));
        } catch (Exception e) {
            ;
        } // End of Exception.

        // ********************
        // Return
        return (_LVALUE);
    } // End of obtainTimeAsLong Method.

    /**
     * Method to obtain a Random Vendor.
     */
    public String obtainRandomVendor() {
        if (RESOURCE_VENDORS.size() == 0) {
            return (obtainRandomWord());
        }
        int _vendor = RANDOM.nextInt(RESOURCE_VENDORS.size() - 1);
        return ((String) RESOURCE_VENDORS.get(_vendor));
    } // End of obtainRandomVendor Method.

    /**
     * Method to obtain a Random Resource Type.
     */
    public String obtainRandomType() {
        if (RESOURCE_TYPES.size() == 0) {
            return (obtainRandomWord());
        }
        int _type = RANDOM.nextInt(RESOURCE_TYPES.size() - 1);
        return ((String) RESOURCE_TYPES.get(_type));
    } // End of obtainRandomType Method.

    /**
     * Method to obtain a Random Resource Model.
     */
    public String obtainRandomModel() {
        if (RESOURCE_MODELS.size() == 0) {
            return (obtainRandomWord());
        }
        int _type = RANDOM.nextInt(RESOURCE_MODELS.size() - 1);
        return ((String) RESOURCE_MODELS.get(_type));
    } // End of obtainRandomModel Method.

    /**
     * Method to obtain a Random Resource Actual Model.
     */
    public String obtainRandomActualModel() {
        if (RESOURCE_ACTUAL_MODELS.size() == 0) {
            return (obtainRandomWord());
        }
        int _type = RANDOM.nextInt(RESOURCE_ACTUAL_MODELS.size() - 1);
        return ((String) RESOURCE_ACTUAL_MODELS.get(_type));
    } // End of obtainRandomActualModel Method.

    /**
     * Method to obtain a Random Resource OS Versions.
     */
    public String obtainRandomOSVersion() {
        if (RESOURCE_OS_VERSIONS.size() == 0) {
            return (obtainRandomWord());
        }
        int _type = RANDOM.nextInt(RESOURCE_OS_VERSIONS.size() - 1);
        return ((String) RESOURCE_OS_VERSIONS.get(_type));
    } // End of obtainRandomOSVersion Method.

    /**
     * Method to obtain a Random Key.
     */
    public String obtainRandomKey() {
        String _KEY = TIMESTAMP.get().substring(0, 14);
        int KeySuffix = RANDOM.nextInt(9999);
        String Suffix = Integer.toString(KeySuffix);
        while (Suffix.length() < 4) {
            Suffix = "0" + Suffix;
        }
        return (_KEY + "-" + Suffix);
    } // End of obtainRandomKey Method.

    /**
     * Method to obtain a Random Key.
     */
    public String obtainUniqueKey() {
        String _KEY = TIMESTAMP.get().substring(0, 14);
        if (_KEY.equalsIgnoreCase(LAST_KEY_TIMESTAMP)) {
            LAST_KEY_INCREMENT++;
        } else {
            LAST_KEY_INCREMENT = 1;
        }
        LAST_KEY_TIMESTAMP = _KEY;
        String Suffix = Integer.toString(LAST_KEY_INCREMENT);
        while (Suffix.length() < 4) {
            Suffix = "0" + Suffix;
        }
        return (_KEY + "-" + Suffix);
    } // End of obtainUniqueKey Method.

    /**
     * Method to obtain a Random Long.
     */
    public String obtainRandomLong() {
        return (Long.toString(RANDOM.nextLong()));
    } // End of obtainRandomLong Method.

    /**
     * Method to obtain a Random IP V4 Address.
     */
    public String obtainRandomIPV4Address() {
        String _IPADDR = "";
        for (int i = 0; i <= 3; i++) {
            int Octet = RANDOM.nextInt(255);
            if (i == 0) {
                _IPADDR = Integer.toString(Octet);
            } else {
                _IPADDR = _IPADDR + "." + Integer.toString(Octet);
            }
        } // End of For Loop.
        return (_IPADDR);
    } // End of obtainRandomIPV4Address Method.

    /**
     * Method to obtain a Random Character String.
     */
    public String obtainRandomString(int size) {
        LP_RANDOM_STRING_GENERATION.Start();
        if (size > MAX_RSIZE) {
            size = MAX_RSIZE;
        }
        StringBuffer rsbuf = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            int character = RANDOM.nextInt(CHARSET.length() - 1);
            rsbuf.append(CHARSET.substring(character, 1));
        } // End of For Loop.

        LP_RANDOM_WORD_STRING_GENERATION.Stop();

        // *******************************
        // Return random Character String
        return (rsbuf.substring(0, size));
    } // End of obtainRandomString Method.

    /**
     * Method to obtain a Random Word.
     */
    public String obtainRandomWord() {
        if (!isWORDLISTAvailable) {
            return (obtainRandomString(8));
        }
        int word = RANDOM.nextInt(WORDLIST.size() - 1);
        return ((String) WORDLIST.get(word));
    } // End of obtainRandomWord Method.

    /**
     * Method to obtain a Random Search Word.
     */
    public String obtainRandomSearchWord() {
        if ((!isSEARCHWORDSAvailable) ||
                (SearchWords.size() == 0)) {
            return (obtainRandomWord());
        }
        int word = RANDOM.nextInt(SearchWords.size() - 1);
        return ((String) SearchWords.get(word));
    } // End of obtainRandomSearchWord Method.

    /**
     * Method to obtain a Random Word String
     */
    public String obtainRandomWordString(int size) {
        if (!isWORDLISTAvailable) {
            return (obtainRandomString(size));
        }

        LP_RANDOM_WORD_STRING_GENERATION.Start();
        if (size > MAX_RSIZE) {
            size = MAX_RSIZE;
        }
        StringBuffer rsbuf = new StringBuffer(size);
        while (rsbuf.length() < size) {
            int word = RANDOM.nextInt(WORDLIST.size() - 1);
            String Zword = (String) WORDLIST.get(word);

            int ToGo = (size - rsbuf.length());
            if (ToGo >= Zword.length()) {
                rsbuf.append(Zword);
            } else {
                rsbuf.append(Zword.substring(0, ToGo));
            }

            if (rsbuf.length() < size) {
                rsbuf.append(" ");
            }
        } // End of While Loop.

        LP_RANDOM_WORD_STRING_GENERATION.Stop();

        // *******************************
        // Return Word String.
        return (rsbuf.substring(0, size));
    } // End of obtainRandomString Method.

    /**
     * Method to obtain a XML Random Word String
     */
    public String obtainSimulatedXMLDocument(int size) {

        LP_RANDOM_XML_DOCUMENT_GENERATION.Start();
        if (size > MAX_RSIZE) {
            size = MAX_RSIZE;
        }
        StringBuffer rsbuf = new StringBuffer(size + 100);
        rsbuf.append("<?xml version=\0421.0\042 encoding=\042UTF-8\042?>");
        rsbuf.append("<SIMULATED><![CDATA[");

        if (!isPHRASELISTAvailable) {
            rsbuf.append(obtainRandomString(size));
        }

        // ******************************
        // Generate
        if (isPHRASELISTAvailable) {
            while (rsbuf.length() < size) {
                int word = RANDOM.nextInt(PHRASELIST.size() - 1);
                String Zword = (String) PHRASELIST.get(word);
                int ToGo = (size - rsbuf.length());
                if (ToGo >= Zword.length()) {
                    rsbuf.append(Zword);
                } else {
                    rsbuf.append(Zword.substring(0, ToGo));
                }

                if (rsbuf.length() < size) {
                    rsbuf.append(" ");
                }

            } // End of While Loop.
        } // End of If.

        // *****************************
        // Append ending XML
        rsbuf.append("]]></SIMULATED>");

        LP_RANDOM_XML_DOCUMENT_GENERATION.Stop();

        // *******************************
        // Return Word String.
        return (rsbuf.toString());
    } // End of obtainSimulatedXMLDocument Method.

    /**
     * Method to obtain a Random Word Phrase String
     */
    public String obtainRandomPhrase(int size) {
        if (!isPHRASELISTAvailable) {
            return (obtainRandomString(size));
        }

        LP_RANDOM_PHRASE_STRING_GENERATION.Start();
        if (size > MAX_RSIZE) {
            size = MAX_RSIZE;
        }
        StringBuffer rsbuf = new StringBuffer(size);
        while (rsbuf.length() < size) {
            int word = RANDOM.nextInt(PHRASELIST.size());
            String Zword = (String) PHRASELIST.get(word);
            int ToGo = (size - rsbuf.length());
            if (ToGo >= Zword.length()) {
                rsbuf.append(Zword);
            } else {
                rsbuf.append(Zword.substring(0, ToGo));
            }

        } // End of While Loop.

        LP_RANDOM_PHRASE_STRING_GENERATION.Stop();

        // *******************************
        // Return Word String.
        return (rsbuf.substring(0, size));
    } // End of obtainRandomPhrase Method.

    /**
     * Dump Lap Timers.
     */
    public void dumpLapTimers() {

        // ***************************************
        // Show the Lap Timings.
        System.out.println(" Generation of Random Strings: ");
        System.out.println("\t" + LP_RANDOM_STRING_GENERATION);

        System.out.println(" Generation of Random Word Strings: ");
        System.out.println("\t" + LP_RANDOM_WORD_STRING_GENERATION);

        System.out.println(" Generation of Random Phrase Strings: ");
        System.out.println("\t" + LP_RANDOM_PHRASE_STRING_GENERATION);

        System.out.println(" Generation of Random XML Documents: ");
        System.out.println("\t" + LP_RANDOM_XML_DOCUMENT_GENERATION);

    } // End of dumpLapTimers Method.

    /**
     * Show Random Dictionary Statistics and Sizes.
     */
    public void showSizes() {

        // ***************************************
        // Show Dictionary Sizes.
        if (isWORDLISTAvailable) {
            System.out.println("** Number of Words in Dictionary: " +
                    WORDLIST.size());
        } else {
            System.out.println("** Word Dictionary Not Available.");
        } // End of Else.

        if (isPHRASELISTAvailable) {
            System.out.println("** Number of Phrases in Dictionary: " +
                    PHRASELIST.size() + ", each Phrase Segment size: " + MAXPHRASESIZE + " Bytes.");
        } else {
            System.out.println("** Pharse Dictionary Not Available.");
        } // End of Else.

        if (isSEARCHWORDSAvailable) {
            System.out.println("** Number of Common Search Words Available: " +
                    SearchWords.size());
        } else {
            System.out.println("** Common Search Words Not Available.");
        } // End of Else.

        // ****************************
        // Show Additional Sizes.
        System.out.println("** Number of Resource Vendors Available: " +
                RESOURCE_VENDORS.size());

        System.out.println("** Number of Resource Types Available: " +
                RESOURCE_TYPES.size());

        System.out.println("** Number of Resource Models Available: " +
                RESOURCE_MODELS.size());

        System.out.println("** Number of Resource Actual Models Available: " +
                RESOURCE_ACTUAL_MODELS.size());

        System.out.println("** Number of Resource OS Versions Available: " +
                RESOURCE_OS_VERSIONS.size());

        System.out.println("** Maximum Size of Largest String which can be formulated: " +
                MAX_RSIZE + " Bytes.");

    } // End of showSizes Method.

} ///:~ End of idxRandomDataGenerator Class
