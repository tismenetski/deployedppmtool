package com.tismenetski.ppmtool.web;


import com.tismenetski.ppmtool.domain.Project;
import com.tismenetski.ppmtool.services.MapValidationErrorService;
import com.tismenetski.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    //Create a new Project
    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project , BindingResult result, Principal principal) //BindingResult is an interface that analyzes an object principal = the user in the claims that have the token
    {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap!=null) return errorMap; // Returns the error map with a BAD_REQUEST Status

        Project project1 =  projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId,Principal principal)
    {
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<Project>(project,HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal)
    {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal)
    {
        projectService.deleteProjectById(projectId, principal.getName());
        return new ResponseEntity<String>("Project with ID: " + projectId + "Was Deleted",HttpStatus.OK);
    }
    /*
    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable String projectId)
    {
        Project project = projectService.findProjectByIdentifier(projectId);
        projectService.saveOrUpdateProject(project);
        return new ResponseEntity<String>("Project with ID: " + projectId + "Was Updated",HttpStatus.OK);
    }
    */
}


/*
ResponseEntity
ResponseEntity represents an HTTP response,
 including headers, body, and status. While @ResponseBody
  puts the return value into the body of the response,
   ResponseEntity also allows us to add headers and status code.
 */