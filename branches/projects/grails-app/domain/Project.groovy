class Project {
	String name
	String email
	
    static constraints  = {
		name(blank:false)
		email(email:true, blank:false)
	}
}
