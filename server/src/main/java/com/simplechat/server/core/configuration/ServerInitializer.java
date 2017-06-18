package com.simplechat.server.core.configuration;

import org.apache.commons.cli.*;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by lampt on 6/17/2017.
 */
public class ServerInitializer {
    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser(args).invoke();
        int port = argumentParser.getPort();
        boolean isSilentModeOn = argumentParser.isSilentModeOn();
        System.out.println(MessageFormat.format("Server Configuration: port: {0}; silent mode: {1}.",
                port, isSilentModeOn ? "on" : "off"));
        System.out.println("Server started at " + LocalDateTime.now().format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME ));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Server stopped at: " + LocalDateTime.now().format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME ))));
        Server.init(port, isSilentModeOn);
    }

    private static class ArgumentParser {
        private final String[] args;
        private int port;
        private boolean isSilentModeOn;

        public ArgumentParser(String... args) {
            this.args = args;
        }

        private void printHelp(Options options) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Simple chat Server - Help menu", options);
        }

        public int getPort() {
            return port;
        }

        public boolean isSilentModeOn() {
            return isSilentModeOn;
        }

        @NotNull
        public ArgumentParser invoke() {
            port = 5555;
            isSilentModeOn = false;

            Option portOption = Option.builder("p")
                    .longOpt("port")
                    .hasArg()
                    .type(Number.class)
                    .desc("The port in which the Server will bind.")
                    .required(false)
                    .build();
            Option silentModeOption = Option.builder("s")
                    .longOpt("smode")
                    .hasArg(false)
                    .desc("A flag, indicating whether or not the silent mode should be turn on.")
                    .required(false)
                    .build();
            Option helpOption = Option.builder("h")
                    .longOpt("help")
                    .hasArg(false)
                    .desc("Show the information about the supported arguments.")
                    .required(false)
                    .build();
            Options options = new Options();
            options.addOption(portOption).addOption(silentModeOption).addOption(helpOption);
            CommandLineParser parser = new DefaultParser();
            try {
                CommandLine line = parser.parse(options, args);

                if (line.hasOption("h")) {
                    printHelp(options);
                    System.exit(0);
                }
                if (line.hasOption("p")) {
                    port = ((Number) line.getParsedOptionValue("p")).intValue();
                }
                if (line.hasOption("s")) {
                    isSilentModeOn = true;
                }
            } catch (Throwable exp) {
                System.err.println("Server did not start.");
                printHelp(options);
                System.exit(0);
            }
            return this;
        }
    }
}
