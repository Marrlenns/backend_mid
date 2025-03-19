package kg.nurtelecom.internet_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kg.nurtelecom.internet_shop.payload.request.CartItemRequest;
import kg.nurtelecom.internet_shop.payload.request.UpdateQuantityRequest;
import kg.nurtelecom.internet_shop.payload.response.CartItemResponse;
import kg.nurtelecom.internet_shop.service.CartService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private ObjectMapper objectMapper;
    private UUID cartItemId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();
        cartItemId = UUID.randomUUID();
    }

    @Test
    void testAddCartItem() throws Exception {
        CartItemRequest request = new CartItemRequest(cartItemId, 1);
        when(cartService.addToCart(any(CartItemRequest.class))).thenReturn(cartItemId);

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json("\"" + cartItemId.toString() + "\""));

        verify(cartService).addToCart(any(CartItemRequest.class));
    }

    @Test
    void testGetCartItems() throws Exception {
        List<CartItemResponse> items = Arrays.asList(
                new CartItemResponse("Gaming PC", 1299, 1, 1299),
                new CartItemResponse("Workstation", 2499, 2, 4998)
        );

        when(cartService.getCartItems(cartItemId)).thenReturn(items);

        mockMvc.perform(get("/api/cart/items/{userId}", cartItemId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(cartService).getCartItems(cartItemId);
    }

    @Test
    void testDeleteCartItem() throws Exception {
        doNothing().when(cartService).deleteItemFromCart(cartItemId, cartItemId);

        mockMvc.perform(delete("/api/cart/items/{userId}/{cartItemId}", cartItemId, cartItemId))
                .andExpect(status().isOk())
                .andExpect(content().string("CartItem deleted successfully"));

        verify(cartService).deleteItemFromCart(cartItemId, cartItemId);
    }

    @Test
    void testUpdateItemQuantity() throws Exception {
        UpdateQuantityRequest request = new UpdateQuantityRequest(5);
        doNothing().when(cartService).updateItemQuantity(cartItemId, cartItemId, 5);

        mockMvc.perform(put("/api/cart/items/{userId}/{cartItemId}", cartItemId, cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Quantity updated successfully"));

        verify(cartService).updateItemQuantity(cartItemId, cartItemId, 5);
    }
}
