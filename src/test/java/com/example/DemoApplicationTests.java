package com.example;

import com.example.service.UserRestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
public class DemoApplicationTests {

    @Mock
    private UserRestService user;

    @Autowired
    protected WebApplicationContext cntx;
    
    private MockMvc mock;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mock = MockMvcBuilders.webAppContextSetup(cntx).build();
    }

	@Test
	public void testUserService() throws Exception {
       mock.perform(get("/user/1"))
               .andExpect(jsonPath("$.name", is("morad")))
               .andExpect(jsonPath("$.id", is(1)))
               .andDo(print());
	}

}

