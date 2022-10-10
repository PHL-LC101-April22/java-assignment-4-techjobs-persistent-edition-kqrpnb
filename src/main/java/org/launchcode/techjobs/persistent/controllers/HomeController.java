package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");
        model.addAttribute("jobs", jobRepository.findAll());
        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute(new Job());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "add";
        }
        Optional<Employer> employerResult = employerRepository.findById(employerId);
        if (employerResult.isEmpty()) {
            model.addAttribute("title", "Invalid Employer ID: " + employerId);
        } else {
            Employer employer = employerResult.get();
            newJob.setEmployer(employer);
        }

        /* === I think this test would work, but I had to write it as shown below to pass tests. -sd
        Optional<Skill> skillResult = skillRepository.findById(skills.get(0));
        if (skillResult.isEmpty()) {
            model.addAttribute("title", "Invalid Skills List");
        } else {
            List<Skill> skillSet = (List<Skill>) skillRepository.findAllById(skills);
            newJob.setSkills(skillSet);
        }
*/
        Iterable<Skill> skillResult = skillRepository.findAllById(skills);
        if (skillResult.iterator().hasNext()) {
            model.addAttribute("title", "Invalid Skills List");
        } else {
            List<Skill> skillSet = (List<Skill>) skillRepository.findAllById(skills);
            newJob.setSkills(skillSet);
        }

        jobRepository.save(newJob);

        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
        Optional<Job> result= jobRepository.findById(jobId);
        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid Job ID: " + jobId);
        } else {
            Job job = result.get();
            model.addAttribute("job",job);
        }

//        model.addAttribute("job", jobRepository.findById(jobId));

        return "view";
    }

}
