package HTTP;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WarehouseServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new WarehouseHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on port 8080...");
    }

    static class WarehouseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            String[] warehouses = requestPath.substring(1).split("_");

            StringBuilder response = new StringBuilder("<html><body><table border='1'>");

            for (String warehouse : warehouses) {
                int warehouseId = Integer.parseInt(warehouse);
                String inventoryData = loadInventoryData(warehouseId);

                response.append("<tr><td colspan='2'><b>Warehouse ").append(warehouseId).append("</b></td></tr>");
                response.append(inventoryData);
            }

            response.append("</table></body></html>");

            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.toString().getBytes());
            }
        }

        private String loadInventoryData(int warehouseId) {
            try {
                String filePath ="HTTP/" + warehouseId + ".txt";
                BufferedReader reader = new BufferedReader(new FileReader(filePath));

                StringBuilder inventoryData = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    inventoryData.append("<tr><td>").append(parts[0]).append("</td><td>").append(parts[1]).append("</td></tr>");
                }

                reader.close();
                return inventoryData.toString();
            } catch (IOException e) {
                return "<tr><td colspan='2'>Error loading inventory data for Warehouse " + warehouseId + "</td></tr>";
            }
        }
    }
}
