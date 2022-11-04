package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Yaroslav Ilin
 */
public abstract class AbstractProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Processor processor = getProcessor(request);
        if (processor == null) {
            processBadRequest(request, response);
        } else {
            if (isResponseData()) {
                response.getWriter().println("<html><body>");
                processor.process(response);
                response.getWriter().println("</body></html>");
            } else {
                processor.process(response);
            }
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected abstract boolean isResponseData();

    protected abstract Processor getProcessor(HttpServletRequest request);

    protected void processBadRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    protected void writeProduct(HttpServletResponse response, Product product) throws IOException {
        response.getWriter().println(product.name() + "\t" + product.price() + "</br>");
    }

    @FunctionalInterface
    protected interface Processor {
        void process(HttpServletResponse response) throws IOException;
    }
}
