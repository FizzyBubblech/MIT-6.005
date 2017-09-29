/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import org.junit.Test;

import minesweeper.server.MinesweeperServer;

/**
 * Tests for MinesweeperServer
 */
public class MinesweeperServerTest {
    
    // Testing strategy
    // 
    // Basic look, flag, deflag, dig commands and X,Y directions
    // Debug mode
    // playerCount
    
    private static final String LOCALHOST = "127.0.0.1";
    private static final int PORT = 4000 + new Random().nextInt(1 << 15);
    
    private static final int MAX_CONNECTION_ATTEMPTS = 10;
    
    private static final String BOARDS_PKG = "minesweeper/server/";
    
    /**
     * Start a MinesweeperServer in debug mode with a board file from BOARDS_PKG.
     * @param boardFile board to load
     * @return thread running the server
     * @throws IOException if the board file cannot be found
     */
    private static Thread startMinesweeperServer(String boardFile) throws IOException {
        final URL boardURL = ClassLoader.getSystemClassLoader().getResource(BOARDS_PKG + boardFile);
        if (boardURL == null) {
            throw new IOException("Failed to locate resource " + boardFile);
        }
        final String boardPath;
        try {
            boardPath = new File(boardURL.toURI()).getAbsolutePath();
        } catch (URISyntaxException urise) {
            throw new IOException("Invalid URL " + boardURL, urise);
        }
        final String[] args = new String[] {
                "--debug",
                "--port", Integer.toString(PORT),
                "--file", boardPath
        };
        Thread serverThread = new Thread(() -> MinesweeperServer.main(args));
        serverThread.start();
        return serverThread;
    }
    
    /**
     * Connect to a MinesweeperServer and return the connected socket.
     * @param server abort connection attempts if the server thread dies
     * @return socket connected to the server
     * @throws IOException if the connection fails
     */
    private static Socket connectToMinesweeperServer(Thread server) throws IOException {
        int attempts = 0;
        while (true) {
            try {
                Socket socket = new Socket(LOCALHOST, PORT);
                socket.setSoTimeout(3000);
                return socket;
            } catch (ConnectException ce) {
                if ( ! server.isAlive()) {
                    throw new IOException("Server thread not running");
                }
                if (++attempts > MAX_CONNECTION_ATTEMPTS) {
                    throw new IOException("Exceeded max connection attempts", ce);
                }
                try { Thread.sleep(attempts * 10); } catch (InterruptedException ie) { }
            }
        }
    }
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test(timeout = 10000)
    public void publishedTest() throws IOException {

        Thread thread = startMinesweeperServer("board_file_5");

        Socket socket = connectToMinesweeperServer(thread);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertEquals("expected HELLO message", in.readLine(), "Welcome to Minesweeper. Board: 7 columns by 7 rows. " + 
                     "Players: 1 including you. Type 'help' for help.");
        in.readLine();
        
        Socket socket2 = connectToMinesweeperServer(thread);
        
        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);

        assertEquals("expected HELLO message", in2.readLine(), "Welcome to Minesweeper. Board: 7 columns by 7 rows. " + 
                     "Players: 2 including you. Type 'help' for help.");
        in2.readLine();

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        in.readLine();
        
        out.println("flag 0 0");
        assertEquals("F - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        in.readLine();
        
        out.println("deflag 0 0");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        in.readLine();
        
        out.println("dig 3 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 1 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        in.readLine();

        out.println("dig 4 1");
        assertEquals("BOOM!", in.readLine());
        in.readLine();

        out.println("look"); // debug mode is on
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("1 1          ", in.readLine());
        assertEquals("- 1          ", in.readLine());
        in.readLine();

        out.println("bye");
        socket.close();
    }
}
