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
		redirect(controller:'item',action:'backlog')
	}
	
	def delete = {
		def project = Project.get(params.id)
		
		if(project) {
		
			if (session.project?.id == project.id)
			{
				session.project = null
			}	
			
			PointsSnapShot.findAllByProject(project).each{ it.delete() }
			Iteration.findAllByProject(project).each{ it.unloadItemsAndDelete() }
			ItemGroup.findAllByProject(project).each{ it.deleteWholeGroup() }
			project.delete()		
		}
		
		redirect(action:'list')
	}
}
