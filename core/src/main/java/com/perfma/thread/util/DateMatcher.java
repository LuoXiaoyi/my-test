package com.perfma.thread.util;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DateMatcher {
    private Pattern regexPattern;
    private boolean patternError;
    private boolean defaultMatches;
    private Matcher matched = null;

    String pattern = "(\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d).*";

    public DateMatcher() {
        try {
            regexPattern = Pattern.compile(pattern);
            setPatternError(false);
        } catch (PatternSyntaxException pe) {
            showErrorPane(pe.getMessage());
        }
    }

    public Pattern getRegexPattern() {
        return regexPattern;
    }

    public boolean isPatternError() {
        return patternError;
    }

    public void setPatternError(boolean patternError) {
        this.patternError = patternError;
    }

    public Matcher checkForDateMatch(String line) {
        try {
            Matcher m = regexPattern.matcher(line);
            if (m != null && m.matches()) {
                setDefaultMatches(true);
                matched = m;
            } else {
                m = getRegexPattern().matcher(line);
                if (m.matches()) {
                    setDefaultMatches(false);
                    matched = m;
                }
            }
        } catch (Exception ex) {
            showErrorPane(ex.getMessage());
        }

        return (matched);
    }

    public Matcher getLastMatch() {
        return (matched);
    }

    public void resetLastMatch() {
        matched = null;
    }

    private void showErrorPane(String message) {
        JOptionPane.showMessageDialog(null, "Error during parsing line for timestamp regular expression!\n" + "Please check regular expression in " +
                "your preferences. Deactivating\n" + "parsing for the rest of the file! Error Message is \"" + message + "\" \n", "Error during " +
                "Parsing", JOptionPane.ERROR_MESSAGE);

        setPatternError(true);
    }

    public boolean isDefaultMatches() {
        return defaultMatches;
    }

    private void setDefaultMatches(boolean defaultMatches) {
        this.defaultMatches = defaultMatches;
    }
}
