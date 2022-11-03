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

/**
 * @author Yaroslav Ilin
 */
public class AddProductTest extends ProductTest {
    @Test
    public void addProductTest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8081/add-product?name=test1&price=228")).build();
        HttpResponse<String> response =  httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assert.assertTrue(response.body().contains("OK"));

        try (PreparedStatement st = sqliteConnector.connect().prepareStatement("SELECT * FROM PRODUCT;")) {
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Assert.assertEquals("test1", rs.getString("name"));
                Assert.assertEquals(228, rs.getInt("price"));
            } else {
                Assert.fail("failed to get products from table");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
