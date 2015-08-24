package com.swiatowski.bitly.rest.controllers;

import com.google.common.collect.Lists;
import com.swiatowski.bitly.core.models.entities.Link;
import com.swiatowski.bitly.core.services.LinkService;
import com.swiatowski.bitly.core.services.exceptions.LinkExistsException;
import com.swiatowski.bitly.core.services.util.LinkList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Piotrek on 8/24/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class LinkControllerTest {
    @InjectMocks
    private LinkController controller;

    @Mock
    private LinkService service;

    private MockMvc mockMvc;

    private ArgumentCaptor<LinkList> linkArgumentCaptor;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        linkArgumentCaptor = ArgumentCaptor.forClass(LinkList.class);
    }

    @Test
    public void findAllLinks() throws Exception {
        Link createdLink = new Link();
        createdLink.setId(1L);
        createdLink.setUrl("url");
        createdLink.setEncodeUrl("encode");

        when(service.findAllLinks()).thenReturn(new LinkList(Lists.newArrayList(createdLink)));

        mockMvc.perform(get("/rest/link/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createLink() throws Exception {
        Link createdLink = new Link();
        createdLink.setId(1L);
        createdLink.setUrl("url");
        createdLink.setEncodeUrl("encode");

        when(service.createLink(any(String.class))).thenReturn(createdLink);

        mockMvc.perform(post("/rest/link")
                .content("http://www.google.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void createLinkExistingLink() throws Exception {
        Link createdLink = new Link();
        createdLink.setId(1L);
        createdLink.setUrl("url");
        createdLink.setEncodeUrl("encode");

        when(service.createLink(any(String.class))).thenThrow(new LinkExistsException());

        mockMvc.perform(post("/rest/link")
                .content("http://www.google.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

}