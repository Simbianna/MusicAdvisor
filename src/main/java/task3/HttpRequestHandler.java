package task3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HttpRequestHandler implements HttpHandler {
    CurrentUserParams params;

    public HttpRequestHandler(CurrentUserParams params) {
        this.params = params;
    }

    @Override
    public void handle(HttpExchange exchange){
        handleAuthorizationRequest(exchange);
        /*if ("GET".equals(exchange.getRequestMethod())) {
            handleGetRequest(exchange);
        }*/
    }

    private void handleGetRequest(HttpExchange exchange) {
        if (exchange.getHttpContext().getPath().equals("/"))
            handleAuthorizationRequest(exchange);
    }

    private void handleAuthorizationRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        String result;
        try {
            if (query == null || query.contains("error=")) {
                result = "Authorization code not found. Try again.";
                exchange.sendResponseHeaders(200, result.length());
                exchange.getResponseBody().write(result.getBytes());
                exchange.getResponseBody().close();
                exchange.close();
                params.setUserCode("-1");

            } else {
                result = "Got the code. Return back to your program.";
                exchange.sendResponseHeaders(200, result.length());
                exchange.getResponseBody().write(result.getBytes());
                params.setUserCode(query.replace("code=", ""));
                exchange.getResponseBody().close();
                exchange.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
