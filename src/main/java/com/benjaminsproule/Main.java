package com.benjaminsproule;

import org.apache.commons.cli.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("t", "title", true, "Title of the meeting");
        options.addOption("m", "meetingRoom", true, "Meeting room of the meeting if it is an in-person meeting");
        options.addOption("z", "zoomId", true, "Zoom meeting ID if the meeting is on Zoom");

        CommandLine cmd = getCommandLine(args, options);

        String meetingTitle = cmd.getOptionValue("t");
        String meetingRoom = cmd.getOptionValue("m");
        String zoomMeetingId = cmd.getOptionValue("z");

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(meetingTitle, meetingRoom, zoomMeetingId));
    }

    private static void createAndShowGUI(String meetingTitle, String meetingRoom, String zoomMeetingId) {
        JFrame frame = new JFrame("You have a meeting!");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300, 200));
        frame.setAlwaysOnTop(true);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.setContentPane(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;

        JLabel headingLabel = new JLabel("You have a meeting!");
        panel.add(headingLabel, c);

        JLabel meetingTitleLabel = new JLabel(meetingTitle);
        c.gridy += 1;
        panel.add(meetingTitleLabel, c);

        JLabel zoomMeetingIdLabel = new JLabel("Zoom meeting ID: " + zoomMeetingId);
        c.gridy += 1;
        panel.add(zoomMeetingIdLabel, c);

        if (meetingRoom != null) {
            JLabel meetingRoomLabel = new JLabel("Meeting room: " + meetingRoom);
            c.gridy += 1;
            panel.add(meetingRoomLabel, c);
        }

        if (zoomMeetingId != null) {
            JButton openZoomButton = new JButton("Open Zoom meeting");
            openZoomButton.addActionListener(actionEvent -> {
                try {
                    String openCommand = switch (System.getProperty("os.name")) {
                        case "Windows" -> "start \"\"";
                        case "Mac OS X" -> "open";
                        case "Linux" -> "xdg-open";
                        default -> "";
                    };
                    Runtime.getRuntime().exec(openCommand + " zoommtg://zoom.us/join?action=join&confno=" + zoomMeetingId.replace(" ", ""));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                frame.dispose();
            });
            c.gridy += 1;
            panel.add(openZoomButton, c);
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static CommandLine getCommandLine(String[] args, Options options) throws ParseException {
        HelpFormatter formatter = new HelpFormatter();
        if (args.length == 0) {
            formatter.printHelp("meeting-reminder", options);
            throw new ParseException("No arguments given.");
        }

        CommandLineParser parser = new DefaultParser();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("utility-name", options);
            throw e;
        }
    }
}
