package Models;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Sends plain-text emails through an SMTP server over SSL, using only the JDK.
 * Settings come from environment variables so no password is stored in the code:
 * ECONEST_MAIL_USER, ECONEST_MAIL_PASSWORD, and optionally ECONEST_SMTP_HOST / ECONEST_SMTP_PORT
 * (defaults: smtp.gmail.com, 465).
 */
public class EmailService {

    private static final String DEFAULT_HOST = "smtp.gmail.com";
    private static final int DEFAULT_PORT = 465;
    private static final int TIMEOUT_MILLIS = 15000;
    private static final String LINE_END = "\r\n";

    private String host;
    private int port;
    private String username;
    private String password;

    public EmailService() {
        host = readSetting("ECONEST_SMTP_HOST", DEFAULT_HOST);
        port = parsePort(readSetting("ECONEST_SMTP_PORT", String.valueOf(DEFAULT_PORT)));
        username = readSetting("ECONEST_MAIL_USER", "");
        password = readSetting("ECONEST_MAIL_PASSWORD", "");
    }

    public boolean isConfigured() {
        return !username.isEmpty() && !password.isEmpty();
    }

    /**
     * Sends one email. Blocks until the server answers, so call it off the Swing thread.
     *
     * @throws IOException when the service is not configured, the input is unsafe or the server refuses the message
     */
    public void sendEmail(String to, String subject, String body) throws IOException {
        if (!isConfigured()) {
            throw new IOException("Email is not configured. Set the ECONEST_MAIL_USER and ECONEST_MAIL_PASSWORD environment variables.");
        }

        if (containsLineBreak(to) || containsLineBreak(subject)) {
            throw new IOException("Recipient and subject must not contain line breaks.");
        }

        Socket plainSocket = new Socket();
        plainSocket.connect(new InetSocketAddress(host, port), TIMEOUT_MILLIS);

        try (SSLSocket socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault())
                .createSocket(plainSocket, host, port, true)) {
            socket.setSoTimeout(TIMEOUT_MILLIS);

            SSLParameters parameters = socket.getSSLParameters();
            parameters.setEndpointIdentificationAlgorithm("HTTPS");
            socket.setSSLParameters(parameters);
            socket.startHandshake();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            expectReply(reader, "220");
            sendCommand(writer, "EHLO econest");
            expectReply(reader, "250");
            sendCommand(writer, "AUTH LOGIN");
            expectReply(reader, "334");
            sendCommand(writer, encode(username));
            expectReply(reader, "334");
            sendCommand(writer, encode(password));
            expectReply(reader, "235");
            sendCommand(writer, "MAIL FROM:<" + username + ">");
            expectReply(reader, "250");
            sendCommand(writer, "RCPT TO:<" + to + ">");
            expectReply(reader, "25");
            sendCommand(writer, "DATA");
            expectReply(reader, "354");
            sendCommand(writer, buildMessage(to, subject, body) + LINE_END + ".");
            expectReply(reader, "250");
            sendCommand(writer, "QUIT");
        }
    }

    private String buildMessage(String to, String subject, String body) {
        return "From: " + username + LINE_END
                + "To: " + to + LINE_END
                + "Subject: " + encodeHeader(subject) + LINE_END
                + "Date: " + DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()) + LINE_END
                + "MIME-Version: 1.0" + LINE_END
                + "Content-Type: text/plain; charset=UTF-8" + LINE_END
                + "Content-Transfer-Encoding: base64" + LINE_END
                + LINE_END
                + Base64.getMimeEncoder().encodeToString(body.getBytes(StandardCharsets.UTF_8));
    }

    private void sendCommand(BufferedWriter writer, String command) throws IOException {
        writer.write(command + LINE_END);
        writer.flush();
    }

    private void expectReply(BufferedReader reader, String expectedCode) throws IOException {
        String line;
        String lastLine = null;

        while ((line = reader.readLine()) != null) {
            lastLine = line;
            if (line.length() < 4 || line.charAt(3) != '-') {
                break;
            }
        }

        if (lastLine == null) {
            throw new IOException("The mail server closed the connection.");
        }

        if (!lastLine.startsWith(expectedCode)) {
            throw new IOException("Mail server replied: " + lastLine);
        }
    }

    private String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeHeader(String text) {
        boolean isAscii = text.chars().allMatch(c -> c < 128);
        return isAscii ? text : "=?UTF-8?B?" + encode(text) + "?=";
    }

    private boolean containsLineBreak(String text) {
        return text.indexOf('\r') >= 0 || text.indexOf('\n') >= 0;
    }

    private String readSetting(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }

    private int parsePort(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException err) {
            return DEFAULT_PORT;
        }
    }
}
