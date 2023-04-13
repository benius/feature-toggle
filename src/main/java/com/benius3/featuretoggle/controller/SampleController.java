package com.benius3.featuretoggle.controller;

import com.benius3.featuretoggle.annotation.FeatureToggle;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <code>SampleController</code> is a sample controller for testing feature toggle.
 *
 * @author masonhsieh
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class SampleController {

    @PostMapping("")
    @FeatureToggle(featureName = "user.create")
    public String createUser() {
        return "User created";
    }

    @DeleteMapping("")
    @FeatureToggle(featureName = "user.delete")
    public String deleteUser() {
        return "User deleted";
    }
}

