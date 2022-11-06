package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.ProductStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractProductServlet {
    private final ProductStorage productStorage;

    public GetProductsServlet(ProductStorage productStorage) {
        this.productStorage = productStorage;
    }

    @Override
    protected boolean isResponseData() {
        return true;
    }

    @Override
    protected Processor getProcessor(HttpServletRequest request) {
        List<Product> products = productStorage.list();
        return response -> process(response, products);
    }

    private void process(HttpServletResponse response, List<Product> products) throws IOException {
        for (Product product : products) {
            writeProduct(response, product);
        }
    }
}
