package com.shorty.shorty.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.shorty.shorty.dto.request.RequestLogin;
import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.response.ResponseLogin;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.repository.UserRepository;
import com.shorty.shorty.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest
public class AdministrationControllerTests_requests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserRepository userRepository;
    @MockBean
    UserService userService;

    @Test
    public void register_test_requests_goodRequest() throws Exception {
        RequestRegister requestRegister = new RequestRegister("ime");

        ResponseRegister response = new ResponseRegister();
        response.setSuccess(true);
        response.setDescription(null);
        response.generatePassword();

        UserService userService = mock(UserService.class);
        when(userService.register(requestRegister, userRepository)).thenReturn(response);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/administration/register")
                .contentType(MediaType.APPLICATION_JSON).content(ow.writeValueAsString(requestRegister))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void register_test_requests_emptyRequest() throws Exception {
        RequestRegister requestRegister = null;

        ResponseRegister response = new ResponseRegister();

        UserService userService = mock(UserService.class);
        when(userService.register(requestRegister, userRepository)).thenReturn(response);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/administration/register")
                .contentType(MediaType.APPLICATION_JSON).content(ow.writeValueAsString(requestRegister))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }



    @Test
    public void login_test_requests_goodRequest() throws Exception {
        RequestLogin requestLogin = new RequestLogin("ime", "pass");

        ResponseLogin response = new ResponseLogin();
        response.setSuccess(true);

        UserService userService = mock(UserService.class);
        when(userService.login(requestLogin, userRepository)).thenReturn(response);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/administration/login")
                .contentType(MediaType.APPLICATION_JSON).content(ow.writeValueAsString(requestLogin))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void login_test_requests_emptyRequest() throws Exception {
        RequestLogin requestLogin = null;

        ResponseLogin response = new ResponseLogin();

        UserService userService = mock(UserService.class);
        when(userService.login(requestLogin, userRepository)).thenReturn(response);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/administration/login")
                .contentType(MediaType.APPLICATION_JSON).content(ow.writeValueAsString(requestLogin))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

}
