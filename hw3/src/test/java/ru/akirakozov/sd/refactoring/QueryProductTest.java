package ru.akirakozov.sd.refactoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yaroslav Ilin
 */
public class QueryProductTest extends ProductTest {
    private Map<String, Integer> testProducts;

    @Before
    public void setUp() {
        testProducts = Map.of("test1", 1, "test2", 2, "test3", 3);
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
    }

    @Test
    public void queryMinTest() throws IOException, InterruptedException {
        Map.Entry<String, Integer> expectedMin = testProducts.entrySet().stream()
                .min(Comparator.comparingInt(Map.Entry<String, Integer>::getValue))
                .orElseThrow();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUrl() + "/query?command=min"))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        result = result.replace("<html><body>", "")
                .replace("</body></html>", "")
                .replace("<h1>Product with min price: </h1>", "")
                .replace(System.lineSeparator(), "");
        for (var testProduct : testProducts.entrySet()) {
            if (testProduct.getKey().equals(expectedMin.getKey()) &&
                    testProduct.getValue().equals(expectedMin.getValue())) {
                Assert.assertTrue(
                        "result doesn't contains product with name: " + testProduct.getKey(),
                        result.contains(testProduct.getKey())
                );
                Assert.assertTrue(
                        "result doesn't have price: " + testProduct.getValue(),
                        result.contains(String.valueOf(testProduct.getValue()))
                );
            } else {
                Assert.assertFalse(
                        "result contains unexpected product with name: " + testProduct.getKey(),
                        result.contains(testProduct.getKey())
                );
                Assert.assertFalse(
                        "result has unexpected price: " + testProduct.getValue(),
                        result.contains(String.valueOf(testProduct.getValue()))
                );
            }
        }
    }

    @Test
    public void queryMaxTest() throws IOException, InterruptedException {
        Map.Entry<String, Integer> expectedMax = testProducts.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry<String, Integer>::getValue))
                .orElseThrow();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUrl() + "/query?command=max"))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        result = result.replace("<html><body>", "")
                .replace("</body></html>", "")
                .replace("<h1>Product with max price: </h1>", "")
                .replace(System.lineSeparator(), "");
        for (var testProduct : testProducts.entrySet()) {
            if (testProduct.getKey().equals(expectedMax.getKey()) &&
                    testProduct.getValue().equals(expectedMax.getValue())) {
                Assert.assertTrue(
                        "result doesn't contains product with name: " + testProduct.getKey(),
                        result.contains(testProduct.getKey())
                );
                Assert.assertTrue(
                        "result doesn't have price: " + testProduct.getValue(),
                        result.contains(String.valueOf(testProduct.getValue()))
                );
            } else {
                Assert.assertFalse(
                        "result contains unexpected product with name: " + testProduct.getKey(),
                        result.contains(testProduct.getKey())
                );
                Assert.assertFalse(
                        "result has unexpected price: " + testProduct.getValue(),
                        result.contains(String.valueOf(testProduct.getValue()))
                );
            }
        }
    }

    @Test
    public void querySumTest() throws IOException, InterruptedException {
        int expectedSum = testProducts.values().stream().mapToInt(i -> i).sum();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUrl() + "/query?command=sum"))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        result = result.replace("<html><body>", "")
                .replace("</body></html>", "")
                .replace(System.lineSeparator(), "");
        Assert.assertTrue(
                "expected: " + expectedSum + " but found: " + result,
                result.contains(String.valueOf(expectedSum))
        );
    }

    @Test
    public void queryCountTest() throws IOException, InterruptedException {
        long expectedCount = testProducts.values().stream().mapToInt(i -> i).count();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUrl() + "/query?command=count"))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        result = result.replace("<html><body>", "")
                .replace("</body></html>", "")
                .replace(System.lineSeparator(), "");
        Assert.assertTrue(
                "expected: " + expectedCount + " but found: " + result,
                result.contains(String.valueOf(expectedCount))
        );
    }

    @Test
    public void unexpectedQueryTest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUrl() + "/query?command=unexpected"))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        result = result.replace(System.lineSeparator(), "");
        Assert.assertEquals("Unknown command: unexpected", result);
    }
}
