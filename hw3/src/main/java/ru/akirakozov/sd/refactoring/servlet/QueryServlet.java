package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.ProductStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractProductServlet {
    private final ProductStorage productStorage;

    public QueryServlet(ProductStorage productStorage) {
        this.productStorage = productStorage;
    }

    @Override
    protected boolean isResponseData() {
        return true;
    }

    @Override
    protected Processor getProcessor(HttpServletRequest request) {
        String command = request.getParameter("command");
        if ("max".equals(command)) {
            Product product = productStorage.max();
            return response -> processMaxProduct(response, product);
        } else if ("min".equals(command)) {
            Product product = productStorage.min();
            return response -> processMinProduct(response, product);
        } else if ("sum".equals(command)) {
            int sum = productStorage.sum();
            return response -> processSum(response, sum);
        } else if ("count".equals(command)) {
            int count = productStorage.count();
            return response -> processCount(response, count);
        }
        return null;
    }

    @Override
    protected void processBadRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("Unknown command: " + request.getParameter("command"));
    }

    private void processMaxProduct(HttpServletResponse response, Product product) throws IOException {
        response.getWriter().println("<h1>Product with max price: </h1>");
        writeProduct(response, product);
    }

    private void processMinProduct(HttpServletResponse response, Product product) throws IOException {
        response.getWriter().println("<h1>Product with min price: </h1>");
        writeProduct(response, product);
    }

    private void processSum(HttpServletResponse response, int sum) throws IOException {
        response.getWriter().println("Summary price: ");
        response.getWriter().println(sum);
    }

    private void processCount(HttpServletResponse response, int count) throws IOException {
        response.getWriter().println("Number of products: ");
        response.getWriter().println(count);
    }

}
