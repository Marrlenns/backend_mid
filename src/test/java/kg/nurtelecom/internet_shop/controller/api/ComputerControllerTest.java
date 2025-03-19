package kg.nurtelecom.internet_shop.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kg.nurtelecom.internet_shop.payload.request.ComputerRequest;
import kg.nurtelecom.internet_shop.payload.response.ComputerDetailResponse;
import kg.nurtelecom.internet_shop.payload.response.ComputerResponse;
import kg.nurtelecom.internet_shop.service.ComputerService;
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

class ComputerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ComputerService computerService;

    @InjectMocks
    private ComputerController computerController;

    private ObjectMapper objectMapper;
    private UUID testId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(computerController).build();
        objectMapper = new ObjectMapper();
        testId = UUID.randomUUID();
    }

    @Test
    void testAddComputer() throws Exception {
        ComputerRequest request = createSampleComputerRequest();

        when(computerService.add(any(ComputerRequest.class))).thenReturn(testId);

        mockMvc.perform(post("/api/computer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json("\"" + testId.toString() + "\""));

        verify(computerService).add(any(ComputerRequest.class));
    }

    @Test
    void testUpdateComputerSuccess() throws Exception {
        ComputerRequest request = createSampleComputerRequest();

        doNothing().when(computerService).update(eq(testId), any(ComputerRequest.class));

        mockMvc.perform(put("/api/computer/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Computer updated successfully!"));

        verify(computerService).update(eq(testId), any(ComputerRequest.class));
    }

    @Test
    void testUpdateComputerNotFound() throws Exception {
        ComputerRequest request = createSampleComputerRequest();

        doThrow(new RuntimeException("Computer not found")).when(computerService).update(eq(testId), any(ComputerRequest.class));

        mockMvc.perform(put("/api/computer/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Computer with this ID not found!"));

        verify(computerService).update(eq(testId), any(ComputerRequest.class));
    }

    @Test
    void testDeleteComputerSuccess() throws Exception {
        when(computerService.delete(testId)).thenReturn(true);

        mockMvc.perform(delete("/api/computer/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(content().string("Computer deleted successfully!"));

        verify(computerService).delete(testId);
    }

    @Test
    void testDeleteComputerNotFound() throws Exception {
        when(computerService.delete(testId)).thenReturn(false);

        mockMvc.perform(delete("/api/computer/{id}", testId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Computer with this ID not found!"));

        verify(computerService).delete(testId);
    }

    @Test
    void testGetAllComputers() throws Exception {
        List<ComputerResponse> computers = Arrays.asList(
                createSampleComputerResponse(),
                createSampleComputerResponse()
        );

        when(computerService.findAll()).thenReturn(computers);

        mockMvc.perform(get("/api/computer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(computerService).findAll();
    }

    @Test
    void testGetComputerByIdSuccess() throws Exception {
        ComputerDetailResponse computer = createSampleComputerDetailResponse();

        when(computerService.getById(testId)).thenReturn(Optional.of(computer));

        mockMvc.perform(get("/api/computer/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(computerService).getById(testId);
    }

    @Test
    void testGetComputerByIdNotFound() throws Exception {
        when(computerService.getById(testId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/computer/{id}", testId))
                .andExpect(status().isNotFound());

        verify(computerService).getById(testId);
    }


    private ComputerRequest createSampleComputerRequest() {
        return new ComputerRequest(
                "Gaming PC",
                "Powerful gaming computer with latest components",
                1299
        );
    }

    private ComputerResponse createSampleComputerResponse() {
        return new ComputerResponse(
                UUID.randomUUID(),
                "Gaming PC",
                1299
        );
    }

    private ComputerDetailResponse createSampleComputerDetailResponse() {
        return new ComputerDetailResponse(
                UUID.randomUUID(),
                "Gaming PC",
                "Powerful gaming computer with latest components",
                1299
        );
    }
}