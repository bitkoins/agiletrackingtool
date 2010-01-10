class ProjectController {
	def scaffold = Project
	 
	static navigation = [
		group:'tags', 
		order:1, 
		title:'Projects', 
		action:'list' ,
		subItems: [
			[action:'list', order:1, title:'Overview'],
			[action:'create', order:10, title:'Create new project'],
		] 
	]
	
	def list = {
		return [projects:Project.list()]
	} 
	
	def show = {
		redirect(action:'list')
	}
	
	def select = {
		session.project = Project.get(params.id) 
		redirect(controller:'item',action:'backlog')
	}
}
