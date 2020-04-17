package com.tismenetski.ppmtool.services;

import com.tismenetski.ppmtool.domain.Backlog;
import com.tismenetski.ppmtool.domain.Project;
import com.tismenetski.ppmtool.domain.ProjectTask;
import com.tismenetski.ppmtool.exceptions.ProjectNotFoundException;
import com.tismenetski.ppmtool.repositories.BacklogRepository;
import com.tismenetski.ppmtool.repositories.ProjectRepository;
import com.tismenetski.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {


    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;


    public ProjectTask addProjectTask(String projectIdentifier , ProjectTask projectTask,String username)
    {
            //Exceptions: Project not found
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier,username).getBacklog(); //get the backlog from the database using the given projectIdentifier->extracted via the project service since project service contains the error handling logic for exceptions

            projectTask.setBacklog(backlog); //set the backlog the the project task
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            //Add sequence to project task
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //Check if projectTask status is blank or it's null
            if (projectTask.getStatus()=="" || projectTask.getStatus()==null)
            {
            projectTask.setStatus("TO_DO"); //Set Status to TO_DO
            }
            //Check if projectTask Priority is blank or it's null
            if (projectTask.getPriority()==null || projectTask.getPriority()==0)
            {
                projectTask.setPriority(3); //Set Priority to 3
            }

            return projectTaskRepository.save(projectTask);

        //PTs to be added to a specific project, project !=null , BL exists
        //set the backlog to the project task
        //we want our project sequence to be like this : IDPRO-1 IDPRO-2 ....
        //Update the BL sequence
        //INITIAL priority when priority is null
        //INITIAL status when status is null
    }

    //
    public Iterable<ProjectTask> findBacklogById(String id,String username) {

        projectService.findProjectByIdentifier(id,username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);

    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username)
    {
        //make sure we are searching on the right backlog
        projectService.findProjectByIdentifier(backlog_id,username);

        //make sure our task exist
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null) throw  new ProjectNotFoundException("Project Task  "+ pt_id +" Not Found");

        //make sure that backlog/project id in the path corresponds to the right project
        if (!projectTask.getProjectIdentifier().equals(backlog_id))
            throw new ProjectNotFoundException("Project Task "+ pt_id+ " does not exist in project: "+ backlog_id);

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id,String pt_id,String username)
    {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id,username);

        //if (projectTask == null) throw new ProjectNotFoundException("")
        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id,String pt_id,String username)
    {
        ProjectTask projectTask  = findPTByProjectSequence(backlog_id,pt_id,username);
        projectTaskRepository.delete(projectTask);
    }
}
