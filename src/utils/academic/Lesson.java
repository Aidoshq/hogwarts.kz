package utils.academic;

import java.io.Serializable;

import enumerations.LessonType;
import users.Teacher;

/**
* @author kama
*/
public class Lesson implements Serializable {
    
    /**
    * Show if it is Lecture of Practice
    */
    private LessonType lessonType;
    
    /**
    * Instructor of the lesson
    */
    private Teacher instructor;

	public Lesson() {
		super();
	}
	public Lesson(LessonType lessonType, Teacher instructor) {
		this.lessonType=lessonType;
		this.instructor=instructor;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public Teacher getInstructor() {
		return instructor;
	}

	public void setInstructor(Teacher instructor) {
		this.instructor = instructor;
	}
    
    
    

    
    
    
}
