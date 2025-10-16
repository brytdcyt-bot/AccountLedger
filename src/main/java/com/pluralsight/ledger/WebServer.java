package com.pluralsight.ledger;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Minimal embedded HTTP server for serving static HTML ledger files.
 * Useful for local GUI testing (vendors.html, ledger.html, etc.).
 */
public class WebServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new StaticHandler("src/main/resources/web"));
        server.setExecutor(null);

        System.out.println("🌐 WebServer running on http://localhost:" + port);
        server.start();
    }

    // Simple inline handler to return static HTML content
    static class StaticHandler implements HttpHandler {
        private final String rootDir;

        StaticHandler(String rootDir) {
            this.rootDir = rootDir;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String uri = exchange.getRequestURI().getPath();
            if (uri.equals("/")) uri = "/index.html";

            java.nio.file.Path path = java.nio.file.Paths.get(rootDir + uri);
            if (!java.nio.file.Files.exists(path)) {
                String msg = "404 Not Found";
                exchange.sendResponseHeaders(404, msg.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(msg.getBytes());
                }
                return;
            }

            byte[] bytes = java.nio.file.Files.readAllBytes(path);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
