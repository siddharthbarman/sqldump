package com.sbytestream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;

public class App {
    private static void help() {
        System.out.println("Dumps result of sql queries to a csv file.");
        System.out.println("Queries speicifed in an XML file are executed and results are stored as csv file in the output folder.");
        System.out.println("See the sample queries files named queries.xml");
        System.out.println("Syntax:");
        System.out.println("java -jar sqldump -i <input-queries-file> -o <output-folder> -c <connection-string> -h");
        System.out.println("-i specifies the XML file containing queries to execute.");
        System.out.println("-o specifies the folder where results will be stored");
        System.out.println("-c JDBC connection string to SQL Server");
        System.out.println("-h optional. Default is false. If true write the column names as the header row");
        System.out.println("Sample connection string:");
        System.out.println("jdbc:sqlserver://SQLSVR1\\SQL2017;user=sa;password=password123;integratedSecurity=false;trustServerCertificate=true;DatabaseName=BookStore;");
    }

    public static void main(String[] args) {
        System.out.println(logo);
        log(Level.INFO, "Application started at %s", new Date().toString());

        if (args.length == 0) {
            help();
            return;
        }

        CmdLineParser cmd = new CmdLineParser(args);
        log(Level.INFO, cmd.toString());

        if (cmd.hasFlag(FLAG_CONN_STR)) {
            System.out.println("Connection string not specified.");
            System.out.println("Use the -c flag to specify the connection string to SQL Server.");
            return;
        }

        if (cmd.hasFlag(FLAG_OUT_FOLDER)) {
            System.out.println("Output folder not specified.");
            System.out.println("Use the -o flag to specify the output folder where results should be stored.");
            return;
        }

        if (cmd.hasFlag(FLAG_QUERY_FILE)) {
            System.out.println("Queries file not specified.");
            System.out.println("Use the -i flag to specify the queries file.");
            return;
        }

        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            process(cmd.getParamValue(FLAG_QUERY_FILE), cmd.getParamValue(FLAG_OUT_FOLDER),
                    cmd.getParamValue(FLAG_CONN_STR), cmd.hasFlag(FLAG_WRITE_HEADER));
        }
        catch(Exception e) {
            System.out.println("Something went terribly wrong:");
            System.out.println(e.getMessage());
            logger.error(e.toString());
        }
    }

    private static void process(String queriesFilePath, String outputFolder, String connStr, boolean writeHeader) throws ParserConfigurationException, SAXException, IOException, SQLException {
        QueryFileReader reader = new QueryFileReader();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Connection conn = DriverManager.getConnection(connStr);
        log(Level.INFO, "Connection established in %d ms", stopWatch.stop());

        for(QueryWork qw : reader.read(queriesFilePath)) {
            executeQuery(qw, conn, outputFolder, writeHeader);
        }

        conn.close();
    }

    private static void executeQuery(QueryWork qw, Connection conn, String outputPath, boolean writerHeader) throws SQLException, IOException {
        logger.info(String.format("Query work: %s", qw.toString()));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(qw.getQuery());
             ) {

            log(Level.INFO, "Query completed in %d ms", stopWatch.reset());

            ResultSetMetaData metadata = rs.getMetaData();
            Path outputFilePath = Paths.get(outputPath, qw.getFilename());
            ResultWriter writer = new ResultWriter(outputFilePath.toString(),  metadata, WRITE_AFTER_LINE_COUNT);

            writer.write(rs, writerHeader);
            writer.close();

            String writeMessage = String.format("Wrote results to %s", outputFilePath.toString());
            logger.info(writeMessage);
            System.out.println(writeMessage);
        }
    }

    private static void log(Level level, String format, Object ... args) {
        switch (level) {
            case DEBUG:
                logger.debug(String.format(format, args));
                break;

            case ERROR:
                logger.error(String.format(format, args));
                break;

            case INFO:
                logger.info(String.format(format, args));
                break;

            case TRACE:
                logger.trace(String.format(format, args));
                break;

            case WARN:
                logger.warn(String.format(format, args));
                break;
        }
    }

    private static void log(Level level, String[] array) {
        StringBuilder sb = new StringBuilder();
        if (array != null && array.length > 0) {
            for(int n=0; n < array.length; n++) {
                sb.append(array[n]);
                sb.append(' ');

            }
            if (sb.length() > 0) {
                log(level, sb.toString());
            }
        }
    }


    private static Logger logger = LoggerFactory.getLogger(App.class);
    private static final String FLAG_OUT_FOLDER = "o";
    private static final String FLAG_QUERY_FILE = "i";
    private static final String FLAG_CONN_STR = "c";
    private static final String FLAG_WRITE_HEADER = "h";
    private static final int WRITE_AFTER_LINE_COUNT = 100;
    private static final String logo = " ___  ___  _    ___  _ _  __ __  ___ \n" +
            "/ __>| . || |  | . \\| | ||  \\  \\| . \\\n" +
            "\\__ \\| | || |_ | | || ' ||     ||  _/\n" +
            "<___/`___\\|___||___/`___'|_|_|_||_|  \n" +
            "                                     ";

}
