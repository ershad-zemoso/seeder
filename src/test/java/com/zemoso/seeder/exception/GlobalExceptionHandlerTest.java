package com.zemoso.seeder.exception;

import com.zemoso.seeder.controller.ContractController;
import com.zemoso.seeder.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    MockMvc mockMvc;

    @Mock
    private ContractService contractService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ContractController(contractService))
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void testNotFound() throws Exception {
        Mockito.when(contractService.getAllUserContracts(12345L)).thenThrow(new NotFoundException("No contracts found for User Id: 12345"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/contracts/12345"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is("No contracts found for User Id: 12345")));
    }

    @Test
    void testInternalServerError() throws Exception {
        Mockito.when(contractService.getAllUserContracts(12345L)).thenThrow(new NullPointerException("Some NPE"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/contracts/12345"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
