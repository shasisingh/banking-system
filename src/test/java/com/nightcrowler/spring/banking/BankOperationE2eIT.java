package com.nightcrowler.spring.banking;

import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.model.AccountOperationDto;
import com.nightcrowler.spring.banking.model.MoneyTransferDto;
import com.nightcrowler.spring.banking.repository.AccountRepository;
import com.nightcrowler.spring.banking.repository.CustomerRepository;
import com.nightcrowler.spring.banking.security.JwtGenerator;
import com.nightcrowler.spring.banking.utils.DBHelperUtil;
import com.nightcrowler.spring.banking.utils.TestDbContainerInitializer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankOperationE2eIT {

    @RegisterExtension
    static TestDbContainerInitializer testDbContainerInitializer = new TestDbContainerInitializer();

    @LocalServerPort
    int port;

    private static DBHelperUtil dbHelperUtil;

    private static JwtGenerator jwtGenerator;

    @BeforeAll
    public static void setUpAll(@Autowired CustomerRepository customerRepository, @Autowired AccountRepository accountRepository, @Autowired JwtGenerator jwtGenerator) {
        dbHelperUtil = new DBHelperUtil(customerRepository, accountRepository);
        BankOperationE2eIT.jwtGenerator = jwtGenerator;
    }

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    void testTransferMoneyApi() {

        var baseCustomer = dbHelperUtil.createCustomer("38Kw8B");
        var creditorAccount = dbHelperUtil.createNewAccount(baseCustomer, "NL0000000000000001");
        var debitorAccount = dbHelperUtil.createNewAccount(baseCustomer, "NL0000000000000002");

        var expectedBalance = creditorAccount.getBalance().add(new BigDecimal("100.01"));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("RaviDu")))
                .body(new MoneyTransferDto(new AccountDto(creditorAccount), new AccountDto(debitorAccount), new BigDecimal("100.01")))
                .when()
                .post("/api/v1/banking/transactions/accounts/transfer")
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("accountNumber", equalTo(creditorAccount.getAccountNumber()))
                .body("accountBalance", equalTo(expectedBalance.floatValue()));

        // check if the balance is updated via available balance
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("benjamintorres")))
                .body(new AccountDto(creditorAccount))
                .when()
                .get("/api/v1/banking/transactions/accounts/balance")
                .then().log().ifValidationFails()
                .statusCode(200)
                .body(equalTo(expectedBalance.toEngineeringString()));

        //check if the balance is updated via available balance for the second account
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("JanetMachado")))
                .body(new AccountDto(debitorAccount))
                .when()
                .get("/api/v1/banking/transactions/accounts/balance")
                .then().log().ifValidationFails()
                .statusCode(200)
                .body(equalTo(debitorAccount.getBalance().subtract(new BigDecimal("100.01")).toEngineeringString()));
    }

    @Test
    void testWithdrawApi() {
        var newCust = dbHelperUtil.createCustomer("McwcNHKy");
        var newAccount = dbHelperUtil.createNewAccount(newCust, "NL00000000000000X1");
        var expectedBalance = newAccount.getBalance().subtract(new BigDecimal("100.01"));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("ChaoPaswan")))
                .body(new AccountOperationDto(new AccountDto(newAccount), new BigDecimal("100.01")))
                .when()
                .post("/api/v1/banking/transactions/accounts/withdraw")
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("accountNumber", equalTo(newAccount.getAccountNumber()))
                .body("accountBalance", equalTo(expectedBalance.floatValue()));
    }

    @Test
    void testDepositApi() {
        var newCust = dbHelperUtil.createCustomer("wcZ7bCyg");
        var newAccount = dbHelperUtil.createNewAccount(newCust, "NL00000000000000X4");
        var expectedBalance = newAccount.getBalance().add(new BigDecimal("100.01"));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("JingjingAlam")))
                .body(new AccountOperationDto(new AccountDto(newAccount), new BigDecimal("100.01")))
                .when()
                .post("/api/v1/banking/transactions/accounts/deposit")
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("accountNumber", equalTo(newAccount.getAccountNumber()))
                .body("accountBalance", equalTo(expectedBalance.floatValue()));
    }

    @Test
    void testGetAccountBalanceApi() {
        var newCust = dbHelperUtil.createCustomer("HhfMFTjm");
        var newAccount = dbHelperUtil.createNewAccount(newCust, "NL00000000000000X5");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("DiMao")))
                .body(new AccountDto(newAccount))
                .when()
                .get("/api/v1/banking/transactions/accounts/balance")
                .then().log().ifValidationFails()
                .statusCode(200)
                .body(equalTo(newAccount.getBalance().setScale(2, RoundingMode.HALF_UP).toString()));

    }

    @Test
    @DisplayName("Test adding new customer")
    void testAddCustomer() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("RitaRojas")))
                .body("""
                         {
                                "firstName": "John",
                                "lastName": "Doe",
                                "email": "emailId@gmail.com",
                                "countryCode": "NL",
                                "siebelId": "000100101",
                                "accountType": "SAVINGS"
                            }
                        """)
                .when()
                .post("/api/v1/administration/customers")
                .then().log().ifValidationFails()
                .statusCode(201)
                .body("firstName", equalTo("John"))
                .body("lastName", equalTo("Doe"))
                .body("email", equalTo("emailId@gmail.com"))
                .body("countryCode", equalTo("NL"));
    }

    @Test
    @DisplayName("Test getting all customers")
    void testGetAllCustomers() {

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("superman")))
                .when()
                .get("/api/v1/administration/customers")
                .then().log().ifValidationFails()
                .statusCode(200);
    }

    @Test
    @DisplayName("Test getting customer by id")
    void testGetCustomerById() {
        var newCust = dbHelperUtil.createCustomer();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("xman")))
                .when()
                .get("/api/v1/administration/customers/%s".formatted(newCust.getSiebelId()))
                .then().log().ifValidationFails()
                .statusCode(200);
    }

    @Test
    @DisplayName("Test add new account")
    void testAddAccount() {
        var newCust = dbHelperUtil.createCustomer();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer %s".formatted(generateToken("earth-616")))
                .body("""
                         {
                                "accountType": "SAVINGS",
                                "accountBalance": 100.01,
                                "currencyCode": "EUR",
                                "accountNumber": "NL2000000000000001",
                                "relationNumber": "%s"
                            }
                        """.formatted(newCust.getSiebelId()))
                .when()
                .post("api/v1/administration/customers/%s/accounts".formatted(newCust.getSiebelId()))
                .then().log().ifValidationFails()
                .statusCode(201)
                .body("accountType", equalTo("SAVINGS"))
                .body("accountBalance", equalTo(100.01f))
                .body("accountNumber", equalTo("NL2000000000000001"));
    }

    private String generateToken(String username) {
        return jwtGenerator.generate(username,"admin");
    }
}
