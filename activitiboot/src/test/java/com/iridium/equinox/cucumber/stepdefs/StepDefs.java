package com.iridium.equinox.cucumber.stepdefs;

import com.iridium.equinox.ActivitibootApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ActivitibootApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
