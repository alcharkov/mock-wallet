package lt.alcharkov;

import static org.junit.Assert.*;

import com.despegar.http.client.HttpResponse;
import com.despegar.sparkjava.test.SparkServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.alcharkov.model.impl.Account;
import lt.alcharkov.model.Model;
import lt.alcharkov.model.impl.Transaction;
import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

public class MainTest {

    private static ObjectMapper mapper = new ObjectMapper();

    public static class TestSparkApplication implements SparkApplication {
        @Override
        public void init() {
            MainKt.main();
        }
    }

    @ClassRule
    public static SparkServer<TestSparkApplication> testServer = new SparkServer<>(MainTest.TestSparkApplication.class, 4567);

    @Test
    public void test() throws Exception {
        HttpResponse httpResponse = getHttpGetResponse("/ping");
        assertEquals(200, httpResponse.code());
        assertEquals("pong", new String(httpResponse.body()));
        assertNotNull(testServer.getApplication());
    }


    @Test
    public void testAccount() throws Exception{
        HttpResponse createAccResponse = getHttpPostResponse("/api/account/add", new Account(0, 3000));
        assertEquals(200, createAccResponse.code());
        int id = Integer.parseInt(new String(createAccResponse.body()));
        HttpResponse getAccResponse = getHttpGetResponse(String.format("/api/account/%d", id));
        assertEquals(200, getAccResponse.code());
        assertEquals(id, mapper.readValue(new String(getAccResponse.body()), Account.class).getId());
    }

    @Test
    public void testTransaction() throws Exception{
        int balance = 3000;
        HttpResponse createFromResponse = getHttpPostResponse("/api/account/add", new Account(0, balance));
        HttpResponse createToResponse = getHttpPostResponse("/api/account/add", new Account(0, 0));
        int fromId = Integer.parseInt(new String(createFromResponse.body()));
        int toId = Integer.parseInt(new String(createToResponse.body()));

        HttpResponse createTransactionResponse = getHttpPostResponse("/api/transaction/add",
                new Transaction(0, fromId, toId, balance));
        assertEquals(200, createTransactionResponse.code());

        int createTransactionResponseId = Integer.parseInt(new String(createTransactionResponse.body()));
        HttpResponse getTransactionResponse = getHttpGetResponse(String.format("/api/transaction/%d", createTransactionResponseId));
        int transactionFromId = mapper.readValue(new String(getTransactionResponse.body()), Transaction.class).getAfrom();
        int transactionToId = mapper.readValue(new String(getTransactionResponse.body()), Transaction.class).getAto();

        HttpResponse getFromResponse = getHttpGetResponse(String.format("/api/account/%d", transactionFromId));
        HttpResponse getToResponse = getHttpGetResponse(String.format("/api/account/%d", transactionToId));

        Account fromAccount =  mapper.readValue(new String(getFromResponse.body()), Account.class);
        assertEquals(0, fromAccount.getBalance());
        Account toAccount =  mapper.readValue(new String(getToResponse.body()), Account.class);
        assertEquals(balance, toAccount.getBalance());
    }

    private HttpResponse getHttpGetResponse(String path) throws com.despegar.http.client.HttpClientException {
        return testServer.execute(testServer.get(path, false));
    }

    @NotNull
    private HttpResponse getHttpPostResponse(String path, Model model) throws JsonProcessingException, com.despegar.http.client.HttpClientException {
        return testServer.execute(testServer.post(path, mapper.writeValueAsString(model), false));
    }
}
