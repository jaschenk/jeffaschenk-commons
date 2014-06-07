package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * Java Class to provide a Linked List facility for a pool of DN's
 * or Distingushed Names.  This class will be used from other
 * IRR utility functions, such as copyentry, deleteentry and moveentry.
 * Each of these functions will need to keep track of entry children.
 * The mthods contained will facilitate the manipulation of the DN
 * linked list.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxDNLinkList {
    private LinkedList<String> dnList = new LinkedList<>();

    /**
     * Initial Constructor used when no argument supplied.
     */
    public idxDNLinkList() {
    } // end of Constructor

    /**
     * Initial Constructor used when single object argument supplied.
     * Clones Object.
     *
     * @param dnllob Existing DN Link List Object to be cloned.
     */
    public idxDNLinkList(idxDNLinkList dnllob) {
        Iterator dnentries_itr = dnllob.dnList.iterator();
        while (dnentries_itr.hasNext()) {
            Object dno = dnentries_itr.next();
            add((String) dno);
        }
    } // end of Constructor

    /**
     * Initial Constructor used when single string argument supplied.
     *
     * @param dnentry to be added to new LinkedList.
     */
    public idxDNLinkList(String dnentry) {
        add(dnentry);
    } // end of Constructor

    /**
     * Add new Entry to DNLinkedList
     *
     * @param dnentry to be added to LinkedList.
     */
    public void add(String dnentry) {
        dnList.add(dnentry);
        return;
    } // end of Method

    /**
     * Add new Entry to DNLinkedList when index and value arguments supplied.
     *
     * @param dnentry_index    Index to LinkedList Entry.
     * @param dnentry to be added to LinkedList.
     */
    public void add(int dnentry_index, String dnentry) {
        dnList.add(dnentry_index, dnentry);
        return;
    } // end of Method

    /**
     * Add Entry to Beginning of DNLinkedList.
     *
     * @param dnentry to be added to LinkedList.
     */
    public void addFirst(String dnentry) {
        dnList.addFirst(dnentry);
        return;
    } // end of Method

    /**
     * Add Entry to End of DNLinkedList.
     *
     * @param dnentry to be added to LinkedList.
     */
    public void addLast(String dnentry) {
        dnList.addLast(dnentry);
        return;
    } // end of Method

    /**
     * Remove indicated Entry from DNLinkedList.
     *
     * @param dnentry to be removed from the LinkedList.
     */
    public void remove(String dnentry) {
        dnList.remove(dnentry);
        return;
    } // end of Method

    /**
     * Remove indicated Entry from DNLinkedList.
     *
     * @param dnentry_index index entry to be removed from the LinkedList.
     */
    public boolean remove(int dnentry_index) {
        if (dnentry_index < dnList.size() - 1) {
            dnList.remove(dnentry_index);
            return (true);
        } else {
            return (false);
        }
    } // end of Method

    /**
     * Remove First Entry from DNLinkedList.
     */
    public void removeFirst() {
        dnList.removeFirst();
        return;
    } // end of Method

    /**
     * Remove Last Entry from DNLinkedList.
     */
    public void removeLast() {
        dnList.removeLast();
        return;
    } // end of Method

    /**
     * Method to indicate if DNLinkedList is empty or not.
     *
     * @return boolean Indicates whether LinkedList is empty or not.
     */
    public boolean IsEmpty() {
        if (dnList.size() == 0)
            return (true);
        return (false);
    } // end of Method

    /**
     * Method to indicate if DNLinkedList is empty or not.
     *
     * @return boolean Indicates whether LinkedList is empty or not.
     */
    public boolean IsNotEmpty() {
        if (dnList.size() > 0)
            return (true);
        return (false);
    } // end of Method

    /**
     * Method to Clear and remove all entried in DNLinkedList.
     */
    public void clear() {
        dnList.clear();
    } // end of Method

    /**
     * Method to obtain size of DNLinkedList.
     *
     * @return int number of entries contained within DNLinkedList.
     */
    public int size() {
        return (dnList.size());
    } // end of Method

    /**
     * Method to obtain entry from DNLinkedList based upon index.
     *
     * @param dnentry_index Index of entry contained within DNLinkedList.
     * @return String DNLinkedList Entry.
     */
    public String get(int dnentry_index) {
        if (dnentry_index < dnList.size() - 1) {
            Object dno = dnList.get(dnentry_index);
            return ((String) dno);
        } else {
            return ("");
        }
    } // end of Method

    /**
     * Method to obtain First entry from DNLinkedList.
     *
     * @return String DNLinkedList Entry.
     */
    public String getfirst() {
        if (dnList.size() > -1) {
            Object dno = dnList.get(0);
            return ((String) dno);
        } else {
            return ("");
        }
    } // end of Method

    /**
     * Method to obtain Last entry from DNLinkedList.
     *
     * @return String DNLinkedList Entry.
     */
    public String getlast() {
        if (dnList.size() > -1) {
            Object dno = dnList.get(dnList.size() - 1);
            return ((String) dno);
        } else {
            return ("");
        }
    } // end of Method

    /**
     * Method to set entry from DNLinkedList with new contents.
     *
     * @param dnentry_index    Index of entry.
     * @param dnentry Value of entry.
     * @return boolean indicates is set was successful or not.
     */
    public boolean set(int dnentry_index, String dnentry) {
        if (dnentry_index < dnList.size() - 1) {
            dnList.set(dnentry_index, dnentry);
            return (true);
        } else {
            return (false);
        }
    } // end of Method

    /**
     * Method to Obtain Index Entry and Remove from DNLinkedList.
     *
     * @param dnentry_index Index of entry.
     * @return String Value of entry.
     */
    public String pop(int dnentry_index) {
        if (dnentry_index < dnList.size() - 1) {
            Object dno = dnList.get(dnentry_index);
            dnList.remove(dnentry_index);
            return ((String) dno);
        } else {
            return ("");
        }
    } // end of Method

    /**
     * Method to Obtain First Entry and Remove from DNLinkedList.
     *
     * @return String Value of entry.
     */
    public String popfirst() {
        if (dnList.size() > -1) {
            Object dno = dnList.get(0);
            dnList.remove(0);
            return ((String) dno);
        } else {
            return ("");
        }
    } // end of Method

    /**
     * Method to Obtain Last Entry and Remove from DNLinkedList.
     *
     * @return String Value of entry.
     */
    public String poplast() {
        if (dnList.size() > -1) {
            Object dno = dnList.get(dnList.size() - 1);
            dnList.remove(dnList.size() - 1);
            return ((String) dno);
        } else {
            return ("");
        }
    } // end of Method

    /**
     * Method to show all Entries contained in DNLinkedList to STDOUT.
     */
    public void show() {
        Iterator dnentries_itr = dnList.iterator();
        while (dnentries_itr.hasNext()) {
            Object dno = dnentries_itr.next();
            System.out.println((String) dno);
        }
    } // end of Method

} ///: End of idxDNLinkList Class.
