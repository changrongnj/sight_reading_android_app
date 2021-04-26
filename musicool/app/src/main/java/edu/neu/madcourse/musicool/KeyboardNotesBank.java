package edu.neu.madcourse.musicool;

import java.util.ArrayList;
import java.util.List;

public class KeyboardNotesBank {

    private static final List<List<String>> kbNotes = new ArrayList<>();

    static {
        List<String> k1 = new ArrayList<>();
        k1.add("C");
        kbNotes.add(k1);

        List<String> k2 = new ArrayList<>();
        k2.add("C#");
        k2.add("D♭");
        kbNotes.add(k2);

        List<String> k3 = new ArrayList<>();
        k3.add("D");
        kbNotes.add(k3);

        List<String> k4 = new ArrayList<>();
        k4.add("D#");
        k4.add("E♭");
        kbNotes.add(k4);

        List<String> k5 = new ArrayList<>();
        k5.add("E");
        kbNotes.add(k5);

        List<String> k6 = new ArrayList<>();
        k6.add("F");
        kbNotes.add(k6);

        List<String> k7 = new ArrayList<>();
        k7.add("F#");
        k7.add("G♭");
        kbNotes.add(k7);

        List<String> k8 = new ArrayList<>();
        k8.add("G");
        kbNotes.add(k8);

        List<String> k9 = new ArrayList<>();
        k9.add("G#");
        k9.add("A♭");
        kbNotes.add(k9);

        List<String> k10 = new ArrayList<>();
        k10.add("A");
        kbNotes.add(k10);

        List<String> k11 = new ArrayList<>();
        k11.add("A#");
        k11.add("B♭");
        kbNotes.add(k11);

        List<String> k12 = new ArrayList<>();
        k12.add("B");
        kbNotes.add(k12);

    }

    public static List<List<String>> getKbNotes() {
        return kbNotes;
    }
}
