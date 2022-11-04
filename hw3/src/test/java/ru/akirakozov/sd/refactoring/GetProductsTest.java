package ru.akirakozov.sd.refactoring;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yaroslav Ilin
 */
public class GetProductsTest extends ProductTest {
    @Test
    public void getEmptyTest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8081/get-products")).build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        Assert.assertEquals(
                "<html><body>" + System.lineSeparator() + "</body></html>" + System.lineSeparator(),
                result
        );
    }

    @Test
    public void getProductsTest() throws IOException, InterruptedException {
        Map<String, Integer> testProducts = Map.of("test1", 1, "test2", 2, "test3", 3);
        try (PreparedStatement st = sqliteConnector.connect().prepareStatement(
                "INSERT INTO " +
                        "PRODUCT (NAME, PRICE) " +
                        "VALUES " +
                        "(?, ?), " +
                        "(?, ?), " +
                        "(?, ?) " +
                        ";")
        ) {
            int index = 0;
            for (var product : testProducts.entrySet()) {
                st.setString(++index, product.getKey());
                st.setInt(++index, product.getValue());
            }
            int cnt = st.executeUpdate();
            Assert.assertEquals(testProducts.size(), cnt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8081/get-products")).build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        result = result.replace("<html><body>", "")
                .replace("</body></html>", "")
                .replace(System.lineSeparator(), "");
        Set<String[]> products = Arrays.stream(result.split("</br>"))
                .map(it -> it.split("\t"))
                .collect(Collectors.toSet());
        for (var product : products) {
            Assert.assertTrue(testProducts.containsKey(product[0]));
            Assert.assertEquals(String.valueOf(testProducts.get(product[0])), product[1]);
        }
    }
}
