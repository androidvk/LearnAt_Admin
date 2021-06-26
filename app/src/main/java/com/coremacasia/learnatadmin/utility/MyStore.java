package com.coremacasia.learnatadmin.utility;

import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.all_courses.CourseModel;

public class MyStore {
    public static CommonDataModel getCommonData() {
        return commonData;
    }

    public static void setCommonData(CommonDataModel commonData) {
        MyStore.commonData = commonData;
    }

    public static CommonDataModel commonData;

    public static CourseModel getCourseData() {
        return courseData;
    }

    public static void setCourseData(CourseModel courseData) {
        MyStore.courseData = courseData;
    }

    public static CourseModel courseData;

}
