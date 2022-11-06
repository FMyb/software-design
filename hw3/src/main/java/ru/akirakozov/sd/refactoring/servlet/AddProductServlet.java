package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.ProductStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractProductServlet {
    private final ProductStorage productStorage;

    public AddProductServlet(ProductStorage productStorage) {
        this.productStorage = productStorage;
    }

    @Override
    protected boolean isResponseData() {
        return false;
    }

    @Override
    protected Processor getProcessor(HttpServletRequest request) {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        productStorage.create(new Product(name, price));
        return this::process;
    }

    private void process(HttpServletResponse response) throws IOException {
        response.getWriter().println("OK");
    }
}
