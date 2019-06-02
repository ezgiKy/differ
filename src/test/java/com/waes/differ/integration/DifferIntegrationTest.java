package com.waes.differ.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.waes.differ.model.DiffRequest;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DifferIntegrationTest {

    private static final String INVALID_BASE64 = "4rdHFh%2BHYoS8oLdVvbUzEVqB8Lvm7kSPnuwF0AAABYQ%3D";
	
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    public ConnectionHolder getConnectionHolder() {
        // retrieves a connection from data source
        return () -> dataSource.getConnection();
    }
    
    @Test
    @DisplayName("PUT /v1/diff/1/left - Success")
    @DataSet("diffs.yml")
    void testPutLeftSuccess() throws Exception {
    	
    	DiffRequest diffRequest = new DiffRequest();
    	diffRequest.setData("YWFhYmJiY2NjZGRkMjIy");
    	
    	// Execute the PUT request
    	mockMvc.perform(put("/v1/diff/{id}/left", 1)
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(diffRequest)))

                // Validate the response code 
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("PUT /v1/diff/1/left - Bad_Request")
    @DataSet("diffs.yml")
    void testPutLeftBadRequest() throws Exception {
    	
    	DiffRequest diffRequest = new DiffRequest();
    	diffRequest.setData(INVALID_BASE64);
    	
    	// Execute the PUT request
    	mockMvc.perform(put("/v1/diff/{id}/left", 1)
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(diffRequest)))

                // Validate the response code 
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("PUT /v1/diff/1/right - Success")
    @DataSet("diffs.yml")
    void testPutRightSuccess() throws Exception {
    	
    	DiffRequest diffRequest = new DiffRequest();
    	diffRequest.setData("YWEzMzNiY2NjZHFxcXFx");
    	
    	// Execute the PUT request
    	mockMvc.perform(put("/v1/diff/{id}/right", 1)
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(diffRequest)))

                // Validate the response code 
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("PUT /v1/diff/1/right - Bad_Request")
    @DataSet("diffs.yml")
    void testPutRightBadRequest() throws Exception {
    	
    	DiffRequest diffRequest = new DiffRequest();
    	diffRequest.setData(INVALID_BASE64);
    	
    	// Execute the PUT request
    	mockMvc.perform(put("/v1/diff/{id}/right", 1)
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(diffRequest)))

                // Validate the response code 
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("GET /v1/diff/1 - Success")
    @DataSet("diffs.yml")
    void getDiffSuccess() throws Exception {
    	
        // Execute the GET request
        mockMvc.perform(get("/v1/diff/{id}", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            	// Validate the returned fields
                .andExpect(jsonPath("$.diffs", hasSize(2)))
                .andExpect(jsonPath("$.diffs[0].offset", equalTo(2)))
                .andExpect(jsonPath("$.diffs[0].length", equalTo(3)))
                .andExpect(jsonPath("$.diffs[1].offset", equalTo(10)))
                .andExpect(jsonPath("$.diffs[1].length", equalTo(5)));
    }
    
    @Test
    @DisplayName("GET /v1/diff/3 - Success Equal")
    @DataSet("diffs.yml")
    void testGetDiffSuccessEqual() throws Exception {
    	
    	// Execute the GET request
    	mockMvc.perform(get("/v1/diff/{id}", 3))

       		// Validate the response code and content type
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            
        	// Validate the returned fields
            .andExpect(jsonPath("$.diffs", hasSize(0)));
    }
    
    @Test
    @DisplayName("GET /v1/diff/4 - Not_Found")
    @DataSet("diffs.yml")
    void testGetDiffNotFound() throws Exception {
    	
    	// Execute the GET request
    	mockMvc.perform(get("/v1/diff/{id}", 4))

                // Validate the response code 
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("GET /v1/diff/2 - Bad_Request")
    @DataSet("diffs.yml")
    void testGetDiffBadRequest() throws Exception {
    	
    	// Execute the GET request
    	mockMvc.perform(get("/v1/diff/{id}", 2))

                // Validate the response code 
                .andExpect(status().isBadRequest());
    }
    
	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
