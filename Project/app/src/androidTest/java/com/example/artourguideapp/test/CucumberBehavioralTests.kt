package com.example.artourguideapp.test

import cucumber.api.CucumberOptions
@CucumberOptions(features = ["features"],
    glue = ["com.example.artourguideapp.cucumber.steps"],
    tags = ["@e2e", "@smoke"])
@SuppressWarnings("unused")

class CucumberBehavioralTests