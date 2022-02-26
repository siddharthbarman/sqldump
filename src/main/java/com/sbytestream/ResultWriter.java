package com.sbytestream;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultWriter {
    public ResultWriter(String filePath, ResultSetMetaData metaData, int flushLineCount) throws IOException {
        this.writer = new FileWriter(filePath);
        this.metaData = metaData;
        this.firstLine = true;
        this.lineCount = 0;
        this.flushLineCount = flushLineCount;
        this.buffer = new StringBuffer();
    }

    public void write(ResultSet rs, boolean writeHeaders) throws SQLException, IOException {
        if (writeHeaders) {
            writeHeaders();
            buffer.append("\n");
        }

        while (rs.next()) {
            if (!firstLine) {
                buffer.append("\n");
                lineCount++;
                if (lineCount > flushLineCount) {
                    writer.write(getBufferedStringAndReset());
                }
            }

            writeRow(rs);
            firstLine = false;
        }

        if (buffer.length() > 0) {
            writer.write(getBufferedStringAndReset());
        }
    }

    public void close() throws IOException {
        writer.close();
    }

    private void writeHeaders() throws SQLException, IOException {
        int columnCount = metaData.getColumnCount();
        boolean first = true;
        for(int n=1; n <= columnCount; n++) {
            if (!first) {
                writer.write(",");
            }
            writer.write(metaData.getColumnName(n));
            first = false;
        }
    }

    private void writeRow(ResultSet rs) throws SQLException {
        int columnCount = metaData.getColumnCount();
        boolean first = true;
        for (int n = 1; n <= columnCount; n++) {
            if (!first) {
                buffer.append(",");
            }
            buffer.append("\"");
            buffer.append(rs.getString(n));
            buffer.append("\"");
            first = false;
        }
    }

    private String getBufferedStringAndReset() {
        String result = buffer.toString();
        buffer.setLength(0);
        lineCount = 0;
        return result;
    }

    private FileWriter writer;
    private ResultSetMetaData metaData;
    private boolean firstLine;
    private StringBuffer buffer;
    private int lineCount;
    private int flushLineCount;
}
