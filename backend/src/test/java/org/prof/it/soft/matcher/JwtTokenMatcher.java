package org.prof.it.soft.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class JwtTokenMatcher implements Matcher<String> {

    public static JwtTokenMatcher JwtTokenMatcher() {
        return new JwtTokenMatcher();
    }

    @Override
    public boolean matches(Object actual) {
        if (actual instanceof String) {
            return  ((String) actual).length() > 20;
        }
        return false;
    }

    @Override
    public void describeMismatch(Object actual, Description mismatchDescription) {
        mismatchDescription.appendText("The actual value is not a valid JWT token");
    }

    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

    }

    @Override
    public void describeTo(Description description) {
    }
}
