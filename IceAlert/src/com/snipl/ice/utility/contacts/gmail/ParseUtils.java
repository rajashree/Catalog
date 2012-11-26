/**
 * 
 */
package com.snipl.ice.utility.contacts.gmail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Cookie;

/**
 * <code>ParseUtils</code>
 * 
 * Created: Jan 15, 2006 8:12:33 AM
 * 
 * TODO Comment Me
 */
public final class ParseUtils
{

    // class-wide log
  //  private static Log log = LogFactory.getLog(ParseUtils.class);

    private ParseUtils()
    {
    }

    /**
     * Parses a GMail Response and returns an ordered collection of response types
     * 
     * @param response
     *            response data
     * 
     * example: qu -> ["47 MB","1000 MB","5%","#006633"]
     * 
     * @return ordered Collection
     * @throws GMParseException
     */
    public static Collection<List> parseGMResponse(String response)
            throws GMParseException
    {
        //log.debug("Input: " + response);
        if (response == null || response.equals(""))
        {
            new GMParseException("Empty input!");
        }

        Collection<List> items = new Vector<List>();

        // cleanup incoming data
        response = response.replaceAll("\n", "");
        response = response.replaceAll("D\\(\\[", "\nD([");
        response = response.replaceAll("\\]\\);", "]);\n");

        // capture all the dataItems
        Pattern pDataItems = Pattern.compile("D\\((\\[.+\\])\\)");
        Matcher mDataItems = pDataItems.matcher(response);

        while (mDataItems.find())
        {
            // get ArrayList version of dataPack JS array
            List tmpArray = ParseJSArray(mDataItems.group(1));
           //log.info("Data Item: " + mDataItems.group(1));
            items.add(tmpArray);
        }
        return items;
    }

    /**
     * Parses the response data for a contact search and returns the GMContacts
     * 
     * @param input
     *            response string
     * @return Iterable of GMContacts
     * @throws GMParseException
     */
    public static Iterable<GMContact> parseContactData(String input)
            throws GMParseException
    {
        // parse the data
        Collection<List> parseMap = parseGMResponse(input);

        List<GMContact> contacts = new ArrayList<GMContact>();
        for (Iterator iter = parseMap.iterator(); iter.hasNext();)
        {
            List tmpArray = (List) iter.next();
            String settingName = (String) tmpArray.get(0);
            tmpArray.remove(0);

            try
            {
               //log.info("tmpArray: " + tmpArray);
                if (settingName.equals("v"))
                {
                    // version tag
                   //log.info("version: " + (String) tmpArray.get(0));
                }
                else if (settingName.equals("cl") || settingName.equals("p")
                        || settingName.equals("a") || settingName.equals("s"))
                {
                   //log.info("settingName: " + settingName + ": " + tmpArray);
                    for (int i = 0; i < tmpArray.size(); i++)
                    {
                        Object item = tmpArray.get(i);
                        if (item instanceof List)
                        {
                            List aitem = (List) item;
                            if (aitem.get(0).equals("ce"))
                            {
                                // remove the "contact entry" tag
                                aitem.remove(0);
                            }
                            String id = (String) aitem.get(0);
                            String name = (String) aitem.get(1);
                            String email = (String) aitem.get(3);
                            String notes = (aitem.size() > 4) ? (String) aitem
                                    .get(4) : "";

                            GMContact cont = new GMContact(id, name, email,
                                    notes);
                            contacts.add(cont);
                        }
                    }
                }
                else if (settingName.equals("cls"))
                {
                   //log.info("cls ");
                }
                else
                {
                   //log.info("Unknown settingName: " + settingName);
                }
            }
            catch (Exception e)
            {
                throw new GMParseException("failed parsing label packet ("
                        + settingName + ")", input, e);
            }
        }
        return contacts;
    }

    /**
     * Parses a JS array and returns a List
     * 
     * @param input
     * @return
     */
    public static List ParseJSArray(String input)
    {
        Integer[] outReturnOffset = { new Integer(0) };
        return ParseJSArrayRecurse(input, outReturnOffset);
    }

    private static List ParseJSArrayRecurse(String input,
            Integer[] outReturnOffset)
    {
        // instantiate output
        List output = new ArrayList();

        // state variables
        boolean isQuoted = false; // track when we are inside quotes
        String dataHold = ""; // temporary data container
        char lastCharacter = ' ';

        // loop through the entire string
        for (int i = 1; i < input.length(); i++)
        {
            switch (input.charAt(i))
            {
            case '[': // handle start of array marker
                if (!isQuoted)
                {
                    // recurse any nested arrays
                    output.add(ParseJSArrayRecurse(input.substring(i),
                            outReturnOffset));

                    // the returning recursive function write out the total
                    // length of the characters consumed
                    i += outReturnOffset[0].intValue();

                    // assume that the last character is a closing bracket
                    lastCharacter = ']';
                }
                else
                {
                    dataHold += "[";
                }
                break;

            case ']': // handle end of array marker
                if (!isQuoted)
                {
                    if (dataHold != "")
                    {
                        output.add(dataHold);
                    }

                    // determine total number of characters consumed (write to
                    // reference)
                    outReturnOffset[0] = new Integer(i);
                    return output;
                }
                else
                {
                    dataHold += "]";
                    break;
                }

            case '"': // toggle quoted state
                if (isQuoted)
                {
                    isQuoted = false;
                }
                else
                {
                    isQuoted = true;
                    lastCharacter = '"';
                }
                break;

            case ',': // find end of element marker and add to array
                if (!isQuoted)
                {
                    if (dataHold != "")
                    { // this is to filter out adding extra elements after an
                        // empty array
                        output.add(dataHold);
                        dataHold = "";
                    }
                    else if (lastCharacter == '"')
                    { // this is to catch empty strings
                        output.add("");
                    }
                }
                else
                {
                    dataHold += ",";
                }
                break;
            case '\\':
                // handle escape characters
                if (i < input.length() - 1)
                {
                    switch (input.charAt(i + 1))
                    {
                    case '\'':
                        dataHold += '\'';
                        i++;
                        break;
                    case '"':
                        dataHold += '"';
                        i++;
                        break;
                    case '<':
                        dataHold += '<';
                        i++;
                        break;
                    case '>':
                        dataHold += '>';
                        i++;
                        break;
                    case '\\':
                        dataHold += '\\';
                        i++;
                        break;
                    case 'r':
                        dataHold += '\r';
                        i++;
                        break;
                    case 'n':
                        dataHold += '\n';
                        i++;
                        // lastCharacter = '\\';
                        break;
                    case 'u':
                       //log.info("begin unicode sequence");
                        i++;
                        String unicodeHolder = "0x";
                        unicodeHolder += input.charAt(++i);
                        unicodeHolder += input.charAt(++i);
                        unicodeHolder += input.charAt(++i);
                        unicodeHolder += input.charAt(++i);
                       //log.info("Unicode string: " + unicodeHolder);
                        dataHold += (char) Long.decode(unicodeHolder)
                                .longValue();
                        break;
                    default:
                       //log.info("Unknow escape: " + input.charAt(i + 1));
                    }
                }
                break;

            default: // regular characters are added to the data container
                dataHold += input.charAt(i);
                break;
            }
        }

        return output;
    }

    /**
     * Parses the cookie, identified by key out of the cookies
     * 
     * @param key
     * @param cookies
     * @return the cookie value
     */
    public static String getCookieValue(String key, Cookie[] cookies)
    {
        String retVal = "";
        for (int i = 0; i < cookies.length; i++)
        {
            String cookieTxt = cookies[i].toString();
            String[] cookieSplit = cookieTxt.split("=");
            if (cookieSplit != null && cookieSplit[0].equals(key))
            {
                retVal = cookieSplit[1];
                break;
            }
        }
        return retVal;
    }
}