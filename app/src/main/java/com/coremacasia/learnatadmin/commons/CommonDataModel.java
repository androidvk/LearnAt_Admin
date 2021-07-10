package com.coremacasia.learnatadmin.commons;

import com.coremacasia.learnatadmin.menus.category.CategoryHelper;
import com.coremacasia.learnatadmin.menus.courses.CourseHelper;
import com.coremacasia.learnatadmin.menus.courses.Courses;
import com.coremacasia.learnatadmin.menus.mentors.DF_Add_Mentor;
import com.coremacasia.learnatadmin.menus.mentors.MentorHelper;
import com.coremacasia.learnatadmin.menus.subjects.SubjectHelper;

import java.util.ArrayList;

public class CommonDataModel {
    public CommonDataModel(){}

    public ArrayList<CategoryHelper> getCategory() {
        return category;
    }

    private ArrayList<CategoryHelper> category;

    private ArrayList<String> course_id;

    public ArrayList<String> getSubject_id() {
        return subject_id;
    }

    private ArrayList<String> subject_id;

    public ArrayList<String> getCourse_id() {
        return course_id;
    }

    public ArrayList<CourseHelper> getAll_courses() {
        return all_courses;
    }

    private ArrayList<CourseHelper> all_courses;

    private ArrayList<MentorHelper> mentors;

    private ArrayList<SubjectHelper> all_subjects;

    public ArrayList<SubjectHelper> getAll_subjects() {
        return all_subjects;
    }

    public ArrayList<MentorHelper> getMentors() {
        return mentors;
    }
}
