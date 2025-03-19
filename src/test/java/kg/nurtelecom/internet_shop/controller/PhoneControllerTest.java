package kg.nurtelecom.internet_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kg.nurtelecom.internet_shop.payload.request.PhoneRequest;
import kg.nurtelecom.internet_shop.payload.response.PhoneDetailResponse;
import kg.nurtelecom.internet_shop.payload.response.PhoneResponse;
import kg.nurtelecom.internet_shop.service.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PhoneControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private PhoneController phoneController;

    private ObjectMapper objectMapper;
    private UUID testId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(phoneController).build();
        objectMapper = new ObjectMapper();
        testId = UUID.randomUUID();
    }

    @Test
    void testAddPhone() throws Exception {
        PhoneRequest request = createSamplePhoneRequest();

        when(phoneService.add(any(PhoneRequest.class))).thenReturn(testId);

        mockMvc.perform(post("/api/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json("\"" + testId.toString() + "\""));

        verify(phoneService).add(any(PhoneRequest.class));
    }

    @Test
    void testUpdatePhoneSuccess() throws Exception {
        PhoneRequest request = createSamplePhoneRequest();

        when(phoneService.update(eq(testId), any(PhoneRequest.class))).thenReturn(true);

        mockMvc.perform(put("/api/phone/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Phone updated successfully!"));

        verify(phoneService).update(eq(testId), any(PhoneRequest.class));
    }

    @Test
    void testUpdatePhoneNotFound() throws Exception {
        PhoneRequest request = createSamplePhoneRequest();

        when(phoneService.update(eq(testId), any(PhoneRequest.class))).thenReturn(false);

        mockMvc.perform(put("/api/phone/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Phone with this ID not found."));

        verify(phoneService).update(eq(testId), any(PhoneRequest.class));
    }

    @Test
    void testDeletePhoneSuccess() throws Exception {
        when(phoneService.delete(testId)).thenReturn(true);

        mockMvc.perform(delete("/api/phone/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(content().string("Phone deleted successfully!"));

        verify(phoneService).delete(testId);
    }

    @Test
    void testDeletePhoneNotFound() throws Exception {
        when(phoneService.delete(testId)).thenReturn(false);

        mockMvc.perform(delete("/api/phone/{id}", testId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Phone with this ID not found."));

        verify(phoneService).delete(testId);
    }

    @Test
    void testGetAllPhones() throws Exception {
        List<PhoneResponse> phones = Arrays.asList(
                createSamplePhoneResponse(),
                createSamplePhoneResponse()
        );

        when(phoneService.findAll()).thenReturn(phones);

        mockMvc.perform(get("/api/phone"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(phoneService).findAll();
    }

    @Test
    void testGetPhoneByIdSuccess() throws Exception {
        PhoneDetailResponse phone = createSamplePhoneDetailResponse();

        when(phoneService.getById(testId)).thenReturn(Optional.of(phone));

        mockMvc.perform(get("/api/phone/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(phoneService).getById(testId);
    }

    @Test
    void testGetPhoneByIdNotFound() throws Exception {
        when(phoneService.getById(testId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/phone/{id}", testId))
                .andExpect(status().isNotFound());

        verify(phoneService).getById(testId);
    }

    // Вспомогательные методы для создания тестовых данных
    private PhoneRequest createSamplePhoneRequest() {
        return new PhoneRequest(
                "Smartphone X",
                "Latest smartphone with advanced features",
                799
        );
    }

    private PhoneResponse createSamplePhoneResponse() {
        return new PhoneResponse(
                "Smartphone X",
                799
        );
    }

    private PhoneDetailResponse createSamplePhoneDetailResponse() {
        return new PhoneDetailResponse(
                "Smartphone X",
                "Latest smartphone with advanced features",
                799
        );
    }
}