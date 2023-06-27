package com.zemoso.seeder.controller;

import com.google.gson.Gson;
import com.zemoso.seeder.service.CashKickService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.zemoso.seeder.util.TestDataFactory.*;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest(controllers = CashKickController.class)
@ActiveProfiles("test")
class CashKickControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CashKickService cashKickService;

    Gson gson = new Gson();

    @Test
    void testGetUserCashKicks() throws Exception {
        BDDMockito.given(cashKickService.getCashKicks(1L)).willReturn(List.of(getCashKickDto()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/cash-kicks/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("cash kick-1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is("Pending")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].maturityDate", is("Dec 01, 2023")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalReceived", is("200000.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalFinanced", is("180000.00")));
    }

    @Test
    void testCreateCashKick() throws Exception {

        BDDMockito.given(cashKickService.createCashKick(getCashKickRequest())).willReturn(getSummary());

        String cashKickRequestJsonString = gson.toJson(getCashKickRequest());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/cash-kicks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cashKickRequestJsonString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.term", is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selectedContractCount", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPayableAmount", is("132000.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPayout", is("116160.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fee", is("12.00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalFeeAmount", is("15840.00")));
    }
}
