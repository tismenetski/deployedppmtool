package com.tismenetski.ppmtool.services;

import com.tismenetski.ppmtool.domain.Backlog;
import com.tismenetski.ppmtool.domain.Project;
import com.tismenetski.ppmtool.domain.User;
import com.tismenetski.ppmtool.exceptions.ProjectIdException;
import com.tismenetski.ppmtool.exceptions.ProjectNotFoundException;
import com.tismenetski.ppmtool.repositories.BacklogRepository;
import com.tismenetski.ppmtool.repositories.ProjectRepository;
import com.tismenetski.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username)
    {

        if (project.getId()!=null)
        {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            if (existingProject!=null&&(!existingProject.getProjectLeader().equals(username))) //Project exists and project leader equals to the principal username passed from the controller
            {
                throw  new ProjectNotFoundException("Project not found in your account");
            }
            else if (existingProject==null)
            {
             throw new ProjectNotFoundException("Project with ID: "+ project.getProjectIdentifier() + " Cannot be updated because it doesn't exist");
            }
        }

        try
        {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId()==null)
            {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if (project.getId()!=null)
            {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }
        catch (Exception e)
        {
            throw new ProjectIdException("Project ID "+ project.getProjectIdentifier().toUpperCase()+" Already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) throw new ProjectIdException("Project ID  Doesn't exist");
        if (!project.getProjectLeader().equals(username)) throw  new ProjectNotFoundException("Project not found in your account");
        return project;
    }

    public Iterable<Project> findAllProjects(String username)
    {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectById(String projectId, String username)
    {
        projectRepository.delete(findProjectByIdentifier(projectId , username));
    }
}
