package com.xxl.job.executor.test;

import com.xxljob.javaapi.config.XxlJobProperties;
import net.bytebuddy.asm.Advice;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class JobInfoControllerTest extends AbstractSpringMvcTest {

  public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";
  private Cookie cookie;
  @Autowired
  XxlJobProperties properties;

  @Before
  public void login() throws Exception {
    MvcResult ret = mockMvc.perform(
        post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("userName",properties.getLogin().getUsername())
            .param("password",properties.getLogin().getPassword())
    ).andReturn();
    cookie = ret.getResponse().getCookie(LOGIN_IDENTITY_KEY);
  }

  @Test
  public void testAdd() throws Exception {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
    parameters.add("jobGroup", "1");

    MvcResult ret = mockMvc.perform(
        post("/jobinfo/pageList")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            //.content(paramsJson)
            .params(parameters)
            .cookie(cookie)
    ).andReturn();

    System.out.println(ret.getResponse().getContentAsString());
  }

}
