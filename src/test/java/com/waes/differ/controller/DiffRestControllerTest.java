package com.waes.differ.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.differ.exception.DataNotFoundException;
import com.waes.differ.exception.InvalidDiffDataException;
import com.waes.differ.model.Diff;
import com.waes.differ.model.DiffRequest;
import com.waes.differ.service.DiffService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DiffRestControllerTest {

    private static final String INVALID_BASE64 = "4rdHFh%2BHYoS8oLdVvbUzEVqB8Lvm7kSPnuwF0AAABYQ%3D";

	@MockBean
    private DiffService diffService;

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("PUT /v1/diff/1/left - Success")
    void testPutLeftSuccess() throws Exception {
    	
    	DiffRequest diffRequest = new DiffRequest();
    	diffRequest.setData("YWEzMzNiY2NjZHFxcXFx");
    	
    	// Execute the PUT request
    	mockMvc.perform(put("/v1/diff/{id}/left", 1)
    			.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(diffRequest)))

                // Validate the response code 
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("PUT /v1/diff/1/left - Bad_Request")
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
    void testGetDiffSuccess() throws Exception {
    	
		List<Diff> diffs = new ArrayList<Diff>();

		diffs.add(new Diff(0, 2));
		diffs.add(new Diff(5, 3));

		doReturn(diffs).when(diffService).retrieveDiff("1");
    	
    	// Execute the GET request
    	mockMvc.perform(get("/v1/diff/{id}", 1))

       		// Validate the response code and content type
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            
        	// Validate the returned fields
            .andExpect(jsonPath("$.diffs", hasSize(2)))
            .andExpect(jsonPath("$.diffs[0].offset", equalTo(0)))
            .andExpect(jsonPath("$.diffs[0].length", equalTo(2)))
            .andExpect(jsonPath("$.diffs[1].offset", equalTo(5)))
            .andExpect(jsonPath("$.diffs[1].length", equalTo(3)));
    }
    
    @Test
    @DisplayName("GET /v1/diff/1 - Success Equal")
    void testGetDiffSuccessEqual() throws Exception {
    	
    	doReturn(Collections.emptyList()).when(diffService).retrieveDiff("1");
    	
    	// Execute the GET request
    	mockMvc.perform(get("/v1/diff/{id}", 1))

       		// Validate the response code and content type
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            
        	// Validate the returned fields
            .andExpect(jsonPath("$.diffs", hasSize(0)));
    }
    
    @Test
    @DisplayName("GET /v1/diff/1 - Not_Found")
    void testGetDiffNotFound() throws Exception {
    	
    	doThrow(DataNotFoundException.class).when(diffService).retrieveDiff("1");
    	
    	// Execute the GET request
    	mockMvc.perform(get("/v1/diff/{id}", 1))

                // Validate the response code 
                .andExpect(status().isNotFound());
    }
    
    
    @Test
    @DisplayName("GET /v1/diff/1 - Bad_Request")
    void testGetDiffBadRequest() throws Exception {
    	
    	doThrow(InvalidDiffDataException.class).when(diffService).retrieveDiff("1");
    	
    	// Execute the GET request
    	mockMvc.perform(get("/v1/diff/{id}", 1))

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
