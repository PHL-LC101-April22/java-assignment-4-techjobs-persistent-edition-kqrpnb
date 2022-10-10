-- Part 1: Test it with SQL
--'employer','varchar'
--'id','int'
--'name','varchar'
--'skills','varchar'

-- Part 2: Test it with SQL
select name from employer where location = "St. Louis City";

-- Part 3: Test it with SQL
drop table job;

-- Part 4: Test it with SQL

--select distinct name from skill
--left join job_skills on id = skills_id
--where id = skills_id
--order by name asc;

select * from skill
left join job_skills on skill.id = job_skills.skills_id
where job_skills.jobs_id is not null
order by name asc;