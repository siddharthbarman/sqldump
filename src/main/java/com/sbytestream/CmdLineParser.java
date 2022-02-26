package com.sbytestream;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmdLineParser {
    public CmdLineParser(String[] args) {
        parse(args);
    }

    public String getParamValue(String name) {
        return pairs.get(name);
    }

    public boolean getParamValueBoolean(String name, boolean defaultValue) {
        if (!hasFlag(name)) {
            return defaultValue;
        }
        else {
            return Boolean.parseBoolean(getParamValue(name));
        }
    }

    public boolean hasFlag(String flagName) {
        if (!pairs.containsKey(flagName))
            return false;
        else
            return (pairs.get(flagName) == null);
    }

    public String getAt(int index) {
        return rawValues.get(index);
    }

    public String toString() {
        if (flatString == null) {
            StringBuilder sb = new StringBuilder();

            for (int n = 0; n < rawValues.size(); n++) {
                sb.append(rawValues.get(n));
                sb.append(' ');
            }

            flatString = sb.toString();
        }
        return flatString;
    }

    private void parse(String[] args) {
        String currentParamName = null;

        for(int n=0; n < args.length; n++) {
            String current = args[n];
            if (current.charAt(0) == '-') {
                currentParamName = current.substring(1);
                pairs.put(currentParamName, null);
            }
            else {
                if (currentParamName != null) {
                    pairs.put(currentParamName, current);
                }
                else {
                    rawValues.add(current);
                }
            }
        }
    }

    HashMap<String, String> pairs = new HashMap<String, String>();
    List<String> rawValues = new ArrayList<String>();
    String flatString = null;
}

