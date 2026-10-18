package com.fintrack.backend;

import com.fintrack.backend.config.SecurityConfig;
import com.fintrack.backend.controller.TransactionController;
import com.fintrack.backend.dto.TransactionRequest;
import com.fintrack.backend.dto.TransactionResponse;
import com.fintrack.backend.entity.TransactionType;
import com.fintrack.backend.exception.GlobalExceptionHandler;
import com.fintrack.backend.exception.TransactionNotFoundException;
import com.fintrack.backend.security.FirebaseTokenVerifier;
import com.fintrack.backend.service.TransactionService;
import com.google.firebase.auth.FirebaseToken;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Slice tests for {@link TransactionController}.
 *
 * <p>Uses {@code @WebMvcTest} so no database connection is required.
 * {@link TransactionService} is mocked at the bean level. Firebase token
 * verification is mocked via {@link FirebaseTokenVerifier} so no live
 * Firebase credentials are needed.
 *
 * <p>Authentication is established by stubbing {@link FirebaseTokenVerifier}
 * to return a mock {@link FirebaseToken}, which causes
 * {@link com.fintrack.backend.security.FirebaseTokenFilter} to populate the
 * security context with a {@link FirebasePrincipal}. This is exactly the
 * production code path — no security shortcuts are taken.
 */
@WebMvcTest(TransactionController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class TransactionControllerTest {

    private static final String OWNER_UID = "firebase-uid-alice";
    private static final String OTHER_UID = "firebase-uid-bob";
    private static final String VALID_TOKEN = "valid.firebase.token";

    private static final String VALID_REQUEST_JSON = """
            {
              "type": "EXPENSE",
              "amount": "45.00",
              "description": "Grocery shopping",
              "category": "Food",
              "transactionDate": "2026-10-18"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FirebaseTokenVerifier tokenVerifier;

    @MockitoBean
    private TransactionService transactionService;

    private TransactionResponse sampleResponse;

    @BeforeEach
    void setUp() throws Exception {
        // Stub tokenVerifier so the Firebase filter sets up a real FirebasePrincipal
        // in the security context — this follows the exact production code path.
        FirebaseToken mockToken = mock(FirebaseToken.class);
        when(mockToken.getUid()).thenReturn(OWNER_UID);
        when(mockToken.getEmail()).thenReturn("alice@example.com");
        when(tokenVerifier.verify(eq(VALID_TOKEN))).thenReturn(mockToken);

        sampleResponse = new TransactionResponse(
                1L,
                OWNER_UID,
                TransactionType.EXPENSE,
                new BigDecimal("45.00"),
                "Grocery shopping",
                "Food",
                LocalDate.of(2026, 10, 18),
                Instant.parse("2026-10-18T15:00:00Z")
        );
    }

    // ── POST /api/v1/transactions ─────────────────────────────────────────────

    @Test
    void createTransaction_returns201WithBody_whenValidRequest() throws Exception {
        when(transactionService.create(eq(OWNER_UID), any(TransactionRequest.class)))
                .thenReturn(sampleResponse);

        mockMvc.perform(post("/api/v1/transactions")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ownerUid").value(OWNER_UID))
                .andExpect(jsonPath("$.type").value("EXPENSE"))
                .andExpect(jsonPath("$.amount").value(45.00))
                .andExpect(jsonPath("$.description").value("Grocery shopping"))
                .andExpect(jsonPath("$.category").value("Food"));
    }

    @Test
    @WithAnonymousUser
    void createTransaction_returns401_whenUnauthenticated() throws Exception {
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTransaction_returns400_whenMissingRequiredFields() throws Exception {
        String badRequest = """
                {
                  "type": "EXPENSE"
                }
                """;

        mockMvc.perform(post("/api/v1/transactions")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void createTransaction_returns400_whenAmountIsZero() throws Exception {
        String badRequest = """
                {
                  "type": "INCOME",
                  "amount": "0.00",
                  "description": "Test",
                  "category": "Other",
                  "transactionDate": "2026-10-18"
                }
                """;

        mockMvc.perform(post("/api/v1/transactions")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void createTransaction_returns400_whenAmountIsNegative() throws Exception {
        String badRequest = """
                {
                  "type": "INCOME",
                  "amount": "-10.00",
                  "description": "Test",
                  "category": "Other",
                  "transactionDate": "2026-10-18"
                }
                """;

        mockMvc.perform(post("/api/v1/transactions")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badRequest))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/v1/transactions ──────────────────────────────────────────────

    @Test
    void listTransactions_returns200WithUserTransactions() throws Exception {
        when(transactionService.listForUser(OWNER_UID))
                .thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/v1/transactions")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ownerUid").value(OWNER_UID));
    }

    @Test
    void listTransactions_returnsEmptyArray_whenUserHasNone() throws Exception {
        when(transactionService.listForUser(OWNER_UID))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/transactions")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithAnonymousUser
    void listTransactions_returns401_whenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // ── GET /api/v1/transactions/{id} ─────────────────────────────────────────

    @Test
    void getTransaction_returns200_whenOwnerRequests() throws Exception {
        when(transactionService.getForUser(1L, OWNER_UID))
                .thenReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/transactions/1")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getTransaction_returns404_whenNotFoundOrWrongOwner() throws Exception {
        when(transactionService.getForUser(99L, OWNER_UID))
                .thenThrow(new TransactionNotFoundException(99L));

        mockMvc.perform(get("/api/v1/transactions/99")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    // ── PUT /api/v1/transactions/{id} ─────────────────────────────────────────

    @Test
    void updateTransaction_returns200_whenOwnerUpdates() throws Exception {
        TransactionResponse updated = new TransactionResponse(
                1L, OWNER_UID, TransactionType.INCOME,
                new BigDecimal("100.00"), "Updated desc", "Salary",
                LocalDate.of(2026, 10, 18), Instant.parse("2026-10-18T15:00:00Z")
        );
        when(transactionService.update(eq(1L), eq(OWNER_UID), any(TransactionRequest.class)))
                .thenReturn(updated);

        String updateJson = """
                {
                  "type": "INCOME",
                  "amount": "100.00",
                  "description": "Updated desc",
                  "category": "Salary",
                  "transactionDate": "2026-10-18"
                }
                """;

        mockMvc.perform(put("/api/v1/transactions/1")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("INCOME"))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void updateTransaction_returns404_whenWrongOwner() throws Exception {
        when(transactionService.update(eq(1L), eq(OWNER_UID), any(TransactionRequest.class)))
                .thenThrow(new TransactionNotFoundException(1L));

        mockMvc.perform(put("/api/v1/transactions/1")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST_JSON))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /api/v1/transactions/{id} ──────────────────────────────────────

    @Test
    void deleteTransaction_returns204_whenOwnerDeletes() throws Exception {
        mockMvc.perform(delete("/api/v1/transactions/1")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTransaction_returns404_whenNotFoundOrWrongOwner() throws Exception {
        doThrow(new TransactionNotFoundException(99L))
                .when(transactionService).delete(99L, OWNER_UID);

        mockMvc.perform(delete("/api/v1/transactions/99")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void deleteTransaction_returns401_whenUnauthenticated() throws Exception {
        mockMvc.perform(delete("/api/v1/transactions/1"))
                .andExpect(status().isUnauthorized());
    }
}
