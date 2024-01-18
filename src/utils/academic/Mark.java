package utils.academic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
* @author darkhan
*/
public class Mark implements Comparable<Mark>, Serializable{
	private final double[] tableNumeric = {0, 1, 1.33, 1.67, 2.0, 2.33, 2.67, 3.0, 3.33, 3.67, 4.0};
	private final String[] tableAlphabetic = {"F", "D", "D+", "C-", "C", "C+", "B-", "B", "B+", "A-", "A"};
	
    /**
    * first attestation points
    */
    private double firstAttestation;
    
    /**
    * second attestation points
    */
    private double secondAttestation;
    
    /**
    * final exam points
    */
    private double finalExam;
    
    
    
    public Mark() {
		super();
	}
    Mark(double firstAttestation, double secondAttestation, double finalExam){
    	this.firstAttestation = firstAttestation;
    	this.secondAttestation = secondAttestation;
    	this.finalExam = finalExam;
    }
    
    

  
    public double getFirstAttestation() {
		return firstAttestation;
	}
	public void setFirstAttestation(double firstAttestation) {
		this.firstAttestation = firstAttestation;
	}
	public double getSecondAttestation() {
		return secondAttestation;
	}
	public void setSecondAttestation(double secondAttestation) {
		this.secondAttestation = secondAttestation;
	}
	public double getFinalExam() {
		return finalExam;
	}
	public void setFinalExam(double finalExam) {
		this.finalExam = finalExam;
	}
	public double[] getTableNumeric() {
		return tableNumeric;
	}
	public String[] getTableAlphabetic() {
		return tableAlphabetic;
	}
	/**
    * Calculates total mark
    * @return total mark for 1 period
    */
	public double getTotalMark() {
        return firstAttestation + secondAttestation + finalExam;
    }
	/**
	 * Method that calculates GPA
	 * @return gpa
	 */
	public double getGPA() {
		double total = getTotalMark();
		if(total<50) {
			return 0.0;
		}
		total=Math.min(100, total);
		return tableNumeric[((int)total-51)/5+1];
	}
	/**
	 * Method that calculates GPA
	 * @return gpa in it's letter equivalent
	 */
	public String getLetterGPA() {
		double total = getTotalMark();
		if(total<50) {
			return "F";
		}
		total=Math.min(100, total);
		return tableAlphabetic[((int)total-51)/5+1];
	}

	
	
	public int compareTo(Mark o) {
		return Double.compare(getTotalMark(), o.getTotalMark());
	}
    
	public String toString() {
		return Double.toString(getTotalMark());
	}
	public int hashCode() {
		return Objects.hash(getTotalMark());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mark other = (Mark) obj;
		return getTotalMark()==other.getTotalMark();
	}
	
	private HashMap<Double, String> initGradeTable(){
		HashMap<Double, String> gradeTable = new HashMap<Double, String>();
        // Fill up the HashMap with relevant pairs
        gradeTable.put(0.0, "F");
        gradeTable.put(1.0, "D");
        gradeTable.put(1.33, "D+");
        gradeTable.put(1.67, "C-");
        gradeTable.put(2.0, "C");
        gradeTable.put(2.33, "C+");
        gradeTable.put(2.67, "B-");
        gradeTable.put(3.0, "B");
        gradeTable.put(3.33, "B+");
        gradeTable.put(3.67, "A-");
        gradeTable.put(4.0, "A");
        return gradeTable;
	}
    
}
