# Meeting Reminder

## Install schedules

### Linux

Use cron to schedule runs.

1. Open crontab for edit
    ```shell
    crontab -e
    ```
2. Use a [crontab calculator](https://crontab.guru/) to figure out when you want it to run
3. Update the crontab with your schedule
    ```cronexp
    0 9 * * 1-5 export DISPLAY=:0; /path/to/java -jar /path/to/meeting-reminder-1.0-SNAPSHOT-jar-with-dependencies.jar -t title -z "zoom ID"
    ```

### Mac

Mac has deprecated the use of cron, so schedules should be configured via launchd.

Example plist XML file to schedule a run.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Label</key>
        <string>com.benjaminsproule.meeting-reminder-standup</string>
        <key>ProgramArguments</key>
        <array>
            <string>/path/to/java</string>
            <string>-jar</string>
            <string>/path/to/meeting-reminder-1.0-SNAPSHOT-jar-with-dependencies.jar</string>
            <string>-t</string>
            <string>title</string>
            <string>-z</string>
            <!-- Allows spaces in the ID -->
            <string>zoom ID</string>
        </array>
        <key>StartCalendarInterval</key>
        <array>
            <dict>
                <!--
                    Monday    = 1
                    Tuesday   = 2
                    Wednesday = 3
                    Thursday  = 4
                    Friday    = 5
                    Saturday  = 6
                    Sunday    = 7
                -->
                <key>Minute</key>
                <integer>0</integer>
                <key>Hour</key>
                <integer>9</integer>
                <key>Weekday</key>
                <integer>1</integer>
            </dict>
            <dict>
                <key>Minute</key>
                <integer>0</integer>
                <key>Hour</key>
                <integer>9</integer>
                <key>Weekday</key>
                <integer>2</integer>
            </dict>
        </array>
    </dict>
</plist>
```

1. Create the plist file under the user `LaunchAgents` directory.
    ```shell
    xml=$(cat example.xml)
    echo xml ~/Library/LaunchAgents/com.benjaminsproule.meeting-reminder-unique-name.plist
    ```
2. Load the plist file into launchd.
    ```shell
    launchctl bootstrap gui/${UID} ~/Library/LaunchAgents/com.benjaminsproule.meeting-reminder-unique-name.plist
    ```
