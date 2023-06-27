package com.zemoso.seeder.controller;

import com.zemoso.seeder.service.ContractService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.zemoso.seeder.util.TestDataFactory.getContractDto;
import static com.zemoso.seeder.util.TestDataFactory.getSummary;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest(controllers = ContractController.class)
@ActiveProfiles("test")
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    @Test
    void testGetUserContracts() throws Exception {
        BDDMockito.given(contractService.getAllUserContracts(1L)).willReturn(List.of(getContractDto()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/contracts/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Contract-1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].perPayment", is("12000.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].payment", is("126720.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].termLength", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fee", is("12.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type", is("Monthly")));
    }

    @Test
    void testGetContractSummary() throws Exception {
        BDDMockito.given(contractService.getContractsSummary(List.of(1L, 2L))).willReturn(getSummary());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/contracts/summary?id=1&id=2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.term", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selectedContractCount", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPayableAmount", is("132000.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPayout", is("116160.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fee", is("12.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFeeAmount", is("15840.00")));
    }
}
