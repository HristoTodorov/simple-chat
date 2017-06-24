package com.simplechat.perf.client;

import org.apache.commons.cli.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created by lampt on 6/18/2017.
 */
public class PerfClientInitializer {

    public static void main(String[] args) {
        Arrays.asList(args).forEach(System.out::println);
        ArgumentParser parser = new ArgumentParser(args).invoke();
        ClientPerf perf = new ClientPerf(parser.getPort(), parser.getClientSuffix(), parser.getNumberOfUnicastMessages(),
                parser.getNumberOfBroadcastMessages(), parser.getNumberOfListMessages(), parser.getStatsFile());
        perf.run();
    }

    private static class ArgumentParser {
        private final String[] args;
        private int port = 5555;
        private long clientSuffix;
        private long numberOfUnicastMessages;
        private long numberOfBroadcastMessages;
        private long numberOfListMessages;
        private String statsFile;

        public ArgumentParser(String... args) {
            this.args = args;
        }

        public long getClientSuffix() {
            return clientSuffix;
        }

        public long getNumberOfUnicastMessages() {
            return numberOfUnicastMessages;
        }

        public long getNumberOfBroadcastMessages() {
            return numberOfBroadcastMessages;
        }

        public long getNumberOfListMessages() {
            return numberOfListMessages;
        }

        public int getPort() {
            return port;
        }

        public String getStatsFile() {
            return statsFile;
        }

        @NotNull
        public ArgumentParser invoke() {
            Options options = getOptions();
            CommandLineParser parser = new DefaultParser();
            parseOptions(options, parser);
            return this;
        }

        private void parseOptions(Options options, CommandLineParser parser) {
            try {
                CommandLine line = parser.parse(options, args);
                // help option
                if (line.hasOption("h")) {
                    printHelp(options);
                    System.exit(0);
                }
                // port option
                if (line.hasOption("p")) {
                    port = ((Number) line.getParsedOptionValue("p")).intValue();
                }
                // broadcast message option
                if (line.hasOption("bmsg")) {
                    numberOfBroadcastMessages = ((Number) line.getParsedOptionValue("bmsg")).intValue();
                }
                // unicast message option
                if (line.hasOption("umsg")) {
                    numberOfUnicastMessages = ((Number)line.getParsedOptionValue("umsg")).intValue();
                }
                // list message option
                if (line.hasOption("lmsg")) {
                    numberOfListMessages = ((Number)line.getParsedOptionValue("lmsg")).intValue();
                }
                // client name suffix option
                if (line.hasOption("cname")) {
                    clientSuffix = ((Number)line.getParsedOptionValue("cname")).intValue();
                }
                // the stats file absolute path
                if (line.hasOption("sfile")) {
                    statsFile = (String) line.getParsedOptionValue("sfile");
                }
            } catch (Throwable exp) {
                System.err.println("Perf client did not start.");
                exp.printStackTrace();
                printHelp(options);
                System.exit(0);
            }
        }

        private void printHelp(Options options) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Simple chat Server - Help menu", options);
        }

        @NotNull
        private Options getOptions() {
            Option portOption = Option.builder("p")
                    .longOpt("port")
                    .hasArg()
                    .type(Number.class)
                    .desc("The port on which the Server is bind.")
                    .required(false)
                    .build();

            Option unicastMsgOption = Option.builder("umsg")
                    .longOpt("unicastmsg")
                    .hasArg()
                    .type(Number.class)
                    .desc("A total number of unicast messages to send.")
                    .required()
                    .build();

            Option broadcastMsgOption = Option.builder("bmsg")
                    .longOpt("broadcastmsg")
                    .hasArg()
                    .type(Number.class)
                    .desc("A total number of broadcast messages to send.")
                    .required()
                    .build();

            Option listMsgOption = Option.builder("lmsg")
                    .longOpt("listmsg")
                    .hasArg()
                    .type(Number.class)
                    .desc("A total number of list messages to send")
                    .required()
                    .build();

            Option clientNameSuffixOption = Option.builder("cname")
                    .longOpt("clientname")
                    .hasArg()
                    .type(Number.class)
                    .desc("The client name suffix")
                    .required()
                    .build();

            Option statsFileOption = Option.builder("sfile")
                    .longOpt("statsfile")
                    .hasArg()
                    .desc("The absolute path to the stats file.")
                    .required(false)
                    .build();

            Option helpOption = Option.builder("h")
                    .longOpt("help")
                    .hasArg(false)
                    .desc("Show the information about the supported arguments.")
                    .required(false)
                    .build();

            Options options = new Options();
            options.addOption(portOption)
                    .addOption(unicastMsgOption)
                    .addOption(broadcastMsgOption)
                    .addOption(listMsgOption)
                    .addOption(clientNameSuffixOption)
                    .addOption(statsFileOption)
                    .addOption(helpOption);
            return options;
        }
    }
}
