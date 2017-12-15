package com.rudolfschmidt.amr.consumers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BlockConsumer {

    static String handeBlocks(String html, String blocks) {
        final String regex = "<block.*?name=(?<q>[\"'])(?<n>.*?)\\k<q>.*?>(?<c>[\\s\\S]*?)</block>";
        Matcher matcher = Pattern.compile(regex).matcher(blocks);
        while (matcher.find()) {
            final String n = "<block.*?name=(?<q>[\"'])" + matcher.group("n") + "\\k<q>.*?>[\\s\\S]*?</block>";
            html = html.replaceAll(n, matcher.group("c").replace("\\", "\\\\").replace("$", "\\$"));
        }

        return html;
    }

    static String cleanBlocks(String html) {
        final Matcher matcher = Pattern.compile("<block.*?name=.*?>.*?</block>").matcher(html);
        while (matcher.find()) {
            html = html.replace(matcher.group(), "");
        }

        return html;
    }

}
