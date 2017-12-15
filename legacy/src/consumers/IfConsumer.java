package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.model.Model;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rudolfschmidt.amr.model.ModelReader.getModelValue;

public class IfConsumer {

    public static String handleIf(String html, Model model) {
        final String regex = "<if.*?exp=(?<q>[\"'])(?<e>.*?)\\k<q>.*?>(?<c>[\\s\\S]*?)</if>";
        final Matcher matcher = Pattern.compile(regex).matcher(html);
        while (matcher.find()) {
            boolean condition = false;
            final String[] tokens = matcher.group("e").split("\\s");
            for (int i = 0; i < tokens.length; i++) {
                if (!tokens[i].equals("&&") && !tokens[i].equals("||")) {
                    if (i - 1 > 0 && tokens[i - 1].equals("&&")) {
                        condition &= evaluate(tokens[i], model);
                    } else if (i - 1 > 0 && tokens[i - 1].equals("||")) {
                        condition |= evaluate(tokens[i], model);
                    } else {
                        condition = evaluate(tokens[i], model);
                    }
                }
            }
            if (condition) {
                html = html.replace(matcher.group(), matcher.group("c"));
            } else {
                html = html.replace(matcher.group(), "");
            }

        }
        return html;
    }

    private static boolean evaluate(String token, Model model) {

        final Optional<Object> e = getModelValue(token, model);

        if (e.filter(String.class::isInstance).isPresent()) {
            return e.map(String.class::cast).filter(value -> value.length() > 0).isPresent();
        } else if (e.filter(Boolean.class::isInstance).isPresent()) {
            return e.filter(value -> value.equals(Boolean.TRUE)).isPresent();
        } else if (e.filter(Number.class::isInstance).isPresent()) {
            return e.map(Number.class::cast).filter(value -> value.doubleValue() > 0).isPresent();
        } else if (e.isPresent()) {
            return true;
        }
        return false;
    }

}
